package com.ansar.jeticketprinter.view;

import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

public class DialogViewer {

    private DialogViewer(){

    }

    public static void showDialog(String header, String message, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle("پیام");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        alert.getDialogPane().setStyle("-fx-font-family: 'B Yekan';-fx-font-size: 20");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("تایید");

        alert.showAndWait();
    }

}
