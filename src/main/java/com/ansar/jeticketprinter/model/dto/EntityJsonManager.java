package com.ansar.jeticketprinter.model.dto;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class EntityJsonManager<T> {

    private static final Logger logger = Logger.getLogger(EntityJsonManager.class.getName());

    public static final String WINDOW_PROPERTIES = "window.json";
    public static final String PRINTER_INDEX = "printerIndex.json";

    public void serializeToJson(T object, String fileName){
        try (Writer writer = Files.newBufferedWriter(Paths.get(fileName))){
            new Gson().toJson(object, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public T deserializeFromJson(String fileName, Class<T> type) throws InstantiationException, IllegalAccessException {
        try(Reader reader = Files.newBufferedReader(Paths.get(fileName))) {
            return new Gson().fromJson(reader, type);
        } catch (Exception exception){
            logger.info("window.json not found");
            //exception.printStackTrace();
        }
        return type.newInstance();
    }


}
