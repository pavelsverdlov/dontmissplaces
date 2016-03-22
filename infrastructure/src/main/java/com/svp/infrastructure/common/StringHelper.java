package com.svp.infrastructure.common;

import java.util.ArrayList;

/**
 * Created by Pasha on 5/11/2015.
 */
public class StringHelper {
    private static final String separator = "\n";
    public static String[] splitByLineSeparator(String text){
        ArrayList<String> splitted = new ArrayList<String>();
        for (String str : text.split(separator)){
            if(!str.isEmpty()){
                splitted.add(str);
            }
        }
        return splitted.toArray(new String[splitted.size()]);
    }
    public static String joinByLineSeparator(String[] text) {
        if(text.length == 0){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(text[0]);
        for (int i = 1; i < text.length; ++i){
            String txt = text[i];
            if(!txt.isEmpty()) {
                sb.append(separator).append(txt);
            }
        }
        return sb.toString();
    }
    public static boolean endsWithLineSeparator(String txt){
        return txt.endsWith(separator);
    }
    public static String trimLineSeparator(String txt){
        return txt.substring(0,txt.length() - separator.length());
    }
    public static boolean endsWithLineSeparator(CharSequence txt){
        return txt.length() != 0 && txt.charAt(txt.length()-1) == separator.charAt(0);
    }
}
