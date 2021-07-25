package com.ansar.jeticketprinter.model.timeConverting;

import com.ansar.jeticketprinter.model.dto.DateConvertor;
import com.github.mfathi91.time.PersianDate;
import org.junit.Assert;
import org.junit.Test;

public class TimeConvertorTest {

    @Test
    public void testConvertJalalyToGro(){

        PersianDate persianDate = PersianDate.of(1400, 5, 3);
        System.out.println(persianDate.toGregorian().toString());

        System.out.println(PersianDate.now().toGregorian().toString());

    }

    @Test
    public void DateConvertor(){

        String jalaly = "1400/5/3";
        String gregorian = "2021-07-25";

        Assert.assertEquals(DateConvertor.jalalyToGregorian(jalaly), "2021-07-25");
        Assert.assertEquals(DateConvertor.gregorianToJalaly("2021-07-25"), "1400/05/03");

    }

}
