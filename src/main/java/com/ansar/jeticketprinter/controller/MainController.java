package com.ansar.jeticketprinter.controller;

import com.ansar.jeticketprinter.model.database.api.OpenedDatabaseApi;
import com.ansar.jeticketprinter.model.entity.ConnectionProperties;
import com.ansar.jeticketprinter.model.entity.Product;
import com.ansar.jeticketprinter.view.ViewLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import javax.print.PrintService;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

public class MainController implements Initializable {

    private static final Logger logger = Logger.getLogger(MainController.class.getName());



    @FXML private TextField address;
    @FXML private TextField port;
    @FXML private TextField userName;
    @FXML private TextArea searchField;
    @FXML private TextField database;
    @FXML private TextField anbar;
    @FXML private PasswordField password;
    @FXML private ChoiceBox<PrintService> printer;


    @FXML private TableView<Product> table;
    @FXML private TableColumn<Product, String> discount;
    @FXML private TableColumn<Product, String> lowPrice;
    @FXML private TableColumn<Product, String> highPrice;
    @FXML private TableColumn<Product, String> name;
    @FXML private TableColumn<Product, String> id;


    // Data
    private ConnectionProperties properties;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load data
        loadData();
        // Setup columns of table to the Product fields
        mapColumnsToProduct();

        setPrinters();
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
            logger.info("Exception on opening connection");
            alert("Connection problem!", "Please check your connection fields.");
            exception.printStackTrace();
        }

        ConnectionProperties.serializeToXml(properties);
    }

    public void printResult(ActionEvent actionEvent) {
    }

    public void openSettings(ActionEvent actionEvent) throws IOException {
        Parent root = ViewLoader.getSettingsPage();

        Scene scene = new Scene(root);

        Stage settings = new Stage();
        settings.setScene(scene);
        settings.setResizable(false);
        settings.show();
    }


    private void mapColumnsToProduct(){
        // Readable
        name.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        id.setCellValueFactory(new PropertyValueFactory<Product, String>("barcode"));
        highPrice.setCellValueFactory(new PropertyValueFactory<Product, String>("highPrice"));
        lowPrice.setCellValueFactory(new PropertyValueFactory<Product, String>("lowPrice"));
        discount.setCellValueFactory(new PropertyValueFactory<Product, String>("discount"));
        discount.setSortable(false);

        // Writable
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Product, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Product, String> event) {
                event.getRowValue().setName(event.getNewValue());
                event.getTableView().refresh();
            }
        });

        highPrice.setCellFactory(TextFieldTableCell.forTableColumn());
        highPrice.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Product, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Product, String> event) {
                try {
                    event.getRowValue().setHighPrice(event.getNewValue());
                }catch (IllegalArgumentException exception){
                    exception.printStackTrace();
                    alert("Illegal input error!", "Please enter a valid number.");
                }
                event.getTableView().refresh();
            }
        });

        lowPrice.setCellFactory(TextFieldTableCell.forTableColumn());
        lowPrice.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Product, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Product, String> event) {
                try {
                    event.getRowValue().setLowPrice(event.getNewValue());
                }catch (IllegalArgumentException exception){
                    exception.printStackTrace();
                    alert("Illegal input error!", "Please enter a valid number.");
                }
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

    private void setPrinters(){
        PrintService[] services = PrinterJob.lookupPrintServices();
        printer.setItems(FXCollections.observableArrayList(services));
        if (services.length > 0)
            printer.setValue(services[0]);
        else
            alert("No printers found!", "Please call developer");
    }

    /*
     * Update fields in database
     * @param updateType type of update(NAME_UPDATE, UPDATE_PRICE_FOROSH, UPDATE_PRICE_CONSUMER)
     * @param newValue newValue of field
     * @param id primary key
     */
//    @Deprecated
//    private void update(Integer updateType, String newValue, String id){
//        OpenedDatabaseApi api = OpenedDatabaseApi.getInstance();
//
//        try {
//            api.openConnection(readProperties());
//
//            logger.info("Rows updated:" +
//                    "------------------------------------------------------------------------------> "
//                    + api.update(updateType, newValue, id));
//        } catch (SQLException exception) {
//            logger.info("Exception on SQL processing");
//            alert("Connection problem!", "Please check your connection fields.");
//            exception.printStackTrace();
//        }finally {
//            api.closeConnection();
//        }
//    }

    private void alert(String header, String footer){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(footer);
        alert.showAndWait();
    }


}
