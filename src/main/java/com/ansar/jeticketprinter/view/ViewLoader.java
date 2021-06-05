package com.ansar.jeticketprinter.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Objects;

public class ViewLoader {

    private ViewLoader(){

    }

    public static Parent getMainPage() throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(ViewLoader.class.getResource("MainPage.fxml")));
    }

}
