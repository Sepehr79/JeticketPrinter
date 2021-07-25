package com.ansar.jeticketprinter.model.pojo;

public class IntervalProduct {

    private String name;

    private String priceConsumer;

    private String priceForosh;

    private String id;

    private String date;

    public IntervalProduct(String name, String priceConsumer, String priceForosh, String id, String date) {
        this.name = name;
        this.priceConsumer = priceConsumer;
        this.priceForosh = priceForosh;
        this.id = id;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriceConsumer() {
        return priceConsumer;
    }

    public void setPriceConsumer(String priceConsumer) {
        this.priceConsumer = priceConsumer;
    }

    public String getPriceForosh() {
        return priceForosh;
    }

    public void setPriceForosh(String priceForosh) {
        this.priceForosh = priceForosh;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
