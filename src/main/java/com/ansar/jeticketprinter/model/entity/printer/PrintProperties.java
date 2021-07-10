package com.ansar.jeticketprinter.model.entity.printer;

import javax.xml.bind.JAXB;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class PrintProperties {

    private static final Logger logger = Logger.getLogger(PrintProperties.class.getName());

    private int nameX;
    private int nameY;

    private int highPriceX;
    private int highPriceY;

    private int lowPriceX;
    private int lowPriceY;

    private int discountX;
    private int discountY;

    private int dateX;
    private int dateY;

    private int nameFont;
    private int discountFont;
    private int highPriceFont;
    private int lowPriceFont;
    private int dateFont;

    private int ticketHeight;

    private PaperType paperType;

    public PaperType getPaperType() {
        return paperType;
    }

    public void setPaperType(PaperType paperType) {
        this.paperType = paperType;
    }

    public static void serializeToXml(PrintProperties properties){
        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get("printerProperties.xml"))) {
            JAXB.marshal(properties, writer);
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public static PrintProperties deserializeFromXml(){
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("printerProperties.xml"))){
            return JAXB.unmarshal(reader, PrintProperties.class);
        }catch (NoSuchFileException exception){
            logger.info("PrinterProperties file not found");
        } catch (IOException exception){
            exception.printStackTrace();
        }
        PrintProperties properties = new PrintProperties();
        properties.setTicketHeight(165);

        return properties;
    }

    public int getNameX() {
        return nameX;
    }

    public void setNameX(int nameX) {
        this.nameX = nameX;
    }

    public int getNameY() {
        return nameY;
    }

    public void setNameY(int nameY) {
        this.nameY = nameY;
    }

    public int getHighPriceX() {
        return highPriceX;
    }

    public void setHighPriceX(int highPriceX) {
        this.highPriceX = highPriceX;
    }

    public int getHighPriceY() {
        return highPriceY;
    }

    public void setHighPriceY(int highPriceY) {
        this.highPriceY = highPriceY;
    }

    public int getLowPriceX() {
        return lowPriceX;
    }

    public void setLowPriceX(int lowPriceX) {
        this.lowPriceX = lowPriceX;
    }

    public int getLowPriceY() {
        return lowPriceY;
    }

    public void setLowPriceY(int lowPriceY) {
        this.lowPriceY = lowPriceY;
    }

    public int getDiscountX() {
        return discountX;
    }

    public void setDiscountX(int discountX) {
        this.discountX = discountX;
    }

    public int getDiscountY() {
        return discountY;
    }

    public void setDiscountY(int discountY) {
        this.discountY = discountY;
    }

    public int getDateX() {
        return dateX;
    }

    public void setDateX(int dateX) {
        this.dateX = dateX;
    }

    public int getDateY() {
        return dateY;
    }

    public void setDateY(int dateY) {
        this.dateY = dateY;
    }

    public int getNameFont() {
        return nameFont;
    }

    public void setNameFont(int nameFont) {
        this.nameFont = nameFont;
    }

    public int getDiscountFont() {
        return discountFont;
    }

    public void setDiscountFont(int discountFont) {
        this.discountFont = discountFont;
    }

    public int getHighPriceFont() {
        return highPriceFont;
    }

    public void setHighPriceFont(int highPriceFont) {
        this.highPriceFont = highPriceFont;
    }

    public int getLowPriceFont() {
        return lowPriceFont;
    }

    public void setLowPriceFont(int lowPriceFont) {
        this.lowPriceFont = lowPriceFont;
    }

    public int getDateFont() {
        return dateFont;
    }

    public void setDateFont(int dateFont) {
        this.dateFont = dateFont;
    }

    public int getTicketHeight() {
        return ticketHeight;
    }

    public void setTicketHeight(int ticketHeight) {
        this.ticketHeight = ticketHeight;
    }

    @Override
    public String toString() {
        return "PrintProperties{" +
                "nameX=" + nameX +
                ", nameY=" + nameY +
                ", highPriceX=" + highPriceX +
                ", highPriceY=" + highPriceY +
                ", lowPriceX=" + lowPriceX +
                ", lowPriceY=" + lowPriceY +
                ", discountX=" + discountX +
                ", discountY=" + discountY +
                ", dateX=" + dateX +
                ", dateY=" + dateY +
                ", nameFont=" + nameFont +
                ", discountFont=" + discountFont +
                ", highPriceFont=" + highPriceFont +
                ", lowPriceFont=" + lowPriceFont +
                ", dateFont=" + dateFont +
                ", paperHeight=" + ticketHeight +
                '}';
    }
}
