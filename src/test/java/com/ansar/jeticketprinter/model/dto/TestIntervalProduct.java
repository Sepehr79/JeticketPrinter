package com.ansar.jeticketprinter.model.dto;

import com.ansar.jeticketprinter.model.pojo.IntervalProduct;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class TestIntervalProduct {

    @Test
    public void testInsertIntervalProducts(){

        Set<IntervalProduct> intervalProducts = new HashSet<>();

        IntervalProduct intervalProduct1 = new IntervalProduct("a", "2", "2", "1", "2020-01-01 00:00");
        IntervalProduct intervalProduct2 = new IntervalProduct("a", "2", "2", "1", "2021-01-01 00:00");
        IntervalProduct intervalProduct3 = new IntervalProduct("a", "2", "2", "1", "2022-01-01 00:00");

        intervalProducts.add(intervalProduct1);
        intervalProducts.add(intervalProduct2);
        intervalProducts.add(intervalProduct3);

        System.out.println(intervalProducts.size());
        for (IntervalProduct product: intervalProducts)
            System.out.println(product.getDate());

    }

}
