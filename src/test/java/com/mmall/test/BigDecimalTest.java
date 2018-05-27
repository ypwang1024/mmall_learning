package com.mmall.test;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * @program: mmall
 * @description:
 * @author: ypwang
 * @create: 2018-05-25 08:24
 **/
public class BigDecimalTest {

    @Test
    public void test1() {
        System.out.println(0.05 + 0.01);
        System.out.println(1.0 - 0.42);
        System.out.println(4.015 * 100);
        System.out.println(123.3 / 100);
    }

    @Test
    public void test2() {
        BigDecimal b1 = new BigDecimal(0.05);
        BigDecimal b2 = new BigDecimal(0.01);
        System.out.println(b1.add(b2));

        b1 = new BigDecimal(1.0);
        b2 = new BigDecimal(0.42);
        System.out.println(b1.subtract(b2));

        b1 = new BigDecimal(4.015);
        b2 = new BigDecimal(100);
        System.out.println(b1.multiply(b2));

        b1 = new BigDecimal(123.3);
        b2 = new BigDecimal(100);
        System.out.println(b1.divide(b2));
    }

    /**
     * 解决精度丢失的问题，选择BigDecimal的string构造器
     */
    @Test
    public void test3() {
        BigDecimal b1 = new BigDecimal("0.05");
        BigDecimal b2 = new BigDecimal("0.01");
        System.out.println(b1.add(b2));

        b1 = new BigDecimal("1.0");
        b2 = new BigDecimal("0.42");
        System.out.println(b1.subtract(b2));

        b1 = new BigDecimal("4.015");
        b2 = new BigDecimal("100");
        System.out.println(b1.multiply(b2));

        b1 = new BigDecimal("123.3");
        b2 = new BigDecimal("100");
        System.out.println(b1.divide(b2));
    }
}
