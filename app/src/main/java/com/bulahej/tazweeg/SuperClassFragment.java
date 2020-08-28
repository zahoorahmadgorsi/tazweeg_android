package com.bulahej.tazweeg;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.bulahej.tazweeg.utilties.LocaleHelper;

import java.util.Locale;

public class SuperClassFragment extends FragmentActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(LocaleHelper.onAttach(newBase, "ar"));  //Arabic
//        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));  //English
        super.attachBaseContext(LocaleHelper.onAttach(newBase, Locale.getDefault().getLanguage()));    //System Default
    }
}
