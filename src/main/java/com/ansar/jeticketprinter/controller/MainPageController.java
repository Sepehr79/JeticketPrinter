package com.ansar.jeticketprinter.controller;

import com.ansar.jeticketprinter.model.dto.Products;
import com.ansar.jeticketprinter.model.dto.ProductsManager;
import com.ansar.jeticketprinter.model.pojo.IntervalProduct;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainPageController implements Initializable {

    @FXML private IntervalTabController intevalTabController;

    @FXML private MainController mainTabController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        intevalTabController.getSendToMainPage().setOnAction(event -> {
            List<ProductsManager> managers = new ArrayList<>();
            //List<IntervalProduct> intervalProducts = new ArrayList<>(intevalTabController.getTable().getItems());
            intevalTabController.getTable().getItems().stream().forEach(intervalProduct -> {
                ProductsManager manager = new ProductsManager(intervalProduct.getId(),
                        intervalProduct.getName(),
                        intervalProduct.getPriceConsumer(),
                        intervalProduct.getPriceForosh(), "1");

                managers.add(manager);
            });

            mainTabController.getTableView().getItems().addAll(managers);

            mainTabController.getTableView().getItems().add(new ProductsManager("1513", "test", "456", "46513", "1"));
        });
    }
}
