package com.ansar.jeticketprinter;

import com.ansar.jeticketprinter.model.pojo.WindowProperties;
import com.ansar.jeticketprinter.view.ViewLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.print.PrinterException;

public class Main extends Application {

    private WindowProperties properties;

    public static void main(String [] args) throws PrinterException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = ViewLoader.getMainPage();
        primaryStage.setScene(new Scene(root));

        primaryStage.setWidth(properties.getWidth());
        primaryStage.setHeight(properties.getHeight());

        primaryStage.widthProperty().addListener(((observable, oldValue, newValue) -> {
            properties.setWidth(newValue.doubleValue());
        }));

        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            properties.setHeight(newValue.doubleValue());
        });

        primaryStage.show();
    }

    @Override
    public void init(){
        properties = WindowProperties.fromJson();
    }

    @Override
    public void stop(){
        WindowProperties.toJson(properties);
    }
}