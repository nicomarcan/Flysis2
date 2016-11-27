package com.example.nmarcantonio.flysys2;

/**
 * Created by saques on 27/11/2016.
 */


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 *  A helper class for obtaining the settings.
 */
public final class FlysysSettings {


    public static NotificationSetting notificationSetting = NotificationSetting.HOUR_1;

    public static CurrencySetting getCurrencySetting(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        CurrencySetting ret = CurrencySetting.ARS;
        switch (prefs.getString("money_list","ARS")){
            case "ARS":
                ret = CurrencySetting.ARS;
                break;
            case "USD":
                ret = CurrencySetting.USD;
                break;
            case "BRL":
                ret = CurrencySetting.RS;
                break;
        }
        return ret;
    }

    public static DistanceSetting getDistanceSetting(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        DistanceSetting ret = DistanceSetting.METRIC;
        switch (prefs.getString("measure_list","KM")){
            case "KM":
                ret = DistanceSetting.METRIC;
                break;
            case "MI":
                ret = DistanceSetting.IMPERIAL;
                break;
        }
        return ret;
    }


    public static NotificationSetting getNotificationSetting(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        NotificationSetting ret = NotificationSetting.HOUR_1;
        switch (prefs.getString("interval_list","1h")){
            case "1h":
                ret = NotificationSetting.HOUR_1;
                break;
            case "6h":
                ret = NotificationSetting.HOUR_6;
                break;
            case "12h":
                ret = NotificationSetting.HOUR_12;
                break;
            case "24h":
                ret = NotificationSetting.HOUR_24;
                break;
            case "0h":
                ret = NotificationSetting.NEVER;
                break;
            case "5m":
                ret = NotificationSetting.MIN_5;
                break;
        }
        return notificationSetting;
    }

    public enum DistanceSetting {
        METRIC,
        IMPERIAL
    }

    public enum CurrencySetting {
        USD,
        ARS,
        RS
    }

    public enum NotificationSetting {
        MIN_5,
        HOUR_1,
        HOUR_6,
        HOUR_12,
        HOUR_24,
        NEVER
    }

}
