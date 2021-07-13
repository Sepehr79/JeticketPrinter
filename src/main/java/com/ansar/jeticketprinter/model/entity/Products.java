package com.ansar.jeticketprinter.model.entity;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.logging.Logger;

public class Products {

    private static final Logger logger = Logger.getLogger(Products.class.getName());

    private final Product product = new Product();
    private BigDecimal count;

    public Products(String barcode, String name, String highPrice, String lowPrice, BigDecimal count) {
        setBarcode(barcode);
        setName(name);
        setHighPrice(highPrice);
        setLowPrice(lowPrice);
        setCount(count);
    }

    public Products(){

    }

    public String getBarcode() {
        return this.product.getBarcode();
    }

    public void setBarcode(String barcode) {
        this.product.setBarcode(barcode);
    }

    public String getHighPrice() {
        return String.valueOf(product.getHighPrice().multiply(count).intValue());
    }

    public void setHighPrice(String highPrice) {
        if (highPrice == null)
            this.product.setHighPrice(new BigDecimal("0"));
        else if (highPrice.trim().matches("[0-9]*\\.?[0-9]*"))
            this.product.setHighPrice(new BigDecimal(highPrice.trim()));
        else
            throw new IllegalArgumentException("You must enter a number");
    }

    public String getLowPrice() {
        return String.valueOf(product.getLowPrice().multiply(count).intValue());
    }

    public void setLowPrice(String lowPrice) {
        if (lowPrice == null)
            this.product.setLowPrice(new BigDecimal("0"));
        else if (lowPrice.trim().matches("[0-9]*\\.?[0-9]*"))
            this.product.setLowPrice(new BigDecimal(lowPrice.trim()));
        else
            throw new IllegalArgumentException("You must enter a number");
    }

    public String getName() {
        float count = this.count.floatValue();
        if (count > 1)
            return String.valueOf(product.getName() + " " + this.count.intValue() + String.valueOf(" عددی "));
        else if (count < 1)
            return String.valueOf(product.getName() + " " + (int)(count * 1000) + String.valueOf(" گرمی "));
        else
            return product.getName();
    }

    public void setName(String name) {
        if (name == null)
            this.product.setName("");
        else
            this.product.setName(name.trim());
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    /**
     * Calculates discount
     */
    public String getDiscount()  {
        logger.info("High price: " + product.getHighPrice());
        logger.info("Low price: " + product.getLowPrice());

        BigDecimal calculating = product.getHighPrice().subtract(product.getLowPrice());

        calculating = calculating.divide(product.getHighPrice(), MathContext.DECIMAL128);

        calculating = calculating.multiply(new BigDecimal("100"));

        logger.info("Final: " + calculating);

        return String.valueOf(calculating.intValue());
    }
}
