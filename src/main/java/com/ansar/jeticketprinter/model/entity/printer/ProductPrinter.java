package com.ansar.jeticketprinter.model.entity.printer;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class ProductPrinter {

    private ProductPaper productPaper;
    private PrintService printService;

    public ProductPrinter(ProductPaper paper, PrintService printService){
        this.productPaper =  paper;
        this.printService = printService;
    }

    public void print() throws PrinterException {

        PrinterJob printerJob = PrinterJob.getPrinterJob();

        int width = Math.round(MediaSize.ISO.A5.getX(MediaSize.MM));
        int height = Math.round(MediaSize.ISO.A5.getY(MediaSize.MM));

        HashPrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
        attr.add(new MediaPrintableArea(0, 0, width, height, MediaPrintableArea.MM));

        printerJob.setPrintable(productPaper);
        printerJob.setPrintService(printService);

        printerJob.print(attr);
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
