package com.svp.infrastructure.common;

import java.util.Formatter;
import java.util.Locale;

public class StringEx {
//    private final static Formatter format = new Formatter();
    public static String toString(int i){
        return String.format(Locale.getDefault(),"%d",i);
    }
}
