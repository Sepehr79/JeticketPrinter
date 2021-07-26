package com.ansar.jeticketprinter.controller;

import com.ansar.jeticketprinter.model.database.api.OpenedDatabaseApi;
import com.ansar.jeticketprinter.model.dto.DateConvertor;
import com.ansar.jeticketprinter.model.pojo.ConnectionProperties;
import com.ansar.jeticketprinter.model.pojo.IntervalProduct;
import com.ansar.jeticketprinter.view.ButtonCell;
import com.ansar.jeticketprinter.view.DateTextFiled;
import com.ansar.jeticketprinter.view.TimeTextField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class IntervalTabController implements Initializable {

    @FXML private TableView<IntervalProduct> table;
    @FXML private TableColumn<IntervalProduct, Boolean> delete;
    @FXML private TableColumn<IntervalProduct, String> priceConsumer;
    @FXML private TableColumn<IntervalProduct, String> priceForosh;
    @FXML private TableColumn<IntervalProduct, String> name;
    @FXML private TableColumn<IntervalProduct, String> id;
    @FXML private TableColumn<IntervalProduct, String> date;

    @FXML private TextField anbar;
    @FXML private GridPane gridPane;

    @FXML private Button sendToMainPage;


    private static final DateTextFiled fromDate = new DateTextFiled();
    private static final DateTextFiled toDate = new DateTextFiled();

    private static final TimeTextField fromTime = new TimeTextField("00:00");
    private static final TimeTextField toTime = new TimeTextField("23:59");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadGridPane();
        mapColumns();
    }

    public void search(ActionEvent actionEvent){
        table.getItems().clear();

        final String fromDateString = DateConvertor.jalalyToGregorian(fromDate.getText()) + " " + fromTime.getText();
        final String toDateString = DateConvertor.jalalyToGregorian(toDate.getText()) + " " + toTime.getText();

        ConnectionProperties connectionProperties = ConnectionProperties.deserializeFromXml();
        connectionProperties.setAnbar(anbar.getText());

        OpenedDatabaseApi api = OpenedDatabaseApi.getInstance();
        try {
            api.openConnection(connectionProperties);
            table.getItems().addAll(api.getProductsManager(fromDateString, toDateString));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            api.closeConnection();
        }
    }

    public void sendToMain(ActionEvent actionEvent) {
    }

    private void loadGridPane(){
        gridPane.add(toDate, 5, 0);
        gridPane.add(fromDate, 2, 0);
        gridPane.add(fromTime, 1, 0);
        gridPane.add(toTime, 4, 0);
    }

    private void mapColumns(){
        name.setCellValueFactory(new PropertyValueFactory<IntervalProduct, String>("name"));
        id.setCellValueFactory(new PropertyValueFactory<IntervalProduct, String>("id"));
        priceConsumer.setCellValueFactory(new PropertyValueFactory<IntervalProduct, String>("priceConsumer"));
        priceForosh.setCellValueFactory(new PropertyValueFactory<IntervalProduct, String>("priceForosh"));
        date.setCellValueFactory(new PropertyValueFactory<IntervalProduct, String>("date"));
        delete.setSortable(false);


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
}
