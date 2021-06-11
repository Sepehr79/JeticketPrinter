package com.ansar.jeticketprinter;

import com.ansar.jeticketprinter.view.ViewLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.print.PrinterException;

public class Main extends Application {

    public static void main(String [] args) throws PrinterException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = ViewLoader.getMainPage();

        primaryStage.setScene(new Scene(root));

        primaryStage.show();
    }
}