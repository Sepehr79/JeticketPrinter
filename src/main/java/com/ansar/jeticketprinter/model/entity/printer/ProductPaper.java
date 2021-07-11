package com.ansar.jeticketprinter.model.entity.printer;

import com.ansar.jeticketprinter.model.entity.Product;
import com.github.mfathi91.time.PersianDate;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.List;

public class ProductPaper implements Printable {

    private List<Product> products;
    private PrintProperties printProperties;

    private static final float CM_TO_PX =  37.795280352161f;


    public ProductPaper(List<Product>  products, PrintProperties properties){
        this.products = products;
        this.printProperties = properties;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex  < (float) products.size() / printProperties.getProductCounter()){
            Graphics2D g2d = (Graphics2D)graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

            PersianDate persianDate = PersianDate.now();
            int month = persianDate.getMonthValue();
            int day = persianDate.getDayOfMonth();

            int productLength = 0;

           for (int i = 0; i < printProperties.getProductCounter() && i < products.size(); i++){
                g2d.setFont(new Font("B Yekan", Font.PLAIN, printProperties.getNameFont()));
                g2d.drawString(products.get(pageIndex * printProperties.getProductCounter() + i).getName(),
                        ((printProperties.getNameX() * CM_TO_PX - g2d.getFontMetrics().stringWidth(products.get(pageIndex * printProperties.getProductCounter() + i).getName()))),
                        (printProperties.getNameY() * CM_TO_PX + productLength));

                g2d.setFont(new Font("B Yekan", Font.PLAIN, printProperties.getDiscountFont()));
                g2d.drawString(products.get(pageIndex * printProperties.getProductCounter() + i).getDiscount(),
                        (printProperties.getDiscountX() * CM_TO_PX - g2d.getFontMetrics().stringWidth(products.get(pageIndex * printProperties.getProductCounter() + i).getDiscount())) ,
                        (printProperties.getDiscountY() * CM_TO_PX + productLength) );

                g2d.setFont(new Font("B Yekan", Font.PLAIN, printProperties.getHighPriceFont()));
                g2d.drawString(products.get(pageIndex * printProperties.getProductCounter() + i).getHighPrice(),
                        (printProperties.getHighPriceX() * CM_TO_PX - g2d.getFontMetrics().stringWidth(products.get(pageIndex * printProperties.getProductCounter() + i).getHighPrice())) ,
                        (printProperties.getHighPriceY() * CM_TO_PX + productLength) );

                g2d.setFont(new Font("B Yekan", Font.PLAIN, printProperties.getLowPriceFont()));
                g2d.drawString(products.get(pageIndex * printProperties.getProductCounter() + i).getLowPrice(),
                        (printProperties.getLowPriceX() * CM_TO_PX - g2d.getFontMetrics().stringWidth(products.get(pageIndex * printProperties.getProductCounter() + i).getLowPrice())) ,
                        (printProperties.getLowPriceY() * CM_TO_PX + productLength) );

                g2d.setFont(new Font("B Yekan", Font.PLAIN, printProperties.getDateFont()));
                g2d.drawString( + month + "/" + day,
                        (printProperties.getDateX() * CM_TO_PX - g2d.getFontMetrics().stringWidth(month + "/" + day)) ,
                        (printProperties.getDateY() * CM_TO_PX + productLength) );

                productLength += printProperties.getTicketHeight() * CM_TO_PX;
            }

            return PAGE_EXISTS;
        }

        return NO_SUCH_PAGE;
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
