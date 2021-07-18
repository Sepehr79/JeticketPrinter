package com.ansar.jeticketprinter.view;

import com.ansar.jeticketprinter.model.entity.ProductsManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;

public class ButtonCell extends TableCell<ProductsManager, Boolean> {
    final Button cellButton = new Button(String.valueOf("×"));

    public ButtonCell(TableView<ProductsManager> tblView) {

        cellButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                int selectIndex = getTableRow().getIndex();

                tblView.getItems().remove(selectIndex);
            }
        });
    }

    @Override
    protected void updateItem(Boolean t, boolean empty) {
        super.updateItem(t, empty);
        if(!empty){
            setGraphic(cellButton);
        }else
            setGraphic(null);
    }
}
