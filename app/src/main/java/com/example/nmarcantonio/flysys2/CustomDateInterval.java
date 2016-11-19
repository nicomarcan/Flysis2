package com.example.nmarcantonio.flysys2;

import java.util.Date;

/**
 * Created by john on 19/11/16.
 */

public class CustomDateInterval {
    private static long[] interval(Date from, Date to) {
        long diff = Math.abs(from.getTime() - to.getTime());
        long days = diff / (24 * 60 * 60 * 1000);
        diff = diff % (24 * 60 * 60 * 1000);
        long hours = diff / (60 * 60 * 1000);
        diff = diff % (60 * 60 * 1000);
        long minutes = diff / (60 * 1000);
        long[] interval = new long[3];
        interval[0] = days;
        interval[1] = hours;
        interval[2] = minutes;
        return interval;
    }

    public static String longInterval(Date from, Date to) {
        long[] interval = interval(from, to);
        String ret = "";
        if (interval[0] > 0) {
            ret += " " + interval[0] + " dia";
            if (interval[0] > 1) {
                ret += "s";
            }
            if (interval[1] > 0 || interval[2] > 0) {
                ret += ",";
            }
            else {
                ret += ".";
            }
        }
        if (interval[1] > 0) {
            ret += " " + interval[1] + " hora";
            if (interval[1] > 1) {
                ret += "s";
            }
            if (interval[2] > 0) {
                ret += ",";
            }
            else {
                ret += ".";
            }
        }
        if (interval[2] > 0) {
            ret += " " + interval[2] + " minuto";
            if (interval[2] > 1) {
                ret += "s";
            }
            ret += ".";
        }
        return ret;
    }
}
