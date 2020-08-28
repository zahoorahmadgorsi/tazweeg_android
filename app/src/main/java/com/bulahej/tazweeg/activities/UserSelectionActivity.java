package com.bulahej.tazweeg.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.constant.Constants;
import com.bulahej.tazweeg.utilties.LocaleHelper;


public class UserSelectionActivity extends AppCompatActivity {

    Button btnMember, btnConsultant, btnMemberSignUp, btnConsultantSignUp, btnEnglish,btnArabic;
    ImageView imgSnapChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);

        //assigning tap events on member and consultant linear lay out
        btnMember = findViewById(R.id.btnMember);
        btnConsultant = findViewById(R.id.btnConsultant);
        btnMemberSignUp = findViewById(R.id.btnMemberSignUp);
        btnConsultantSignUp = findViewById(R.id.btnConsultantSignUp);
        btnEnglish = findViewById(R.id.btnEnglish);
        btnArabic = findViewById(R.id.btnArabic);

        btnMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TapOnUser(Constants.USER_TYPE_MEMBER);
            }
        });
        btnConsultant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TapOnUser(Constants.USER_TYPE_CONSULTANT);
            }
        });
        btnMemberSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TapOnSignUp(Constants.USER_TYPE_MEMBER);
            }
        });
        btnConsultantSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TapOnSignUp(Constants.USER_TYPE_CONSULTANT);
            }
        });

        //assigning tap events on english and arabic buttons
        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeLocale("en");
            }
        });
        btnArabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeLocale("ar");
            }
        });
        //assigning tap events on snapChat image
        imgSnapChat = findViewById(R.id.imgSnapChat);
        imgSnapChat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                Log.i("imgSnapChat", "imgSnapChat onTouch");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.urlSnapChat));
                startActivity(browserIntent);
                return false;
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    private void TapOnUser(@Constants.UserType int UserType)
    {
        SharedPreferences preferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        preferences.edit().putInt(Constants.CURRENT_USER,UserType).apply();
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
    }

    private void TapOnSignUp(@Constants.UserType int UserType)
    {
        SharedPreferences preferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        preferences.edit().putInt(Constants.CURRENT_USER,UserType).apply();
        Intent intent = new Intent(this, PhoneActivity.class);
        startActivity(intent);
    }

    //This method receive the language as a param and if current app lang is not set according to param then it changes the application language
    private void ChangeLocale(String newLanguage) {
        String currentLanguage = LocaleHelper.getLanguage(getApplicationContext());
        if (!currentLanguage.equals(newLanguage)) {
            Context context = LocaleHelper.setLocale(getApplicationContext(),newLanguage);
            //Restarting the activity
            finish();
            Intent intent = getIntent();
            startActivity(intent);
            overridePendingTransition(R.anim.from_middle, R.anim.to_middle); //Animation
        }
        else{
            Toast.makeText(getApplicationContext(), "Already using the same language", Toast.LENGTH_LONG).show();
        }
    }
}
