package com.ansar.jeticketprinter.model.entity;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;
import java.util.logging.Logger;

public class Product {

    private static final Logger logger = Logger.getLogger(Product.class.getName());

    private String barcode;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal count;
    private String name;

    public Product(String barcode, String name, String highPrice, String lowPrice, String count) {
        this.barcode = barcode.trim();
        this.highPrice = new BigDecimal(highPrice.trim());
        this.lowPrice = new BigDecimal(lowPrice.trim());
        this.count = new BigDecimal(count.trim());
        this.name = name.trim();
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getHighPrice() {
        return highPrice.toString();
    }

    public void setHighPrice(String highPrice) {
        if (highPrice.trim().matches("[0-9]*\\.?[0-9]*"))
            this.highPrice = new BigDecimal(highPrice.trim());
        else
            throw new IllegalArgumentException("You must enter a number");
    }

    public String getLowPrice() {
        return lowPrice.toString();
    }

    public void setLowPrice(String lowPrice) {
        if (lowPrice.trim().matches("[0-9]*\\.?[0-9]*"))
            this.lowPrice = new BigDecimal(lowPrice.trim());
        else
            throw new IllegalArgumentException("You must enter a number");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(String count) {
        if (count.trim().matches("[0-9]*\\.?[0-9]*"))
            this.count = new BigDecimal(count.trim());
        else
            throw new IllegalArgumentException("You must enter a number");
    }

    /**
     * Calculates discount
     * @return
     */
    public String getDiscount() {
        try {

            logger.info("High price: " + highPrice);
            logger.info("Low price: " + lowPrice);

            BigDecimal calculating = highPrice.subtract(lowPrice);

            logger.info("Subtracted price:" + calculating);

            calculating = calculating.divide(highPrice, MathContext.DECIMAL128);

            logger.info("Divided price: " + calculating);

            calculating = calculating.multiply(new BigDecimal("100"));

            logger.info("Final: " + calculating);

            return String.valueOf(calculating.intValue());
        }catch (NumberFormatException exception){
            exception.printStackTrace();
            logger.info("Exception in calculating discount");
        }
        return "-1";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return this.barcode.equals(product.barcode);
    }

    @Override
    public int hashCode() {
        return this.barcode.hashCode();
    }
}
