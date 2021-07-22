package com.ansar.jeticketprinter.model.pojo;

import java.util.logging.Logger;

public class WindowProperties {
    private static final Logger logger = Logger.getLogger(WindowProperties.class.getName());

    private double width = 1121;
    private double height = 750;

    public WindowProperties(int width, int height) {
        this.width = width;
        this.height = height;
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
