package com.ansar.jeticketprinter.model.entity;

import java.math.BigDecimal;

public class ProductsManager {

    private final Products products = new Products();

    public ProductsManager(String barcode, String name, String highPrice, String lowPrice, String count){
        setBarcode(barcode);
        setName(name);
        setHighPrice(highPrice);
        setLowPrice(lowPrice);
        setCount(count);
    }

    public String getBarcode() {
        return this.products.getBarcode();
    }

    public void setBarcode(String barcode) {
        this.products.setBarcode(barcode);
    }

    public String getHighPrice() {
        return String.valueOf(products.getHighPrice());
    }

    public void setHighPrice(String highPrice) {
        products.setHighPrice(highPrice);
    }

    public String getLowPrice() {
        return String.valueOf(products.getLowPrice());
    }

    public void setLowPrice(String lowPrice) {
        this.products.setLowPrice(lowPrice);
    }

    public String getName() {
        return products.getName();
    }

    public void setName(String name) {
        this.products.setName(name);
    }

    public String getCount() {
        return products.getCount().toString();
    }

    public void setCount(String count) {
        if (count == null)
            this.products.setCount(new BigDecimal(""));
        else if (count.trim().matches("[0-9]*\\.?[0-9]*"))
            this.products.setCount(new BigDecimal(count.trim()));
        else
            throw new IllegalArgumentException("You must enter a number");
    }

    /**
     * Calculates discount
     */
    public String getDiscount()  {
        return products.getDiscount();
    }

}
