package com.ansar.jeticketprinter.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
                if (event.getCode() == KeyCode.RIGHT && getCaretPosition() == 2)
                    positionCaret(3);
                if (event.getCode() == KeyCode.LEFT && getCaretPosition() == 4)
                    positionCaret(3);
                if (event.getCode() == KeyCode.LEFT && getCaretPosition() == 1)
                    positionCaret(2);

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

        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (getCaretPosition() == 3)
                    positionCaret(4);
                if (getCaretPosition() == 0)
                    positionCaret(1);
            }
        });
    }

    private boolean matchesTimeFormat(String text){
        return text.matches("[0-2]?[0-9]?:[0-5]?[0-9]?");
    }

}
