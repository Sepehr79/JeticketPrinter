package com.ansar.jeticketprinter.controller;

import com.ansar.jeticketprinter.model.entity.*;
import com.ansar.jeticketprinter.model.entity.printer.PaperType;
import com.ansar.jeticketprinter.model.entity.printer.PrintProperties;
import com.ansar.jeticketprinter.model.entity.printer.ProductPaper;
import com.ansar.jeticketprinter.model.entity.printer.ProductPrinter;
import com.ansar.jeticketprinter.view.IntegerInputSpinner;
import com.ansar.jeticketprinter.view.ViewLoader;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

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

    @FXML private RadioButton a4;
    @FXML private RadioButton a5;

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

    static final Text test = new Text("این یک تست است.");


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
        discountFont = new IntegerInputSpinner(printProperties.getDiscountFont());
        gridPane.add(discountFont, 1, 2);

        discountY = new IntegerInputSpinner(printProperties.getDiscountY());
        gridPane.add(discountY, 2, 2);

        discountX = new IntegerInputSpinner(printProperties.getDiscountX());
        gridPane.add(discountX, 3, 2);

        // Name
        nameFont = new IntegerInputSpinner(printProperties.getNameFont());
        gridPane.add(nameFont, 1, 3);

        nameY = new IntegerInputSpinner(printProperties.getNameY());
        gridPane.add(nameY, 2, 3);

        nameX = new IntegerInputSpinner(printProperties.getNameX());
        gridPane.add(nameX, 3, 3);

        // High price
        highPriceFont = new IntegerInputSpinner(printProperties.getHighPriceFont());
        gridPane.add(highPriceFont, 1, 4);

        highPriceY = new IntegerInputSpinner(printProperties.getHighPriceY());
        gridPane.add(highPriceY, 2, 4);

        highPriceX = new IntegerInputSpinner(printProperties.getHighPriceX());
        gridPane.add(highPriceX, 3, 4);

        // Low price
        lowPriceFont = new IntegerInputSpinner(printProperties.getLowPriceFont());
        gridPane.add(lowPriceFont, 1, 5);

        lowPriceY = new IntegerInputSpinner(printProperties.getLowPriceY());
        gridPane.add(lowPriceY, 2, 5);

        lowPriceX = new IntegerInputSpinner(printProperties.getLowPriceX());
        gridPane.add(lowPriceX, 3, 5);

        // Date
        dateFont = new IntegerInputSpinner(printProperties.getDateFont());
        gridPane.add(dateFont, 1, 6);

        dateY = new IntegerInputSpinner(printProperties.getDateY());
        gridPane.add(dateY, 2, 6);

        dateX = new IntegerInputSpinner(printProperties.getDateX());
        gridPane.add(dateX, 3, 6);

        // Paper height
        paperHeight = new IntegerInputSpinner(printProperties.getTicketHeight());
        gridPane.add(paperHeight, 0, 1);

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
        PrintService printService = printers.getValue();
        if (printService != null){
            PrintProperties properties = getPrintProperties();
            Product product = new Product("111", test.getText(), "5000", "4000", "1");
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
            alert("پرینتر انتخاب نشد!", "لطفا یک پرینتر را انتخاب کرده و دوباره تلاش کنید", Alert.AlertType.WARNING);
        }
        saveData();
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

        if (a5.isSelected())
            printProperties.setPaperType(PaperType.A5);
        else if (a4.isSelected())
            printProperties.setPaperType(PaperType.A4);

        return printProperties;
    }

    public void alert(String header, String footer, Alert.AlertType type){
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setHeaderText(header);
        alert.setContentText(footer);
        alert.showAndWait();
    }

    private void saveData(){
        PrintProperties printProperties = getPrintProperties();

        PrintProperties.serializeToXml(printProperties);
    }
}
