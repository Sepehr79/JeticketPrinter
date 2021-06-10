package com.ansar.jeticketprinter.model.entity;

import org.junit.Assert;
import org.junit.Test;

public class TestPrinterProperties {

    @Test
    public void testCreateProperties(){

        PrintProperties properties = new PrintProperties();

        Assert.assertEquals(0, (int) properties.getDateFont());

        System.out.println(properties);

    }

    @Test
    public void testSerialize(){
        PrintProperties properties = new PrintProperties();
        properties.setNameFont(5);

        PrintProperties.serializeToXml(properties);

        PrintProperties serialized = PrintProperties.deserializeFromXml();

        Assert.assertEquals(serialized.getNameFont(), 5);
    }

}
