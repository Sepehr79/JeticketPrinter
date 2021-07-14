package com.ansar.jeticketprinter.printer;

import com.ansar.jeticketprinter.model.entity.ProductsManager;
import com.ansar.jeticketprinter.view.fonts.FontLoader;
import com.github.mfathi91.time.PersianDate;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.List;
import java.util.logging.Logger;

public class ProductPaper implements Printable {

    private static final Logger logger = Logger.getLogger(ProductPaper.class.getName());

    private List<ProductsManager> managers;
    private PrintProperties printProperties;

    private static final float MM_TO_PX =  3.7795280352161f;

    public ProductPaper(List<ProductsManager> managers, PrintProperties properties){
        this.managers = managers;
        this.printProperties = properties;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {

        if (pageIndex < (float) managers.size() / printProperties.getProductCounter()){
            Graphics2D g2d = (Graphics2D)graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

            PersianDate persianDate = PersianDate.now();
            int month = persianDate.getMonthValue();
            int day = persianDate.getDayOfMonth();

            int productLength = 0;

           for (int i = 0; i < printProperties.getProductCounter() && (pageIndex * printProperties.getProductCounter()) + i < managers.size(); i++){
                g2d.setFont(FontLoader.getBYekan());
                g2d.drawString(managers.get(pageIndex * printProperties.getProductCounter() + i).getName(),
                        ((printProperties.getNameX() * MM_TO_PX - g2d.getFontMetrics().stringWidth(managers.get(pageIndex * printProperties.getProductCounter() + i).getName()))),
                        (printProperties.getNameY() * MM_TO_PX + productLength));

                g2d.setFont(new Font("B Yekan", Font.PLAIN, printProperties.getDiscountFont()));
                g2d.drawString(managers.get(pageIndex * printProperties.getProductCounter() + i).getDiscount(),
                        (printProperties.getDiscountX() * MM_TO_PX - g2d.getFontMetrics().stringWidth(managers.get(pageIndex * printProperties.getProductCounter() + i).getDiscount())) ,
                        (printProperties.getDiscountY() * MM_TO_PX + productLength) );

               g2d.setFont(new Font("B Yekan", Font.PLAIN, printProperties.getHighPriceFont()));
                if (!managers.get(pageIndex * printProperties.getProductCounter() + i).getHighPrice().equals("0")){
                    g2d.drawString(managers.get(pageIndex * printProperties.getProductCounter() + i).getHighPrice(),
                            (printProperties.getHighPriceX() * MM_TO_PX - g2d.getFontMetrics().stringWidth(managers.get(pageIndex * printProperties.getProductCounter() + i).getHighPrice())) ,
                            (printProperties.getHighPriceY() * MM_TO_PX + productLength));

                    // Draw line on high price
                    int x1 = (int) (printProperties.getHighPriceX() * MM_TO_PX - g2d.getFontMetrics().stringWidth(managers.get(pageIndex * printProperties.getProductCounter() + i).getHighPrice()));
                    int x2 = (int) (printProperties.getHighPriceX() * MM_TO_PX);
                    int y = (int) (printProperties.getHighPriceY() * MM_TO_PX + productLength - (g2d.getFontMetrics().getHeight() / 6));
                    g2d.drawLine(x1, y, x2, y);
                }else {
                    String message = "بدون قیمت";
                    g2d.drawString(String.valueOf(message),
                            printProperties.getHighPriceX() * MM_TO_PX - g2d.getFontMetrics().stringWidth(message),
                            printProperties.getHighPriceY() * MM_TO_PX + productLength);

                }



                g2d.setFont(new Font("B Yekan", Font.PLAIN, printProperties.getLowPriceFont()));
                g2d.drawString(managers.get(pageIndex * printProperties.getProductCounter() + i).getLowPrice(),
                        (printProperties.getLowPriceX() * MM_TO_PX - g2d.getFontMetrics().stringWidth(managers.get(pageIndex * printProperties.getProductCounter() + i).getLowPrice())) ,
                        (printProperties.getLowPriceY() * MM_TO_PX + productLength) );

                g2d.setFont(new Font("B Yekan", Font.PLAIN, printProperties.getDateFont()));
                g2d.drawString( + month + "/" + day,
                        (printProperties.getDateX() * MM_TO_PX - g2d.getFontMetrics().stringWidth(month + "/" + day)) ,
                        (printProperties.getDateY() * MM_TO_PX + productLength) );

                productLength += printProperties.getTicketHeight() * MM_TO_PX;
            }

            return PAGE_EXISTS;
        }

        return NO_SUCH_PAGE;
    }

    public List<ProductsManager> getProducts() {
        return managers;
    }

    public void setProducts(List<ProductsManager> managers) {
        this.managers = managers;
    }

    public PrintProperties getPrintProperties() {
        return printProperties;
    }

    public void setPrintProperties(PrintProperties printProperties) {
        this.printProperties = printProperties;
    }
}
