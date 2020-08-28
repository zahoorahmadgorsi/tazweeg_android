package com.bulahej.tazweeg.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bulahej.tazweeg.R;


public class OnboardingFragment1 extends Fragment {
    private ImageView imgView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(
                R.layout.onboarding_screen1,
                container,
                false
        );

        imgView = rootView.findViewById(R.id.imgView);
        imgView.setImageResource(R.drawable.intro_01);
        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        rootView.setPadding(0, 0, 0, 0);
        return rootView;
    }
}
