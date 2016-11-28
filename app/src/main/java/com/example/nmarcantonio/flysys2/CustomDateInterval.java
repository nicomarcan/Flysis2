package com.example.nmarcantonio.flysys2;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by john on 19/11/16.
 */

public class CustomDateInterval {
    private static long[] interval(Date from, long fromTimeZone,  Date to, long toTimeZone) {
        long diff = to.getTime() - toTimeZone - (from.getTime() - fromTimeZone);
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

    public static String longInterval(Date from, long fromTimeZone, Date to, long toTimeZone) {
        long[] interval = interval(from, fromTimeZone, to, toTimeZone);
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
        if (interval[0] <= 0 && interval[1] <= 0 && interval[2] <= 0) {
            ret += "0 minutos.";
        }
        return ret;
    }

    public static String significantInterval(Date from, long fromTimeZone, Date to, long toTimeZone) {
        long[] interval = interval(from, fromTimeZone, to, toTimeZone);
        String ret = ZERO;

        if (interval[0] > 0) {
            ret = ONE_DAY_PLUS;
        }
        else if (interval[0] == 0){
            if (interval[1] >= 12) {
                ret = ONE_DAY;
            }
            if (interval[1] >= 6) {
                ret = TWELVE_HOURS;
            }
            else if (interval[1] >= 3) {
                ret = SIX_HOURS;
            }
            else if (interval[1] >= 1) {
                ret = THREE_HOURS;
            }
            else if (interval[1] == 0) {
                if (interval[2] >= 30) {
                    ret = ONE_HOUR;
                }
                else if (interval[2] >= 10) {
                    ret = THIRTY_MINUTES;
                }
                else if (interval[2] >= 0) {
                    ret = TEN_MINUTES;
                }
            }
        }
        return ret;
    }

    public static String ONE_DAY_PLUS = "ONE_DAY_PLUS";
    public static String ONE_DAY = "ONE_DAY";
    public static String TWELVE_HOURS = "TWELVE_HOURS";
    public static String SIX_HOURS = "SIX_HOURS";
    public static String THREE_HOURS = "THREE_HOURS";
    public static String ONE_HOUR = "ONE_HOUR";
    public static String THIRTY_MINUTES = "THIRTY_MINUTES";
    public static String TEN_MINUTES = "TEN_MINUTES";
    public static String ZERO = "ZERO";
}
