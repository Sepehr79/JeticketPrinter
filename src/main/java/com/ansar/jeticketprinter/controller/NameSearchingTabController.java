package com.ansar.jeticketprinter.controller;

import com.ansar.jeticketprinter.model.database.api.OpenedDatabaseApi;
import com.ansar.jeticketprinter.model.dto.ProductsManager;
import com.ansar.jeticketprinter.model.pojo.ConnectionProperties;
import com.ansar.jeticketprinter.model.pojo.SearchingType;
import com.ansar.jeticketprinter.view.ButtonCell;
import com.ansar.jeticketprinter.view.CounterCell;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Set;

public class NameSearchingTabController implements Initializable {

    public RadioButton nameSearchingStart;
    public ToggleGroup nameSearchingGroup;
    public RadioButton nameSearchingMiddle;
    public RadioButton nameSearchingAll;
    public TextField name;
    public TextField anbar;
    public Button search;
    public Button sendToMainPage;
    public TableView<ProductsManager> table;
    public TableColumn<ProductsManager, Boolean> productCounterColumn;
    public TableColumn<ProductsManager, String> productBarcodeColumn;
    public TableColumn<ProductsManager, String> productNameColumn;
    public TableColumn<ProductsManager, String> productPriceConsumerColumn;
    public TableColumn<ProductsManager, String> productPriceForoshColumn;
    public TableColumn<ProductsManager, Boolean> productDeleteColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mapColumnsToProduct();
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


    public void doSearch(ActionEvent actionEvent) {
        searchProducts();
    }

    public void sendToMainPage(ActionEvent actionEvent) {
    }

    public void doClean(ActionEvent actionEvent) {
        table.getItems().clear();
    }

    private void searchProducts(){
        SearchingType searchingType = null;
        if (nameSearchingStart.isSelected())
            searchingType = SearchingType.START;
        else if (nameSearchingMiddle.isSelected())
            searchingType = SearchingType.MIDDLE;
        else
            searchingType = SearchingType.ALL;

        String name = this.name.getText();
        String anbar = this.anbar.getText();

        ConnectionProperties properties = ConnectionProperties.deserializeFromXml();
        properties.setAnbar(anbar);

        OpenedDatabaseApi api = OpenedDatabaseApi.getInstance();
        Set<ProductsManager> managers = null;
        try {
            api.openConnection(properties);
            managers = api.searchProductsByName(name, searchingType);
            api.closeConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if (managers != null){
            table.getItems().clear();
            table.getItems().addAll(managers);

            if (table.getItems().size() > 0)
                Platform.runLater(() -> {
                    table.requestFocus();
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
}
