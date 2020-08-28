package com.bulahej.tazweeg.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.adapters.ConsultantAdapter;
import com.bulahej.tazweeg.adapters.StateAdapter;
import com.bulahej.tazweeg.apis_responses.TazweegApi;
import com.bulahej.tazweeg.apis_responses.UserResponse.User;
import com.bulahej.tazweeg.apis_responses.list_countries.Country;
import com.bulahej.tazweeg.apis_responses.list_emirates.Emirate;
import com.bulahej.tazweeg.apis_responses.list_emirates.EmirateResponse;
import com.bulahej.tazweeg.apis_responses.signup_login.ConsultantResponse;
import com.bulahej.tazweeg.constant.Key;
import com.bulahej.tazweeg.utilties.LocaleHelper;
import com.bulahej.tazweeg.utilties.Utilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StateAndConsultantActivity extends AppCompatActivity implements StateAdapter.OnItemClickListener, ConsultantAdapter.OnConsultItemClickListener {

    private ProgressBar pbProgress;
//    private ConstraintLayout clItems;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout ;
    private StateAdapter stateAdapter;
    private ConsultantAdapter consultantAdapter;
    private List<Emirate> emiratesData;
    private List<User> consultantsData;
    private int userID, pageType, selectedStateID = -1;
    private Country selectedCountry;
    private TextView lblEmirates;
    Resources resources;
    String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_consultlant);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.signup);
        toolbar.setTitleTextColor(0xFF000000);
        resources = this.getResources();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null){
                selectedCountry = (Country) extras.get(Key.KEY_SELECTED_COUNTRY);
                userID = extras.getInt(Key.KEY_USER_ID);
                pageType = extras.getInt(Key.KEY_PAGE_TYPE);
                mobileNumber = extras.getString(Key.KEY_MOBILE_NUMBER);
            }
        }

        //Initializing the widgets
        initUIElements();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if( recyclerView.getAdapter() == consultantAdapter){
                    callGetConsultantsApi(true);
                }else if (recyclerView.getAdapter() == stateAdapter){
                    callGetEmiratesApi(true);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        //Loading list of emirates
        callGetEmiratesApi(false);
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


    private void callGetEmiratesApi(Boolean isRefresh) {
        lblEmirates.setText(resources.getString(R.string.select_state));
//        clItems.setVisibility(View.GONE);
        if (!isRefresh) {
            pbProgress.setVisibility(View.VISIBLE);
        }
        emiratesData = new ArrayList<>();
        setStatesData();
        TazweegApi.getInstance().getStatesByCountry(selectedCountry.getCountryId()).enqueue(new Callback<EmirateResponse>() {
            @Override
            public void onResponse(Call<EmirateResponse> call, Response<EmirateResponse> response) {
                pbProgress.setVisibility(View.GONE);
                if (response.body() != null) {
                    int status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status == 1) {
//                        clItems.setVisibility(View.VISIBLE);
                        emiratesData = response.body().getEmirates();
                        setStatesData();
                    } else {
                        Utilities.myToastMessage(getApplicationContext(), message);
                    }
                }
            }

            @Override
            public void onFailure(Call<EmirateResponse> call, Throwable t) {
                pbProgress.setVisibility(View.GONE);
                Utilities.myToastMessage(getApplicationContext(), getString(R.string.failure));
            }

        });
    }

    private void callGetConsultantsApi(Boolean isRefresh) {
        lblEmirates.setText(resources.getString(R.string.select_consultant));
        if (!isRefresh) {
            pbProgress.setVisibility(View.VISIBLE);
        }

        setConsultantsData(null);
        TazweegApi.getInstance().getListOfConsultants(selectedStateID).enqueue(new Callback<ConsultantResponse>() {
            @Override
            public void onResponse(Call<ConsultantResponse> call, Response<ConsultantResponse> response) {
                pbProgress.setVisibility(View.GONE);
                if (response.body() != null) {
                    int status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status == 1) {
                        consultantsData = response.body().getConsultants();
                        setConsultantsData(consultantsData);
                    } else {
                        Utilities.myToastMessage(getApplicationContext(), message);
                        setConsultantsData(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<ConsultantResponse> call, Throwable t) {
                pbProgress.setVisibility(View.GONE);
                Utilities.myToastMessage(getApplicationContext(), getString(R.string.failure));
            }
        });
    }

    private void setConsultantsData(List<User> consultantsData) {
        if (consultantsData == null)  {
            consultantsData = new ArrayList<>();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(StateAndConsultantActivity.this));
        consultantAdapter = new ConsultantAdapter(StateAndConsultantActivity.this, consultantsData);
        consultantAdapter.setOnConsulItemClickListener(this);
        recyclerView.setAdapter(consultantAdapter);
    }

    private void setStatesData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        stateAdapter = new StateAdapter(this, emiratesData,false);
        stateAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(stateAdapter);
    }

    private void initUIElements() {
        pbProgress = findViewById(R.id.pbProgress);
//        clItems = findViewById(R.id.clItems);
        recyclerView = findViewById(R.id.rvEmirate);
        recyclerView = findViewById(R.id.rvStates);
        swipeRefreshLayout = findViewById(R.id.swipe_view);
        lblEmirates = findViewById(R.id.lblEmirates);
    }

    //Emirate click.
    @Override
    public void onStateItemClick(int position, Emirate emirateData) {
        selectedStateID = emirateData.getStateId();
        callGetConsultantsApi(false);
    }

    @Override
    public void onConsultItemClick(int position, User consultant) {
        Intent intent = new Intent(this, CommonSignupActivity.class);
        intent.putExtra(Key.KEY_SELECTED_COUNTRY, selectedCountry);
        intent.putExtra(Key.KEY_SELECTED_CONSULTANT, consultant);
        intent.putExtra(Key.KEY_STATE_ID, selectedStateID);
        intent.putExtra(Key.KEY_MOBILE_NUMBER, mobileNumber);
        startActivity(intent);
    }

}
