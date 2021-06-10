package com.ansar.jeticketprinter.controller;

import com.ansar.jeticketprinter.model.entity.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;

import javax.print.PrintService;
import java.awt.print.PrinterAbortException;
import java.awt.print.PrinterJob;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

public class SettingsController implements Initializable {

    private static final Logger logger = Logger.getLogger(SettingsController.class.getName());

    @FXML private GridPane gridPane;
    @FXML private ChoiceBox<PrintService> printers;

    private IntegerInputSpinner nameX;
    private IntegerInputSpinner nameY;
    private IntegerInputSpinner discountX;
    private IntegerInputSpinner discountY;
    private IntegerInputSpinner highPriceX;
    private IntegerInputSpinner highPriceY;
    private IntegerInputSpinner lowPriceX;
    private IntegerInputSpinner lowPriceY;
    private IntegerInputSpinner dateX;
    private IntegerInputSpinner dateY;

    private IntegerInputSpinner nameFont;
    private IntegerInputSpinner discountFont;
    private IntegerInputSpinner highPriceFont;
    private IntegerInputSpinner lowPriceFont;
    private IntegerInputSpinner dateFont;

    private IntegerInputSpinner paperHeight;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load data
        loadSpinners();

        // Set system printers
        PrintService[] services = PrinterJob.lookupPrintServices();
        printers.setItems(FXCollections.observableArrayList(services));
    }

    public void save(ActionEvent actionEvent) {
        PrintProperties printProperties = getPrintProperties();

        PrintProperties.serializeToXml(printProperties);

        nameX.getScene().getWindow().hide();
    }

    public void reset(ActionEvent actionEvent) {
        loadSpinners();
    }

    private void loadSpinners(){
        // Load data
        PrintProperties printProperties = PrintProperties.deserializeFromXml();

        // Discount
        discountFont = new IntegerInputSpinner(printProperties.getDiscountFont());
        gridPane.add(discountFont, 1, 1);

        discountY = new IntegerInputSpinner(printProperties.getDiscountY());
        gridPane.add(discountY, 2, 1);

        discountX = new IntegerInputSpinner(printProperties.getDiscountX());
        gridPane.add(discountX, 3, 1);

        // Name
        nameFont = new IntegerInputSpinner(printProperties.getNameFont());
        gridPane.add(nameFont, 1, 2);

        nameY = new IntegerInputSpinner(printProperties.getNameY());
        gridPane.add(nameY, 2, 2);

        nameX = new IntegerInputSpinner(printProperties.getNameX());
        gridPane.add(nameX, 3, 2);

        // High price
        highPriceFont = new IntegerInputSpinner(printProperties.getHighPriceFont());
        gridPane.add(highPriceFont, 1, 3);

        highPriceY = new IntegerInputSpinner(printProperties.getHighPriceY());
        gridPane.add(highPriceY, 2, 3);

        highPriceX = new IntegerInputSpinner(printProperties.getHighPriceX());
        gridPane.add(highPriceX, 3, 3);

        // Low price
        lowPriceFont = new IntegerInputSpinner(printProperties.getLowPriceFont());
        gridPane.add(lowPriceFont, 1, 4);

        lowPriceY = new IntegerInputSpinner(printProperties.getLowPriceY());
        gridPane.add(lowPriceY, 2, 4);

        lowPriceX = new IntegerInputSpinner(printProperties.getLowPriceX());
        gridPane.add(lowPriceX, 3, 4);

        // Date
        dateFont = new IntegerInputSpinner(printProperties.getDateFont());
        gridPane.add(dateFont, 1, 5);

        dateY = new IntegerInputSpinner(printProperties.getDateY());
        gridPane.add(dateY, 2, 5);

        dateX = new IntegerInputSpinner(printProperties.getDateX());
        gridPane.add(dateX, 3, 5);

        // Paper height
        paperHeight = new IntegerInputSpinner(printProperties.getPaperHeight());
        gridPane.add(paperHeight, 0, 1);
    }


    public void doTest(ActionEvent actionEvent) {
        PrintService printService = printers.getValue();
        if (printService != null){
            PrintProperties properties = getPrintProperties();
            Product product = new Product("111", "این یک تست است", "5000", "4000", "1");
            List<Product> products = new ArrayList<Product>(Collections.singletonList(product));
            ProductPrinter printer = new ProductPrinter(new ProductPaper(products, properties), printService);
            try {
                printer.print();
            }catch (PrinterAbortException exception){
                logger.info("Printer aborted!");
                exception.printStackTrace();
            }
            catch (Exception exception) {
                alert("An error while printing!", "Please call developer", Alert.AlertType.ERROR);
                exception.printStackTrace();
            }
        }else {
            alert("No printers selected!", "Please select a printer and continue", Alert.AlertType.WARNING);
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

        printProperties.setPaperHeight(paperHeight.getValue());

        return printProperties;
    }

    public void alert(String header, String footer, Alert.AlertType type){
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setHeaderText(header);
        alert.setContentText(footer);
        alert.showAndWait();
    }
}
