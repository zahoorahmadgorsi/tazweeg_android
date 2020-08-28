package com.bulahej.tazweeg.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.adapters.ChangeLanguageAdapter;
import com.bulahej.tazweeg.utilties.LocaleHelper;

import java.util.ArrayList;

public class ChangeLanguageActivity extends AppCompatActivity implements ChangeLanguageAdapter.OnItemClickListener{
    Context context;
    private ArrayList<String> nameArray = new ArrayList<>();
    private ArrayList<Integer> imageArray = new ArrayList<>();
    private Resources resources;
    private RecyclerView rvChangeLanguage;
    private ChangeLanguageAdapter changeLanguageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.change_language);
        toolbar.setTitleTextColor(0xFF000000);
        context = this;
        resources = this.getResources();
        initUIElements();
        //populating title array
        nameArray.add(resources.getString(R.string.english));
        nameArray.add(resources.getString(R.string.arabic));
        //populating images array
        imageArray.add(R.drawable.english_flag);
        imageArray.add(R.drawable.arabic_flag);
        setChangeLanguageData();   //set cached data
    }

    //This method is used to implement localization
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    //When back button is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initUIElements() {
        rvChangeLanguage = findViewById(R.id.rvChangeLanguage);
    }

    private void setChangeLanguageData(  ) {
        rvChangeLanguage.setLayoutManager(new LinearLayoutManager(ChangeLanguageActivity.this));
        changeLanguageAdapter = new ChangeLanguageAdapter(ChangeLanguageActivity.this, nameArray,imageArray);
        changeLanguageAdapter.setItemClickListener(this);
        rvChangeLanguage.setAdapter(changeLanguageAdapter);
    }

    @Override
    public void onItemClick(int position) {
        if (position == 0){
            ChangeLocale("en");
        }else if (position == 1){
            ChangeLocale("ar");
        }
    }

    //same method as in userselectionactivity
    //This method receive the language as a param and if current app lang is not set according to param then it changes the application language
    private void ChangeLocale(String newLanguage) {
        String currentLanguage = LocaleHelper.getLanguage(getApplicationContext());
        if (!currentLanguage.equals(newLanguage)) {
            LocaleHelper.setLocale(getApplicationContext(),newLanguage);
            Intent intent = new Intent(ChangeLanguageActivity.this, MainActivity.class);
            //If set, and the activity being launched is already running in the current task, then instead of launching a new instance of that activity, all of the other activities on top of it will be closed and this
            // Intent will be delivered to the (now on top) old activity as a new Intent.
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.from_middle, R.anim.to_middle); //Animation
        }
    }
}
