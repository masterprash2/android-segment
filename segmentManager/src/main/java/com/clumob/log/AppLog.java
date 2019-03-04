package com.clumob.log;

import android.util.Log;

/**
 * Created by saurabh.garg on 3/26/18.
 */

public class AppLog {

    private static AppLog logger = new AppLog();

    private boolean isInDebugMode = false;

    public static void init(boolean isDebugMode) {
        logger.isInDebugMode = isDebugMode;
    }

    public static boolean isInDebugMode() {
        return logger.isInDebugMode;
    }

    public static void v(String tag, String value) {
        if(isInDebugMode()) {
            Log.v(tag,value);
        }
    }

    public static void d(String tag, String value) {
        if(isInDebugMode()) {
            Log.d(tag,value);
        }
    }

    public static void i(String tag, String value) {
        if(isInDebugMode()) {
            Log.i(tag,value);
        }
    }

    public static void w(String tag, String value) {
        if(isInDebugMode()) {
            Log.w(tag,value);
        }
    }

    public static void e(String tag, String value) {
        if(isInDebugMode()) {
            Log.e(tag,value);
        }
    }

    public static void printStack(Exception ex) {
        if(isInDebugMode()) {
            ex.printStackTrace();
        }
    }

    public static void printStack(Throwable ex) {
        if(isInDebugMode()) {
            ex.printStackTrace();
        }
    }

    public static void printStackAlways(Throwable ex) {
        ex.printStackTrace();
    }

    public static void always(String bbapp, String message) {
        Log.d(bbapp,message);
    }

}
