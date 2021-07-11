package com.ansar.jeticketprinter.controller;

import com.ansar.jeticketprinter.model.entity.*;
import com.ansar.jeticketprinter.model.entity.printer.PrintProperties;
import com.ansar.jeticketprinter.model.entity.printer.ProductPaper;
import com.ansar.jeticketprinter.model.entity.printer.ProductPrinter;
import com.ansar.jeticketprinter.view.NumberInputSpinner;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import javax.print.PrintService;
import java.awt.print.PrinterAbortException;
import java.awt.print.PrinterJob;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class SettingsController implements Initializable {

    private static final Logger logger = Logger.getLogger(SettingsController.class.getName());

    @FXML private GridPane gridPane;
    @FXML private ChoiceBox<PrintService> printers;

    @FXML private RadioButton a4;
    @FXML private RadioButton a5;

    private NumberInputSpinner nameX;
    private NumberInputSpinner nameY;
    private NumberInputSpinner discountX;
    private NumberInputSpinner discountY;
    private NumberInputSpinner highPriceX;
    private NumberInputSpinner highPriceY;
    private NumberInputSpinner lowPriceX;
    private NumberInputSpinner lowPriceY;
    private NumberInputSpinner dateX;
    private NumberInputSpinner dateY;

    private NumberInputSpinner nameFont;
    private NumberInputSpinner discountFont;
    private NumberInputSpinner highPriceFont;
    private NumberInputSpinner lowPriceFont;
    private NumberInputSpinner dateFont;

    private NumberInputSpinner paperHeight;
    private NumberInputSpinner productCounter;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load data
        loadData();

        // Set system printers
        PrintService[] services = PrinterJob.lookupPrintServices();
        printers.setItems(FXCollections.observableArrayList(services));
    }

    public void save(ActionEvent actionEvent) {
        saveData();
        nameX.getScene().getWindow().hide();
    }

    public void reset(ActionEvent actionEvent) {
        loadData();
    }

    private void loadData(){
        // Load data
        PrintProperties printProperties = PrintProperties.deserializeFromXml();

        // Discount
        discountFont = new NumberInputSpinner(printProperties.getDiscountFont());
        gridPane.add(discountFont, 1, 2);

        discountY = new NumberInputSpinner(printProperties.getDiscountY());
        gridPane.add(discountY, 2, 2);

        discountX = new NumberInputSpinner(printProperties.getDiscountX());
        gridPane.add(discountX, 3, 2);

        // Name
        nameFont = new NumberInputSpinner(printProperties.getNameFont());
        gridPane.add(nameFont, 1, 3);

        nameY = new NumberInputSpinner(printProperties.getNameY());
        gridPane.add(nameY, 2, 3);

        nameX = new NumberInputSpinner(printProperties.getNameX());
        gridPane.add(nameX, 3, 3);

        // High price
        highPriceFont = new NumberInputSpinner(printProperties.getHighPriceFont());
        gridPane.add(highPriceFont, 1, 4);

        highPriceY = new NumberInputSpinner(printProperties.getHighPriceY());
        gridPane.add(highPriceY, 2, 4);

        highPriceX = new NumberInputSpinner(printProperties.getHighPriceX());
        gridPane.add(highPriceX, 3, 4);

        // Low price
        lowPriceFont = new NumberInputSpinner(printProperties.getLowPriceFont());
        gridPane.add(lowPriceFont, 1, 5);

        lowPriceY = new NumberInputSpinner(printProperties.getLowPriceY());
        gridPane.add(lowPriceY, 2, 5);

        lowPriceX = new NumberInputSpinner(printProperties.getLowPriceX());
        gridPane.add(lowPriceX, 3, 5);

        // Date
        dateFont = new NumberInputSpinner(printProperties.getDateFont());
        gridPane.add(dateFont, 1, 6);

        dateY = new NumberInputSpinner(printProperties.getDateY());
        gridPane.add(dateY, 2, 6);

        dateX = new NumberInputSpinner(printProperties.getDateX());
        gridPane.add(dateX, 3, 6);

        // Paper height
        paperHeight = new NumberInputSpinner(printProperties.getTicketHeight());
        gridPane.add(paperHeight, 0, 1);

        productCounter = new NumberInputSpinner(printProperties.getProductCounter());
        gridPane.add(productCounter, 0, 3);

        // Radio buttons
        switch (printProperties.getPaperType()){
            case A4:
                a4.setSelected(true);
                break;
            case A5:
                a5.setSelected(true);
                break;
        }
    }


    public void doTest(ActionEvent actionEvent) {
        // Get printer
        PrintService printService = printers.getValue();

        if (printService != null){
            // Read printing data from scene
            PrintProperties properties = getPrintProperties();

            Product product = new Product("111", String.valueOf("این یک تست است"), "5000", "4000", "1");
            List<Product> products = getListOfDuplicateProducts(product, properties.getProductCounter());

            ProductPrinter printer = new ProductPrinter(new ProductPaper(products, properties), printService);
            try {
                printer.print();
            }catch (PrinterAbortException exception){
                logger.info("Printer aborted!");
                exception.printStackTrace();
            }
            catch (Exception exception) {
                // TODO working for alerting exception message
                alert("خطایی هنگام عملیات رخ داد!", "لطفا متن خطا را چک کنید", Alert.AlertType.ERROR);
                exception.printStackTrace();
            }
        }else {
            alert("پرینتر انتخاب نشد!", "لطفا یک پرینتر را انتخاب کرده و دوباره تلاش کنید", Alert.AlertType.WARNING);
        }
    }

    public PrintProperties getPrintProperties(){
        PrintProperties printProperties = new PrintProperties();

        printProperties.setNameFont(nameFont.getValue());
        printProperties.setNameX(nameX.getValue());
        printProperties.setNameY(nameY.getValue());

        printProperties.setDiscountFont(discountFont.getValue());
        printProperties.setDiscountX(discountX.getValue());
        printProperties.setDiscountY(discountY.getValue());

        printProperties.setHighPriceFont(highPriceFont.getValue());
        printProperties.setHighPriceX(highPriceX.getValue());
        printProperties.setHighPriceY(highPriceY.getValue());

        printProperties.setLowPriceFont(lowPriceFont.getValue());
        printProperties.setLowPriceY(lowPriceY.getValue());
        printProperties.setLowPriceX(lowPriceX.getValue());

        printProperties.setDateFont(dateFont.getValue());
        printProperties.setDateX(dateX.getValue());
        printProperties.setDateY(dateY.getValue());

        printProperties.setTicketHeight(paperHeight.getValue());

        printProperties.setProductCounter(productCounter.getValue());

        if (a5.isSelected())
            printProperties.setPaperType(PrintProperties.PaperType.A5);
        else if (a4.isSelected())
            printProperties.setPaperType(PrintProperties.PaperType.A4);

        return printProperties;
    }

    public void alert(String header, String footer, Alert.AlertType type){
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setHeaderText(String.valueOf(header));
        alert.setContentText(String.valueOf(footer));
        alert.showAndWait();
    }

    private void saveData(){
        PrintProperties printProperties = getPrintProperties();

        PrintProperties.serializeToXml(printProperties);
    }

    private List<Product> getListOfDuplicateProducts(Product product, int size){
        List<Product> products = new ArrayList<>();

        for (int i = 0; i < size ; i++){
            products.add(product);
        }

        return products;


    }
}
