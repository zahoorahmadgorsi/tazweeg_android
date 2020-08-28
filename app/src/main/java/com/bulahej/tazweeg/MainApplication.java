package com.bulahej.tazweeg;

import android.app.Application;
import android.content.Context;

import com.bulahej.tazweeg.utilties.LocaleHelper;

import java.util.Locale;

public class MainApplication extends Application {
//This context doesn't work when you are translating the app on fly as it contains the static context.
//    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, Locale.getDefault().getLanguage()));
    }

}
