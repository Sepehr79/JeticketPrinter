package com.ansar.jeticketprinter.view;

import com.ansar.jeticketprinter.model.dto.ProductsManager;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;

public class CounterCell extends TableCell<ProductsManager, Boolean> {

    private final TableView<ProductsManager> tableView;

    public CounterCell(TableView<ProductsManager> tableView) {
        this.tableView = tableView;
    }

    @Override
    protected void updateItem(Boolean t, boolean empty) {
        super.updateItem(t, empty);
        if(!empty){
            this.setText(String.valueOf(this.getTableRow().getIndex() + 1));
        }else
            this.setText("");
    }
}
