package com.ansar.jeticketprinter.controller;

import com.ansar.jeticketprinter.model.database.api.OpenedDatabaseApi;
import com.ansar.jeticketprinter.model.dto.ProductsManager;
import com.ansar.jeticketprinter.model.pojo.ConnectionProperties;
import com.ansar.jeticketprinter.model.pojo.SearchingType;
import com.ansar.jeticketprinter.view.ButtonCell;
import com.ansar.jeticketprinter.view.CounterCell;
import com.ansar.jeticketprinter.view.DialogViewer;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Set;

public class NameSearchingTabController implements Initializable {

    public TextField name;
    public Button search;
    public Button sendToMainPage;
    public TableView<ProductsManager> table;
    public TableColumn<ProductsManager, Boolean> productCounterColumn;
    public TableColumn<ProductsManager, String> productBarcodeColumn;
    public TableColumn<ProductsManager, String> productNameColumn;
    public TableColumn<ProductsManager, String> productPriceConsumerColumn;
    public TableColumn<ProductsManager, String> productPriceForoshColumn;
    public TableColumn<ProductsManager, Boolean> productDeleteColumn;

    public RadioButton nameSearchingStart;
    public RadioButton nameSearchingMiddle;
    public RadioButton nameSearchingAll;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mapColumnsToProduct();
        configEvents();

        name.setOnKeyPressed(event -> {
          Platform.runLater(() ->{
              searchProducts(identifySearchingType());
          });
        });
    }
    private void mapColumnsToProduct() {
        // Readable
        productNameColumn.setCellValueFactory(new PropertyValueFactory<ProductsManager, String>("name"));
        productBarcodeColumn.setCellValueFactory(new PropertyValueFactory<ProductsManager, String>("barcode"));
        productPriceConsumerColumn.setCellValueFactory(new PropertyValueFactory<ProductsManager, String>("highPrice"));
        productPriceForoshColumn.setCellValueFactory(new PropertyValueFactory<ProductsManager, String>("lowPrice"));

        productDeleteColumn.setSortable(false);

        // Delete button
        productDeleteColumn.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        productDeleteColumn.setCellFactory(p -> new ButtonCell<ProductsManager>(table));

        productCounterColumn.setCellFactory(p -> new CounterCell<ProductsManager>(table));
    }


    public void doSearch(KeyEvent actionEvent) {
        if (actionEvent.getCode() == KeyCode.DOWN){
            table.requestFocus();
            table.getSelectionModel().selectFirst();
        } else {
            searchProducts(identifySearchingType());
        }

    }

    public void sendToMainPage(ActionEvent actionEvent) {
    }

    public void doClean(ActionEvent actionEvent) {
        table.getItems().clear();
    }

    public void searchProducts(SearchingType type){

        String name = this.name.getText();

        ConnectionProperties properties = ConnectionProperties.deserializeFromXml();
        OpenedDatabaseApi api = OpenedDatabaseApi.getInstance();
        Set<ProductsManager> managers = null;
        try {
            api.openConnection(properties);
            managers = api.searchProductsByName(name, type);
            api.closeConnection();
        } catch (SQLException throwables) {
            DialogViewer.showDialog(
                    "اخطار",
                    "اتصال با دیتابیس ممکن نیست لطفا تنظیمات اتصال را بررسی کنید",
                    Alert.AlertType.ERROR
            );
        }

        if (managers != null){
            table.getItems().clear();
            table.getItems().addAll(managers);

            if (table.getItems().size() > 0)
                Platform.runLater(() -> {
                    table.getSelectionModel().selectFirst();
                });
        }
    }

    public TableView<ProductsManager> getTable() {
        return table;
    }

    public Button getSendToMainPage() {
        return sendToMainPage;
    }

    public TextField getName() {
        return name;
    }

    public Button getSearch() {
        return search;
    }

    private void configEvents(){
        name.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN){
                table.requestFocus();
                table.getSelectionModel().selectFirst();
            }else if (event.getCode() == KeyCode.ENTER)
                search.requestFocus();
        });

        search.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER)
                searchProducts(identifySearchingType());
        });
    }

    private SearchingType identifySearchingType(){
        SearchingType searchingType = SearchingType.START;
        if (nameSearchingMiddle.isSelected())
            searchingType = SearchingType.MIDDLE;
        else if (nameSearchingAll.isSelected())
            searchingType = SearchingType.ALL;

        return searchingType;
    }

}
