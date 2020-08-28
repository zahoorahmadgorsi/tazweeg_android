package com.bulahej.tazweeg.activities.onBoarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.SuperClassFragment;
import com.bulahej.tazweeg.activities.UserSelectionActivity;
import com.bulahej.tazweeg.constant.Constants;
import com.bulahej.tazweeg.fragments.OnboardingFragment1;
import com.bulahej.tazweeg.fragments.OnboardingFragment10;
import com.bulahej.tazweeg.fragments.OnboardingFragment11;
import com.bulahej.tazweeg.fragments.OnboardingFragment12;
import com.bulahej.tazweeg.fragments.OnboardingFragment13;
import com.bulahej.tazweeg.fragments.OnboardingFragment14;
import com.bulahej.tazweeg.fragments.OnboardingFragment2;
import com.bulahej.tazweeg.fragments.OnboardingFragment3;
import com.bulahej.tazweeg.fragments.OnboardingFragment4;
import com.bulahej.tazweeg.fragments.OnboardingFragment5;
import com.bulahej.tazweeg.fragments.OnboardingFragment6;
import com.bulahej.tazweeg.fragments.OnboardingFragment7;
import com.bulahej.tazweeg.fragments.OnboardingFragment8;
import com.bulahej.tazweeg.fragments.OnboardingFragment9;
import com.gc.materialdesign.views.ButtonFlat;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class OnboardingActivity extends SuperClassFragment {

    private ViewPager pager;
    private SmartTabLayout indicator;
    private ButtonFlat skip;
    private ButtonFlat next;
    private int totalFragmentsCount = 14;
//private int totalFragmentsCount = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_on_boarding);

        pager = findViewById(R.id.pager);
        indicator = findViewById(R.id.indicator);
        skip = findViewById(R.id.skip);
        next = findViewById(R.id.next);

        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0 : return new OnboardingFragment1();
                    case 1 : return new OnboardingFragment2();
                    case 2 : return new OnboardingFragment3();
                    case 3 : return new OnboardingFragment4();
                    case 4 : return new OnboardingFragment5();
                    case 5 : return new OnboardingFragment6();
                    case 6 : return new OnboardingFragment7();
                    case 7 : return new OnboardingFragment8();
                    case 8 : return new OnboardingFragment9();
                    case 9 : return new OnboardingFragment10();
                    case 10 : return new OnboardingFragment11();
                    case 11 : return new OnboardingFragment12();
                    case 12 : return new OnboardingFragment13();
                    case 13 : return new OnboardingFragment14();
                    default: return null;
                }
            }

            @Override
            public int getCount() {
                return totalFragmentsCount;
            }
        };

        pager.setAdapter(adapter);

        indicator.setViewPager(pager);

        indicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if(position == totalFragmentsCount - 1 ){
                    skip.setVisibility(View.GONE);
                    next.setText(getResources().getString(R.string.done));
                } else {
                    skip.setVisibility(View.VISIBLE);
                    next.setText(getResources().getString(R.string.next));
                }
            }

        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishOnboarding();
            }
        });

        next.setRippleSpeed( next.getRippleSpeed() * 12 );
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pager.getCurrentItem() == totalFragmentsCount - 1){
                    finishOnboarding();
                } else {
                    pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                }
            }
        });
    }

    private void finishOnboarding() {
        SharedPreferences preferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        preferences.edit().putBoolean(Constants.ON_BOARDING_COMPLETE,true).apply();
        Intent main = new Intent(this, UserSelectionActivity.class);
        startActivity(main);

        finish();
    }

}
