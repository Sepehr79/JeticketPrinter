package com.ansar.jeticketprinter.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Objects;

public class ViewLoader {

    public static final String MAIN_PAGE = "MainPage.fxml";
    public static final String SETTING_PAGE = "Settings.fxml";
    public static final String INTERVAL_PAGE = "IntervalTab.fxml";
    public static final String MAIN_TAB = "MainTab.fxml";
    public static final String NAME_SEARCHING_PAGE = "nameSearchingTab.fxml";

    private ViewLoader(){

    }

    public static Parent getPage(String pageName) throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(ViewLoader.class.getResource(pageName)));
    }

}
