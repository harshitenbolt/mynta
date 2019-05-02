package com.canvascoders.opaper.utils;

import android.util.Log;

/**
 * Created by Nikhil on 2017.
 */
public class Mylogger {
    private boolean lockLog=true;
    private static Mylogger ourInstance = new Mylogger();

    public static Mylogger getInstance() {
        return ourInstance;
    }

    private Mylogger() {

    }


    public void Logit(String tagName, String log) {
        if (lockLog == true)
            Log.d("@Log" + tagName, log+"");
    }

    public void setEnable(boolean lockLog) {
        this.lockLog = lockLog;
    }
}
