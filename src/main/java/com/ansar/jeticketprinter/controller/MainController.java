package com.ansar.jeticketprinter.controller;

import com.ansar.jeticketprinter.model.pojo.ConnectionProperties;
import com.ansar.jeticketprinter.model.database.api.OpenedDatabaseApi;
import com.ansar.jeticketprinter.model.dto.*;
import com.ansar.jeticketprinter.model.pojo.PrintProperties;
import com.ansar.jeticketprinter.model.pojo.PrinterIndex;
import com.ansar.jeticketprinter.model.pojo.SearchingType;
import com.ansar.jeticketprinter.printer.ProductPaper;
import com.ansar.jeticketprinter.printer.ProductPrinter;
import com.ansar.jeticketprinter.view.ButtonCell;
import com.ansar.jeticketprinter.view.CounterCell;
import com.ansar.jeticketprinter.view.DialogViewer;
import com.ansar.jeticketprinter.view.ViewLoader;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
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
import java.util.stream.Collectors;

public class MainController implements Initializable {
    // Debugging
    private static final Logger logger = Logger.getLogger(MainController.class.getName());

    private static final EntityJsonManager<PrinterIndex> printerIndexManager = new EntityJsonManager<>();


    @FXML private TextField address;
    @FXML private TextField port;
    @FXML private TextField userName;
    @FXML private TextArea barcodes;
    @FXML private TextField barcode;
    @FXML private TextField database;
    @FXML private TextField anbar;
    @FXML private PasswordField password;
    @FXML private ChoiceBox<PrintService> printer;
    @FXML private CheckBox connectionSettings;
    @FXML private GridPane connectionView;

    @FXML private TextField nameSearchingTextField;

    @FXML private TableView<ProductsManager> table;
    @FXML private TableColumn<ProductsManager, String> discount;
    @FXML private TableColumn<ProductsManager, String> lowPrice;
    @FXML private TableColumn<ProductsManager, String> highPrice;
    @FXML private TableColumn<ProductsManager, String> name;
    @FXML private TableColumn<ProductsManager, String> id;
    @FXML private TableColumn<ProductsManager, Boolean> delete;
    @FXML private TableColumn<ProductsManager, Boolean> row;

    // Settings window
    private static final Stage settingsWindow = new Stage();
    private static final Stage nameSearchingWindow = new Stage();

    // Data
    private ConnectionProperties properties;
    private PrinterIndex printerIndex;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Update properties
        loadData();

        // Setup columns of table to the Product fields
        mapColumnsToProduct();

        // Read system printers
        setPrinters();
    }

    //////////////////////////////////////////////// Events //////////////////////////////////////////////////

    /**
     * Single barcode reader
     */
    public void clearBarcode(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER){
            searchResultFrom(barcode);
            barcode.clear();
        }
    }

    public void clear(ActionEvent actionEvent) {
        table.getItems().clear();
    }

    public void search(ActionEvent actionEvent) {
        try {
            if (!barcodes.getText().equals(""))
                searchResultFrom(barcodes);
        }catch (NullPointerException exception){
            logger.info("Null pointer cause no arg on barcodes label");
        }
    }

    public void printResult(ActionEvent actionEvent) {

        // Read products
        List<ProductsManager> managers = table.getItems().subList(0, table.getItems().size());

        // Use selected printer
        PrintService printService = printer.getValue();

        // Read print properties
        PrintProperties printProperties = PrintProperties.deserializeFromXml();

        // If a printer selected
        if (printService != null){
            ProductPrinter printer = new ProductPrinter(new ProductPaper(managers, printProperties), printService);
            try {
                if (managers.size() > 1000)
                    DialogViewer.showDialog("اخطار", "لطفا تعداد محصولات کمتر از 1000 را انتخاب کنید", Alert.AlertType.WARNING);
                else
                    printer.print();
            }catch (PrinterAbortException exception){
                // When printing canceled
                logger.info("Printer aborted");
                exception.printStackTrace();
            } catch (PrinterException exception) {
                // When any exception
                DialogViewer.showDialog("خطایی در اتصال با پرینتر رخ داد", "لطفا با توسعه دهنده تماس بگیرید", Alert.AlertType.ERROR);
                exception.printStackTrace();
            }
        }else
            DialogViewer.showDialog("هیچ پرینتری انتخاب نشده است", "لطفا ابتدا یک پرینتر را انتخاب کرده و دوباره تلاش کنید", Alert.AlertType.WARNING);


    }

    public void doConnectionSettings(ActionEvent actionEvent) {
        connectionView.setVisible(connectionSettings.isSelected());
    }

    public void testConnection(ActionEvent actionEvent) {
        properties = readProperties();
        if (properties != null){
            OpenedDatabaseApi api = OpenedDatabaseApi.getInstance();
            try {
                api.openConnection(properties);

                DialogViewer.showDialog("پیام", "اتصال موفقیت آمیز بود", Alert.AlertType.INFORMATION);
            } catch (SQLException throwables) {
                DialogViewer.showDialog("پیام", "اتصال برقرار نشد لطفا تنظیمات خود را چک کنید", Alert.AlertType.ERROR);
            }finally {
                api.closeConnection();
            }
        }
        ConnectionProperties.serializeToXml(properties);
    }

    public void openSearchingNameTab(MouseEvent mouseEvent) throws IOException {
        openSearchingName(mouseEvent);
    }

    public void openSearchingNameTabKey(KeyEvent keyEvent) throws IOException {
        openSearchingName(keyEvent);
    }
    ///// End of events /////

    /**
     * Open settings window
     */
    public void openSettings(ActionEvent actionEvent) throws IOException {
        Parent root = ViewLoader.getPage(ViewLoader.SETTING_PAGE);
        Scene scene = new Scene(root);

        if (!settingsWindow.isShowing()){
            settingsWindow.setTitle(("تنظیمات"));
            settingsWindow.setScene(scene);
            settingsWindow.setResizable(false);
            settingsWindow.show();
        }

    }

    private void mapColumnsToProduct(){
        // Readable
        name.setCellValueFactory(new PropertyValueFactory<ProductsManager, String>("name"));
        id.setCellValueFactory(new PropertyValueFactory<ProductsManager, String>("barcode"));
        highPrice.setCellValueFactory(new PropertyValueFactory<ProductsManager, String>("highPrice"));
        lowPrice.setCellValueFactory(new PropertyValueFactory<ProductsManager, String>("lowPrice"));
        discount.setCellValueFactory(new PropertyValueFactory<ProductsManager, String>("discount"));
        delete.setSortable(false);

        // Delete button
        delete.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        delete.setCellFactory(p -> new ButtonCell<ProductsManager>(table));

        row.setCellFactory(p -> new CounterCell<ProductsManager>(table));

        // Writable
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(event -> {
            event.getRowValue().setName(event.getNewValue());
            event.getTableView().refresh();
        });


        highPrice.setCellFactory(TextFieldTableCell.forTableColumn());
        lowPrice.setCellFactory(TextFieldTableCell.forTableColumn());

        highPrice.setOnEditCommit(event -> {
            try {
                event.getRowValue().setHighPrice(event.getNewValue());
            }catch (IllegalArgumentException exception){
                exception.printStackTrace();
                DialogViewer.showDialog("ورودی اشتباه", "لطفا یک عدد صحیح را وارد کنید", Alert.AlertType.ERROR);
            }
            event.getTableView().refresh();
        });

        lowPrice.setOnEditCommit(event -> {
            try {
                event.getRowValue().setLowPrice(event.getNewValue());
            }catch (IllegalArgumentException exception){
                exception.printStackTrace();
                DialogViewer.showDialog("ورودی اشتباه", "لطفا یک عدد صحیح را وارد کنید", Alert.AlertType.ERROR);
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

        String anbar = "";
        if (this.anbar.getText() != null)
            anbar = this.anbar.getText();

        if (address != null && port != null && userName != null && password != null && dataBase != null){
            return new ConnectionProperties.Builder().
                    address(address.trim()).
                    port(port.trim()).
                    userName(userName.trim()).
                    password(password.trim()).
                    databaseName(dataBase.trim()).
                    anbar(anbar).build();
        }

        return null;
    }

    private void loadData()  {
        try {
            printerIndex = printerIndexManager.deserializeFromJson(EntityJsonManager.PRINTER_INDEX, PrinterIndex.class);
        } catch (Exception exception) {
            logger.info("Exception on reading printerIndex.json file");
            exception.printStackTrace();
            printerIndex = new PrinterIndex();
        }

        properties = ConnectionProperties.deserializeFromXml();
        address.setText(properties.getAddress());
        port.setText(properties.getPort());
        userName.setText(properties.getUserName());
        database.setText(properties.getDatabaseName());
        anbar.setText(properties.getAnbar());
        password.setText(properties.getPassword());
    }

    private void setPrinters(){
        // Add event when printer selected
        printer.getSelectionModel().selectedIndexProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                int index = printer.getSelectionModel().getSelectedIndex();
                printerIndex.setIndexNumber(index);
                printerIndexManager.serializeToJson(printerIndex, EntityJsonManager.PRINTER_INDEX);
            }
        });


        PrintService[] services = PrinterJob.lookupPrintServices();
        if (services.length < 1)
            DialogViewer.showDialog(String.valueOf("هیچ پرینتری یافت نشد"), String.valueOf("لطفا اتصال پرینتر های خودرا بررسی کنید"), Alert.AlertType.ERROR);
        else{
            printer.setItems(FXCollections.observableArrayList(services));
            if (printerIndex.getIndexNumber() < services.length)
                printer.getSelectionModel().select(printerIndex.getIndexNumber());
        }
    }
    /**
     * Read properties from input field and update table
     * @param textInputControl (TextFiled or TextArea)
     */
    private void searchResultFrom(TextInputControl textInputControl){
        // Update properties
        properties = readProperties();
        if (properties != null){
            // Read barcode(s)
            String[] barcodes = textInputControl.getText().split("\n");

            OpenedDatabaseApi api = OpenedDatabaseApi.getInstance();

            try {
                api.openConnection(properties);

                // Read products based on their brocades
                Set<ProductsManager> products = api.getProductsById(barcodes);

                // Add new products to the current table
                table.getItems().addAll(products);

                table.refresh();
            } catch (SQLException exception) {
                // When properties not true
                logger.info("Exception on opening connection");
                DialogViewer.showDialog("خطا در اتصال", "لطفا تنظیمات اتصال خود را چک کنید", Alert.AlertType.ERROR);
                exception.printStackTrace();
            }finally {
                api.closeConnection();
            }
            // Save properties
            ConnectionProperties.serializeToXml(properties);
        }else {
            DialogViewer.showDialog("فیلد خالی", "لطفا تمام ورودی ها را تکمیل کنید", Alert.AlertType.ERROR);
        }
    }

    public void updateIndex(MouseEvent mouseEvent) {
        System.out.println("Update");
    }

    ///////////////////////////////Getters/////////////////////////////////////////////////
    public TableView<ProductsManager> getTableView(){
        return table;
    }

    public TextField getBarcode() {
        return barcode;
    }

    private void deselect(TextField textField) {
        Platform.runLater(() -> {
            if (textField.getText().length() > 0 &&
                    textField.selectionProperty().get().getEnd() == 0) {
                deselect(textField);
            }else{
                textField.selectEnd();
                textField.deselect();
            }
        });
    }

    private void openSearchingName(Event action) throws IOException {
        if (!nameSearchingWindow.isShowing()){
            // Save properties
            ConnectionProperties properties = readProperties();
            ConnectionProperties.serializeToXml(properties);

            Scene scene = ViewLoader.getNameSearchingScene();
            nameSearchingWindow.setScene(scene);
            nameSearchingWindow.show();
            nameSearchingWindow.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ESCAPE)
                    nameSearchingWindow.close();
            });

            FXMLLoader loader = (FXMLLoader) scene.getUserData();
            NameSearchingTabController nameSearchingTabController = loader.getController();

            if (action instanceof KeyEvent){
                nameSearchingTabController.getName().setText((((KeyEvent) action).getText()));
            }
            nameSearchingTabController.getName().requestFocus();
            deselect(nameSearchingTabController.getName());

            nameSearchingTabController.getTable().setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER){
                    ProductsManager productsManager = nameSearchingTabController.getTable().getSelectionModel().getSelectedItem();
                    this.table.getItems().add(productsManager);
                    nameSearchingWindow.close();
                }
            });

            nameSearchingTabController.getSendToMainPage().addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
                List<ProductsManager> managers = new ArrayList<>(nameSearchingTabController.table.getItems());
                table.getItems().addAll(managers);
            });

            nameSearchingTextField.clear();
        }
    }
}
