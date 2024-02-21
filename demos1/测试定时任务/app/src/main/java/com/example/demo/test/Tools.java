package com.example.demo.test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tools {
    public static String getNow() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }
}
