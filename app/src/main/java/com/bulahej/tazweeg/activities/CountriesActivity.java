package com.bulahej.tazweeg.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.adapters.CountryAdapter;
import com.bulahej.tazweeg.apis_responses.TazweegApi;
import com.bulahej.tazweeg.apis_responses.list_countries.Country;
import com.bulahej.tazweeg.apis_responses.list_countries.CountryResponse;
import com.bulahej.tazweeg.constant.Key;
import com.bulahej.tazweeg.utilties.LocaleHelper;
import com.bulahej.tazweeg.utilties.Utilities;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountriesActivity extends AppCompatActivity implements CountryAdapter.OnItemClickListener{
//public class CountriesActivity extends AppCompatActivity {

    private ProgressBar pbProgress;
    private RecyclerView gvCountries;
//    private GridView gvCountries;
    private CountryAdapter countryAdapter;
    private ArrayList<Country> countriesData;
    private int selectedStateID = -1;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.signup);
        toolbar.setTitleTextColor(0xFF000000);
        context = this;
        //Initializing the widgets
        initUIElements();
        //Loading list of countries
        callGetCountriesApi();

    }

    private void initUIElements() {
        pbProgress = findViewById(R.id.pbProgress);
        gvCountries = findViewById(R.id.rvCountries);

//        gvCountries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView parent, View view, int position, long id) {
//                SharedPreferences preferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
//                if(Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_MEMBER) {// but if consultant is trying to signup
//                    Intent intent = new Intent(getApplicationContext(), StateAndConsultantActivity.class);    //by default intent is made for member
//                    intent.putExtra(Key.KEY_SELECTED_COUNTRY, countriesData.get(position));
//                    intent.putExtra(Key.KEY_USER_ID, 123);  //will be used in case of transfer
//                    intent.putExtra(Key.KEY_PAGE_TYPE, Constants.PAGE_TYPE_SIGNUP);
//                    startActivity(intent);
//                }
//                else if(Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_CONSULTANT){// but if consultant is trying to signup
//                    Intent intent = new Intent(getApplicationContext(), CommonSignupActivity.class);
//                    intent.putExtra(Key.KEY_SELECTED_COUNTRY, countriesData.get(position));
//                    startActivity(intent);
//                }
//            }
//        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
//                Intent intent = new Intent(this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void callGetCountriesApi() {
        pbProgress.setVisibility(View.VISIBLE);
        TazweegApi.getInstance().getListOfCountries().enqueue(new Callback<CountryResponse>() {
            @Override
            public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {
                pbProgress.setVisibility(View.GONE);
                if (response.body() != null) {
                    int status = response.body().getStatus();
                    if (status == 1) {
                        countriesData = new ArrayList<>(response.body().getCountries());
                        setCountriesData();
                    } else {
                        Utilities.myToastMessage(getApplicationContext(), "Error");
                    }
                }
            }

            @Override
            public void onFailure(Call<CountryResponse> call, Throwable t) {
                pbProgress.setVisibility(View.GONE);
                Utilities.myToastMessage(getApplicationContext(), getString(R.string.failure));
            }

        });
    }

    private void setCountriesData() {
        if (countriesData == null)  {
            countriesData = new ArrayList<>();
        }
        gvCountries.setLayoutManager(new LinearLayoutManager(CountriesActivity.this));
        countryAdapter = new CountryAdapter(getApplicationContext(), countriesData);
        countryAdapter.setOnItemClickListener(this);
        gvCountries.setAdapter(countryAdapter);
    }

    @Override
    public void onItemClick(int position, Country country) {
        Intent intent = new Intent();
        intent.putExtra(Key.KEY_SELECTED_COUNTRY, country);
        setResult(RESULT_OK, intent);
        finish();
    }
}
