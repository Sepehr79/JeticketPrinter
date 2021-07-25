package com.ansar.jeticketprinter.model.pojo;

import com.github.mfathi91.time.PersianDate;
import sun.util.resources.LocaleData;

import java.time.LocalDate;

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
        return priceConsumer.split("\\.")[0];
    }

    public void setPriceConsumer(String priceConsumer) {
        this.priceConsumer = priceConsumer;
    }

    public String getPriceForosh() {
        return priceForosh.split("\\.")[0];
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
        String gregorianDate = date.split(" ")[0];
        String time = date.split(" ")[1].substring(0, 5);

        PersianDate persianDate = PersianDate.fromGregorian(LocalDate.parse(gregorianDate));
        return persianDate.toString().replace("-" , "/") + " " + time;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
