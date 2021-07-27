package com.ansar.jeticketprinter.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Objects;

public class ViewLoader {

    public static final String MAIN_PAGE = "MainPage.fxml";
    public static final String SETTING_PAGE = "Settings.fxml";

    private ViewLoader(){

    }

    public static Parent getPage(String pageName) throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(ViewLoader.class.getResource(pageName)));
    }

}
