package com.ansar.jeticketprinter.model.entity;

import javax.print.PrintService;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;

public class ProductPrinter {

    private ProductPaper productPaper;
    private PrintService printService;

    public ProductPrinter(ProductPaper paper, PrintService printService){
        this.productPaper =  paper;
        this.printService = printService;
    }

    public void print() throws PrinterException {
        PrinterJob printerJob = PrinterJob.getPrinterJob();

        printerJob.setPrintable(productPaper);
        printerJob.setPrintService(printService);

        printerJob.print();
    }

    public ProductPaper getProductPaper() {
        return productPaper;
    }

    public void setProductPaper(ProductPaper productPaper) {
        this.productPaper = productPaper;
    }

    public PrintService getPrintService() {
        return printService;
    }

    public void setPrintService(PrintService printService) {
        this.printService = printService;
    }
}
