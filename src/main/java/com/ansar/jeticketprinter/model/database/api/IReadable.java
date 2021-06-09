package com.ansar.jeticketprinter.model.database.api;

import com.ansar.jeticketprinter.model.entity.Product;

import java.sql.SQLException;
import java.util.Set;

public interface IReadable {
    public Set<Product> getProductsById(String ...barcodes) throws SQLException;
}
