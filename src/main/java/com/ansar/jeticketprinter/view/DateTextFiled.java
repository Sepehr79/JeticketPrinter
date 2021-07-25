package com.ansar.jeticketprinter.view;

import com.github.mfathi91.time.PersianDate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;

public class DateTextFiled extends TextField  {

    public DateTextFiled() {
        super(PersianDate.now().toString().replace("-", "/"));
        setFont(Font.font("B Yekan"));
        setStyle("-fx-background-color: white;-fx-background-radius: 20px");
        setAlignment(Pos.CENTER);

        this.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getText().matches("[0-9]") && getCaretPosition() > 0){
                    int index = getCaretPosition();
                    String date = getText();
                    setText(date.substring(0, getCaretPosition() - 1) + event.getText() + date.substring(getCaretPosition()));
                    if ((index == 4 || index == 7))
                        positionCaret(index + 2);
                    else
                        positionCaret(index + 1);
                }
            }
        });

        textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.matches("1?[34]?[0-9]?[0-9]?/[0-1]?[0-9]?/[0123]?[0-9]?"))
                    setText(newValue);
                else
                    setText(oldValue);
            }
        });

    }


}
