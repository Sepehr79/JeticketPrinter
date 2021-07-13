package com.ansar.jeticketprinter.model.database.api;

import com.ansar.jeticketprinter.model.entity.ProductsManager;

import java.sql.SQLException;
import java.util.Set;

public interface IReadable {
    public Set<ProductsManager> getProductsById(String ...barcodes) throws SQLException;
}
