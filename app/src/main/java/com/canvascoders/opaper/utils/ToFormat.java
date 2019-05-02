package com.canvascoders.opaper.utils;

import java.text.DecimalFormat;

/**
 * Created by Nikhil on 11-Apr-17.
 */
public class ToFormat {
    DecimalFormat df = new DecimalFormat("00000");
    DecimalFormat df2 = new DecimalFormat("00");
    private static ToFormat ourInstance = new ToFormat();
    public static ToFormat getInstance() {
        return ourInstance;
    }
    private ToFormat() {
    }


    public String setFormat(String f) {
        Double aDouble = Double.parseDouble(f);
        return df.format(aDouble);
    } public String setFormat2(String f) {
        Double aDouble = Double.parseDouble(f);
        return df2.format(aDouble);
    }
}
