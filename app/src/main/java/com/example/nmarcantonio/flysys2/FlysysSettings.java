package com.example.nmarcantonio.flysys2;

/**
 * Created by saques on 27/11/2016.
 */


/**
 *  A class containing all the settings.
 *  Should only be modified by the user in the Settings view.
 */
public final class FlysysSettings {
    public static DistanceSetting distanceSetting = DistanceSetting.METRIC;
    public static int rangeSetting = 100;
    public static CurrencySetting currencySetting = CurrencySetting.USD;
    public static NotificationSetting notificationSetting = NotificationSetting.HOUR_1;

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
        HOUR_1,
        HOUR_6,
        HOUR_12,
        HOUR_24,
        NEVER
    }

}
