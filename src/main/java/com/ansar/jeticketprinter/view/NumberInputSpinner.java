package com.ansar.jeticketprinter.view;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class NumberInputSpinner extends Spinner<Integer> {

    public NumberInputSpinner(Integer number){
        setEditable(true);

        setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-10000, 10000, number));

        setStyle("-fx-font-family: 'B Yekan'");

        getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("^-?([0]{1}\\.{1}[0-9]+|[1-9]{1}[0-9]*\\.{1}[0-9]+|[0-9]+|0)$")){
                getEditor().setText(newValue);
                getValueFactory().setValue(Integer.valueOf(newValue));
            }
        });

    }

}
