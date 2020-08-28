package com.bulahej.tazweeg.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.apis_responses.TazweegApi;
import com.bulahej.tazweeg.apis_responses.registration_form_new.Type;
import com.bulahej.tazweeg.constant.Constants;
import com.bulahej.tazweeg.constant.Key;
import com.bulahej.tazweeg.fragments.HomeFragment;
import com.bulahej.tazweeg.fragments.MatchingsFragment;
import com.bulahej.tazweeg.fragments.ProfileFragment;
import com.bulahej.tazweeg.fragments.SettingsFragment;
import com.bulahej.tazweeg.utilties.AppUtility;
import com.bulahej.tazweeg.utilties.LocaleHelper;
import com.bulahej.tazweeg.utilties.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bulahej.tazweeg.constant.Constants.INFO_TYPE_CHOOSING;
import static com.bulahej.tazweeg.constant.Constants.INFO_TYPE_PROFILE;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener, MatchingsFragment.OnFragmentInteractionListener,SettingsFragment.OnFragmentInteractionListener{
    private Toolbar toolbar;
    private AlertDialog dialog;
    public AlertDialog getDialog() {
        return dialog;
    }
    private SharedPreferences preferences;
    public SharedPreferences getPreferences() {
        return preferences;
    }

    public BottomNavigationView mBottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUIElements();
        preferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        //Setting bottom navigation as hide at scroll down
        mBottomNavigationView =  findViewById(R.id.bottom_navigation);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                Bundle args = new Bundle();
                switch (item.getItemId()) {
                    case R.id.action_home:
                        toolbar.setTitle(R.string.home);
                        fragment = new HomeFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.action_profile:
                        toolbar.setTitle(R.string.profile);
                        fragment = new ProfileFragment();
                        args.putSerializable(Constants.CURRENT_MEMBER, Constants.loggedInMember);
                        args.putInt(Key.KEY_INFO_TYPE, INFO_TYPE_PROFILE);
                        fragment.setArguments(args);
                        loadFragment(fragment);
                        break;
                    case R.id.action_choosing:
                        toolbar.setTitle(R.string.matching);
                        fragment = new MatchingsFragment();
                        args.putInt(Key.KEY_INFO_TYPE, INFO_TYPE_CHOOSING);
                        fragment.setArguments(args);
                        loadFragment(fragment);
                        break;
                    case R.id.action_settings:
                        toolbar.setTitle(R.string.settings);
                        fragment = new SettingsFragment();
                        loadFragment(fragment);
                        break;
                }
                return true;
            }
        });

        getSpinnersData();
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
//        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initUIElements() {
        dialog = AppUtility.createPleaseWaitDialog(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // load the profile fragment by default
        getSupportActionBar().setTitle(R.string.home);
        toolbar.setTitleTextColor(0xFF000000);
        loadFragment(new HomeFragment());
    }

    //This is an indicator that the activity became active and ready to receive input
    @Override
    protected void onResume (){
        super.onResume();
    }

    private void getSpinnersData() {
//        if (Constants.spinners == null) { //let it commented as getSpinnersData is called from onCreate hence it will be called for the very first time ONLY
        dialog.show();
        TazweegApi.getInstance().getTypes().enqueue(new Callback<Type>() {
            @Override
            public void onResponse(Call<Type> call, Response<Type> response) {
                if (response.body() != null) {
                    int status = response.body().getStatus();
                    if (status == 1) {
                        dialog.dismiss();
                        Type type = response.body();
                        if (type.getDropDowns().size() > 0) {
                            Constants.spinners = Utilities.JSONResponseToDropDowns(type.getDropDowns());  //Assigngin downloaded dropdowns data to constant variable
                        }
                    } else {
                        dialog.dismiss();
                        Utilities.myToastMessage(getApplicationContext(), getString(R.string.general_error ));
                    }
                }
            }

            @Override
            public void onFailure(Call<Type> call, Throwable t) {
                dialog.dismiss();
                Utilities.myToastMessage(getApplicationContext(), getString(R.string.failure));
            }

        });
//        }
    }
    //This method changes the activity language according to the chosen language
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

//    @Override
//    public void onBackPressed() {
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            ShowSignoutAlert();
//        }
//
//    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.i("onFragmentInteraction" , uri.toString());
    }
}
