package com.ansar.jeticketprinter.model.entity;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.logging.Logger;

public class Product {

    private static final Logger logger = Logger.getLogger(Product.class.getName());

    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal count;
    private String name;
    private String barcode;

    public Product(String barcode, String name, String highPrice, String lowPrice, String count) {
        setBarcode(barcode);
        setHighPrice(highPrice);
        setLowPrice(lowPrice);
        setCount(count);
        setName(name);
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        if (barcode == null)
            this.barcode = "";
        else
            this.barcode = barcode.trim();
    }

    public String getHighPrice() {
        return String.valueOf(highPrice.multiply(count).intValue());
    }

    public void setHighPrice(String highPrice) {
        if (highPrice == null)
            this.highPrice = new BigDecimal("0");
        else if (highPrice.trim().matches("[0-9]*\\.?[0-9]*"))
            this.highPrice = new BigDecimal(highPrice.trim());
        else
            throw new IllegalArgumentException("You must enter a number");
    }

    public String getLowPrice() {
        return String.valueOf(lowPrice.multiply(count).intValue());
    }

    public void setLowPrice(String lowPrice) {
        if (lowPrice == null)
            this.lowPrice = new BigDecimal("0");
        else if (lowPrice.trim().matches("[0-9]*\\.?[0-9]*"))
            this.lowPrice = new BigDecimal(lowPrice.trim());
        else
            throw new IllegalArgumentException("You must enter a number");
    }

    public String getName() {
        float count = this.count.floatValue();
        if (count > 1)
            return String.valueOf(name + " " + this.count.intValue() + String.valueOf(" عددی "));
        else if (count < 1)
            return String.valueOf(name + " " + (int)(count * 1000) + String.valueOf(" گرمی "));
        else
            return name;
    }

    public void setName(String name) {
        if (name == null)
            this.name = "";
        else
            this.name = name.trim();
    }

    public String getCount() {
        return count.toString();
    }

    public void setCount(String count) {
        if (count == null)
            this.count = new BigDecimal("1");
        else if (count.trim().matches("[0-9]*\\.?[0-9]*"))
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

            calculating = calculating.divide(highPrice, MathContext.DECIMAL128);

            calculating = calculating.multiply(new BigDecimal("100"));

            logger.info("Final: " + calculating);

            return String.valueOf(calculating.intValue());
        }catch (Exception exception){
            logger.info("Exception in calculating discount: " + exception.getMessage());
            exception.printStackTrace();
        }
        return "";
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

    @Override
    public String toString() {
        return "Product{" +
                "barcode='" + getBarcode() + '\'' +
                ", highPrice=" + getHighPrice() +
                ", lowPrice=" + getLowPrice() +
                ", count=" + getCount() +
                ", name='" + getName() + '\'' +
                ", discount='" + getDiscount() + '\'' +
                '}';
    }
}
