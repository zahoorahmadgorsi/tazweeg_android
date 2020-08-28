package com.bulahej.tazweeg.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.apis_responses.UserResponse.User;
import com.bulahej.tazweeg.constant.Constants;
import com.bulahej.tazweeg.constant.Key;
import com.bulahej.tazweeg.fragments.ProfileFragment;
import com.bulahej.tazweeg.utilties.AppUtility;
import com.bulahej.tazweeg.utilties.LocaleHelper;

public class ProfileActivity extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener{
    private Toolbar toolbar;
    private AlertDialog dialog;
    private User currentMember;
    private int infoType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) { //User is coming to sign up
                currentMember = (User) extras.get(Constants.CURRENT_MEMBER);
                infoType = (int) extras.get(Key.KEY_INFO_TYPE);
            }
        }
        initUIElements();
    }

    private void initUIElements() {
        dialog = AppUtility.createPleaseWaitDialog(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // load the profile fragment by default
        getSupportActionBar().setTitle(R.string.profile);
        toolbar.setTitleTextColor(0xFF000000);

        Fragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.CURRENT_MEMBER, currentMember);
        args.putInt(Key.KEY_INFO_TYPE, infoType);
        fragment.setArguments(args);
        loadFragment(fragment);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
//        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.i("onFragmentInteraction" , uri.toString());
    }

    //This method changes the activity language according to the chosen language
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
}
