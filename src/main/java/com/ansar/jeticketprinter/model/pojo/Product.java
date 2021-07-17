package com.ansar.jeticketprinter.model.pojo;

import java.math.BigDecimal;

public class Product {

    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private String name;
    private String barcode;

    public Product(String barcode, String name, BigDecimal highPrice, BigDecimal lowPrice) {
       this.barcode = barcode;
       this.name = name;
       this.highPrice = highPrice;
       this.lowPrice = lowPrice;
    }

    public Product(){

    }

    public BigDecimal getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(BigDecimal highPrice) {
        this.highPrice = highPrice;
    }

    public BigDecimal getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(BigDecimal lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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
                ", name='" + getName() + '\'' +
                '}';
    }
}
