package com.ansar.jeticketprinter.model.pojo;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class WindowProperties {
    private static final Logger logger = Logger.getLogger(WindowProperties.class.getName());

    private double width = 1121;
    private double height = 750;

    public WindowProperties(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public static void toJson(WindowProperties properties) {
        try(Writer writer = Files.newBufferedWriter(Paths.get("window.json"))) {
            new Gson().toJson(properties, writer);
        } catch (NoSuchFileException exception){
            logger.info("file: window.json not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static WindowProperties fromJson(){
        try(Reader reader = Files.newBufferedReader(Paths.get("window.json"))) {
            return new Gson().fromJson(reader, WindowProperties.class);
        }catch (NoSuchFileException exception){
            logger.info("file: window.json not found");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return new WindowProperties();
    }


    public WindowProperties() {
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
