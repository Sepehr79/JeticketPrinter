package com.ansar.jeticketprinter.model.dto;

import com.github.mfathi91.time.PersianDate;

public class DateTimeProperties {

    private String fromDate;

    private String toDate;

    private String fromTime;

    private String toTime;

    public DateTimeProperties(String fromDate, String toDate, String fromTime, String toTime) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public DateTimeProperties() {
    }

    public String getFromDate() {
        return getGregorianDate(this.fromDate);
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return getGregorianDate(this.toDate);
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    private String getGregorianDate(String date){
        return PersianDate.of(
                Integer.parseInt(date.split("/")[0]),
                Integer.parseInt(date.split("/")[1]),
                Integer.parseInt(date.split("/")[2])).toGregorian().toString();
    }
}
