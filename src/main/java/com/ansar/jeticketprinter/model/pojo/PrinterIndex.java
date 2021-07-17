package com.ansar.jeticketprinter.model.pojo;

import com.google.gson.Gson;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class PrinterIndex {

    private static final Logger logger = Logger.getLogger(PrinterIndex.class.getName());

    private int indexNumber;

    public PrinterIndex() {
    }

    public static void toJson(PrinterIndex printerIndex){
        try(Writer writer = Files.newBufferedWriter(Paths.get("printerIndex.json"))) {
            new Gson().toJson(printerIndex, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PrinterIndex fromJson(){
        try (Reader reader = Files.newBufferedReader(Paths.get("printerIndex.json"))){
            return new Gson().fromJson(reader, PrinterIndex.class);
        } catch (Exception e) {
            logger.info("Json file not found");
            //e.printStackTrace();
        }
        return new PrinterIndex();
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
