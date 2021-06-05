package com.ansar.jeticketprinter;

import com.ansar.jeticketprinter.view.ViewLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.print.Doc;
import javax.print.SimpleDoc;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class Main extends Application implements Printable {
    public static void main(String [] args) throws PrinterException {
        launch(args);
        //Doc myDoc = new SimpleDoc(null, null, null);



        //PrinterJob job = PrinterJob.getPrinterJob();

        //job.setPrintService(PrinterJob.lookupPrintServices()[2]);

        //job.setPrintable(new Main());

        //job.print();

    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {

        // We have only one page, and 'page'
        // is zero-based
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        // User (0,0) is typically outside the
        // imageable area, so we must translate
        // by the X and Y values in the PageFormat
        // to avoid clipping.
        Graphics2D g2d = (Graphics2D)graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        // Now we perform our rendering
        g2d.drawString("Hello world!", 100, 100);

        // tell the caller that this page is part
        // of the printed document
        return PAGE_EXISTS;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = ViewLoader.getMainPage();

        primaryStage.setScene(new Scene(root));

        primaryStage.show();
    }
}