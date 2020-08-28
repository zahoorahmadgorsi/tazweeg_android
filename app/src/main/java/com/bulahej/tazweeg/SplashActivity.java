package com.bulahej.tazweeg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bulahej.tazweeg.activities.MainActivity;
import com.bulahej.tazweeg.activities.UserSelectionActivity;
import com.bulahej.tazweeg.activities.onBoarding.OnboardingActivity;
import com.bulahej.tazweeg.constant.Constants;
import com.bulahej.tazweeg.utilties.Utilities;

public class SplashActivity extends AppCompatActivity {

    private boolean isLoggedIn = false;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        preferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        isLoggedIn = preferences.contains(Constants.LOGGED_IN_USER);    //If user has logged in this this string would have been persisted
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                SplashActivity.this.finish();
                //Starting onboarding activity
                SharedPreferences preferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
//                isLoggedIn = false;
                if(!preferences.getBoolean(Constants.ON_BOARDING_COMPLETE,false)){
                    Intent onboarding = new Intent(SplashActivity.this, OnboardingActivity.class);
                    startActivity(onboarding);
                    finish();
                    return;
                }else if (isLoggedIn){
                    if (Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_MEMBER){
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                    }else{
                        Snackbar.make(findViewById(android.R.id.content), "Consultant is coming soon.", Snackbar.LENGTH_LONG)
                                .setAction("Success", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
//                                        finish();
                                    }
                                }).show();
                    }
                }else
                {
                    Intent intent = new Intent(SplashActivity.this, UserSelectionActivity.class);
                    startActivity(intent);
                }
            }
        }, 200);

    }
}
