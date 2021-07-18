package com.ansar.jeticketprinter.model.entity;

import com.ansar.jeticketprinter.model.pojo.Product;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.logging.Logger;

public class Products {

    private static final Logger logger = Logger.getLogger(Products.class.getName());

    private final Product product;
    private BigDecimal count;

    // Change table fields by the user
    private boolean highPriceSetterUsed = false;
    private boolean lowPriceSetterUsed = false;
    private boolean nameSetterUsed = false;

    public Products(String barcode, String name, String highPrice, String lowPrice, BigDecimal count) {
        product = new Product(barcode, name, new BigDecimal(highPrice), new BigDecimal(lowPrice));
        setCount(count);
    }

    public String getBarcode() {
        return this.product.getBarcode();
    }

    public void setBarcode(String barcode) {
        this.product.setBarcode(barcode);
    }

    public String getHighPrice() {
        if (!highPriceSetterUsed)
            return String.valueOf(product.getHighPrice().multiply(count).intValue());
        // If user changes the field then return value without any processing
        return String.valueOf(product.getHighPrice());
    }

    public void setHighPrice(String highPrice) {
        highPriceSetterUsed = true;
        if (highPrice == null)
            this.product.setHighPrice(new BigDecimal("0"));
        else if (highPrice.trim().matches("[0-9]*\\.?[0-9]*"))
            this.product.setHighPrice(new BigDecimal(highPrice.trim()));
        else
            throw new IllegalArgumentException("You must enter a number");
    }

    public String getLowPrice() {
        if (!lowPriceSetterUsed)
            return String.valueOf(product.getLowPrice().multiply(count).intValue());
        // If user changes the field then return value without any processing
        return String.valueOf(product.getLowPrice());
    }

    public void setLowPrice(String lowPrice) {
        lowPriceSetterUsed = true;
        if (lowPrice == null)
            this.product.setLowPrice(new BigDecimal("0"));
        else if (lowPrice.trim().matches("[0-9]*\\.?[0-9]*"))
            this.product.setLowPrice(new BigDecimal(lowPrice.trim()));
        else
            throw new IllegalArgumentException("You must enter a number");
    }

    public String getName() {
        // If user changes the field then return value without any processing
        if (nameSetterUsed)
            return String.valueOf(product.getName());

        float count = this.count.floatValue();
        if (count > 1)
            return String.valueOf(product.getName() + " " + this.count.intValue() + (" عددی "));
        else if (count < 1)
            return String.valueOf(product.getName() + " " + (int)(count * 1000) + (" گرمی "));
        else
            return product.getName();
    }

    public void setName(String name) {
        nameSetterUsed = true;
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
        BigDecimal highPrice = new BigDecimal(getHighPrice());
        BigDecimal lowPrice = new BigDecimal(getLowPrice());

        logger.info("High price: " + highPrice.intValue());
        logger.info("Low price: " +  lowPrice.intValue());

        BigDecimal calculating =  highPrice.subtract(lowPrice); // product.getHighPrice().subtract(product.getLowPrice());

        try {
            calculating = calculating.divide( highPrice , MathContext.DECIMAL128);
        }catch (ArithmeticException | NumberFormatException exception){
            logger.info("Exception on calculating discount");
           // exception.printStackTrace();
            calculating = new BigDecimal("0");
        }


        calculating = calculating.multiply(new BigDecimal("100"));

        logger.info("Final: " + calculating);

        return String.valueOf(calculating.intValue());
    }
}
