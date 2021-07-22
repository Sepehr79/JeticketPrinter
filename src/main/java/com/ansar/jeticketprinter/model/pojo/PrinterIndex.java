package com.ansar.jeticketprinter.model.pojo;

import java.util.logging.Logger;

public class PrinterIndex {

    private static final Logger logger = Logger.getLogger(PrinterIndex.class.getName());

    private int indexNumber;

    public PrinterIndex() {
    }

    public PrinterIndex(int indexNumber) {
        this.indexNumber = indexNumber;
    }

    public int getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(int indexNumber) {
        this.indexNumber = indexNumber;
    }
}
