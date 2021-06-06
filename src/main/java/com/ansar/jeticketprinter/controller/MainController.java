package com.ansar.jeticketprinter.controller;

import com.ansar.jeticketprinter.model.database.api.OpenedDatabaseApi;
import com.ansar.jeticketprinter.model.entity.ConnectionProperties;
import com.ansar.jeticketprinter.model.entity.Product;
import com.sun.javafx.collections.ImmutableObservableList;
import com.sun.javafx.scene.control.skin.TableViewSkin;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class MainController implements Initializable {


    @FXML private TextField address;
    @FXML private TextField port;
    @FXML private TextField userName;
    @FXML private TextArea searchField;
    @FXML private TextField database;
    @FXML private TextField anbar;
    @FXML private PasswordField password;

    @FXML private TableView<Product> table;
    @FXML private TableColumn<Product, String> discount;
    @FXML private TableColumn<Product, String> lowPrice;
    @FXML private TableColumn<Product, String> highPrice;
    @FXML private TableColumn<Product, String> name;
    @FXML private TableColumn<Product, String> id;
    @FXML private TableColumn<Product, String> count;

    // Data
    private ConnectionProperties properties;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load data
        loadData();
        // Setup columns of table to the Product fields
        mapColumnsToProduct();
    }

    public void clear(ActionEvent actionEvent) {
        table.getItems().clear();
    }

    public void search(ActionEvent actionEvent) {
        properties = readProperties();

        String[] barcodes = searchField.getText().split("\n");

        OpenedDatabaseApi api = OpenedDatabaseApi.getInstance();

        try {
            api.openConnection(properties);

            Set<Product> products = api.getProductsById(barcodes);

            api.closeConnection();

            table.getItems().clear();
            table.getItems().addAll(products);
            table.refresh();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        ConnectionProperties.serializeToXml(properties);
    }


    private void mapColumnsToProduct(){
        // Readable
        name.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        id.setCellValueFactory(new PropertyValueFactory<Product, String>("barcode"));
        highPrice.setCellValueFactory(new PropertyValueFactory<Product, String>("highPrice"));
        lowPrice.setCellValueFactory(new PropertyValueFactory<Product, String>("lowPrice"));
        discount.setCellValueFactory(new PropertyValueFactory<Product, String>("discount"));
        count.setCellValueFactory(new PropertyValueFactory<Product, String>("count"));

        // Writable
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Product, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Product, String> event) {
                event.getRowValue().setName(event.getNewValue());

                OpenedDatabaseApi api = OpenedDatabaseApi.getInstance();

                try {
                    api.openConnection(readProperties());

                    api.updateName(event.getNewValue().trim(), event.getRowValue().getBarcode());

                    api.closeConnection();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }

                event.getTableView().refresh();
            }
        });

        highPrice.setCellFactory(TextFieldTableCell.forTableColumn());
        highPrice.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Product, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Product, String> event) {
                event.getRowValue().setHighPrice(event.getNewValue());
                event.getTableView().refresh();
            }
        });

        lowPrice.setCellFactory(TextFieldTableCell.forTableColumn());
        lowPrice.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Product, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Product, String> event) {
                event.getRowValue().setLowPrice(event.getNewValue());
                event.getTableView().refresh();
            }
        });

        count.setCellFactory(TextFieldTableCell.forTableColumn());
        count.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Product, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Product, String> event) {
                event.getRowValue().setCount(event.getNewValue());
                event.getTableView().refresh();
            }
        });

    }

    /**
     * Set new rows of table
     * @param products list of products that will set to the current table
     */
    private void setItems(List<Product> products){
        ObservableList<Product> data =
                FXCollections.observableArrayList(products);

        table.setItems(data);
        table.refresh();
    }

    /**
     * Read data from input fields
     * @return properties of inputs
     */
    private ConnectionProperties readProperties(){
        String address = this.address.getText().trim();
        String port = this.port.getText().trim();
        String userName = this.userName.getText().trim();
        String password = this.password.getText().trim();
        String dataBase = this.database.getText().trim();
        String anbar = this.anbar.getText().trim();

        return new ConnectionProperties.Builder().
                address(address).
                port(port).
                userName(userName).
                password(password).
                databaseName(dataBase).
                anbar(anbar).build();
    }

    private void loadData(){
        properties = ConnectionProperties.deserializeFromXml();
        address.setText(properties.getAddress());
        port.setText(properties.getPort());
        userName.setText(properties.getUserName());
        database.setText(properties.getDatabaseName());
        anbar.setText(properties.getAnbar());
        password.setText(properties.getPassword());
    }
}
