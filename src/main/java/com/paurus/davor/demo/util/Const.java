package com.paurus.davor.demo.util;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class Const {

    public static final DateTimeFormatter DT_HHmmss = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final SimpleDateFormat yyyyMMdd_HHmmss = new SimpleDateFormat("HH:mm:ss");
}
