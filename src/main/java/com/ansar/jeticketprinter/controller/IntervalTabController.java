package com.ansar.jeticketprinter.controller;

import com.ansar.jeticketprinter.model.database.api.OpenedDatabaseApi;
import com.ansar.jeticketprinter.model.dto.DateTimeProperties;
import com.ansar.jeticketprinter.model.dto.ProductsManager;
import com.ansar.jeticketprinter.model.pojo.ConnectionProperties;
import com.ansar.jeticketprinter.view.ButtonCell;
import com.ansar.jeticketprinter.view.DateTextFiled;
import com.ansar.jeticketprinter.view.TimeTextField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class IntervalTabController implements Initializable {

    @FXML private TableView<ProductsManager> table;
    @FXML private TableColumn<ProductsManager, Boolean> delete;
    @FXML private TableColumn<ProductsManager, String> priceConsumer;
    @FXML private TableColumn<ProductsManager, String> priceForosh;
    @FXML private TableColumn<ProductsManager, String> name;
    @FXML private TableColumn<ProductsManager, String> id;

    @FXML private TextField anbar;
    @FXML private GridPane gridPane;

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
        ConnectionProperties connectionProperties = ConnectionProperties.deserializeFromXml();
        DateTimeProperties dateTimeProperties = getDateTimeProperties();
        connectionProperties.setAnbar(anbar.getText());

        OpenedDatabaseApi api = OpenedDatabaseApi.getInstance();
        try {
            api.openConnection(connectionProperties);
            table.getItems().addAll(api.getProductsManager(dateTimeProperties));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            api.closeConnection();
        }
    }

    public void sendToMain(ActionEvent actionEvent) {
    }

    private void loadGridPane(){
        gridPane.add(toDate, 3, 1);
        gridPane.add(fromDate, 1, 1);
        gridPane.add(fromTime, 1, 2);
        gridPane.add(toTime, 3, 2);
    }

    private DateTimeProperties getDateTimeProperties(){
        String fromDateString = fromDate.getText();
        String toDateString = toDate.getText();
        String fromTimeString = fromTime.getText();
        String toTimeString = toTime.getText();

        return new DateTimeProperties(fromDateString, toDateString, fromTimeString, toTimeString);
    }

    private void mapColumns(){
        name.setCellValueFactory(new PropertyValueFactory<ProductsManager, String>("name"));
        id.setCellValueFactory(new PropertyValueFactory<ProductsManager, String>("barcode"));
        priceConsumer.setCellValueFactory(new PropertyValueFactory<ProductsManager, String>("highPrice"));
        priceForosh.setCellValueFactory(new PropertyValueFactory<ProductsManager, String>("lowPrice"));
        delete.setSortable(false);

        // Delete button
        delete.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        delete.setCellFactory(p -> new ButtonCell(table));
    }

    public void deleteAll(ActionEvent actionEvent) {
        table.getItems().clear();
    }
}
