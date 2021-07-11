package com.ansar.jeticketprinter.controller;

import com.ansar.jeticketprinter.model.database.api.ConnectionProperties;
import com.ansar.jeticketprinter.model.database.api.OpenedDatabaseApi;
import com.ansar.jeticketprinter.model.entity.*;
import com.ansar.jeticketprinter.model.entity.printer.PrintProperties;
import com.ansar.jeticketprinter.model.entity.printer.ProductPaper;
import com.ansar.jeticketprinter.model.entity.printer.ProductPrinter;
import com.ansar.jeticketprinter.view.ButtonCell;
import com.ansar.jeticketprinter.view.ViewLoader;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import java.awt.print.PrinterAbortException;
import java.awt.print.PrinterException;
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
    @FXML private TableColumn<Product, Boolean> delete;


    private static final Stage settingsWindow = new Stage();

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
        if (properties != null){
            String[] barcodes = searchField.getText().split("\n");
            OpenedDatabaseApi api = OpenedDatabaseApi.getInstance();

            try {
                api.openConnection(properties);

                Set<Product> products = api.getProductsById(barcodes);

                //table.getItems().clear();
                table.getItems().addAll(products);
                table.refresh();
            } catch (SQLException exception) {
                logger.info("Exception on opening connection");
                alert("خطا در اتصال", "لطفا تنظیمات اتصال خود را چک کنید", Alert.AlertType.ERROR);
                exception.printStackTrace();
            }finally {
                api.closeConnection();
            }

            ConnectionProperties.serializeToXml(properties);
        }else {
            alert("فیلد خالی", "لطفا تمام ورودی ها را تکمیل کنید", Alert.AlertType.ERROR);
        }
    }

    public void printResult(ActionEvent actionEvent) {
        List<Product> products = table.getItems().subList(0, table.getItems().size());
        PrintService printService = printer.getValue();
        PrintProperties printProperties = PrintProperties.deserializeFromXml();

        if (printService != null){
            ProductPrinter printer = new ProductPrinter(new ProductPaper(products, printProperties), printService);
            try {
                printer.print();
            }catch (PrinterAbortException exception){
                logger.info("Printer aborted");
                exception.printStackTrace();
            } catch (PrinterException exception) {
                alert("خطایی در اتصال با پرینتر رخ داد", "لطفا با توسعه دهنده تماس بگیرید", Alert.AlertType.ERROR);
                exception.printStackTrace();
            }
        }else
            alert("هیچ پرینتری انتخاب نشده است", "لطفا ابتدا یک پرینتر را انتخاب کرده و دوباره تلاش کنید", Alert.AlertType.WARNING);


    }

    public void openSettings(ActionEvent actionEvent) throws IOException {
        Parent root = ViewLoader.getSettingsPage();
        Scene scene = new Scene(root);

        if (!settingsWindow.isShowing()){
            settingsWindow.setTitle(String.valueOf("تنظیمات"));
            settingsWindow.setScene(scene);
            settingsWindow.setResizable(false);
            settingsWindow.show();
        }

    }


    private void mapColumnsToProduct(){
        // Readable
        name.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        id.setCellValueFactory(new PropertyValueFactory<Product, String>("barcode"));
        highPrice.setCellValueFactory(new PropertyValueFactory<Product, String>("highPrice"));
        lowPrice.setCellValueFactory(new PropertyValueFactory<Product, String>("lowPrice"));
        discount.setCellValueFactory(new PropertyValueFactory<Product, String>("discount"));
        delete.setSortable(false);

        // Delete button
        delete.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        delete.setCellFactory(p -> new ButtonCell(table));

        // Writable
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(event -> {
            event.getRowValue().setName(event.getNewValue());
            event.getTableView().refresh();
        });

        highPrice.setCellFactory(TextFieldTableCell.forTableColumn());
        highPrice.setOnEditCommit(event -> {
            try {
                event.getRowValue().setHighPrice(event.getNewValue());
            }catch (IllegalArgumentException exception){
                exception.printStackTrace();
                alert("Illegal input error!", "Please enter a valid number.", Alert.AlertType.ERROR);
            }
            event.getTableView().refresh();
        });

        lowPrice.setCellFactory(TextFieldTableCell.forTableColumn());
        lowPrice.setOnEditCommit(event -> {
            try {
                event.getRowValue().setLowPrice(event.getNewValue());
            }catch (IllegalArgumentException exception){
                exception.printStackTrace();
                alert("Illegal input error!", "Please enter a valid number.", Alert.AlertType.ERROR);
            }
            event.getTableView().refresh();
        });
    }

    /**
     * Read data from input fields
     * @return properties of inputs
     */
    private ConnectionProperties readProperties(){
        String address = this.address.getText();
        String port = this.port.getText();
        String userName = this.userName.getText();
        String password = this.password.getText();
        String dataBase = this.database.getText();
        String anbar = this.anbar.getText();

        if (address != null && port != null && userName != null && password != null && dataBase != null && anbar != null){
            return new ConnectionProperties.Builder().
                    address(address.trim()).
                    port(port.trim()).
                    userName(userName.trim()).
                    password(password.trim()).
                    databaseName(dataBase.trim()).
                    anbar(anbar.trim()).build();
        }

        return null;
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
        if (services.length < 1)
            alert("هیچ پرینتری یافت نشد", "لطفا اتصال پرینتر های خودرا بررسی کنید", Alert.AlertType.ERROR);
        else
            printer.setItems(FXCollections.observableArrayList(services));
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

    private void alert(String header, String footer, Alert.AlertType type){
        Alert alert = new Alert(type);

        alert.setHeaderText(String.valueOf(header));
        alert.setContentText(String.valueOf(footer));
        alert.showAndWait();
    }


}
