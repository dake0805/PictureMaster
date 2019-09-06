package com.nwpu.paim;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LanguageUtil {

    public static void changeAppLanguage(Context context, Locale locale) {

        Resources resources = context.getResources();

        DisplayMetrics metrics = resources.getDisplayMetrics();

        Configuration configuration = resources.getConfiguration();

        if (Build.VERSION.SDK_INT >= 17) {
            configuration.setLocale(locale);
            context = context.createConfigurationContext(configuration);
        } else {
            configuration.locale = locale;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }

//        if (persistence) {
//
//            saveLanguageSetting(context, locale);
//
//        }

    }

//    private static void saveLanguageSetting(Context context, Locale locale) {
//
//        String name = context.getPackageName() + "_" + LANGUAGE;
//
//        SharedPreferences preferences =
//
//                context.getSharedPreferences(name, Context.MODE_PRIVATE);
//
//        preferences.edit().putString(LANGUAGE, locale.getLanguage()).apply();
//
//        preferences.edit().putString(COUNTRY, locale.getCountry()).apply();
//
//    }
}
