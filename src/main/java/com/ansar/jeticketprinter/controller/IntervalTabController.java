package com.ansar.jeticketprinter.controller;

import com.ansar.jeticketprinter.model.database.api.OpenedDatabaseApi;
import com.ansar.jeticketprinter.model.dto.DateConvertor;
import com.ansar.jeticketprinter.model.pojo.ConnectionProperties;
import com.ansar.jeticketprinter.model.pojo.IntervalProduct;
import com.ansar.jeticketprinter.view.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.util.HashSet;
import java.util.ResourceBundle;

public class IntervalTabController implements Initializable {

    @FXML private TableView<IntervalProduct> table;
    @FXML private TableColumn<IntervalProduct, Boolean> delete;
    @FXML private TableColumn<IntervalProduct, String> priceConsumer;
    @FXML private TableColumn<IntervalProduct, String> priceForosh;
    @FXML private TableColumn<IntervalProduct, String> name;
    @FXML private TableColumn<IntervalProduct, String> id;
    @FXML private TableColumn<IntervalProduct, String> date;
    @FXML private TableColumn<IntervalProduct, Boolean> row;

    @FXML private TextField anbar;
    @FXML private GridPane gridPane;

    @FXML private Button sendToMainPage;

    @FXML private Button searchingButton;

    private static final DateTextFiled fromDate = new DateTextFiled();
    private static final DateTextFiled toDate = new DateTextFiled();

    private static final TimeTextField fromTime = new TimeTextField("00:00");
    private static final TimeTextField toTime = new TimeTextField("23:59");

    private static boolean fromTimeFirstClick = false;
    private static boolean toTimeFirstClick = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadGridPane();
        mapColumns();
    }

    public void search(ActionEvent actionEvent){
        updateTable();
    }

    public void sendToMain(ActionEvent actionEvent) {
    }

    private void loadGridPane(){
        gridPane.add(fromTime, 5, 0);
        gridPane.add(toTime, 3, 0);
        gridPane.add(fromDate, 2, 0);
        gridPane.add(toDate, 0, 0);

        fromTime.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER)
                toTime.requestFocus();
        });

        toTime.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER)
                anbar.requestFocus();
        });

        anbar.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER)
                searchingButton.requestFocus();
        });

        searchingButton.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER)
                updateTable();
        });
    }

    private void mapColumns(){
        name.setCellValueFactory(new PropertyValueFactory<IntervalProduct, String>("name"));
        id.setCellValueFactory(new PropertyValueFactory<IntervalProduct, String>("id"));
        priceConsumer.setCellValueFactory(new PropertyValueFactory<IntervalProduct, String>("priceConsumer"));
        priceForosh.setCellValueFactory(new PropertyValueFactory<IntervalProduct, String>("priceForosh"));
        date.setCellValueFactory(new PropertyValueFactory<IntervalProduct, String>("date"));
        delete.setSortable(false);

        row.setCellFactory(p -> new CounterCell<IntervalProduct>(table));

        // Delete button
        delete.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        delete.setCellFactory(p -> new ButtonCell<IntervalProduct>(table));
    }

    public void deleteAll(ActionEvent actionEvent) {
        table.getItems().clear();
    }

    ///////////////////////////////// Getters ///////////////////////////////////////////////
    public Button getSendToMainPage() {
        return sendToMainPage;
    }

    public TableView<IntervalProduct> getTable() {
        return table;
    }

    private void updateTable(){
        table.getItems().clear();

        ConnectionProperties connectionProperties = ConnectionProperties.deserializeFromXml();
        connectionProperties.setAnbar(anbar.getText());

        OpenedDatabaseApi api = OpenedDatabaseApi.getInstance();
        try {
            api.openConnection(connectionProperties);
            table.getItems().addAll(new HashSet<>(api.getProductsManager(DateConvertor.jalalyToGregorian(fromDate.getText()) + " " + fromTime.getText(),
                    DateConvertor.jalalyToGregorian(toDate.getText()) + " " + toTime.getText())));

        } catch (SQLException exception) {
            DialogViewer.showDialog("??????", "?????????? ???? ?????????????? ???????????? ???????? ???????? ?????????????? ?????????? ???? ?????????? ????????", Alert.AlertType.ERROR);
            exception.printStackTrace();
        }catch (DateTimeException exception){
            DialogViewer.showDialog("??????", "?????????? ???????? ?????? ???????? ?????? ????????", Alert.AlertType.ERROR);
        } finally {
            api.closeConnection();
        }
    }

    public TimeTextField getFromTime() {
        return fromTime;
    }
}
