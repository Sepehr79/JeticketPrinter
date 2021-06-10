package com.ansar.jeticketprinter.model.entity;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.time.LocalTime;
import java.util.List;

public class ProductPaper implements Printable {

    private List<Product> products;
    private PrintProperties printProperties;

    public ProductPaper(List<Product>  products, PrintProperties properties){
        this.products = products;
        this.printProperties = properties;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D)graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        int paperSize = 0;
        for (Product product:products){
            g2d.setFont(new Font("B Nazanin", Font.PLAIN, printProperties.getNameFont()));
            g2d.drawString(product.getName(), printProperties.getNameX(), printProperties.getNameY() + paperSize);

            g2d.setFont(new Font("B Nazanin", Font.PLAIN, printProperties.getDiscountFont()));
            g2d.drawString(product.getDiscount(), printProperties.getDiscountX(), printProperties.getDiscountY() + paperSize);

            g2d.setFont(new Font("B Nazanin", Font.PLAIN, printProperties.getHighPriceFont()));
            g2d.drawString(product.getHighPrice(), printProperties.getHighPriceX(), printProperties.getHighPriceY() + paperSize);

            g2d.setFont(new Font("B Nazanin", Font.PLAIN, printProperties.getLowPriceFont()));
            g2d.drawString(product.getLowPrice(), printProperties.getLowPriceX(), printProperties.getLowPriceY() + paperSize);

            g2d.setFont(new Font("B Nazanin", Font.PLAIN, printProperties.getDateFont()));
            g2d.drawString(LocalTime.now().toString(), printProperties.getDateX(), printProperties.getDateY());

            paperSize += printProperties.getPaperHeight();
        }

        // tell the caller that this page is part
        // of the printed document
        return PAGE_EXISTS;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public PrintProperties getPrintProperties() {
        return printProperties;
    }

    public void setPrintProperties(PrintProperties printProperties) {
        this.printProperties = printProperties;
    }
}
