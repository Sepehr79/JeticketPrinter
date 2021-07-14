package com.ansar.jeticketprinter.view.fonts;


import com.ansar.jeticketprinter.printer.PrintProperties;

import java.awt.*;
import java.util.Objects;

public class FontLoader {

    private FontLoader(){

    }

    public static Font getBYekan(){
        PrintProperties properties = PrintProperties.deserializeFromXml();
        Font font = new Font(FontLoader.class.getResource("Yekan.ttf").toExternalForm() , Font.PLAIN, properties.getNameFont());
        return font;
    }

    public static Font getBadr(){
        PrintProperties properties = PrintProperties.deserializeFromXml();
        Font font = new Font(FontLoader.class.getResource("badr.ttf").toExternalForm() , Font.BOLD, properties.getNameFont());
        return font;
    }

    public static Font getNazanin(){
        PrintProperties properties = PrintProperties.deserializeFromXml();
        Font font = new Font(FontLoader.class.getResource("Nazanin.ttf").toExternalForm() , Font.BOLD, properties.getNameFont());
        return font;
    }

}
