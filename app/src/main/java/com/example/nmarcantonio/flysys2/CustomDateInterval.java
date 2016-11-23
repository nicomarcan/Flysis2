package com.example.nmarcantonio.flysys2;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by john on 19/11/16.
 */

public class CustomDateInterval {
    private static long[] interval(Date from, Date to) {
        long diff = from.getTime() - to.getTime();
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

    public static SignificantTimeInterval significantInterval(Date from, Date to) {
        long[] interval = interval(from, to);
        SignificantTimeInterval ret = SignificantTimeInterval.ZERO;
        if (interval[0] > 0) {
            ret = SignificantTimeInterval.ONE_DAY_PLUS;
        }
        else if (interval[0] == 0){
            if (interval[1] >= 12) {
                ret = SignificantTimeInterval.ONE_DAY;
            }
            if (interval[1] >= 6) {
                ret = SignificantTimeInterval.TWELVE_HOURS;
            }
            else if (interval[1] >= 3) {
                ret = SignificantTimeInterval.SIX_HOURS;
            }
            else if (interval[1] >= 1) {
                ret = SignificantTimeInterval.THREE_HOURS;
            }
            else if (interval[1] == 0) {
                if (interval[2] >= 30) {
                    ret = SignificantTimeInterval.ONE_HOUR;
                }
                else if (interval[2] >= 10) {
                    ret = SignificantTimeInterval.THIRTY_MINUTES;
                }
                else if (interval[2] >= 0) {
                    ret = SignificantTimeInterval.TEN_MINUTES;
                }
            }
        }
        return ret;
    }

    public enum SignificantTimeInterval implements Serializable {
        ONE_DAY_PLUS,
        ONE_DAY,
        TWELVE_HOURS,
        SIX_HOURS,
        THREE_HOURS,
        ONE_HOUR,
        THIRTY_MINUTES,
        TEN_MINUTES,
        ZERO
    }
}
