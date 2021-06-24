package com.ansar.jeticketprinter.model.database.api;


import java.util.Base64;

public class TextDecoder {

    private TextDecoder(){

    }

    public static String decode(String text, int count){

        for (int i = 0 ; i < count ; i++){
            text = new String(Base64.getDecoder().decode(text.getBytes()));
        }

        return text;
    }

    public static String encode(String text, int count){

        for (int i = 0 ; i < count ; i++){
            text = Base64.getEncoder().encodeToString(text.getBytes());
        }

        return text;
    }


}