package com.ansar.jeticketprinter.model.dto;

import com.github.mfathi91.time.PersianDate;
import javafx.util.converter.LocalDateStringConverter;

import java.time.LocalDate;

public class DateConvertor {

    private DateConvertor(){

    }

    public static String jalalyToGregorian(String jalaly){
        PersianDate persianDate = PersianDate.of(
                Integer.parseInt(jalaly.split("[/-]")[0]),
                Integer.parseInt(jalaly.split("[/-]")[1]),
                Integer.parseInt(jalaly.split("[-/]")[2])
        );

        return persianDate.toGregorian().toString();
    }

    public static String gregorianToJalaly(String gregorian){
        return PersianDate.fromGregorian(LocalDate.parse(gregorian.replace("/", "-"))).toString();
    }

}
