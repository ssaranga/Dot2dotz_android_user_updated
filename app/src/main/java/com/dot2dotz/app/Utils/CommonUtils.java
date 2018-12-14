package com.dot2dotz.app.Utils;


import android.app.Activity;
import android.content.res.Configuration;

import com.dot2dotz.app.Activities.MainActivity;
import com.dot2dotz.app.Helper.LocaleUtils;
import com.dot2dotz.app.Helper.SharedHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommonUtils {

    private static long diffSeconds;


    public static long getCurrentAndAssignedTime(String assignedAt, String currentTime) {

        String dateStart = assignedAt;
        String dateStop = currentTime;

        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            System.out.print(diffDays + " days, ");
            System.out.print(diffHours + " hours, ");
            System.out.print(diffMinutes + " minutes, ");
            System.out.print(diffSeconds + " seconds.");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return diffSeconds;

    }

    public static void setLanguage(Activity activity) {
        String languageCode = SharedHelper.getKey(activity, "language");
        LocaleUtils.setLocale(activity, languageCode);

        Locale locale = new Locale(languageCode);
        Configuration config = activity.getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config, activity.getBaseContext().getResources().getDisplayMetrics());
    }
}