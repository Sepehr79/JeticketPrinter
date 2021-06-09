package com.ansar.jeticketprinter.controller;

import com.ansar.jeticketprinter.model.entity.IntegerInputSpinner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML private GridPane gridPane;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setSpinners();



    }

    public void save(ActionEvent actionEvent) {
    }

    public void reset(ActionEvent actionEvent) {
    }

    private void setSpinners(){
        // Discount
        discountFont = new IntegerInputSpinner(0);
        gridPane.add(discountFont, 0, 1);

        discountY = new IntegerInputSpinner(0);
        gridPane.add(discountY, 1, 1);

        discountX = new IntegerInputSpinner(0);
        gridPane.add(discountX, 2, 1);

        // Name
        nameFont = new IntegerInputSpinner(0);
        gridPane.add(nameFont, 0, 2);

        nameY = new IntegerInputSpinner(0);
        gridPane.add(nameY, 1, 2);

        nameX = new IntegerInputSpinner(0);
        gridPane.add(nameX, 2, 2);

        // High price
        highPriceFont = new IntegerInputSpinner(0);
        gridPane.add(highPriceFont, 0, 3);

        highPriceY = new IntegerInputSpinner(0);
        gridPane.add(highPriceY, 1, 3);

        highPriceX = new IntegerInputSpinner(0);
        gridPane.add(highPriceX, 2, 3);

        // Low price
        lowPriceFont = new IntegerInputSpinner(0);
        gridPane.add(lowPriceFont, 0, 4);

        lowPriceY = new IntegerInputSpinner(0);
        gridPane.add(lowPriceY, 1, 4);

        lowPriceX = new IntegerInputSpinner(0);
        gridPane.add(lowPriceX, 2, 4);

        // Date
        dateFont = new IntegerInputSpinner(0);
        gridPane.add(dateFont, 0, 5);

        dateY = new IntegerInputSpinner(0);
        gridPane.add(dateY, 1, 5);

        dateX = new IntegerInputSpinner(0);
        gridPane.add(dateX, 2, 5);
    }


}
