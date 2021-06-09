package com.ansar.jeticketprinter.model.entity;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class IntegerInputSpinner extends Spinner<Integer> {

    public IntegerInputSpinner(Integer number){
        setEditable(true);

        setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-10000, 10000, number));

        getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("[-]?\\d*")){
                getEditor().setText(oldValue);
            }
        });


    }

}
