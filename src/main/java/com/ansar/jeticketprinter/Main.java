package com.ansar.jeticketprinter;

import com.ansar.jeticketprinter.model.dto.EntityJsonManager;
import com.ansar.jeticketprinter.model.pojo.WindowProperties;
import com.ansar.jeticketprinter.view.ViewLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.print.PrinterException;
import java.util.logging.Logger;

public class Main extends Application {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final EntityJsonManager<WindowProperties> manager = new EntityJsonManager<>();
    private static WindowProperties properties;

    public static void main(String [] args) throws PrinterException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = ViewLoader.getIntervalTab();
        primaryStage.setScene(new Scene(root));

//        primaryStage.setWidth(properties.getWidth());
//        primaryStage.setHeight(properties.getHeight());
//
//        primaryStage.widthProperty().addListener(((observable, oldValue, newValue) -> {
//            properties.setWidth(newValue.doubleValue());
//        }));
//
//        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
//            properties.setHeight(newValue.doubleValue());
//        });

        primaryStage.show();
    }

    @Override
    public void init(){
        try {
            properties = manager.deserializeFromJson(EntityJsonManager.WINDOW_PROPERTIES, WindowProperties.class);
        } catch (InstantiationException | IllegalAccessException e) {
            logger.info("Exception on reading window properties");
            e.printStackTrace();
        }
    }

    @Override
    public void stop(){
        manager.serializeToJson(properties, EntityJsonManager.WINDOW_PROPERTIES);
    }
}