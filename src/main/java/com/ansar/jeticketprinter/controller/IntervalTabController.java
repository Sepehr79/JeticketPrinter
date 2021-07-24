package com.ansar.jeticketprinter.controller;

import com.ansar.jeticketprinter.view.DateTextFiled;
import com.ansar.jeticketprinter.view.TimeTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class IntervalTabController implements Initializable {

    @FXML private GridPane gridPane;

    private static final DateTextFiled fromDate = new DateTextFiled();
    private static final DateTextFiled toDate = new DateTextFiled();

    private static final TimeTextField fromTime = new TimeTextField("00:00");
    private static final TimeTextField toTime = new TimeTextField("23:59");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gridPane.add(toDate, 0, 0);
        gridPane.add(fromDate, 2, 0);
        gridPane.add(fromTime, 2, 1);
        gridPane.add(toTime, 0, 1);
    }

    public void search(ActionEvent actionEvent) {
    }

    public void sendToMain(ActionEvent actionEvent) {
    }
}
