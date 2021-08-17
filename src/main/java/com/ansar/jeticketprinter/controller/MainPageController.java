package com.ansar.jeticketprinter.controller;

import com.ansar.jeticketprinter.model.dto.ProductsManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;

import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    @FXML private Tab nameSearchingTab;
    @FXML private Tab mainTab;
    @FXML private Tab intevalTab;

    @FXML private IntervalTabController intevalTabController;
    @FXML private MainController mainTabController;
    @FXML private NameSearchingTabController nameSearchingTabController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        intevalTabController.getSendToMainPage().setOnAction(event -> {
            List<ProductsManager> managers = new ArrayList<>();
            intevalTabController.getTable().getItems().stream().forEach(intervalProduct -> {
                ProductsManager manager = new ProductsManager(intervalProduct.getId(),
                        intervalProduct.getName(),
                        intervalProduct.getPriceConsumer(),
                        intervalProduct.getPriceForosh(), "1");

                managers.add(manager);
            });
            mainTabController.getTableView().getItems().addAll(managers);
        });

        nameSearchingTabController.getTable().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER){
                ProductsManager productManager = nameSearchingTabController.getTable().getSelectionModel().getSelectedItem();
                mainTabController.getTableView().getItems().add(productManager);
            }
        });


        intevalTab.selectedProperty().addListener(observable -> {
            if (intevalTab.isSelected())
                Platform.runLater(()->{intevalTabController.getFromTime().requestFocus();});
        });

        mainTab.selectedProperty().addListener(observable -> {
            if (mainTab.isSelected())
                Platform.runLater(()->{mainTabController.getBarcode().requestFocus();});
        });
    }
}
