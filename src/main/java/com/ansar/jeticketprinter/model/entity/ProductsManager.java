package com.ansar.jeticketprinter.model.entity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class ProductsManager {

    private final Products products;

    private static final NumberFormat numberFormatter = NumberFormat.getNumberInstance(Locale.GERMAN);

    public ProductsManager(String barcode, String name, String highPrice, String lowPrice, String count){
        products = new Products(barcode, name, highPrice, lowPrice, new BigDecimal(count));
    }

    public String getBarcode() {
        return this.products.getBarcode();
    }

    public void setBarcode(String barcode) {
        this.products.setBarcode(barcode);
    }

    public String getHighPrice() {
        String highPrice = products.getHighPrice();
        if (highPrice.matches(".*[0-9].*"))
            return String.valueOf(numberFormatter.format(new BigDecimal(products.getHighPrice())));
        return String.valueOf(highPrice);
    }

    public void setHighPrice(String highPrice) {
        products.setHighPrice(highPrice);
    }

    public String getLowPrice() {
        return String.valueOf(numberFormatter.format(new BigDecimal(products.getLowPrice())));
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
        String discount = products.getDiscount();
        if (discount.length() == 1)
            return "   " + discount + "   ";
        else if (discount.length() == 2){
            return " " + discount + " ";
        }
        return discount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductsManager manager = (ProductsManager) o;
        return Objects.equals(products.getBarcode(), manager.products.getBarcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(products.getBarcode());
    }
}
