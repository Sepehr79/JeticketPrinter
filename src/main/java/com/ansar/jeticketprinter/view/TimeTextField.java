package com.ansar.jeticketprinter.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;

public class TimeTextField extends TextField {

    public TimeTextField(String time){
        if (!matchesTimeFormat(time))
            throw new IllegalArgumentException("Wrong format for numbers(HH:MM)");

        setAlignment(Pos.CENTER);
        setText(time);
        setFont(Font.font("B Yekan"));
        setStyle("-fx-background-color: white;-fx-background-radius: 20px");

        textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (matchesTimeFormat(newValue))
                    setText(newValue);
                else
                    setText(oldValue);
            }
        });

        this.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getText().matches("[0-9]") && getCaretPosition() > 0){
                    int index = getCaretPosition();
                    String date = getText();
                    setText(date.substring(0, getCaretPosition() - 1) + event.getText() + date.substring(getCaretPosition()));
                    if ((index == 2))
                        positionCaret(index + 2);
                    else
                        positionCaret(index + 1);
                }
            }
        });
    }

    private boolean matchesTimeFormat(String text){
        return text.matches("[0-2]?[0-9]?:[0-5]?[0-9]?");
    }

}
