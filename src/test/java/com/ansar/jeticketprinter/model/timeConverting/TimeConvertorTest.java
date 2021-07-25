package com.ansar.jeticketprinter.model.timeConverting;

import com.github.mfathi91.time.PersianDate;
import org.junit.Test;

public class TimeConvertorTest {

    @Test
    public void testConvertJalalyToGro(){

        PersianDate persianDate = PersianDate.of(1400, 5, 3);
        System.out.println(persianDate.toGregorian().toString());

        System.out.println(PersianDate.now().toGregorian().toString());

    }

}
