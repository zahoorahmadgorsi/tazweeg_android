package com.bulahej.tazweeg.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.adapters.EmiratesAdapter;
import com.bulahej.tazweeg.apis_responses.TazweegApi;
import com.bulahej.tazweeg.apis_responses.UserResponse.User;
import com.bulahej.tazweeg.apis_responses.UserResponse.UserResponse;
import com.bulahej.tazweeg.apis_responses.list_countries.Country;
import com.bulahej.tazweeg.apis_responses.list_emirates.Emirate;
import com.bulahej.tazweeg.apis_responses.list_emirates.EmirateResponse;
import com.bulahej.tazweeg.apis_responses.registration_form_new.DropDown;
import com.bulahej.tazweeg.apis_responses.registration_form_new.Type;
import com.bulahej.tazweeg.constant.Constants;
import com.bulahej.tazweeg.constant.Key;
import com.bulahej.tazweeg.utilties.AppUtility;
import com.bulahej.tazweeg.utilties.LocaleHelper;
import com.bulahej.tazweeg.utilties.Utilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonSignupActivity extends AppCompatActivity implements EmiratesAdapter.OnItemClickListener {
//    private EditText txtName, txtCountryCode,txtPhone, txtEmail;
    private EditText txtName, txtEmail;
    private Spinner sGender;
    private CheckBox cbTerms;
    private TextView lblSelectEmirates;
    private EmiratesAdapter emiratesAdapter;

    private AlertDialog dialog;

    private ProgressBar pbProgress;
//    private ConstraintLayout clItems;
    private RecyclerView rvEmirate;
    private List<Emirate> emiratesData;
    Context context;
    private User selectedConsultant;
    private Country selectedCountry;
    private List<Integer> selectedStateIDs = new ArrayList<Integer>();
    String commaSeparatedSelectedStates = "";
    private int selectedGenderIndex = 0;
    SharedPreferences preferences;
    private ArrayList<Country> countriesData;
    String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_signup);
        context = this;
        preferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.signup);
        toolbar.setTitleTextColor(0xFF000000);

        initUIElements();
//        txtName.setText("Zahoor Testing For Android");
//        txtEmail.setText("zahoor.gorsi@gmail.com");

        lblSelectEmirates.setVisibility(View.GONE); //Hiding state selection for members
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                selectedCountry = (Country) extras.get(Key.KEY_SELECTED_COUNTRY);
//                txtCountryCode.setText(selectedCountry.getCode().toString());
                mobileNumber = extras.getString(Key.KEY_MOBILE_NUMBER);

                if (Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_CONSULTANT) {
                    callGetEmiratesApi(selectedCountry.getCountryId()); //Only for consultant
                    lblSelectEmirates.setVisibility(View.VISIBLE); //Hiding state selection for members
                }else {
                    selectedStateIDs.add(extras.getInt(Key.KEY_STATE_ID));
                    selectedConsultant =(User) extras.get(Key.KEY_SELECTED_CONSULTANT);
                    rvEmirate.setVisibility(View.GONE);         //Hiding state selection for members
                }
            }
        }

        Button fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    callSignupApi();
                }
            }
        });

        Button btnTermsAndCondition =  findViewById(R.id.btnTermsAndCondition);
        btnTermsAndCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("btnTermsAndCondition", "btnTermsAndCondition onTouch");
                String strURL;
                if (Utilities.isRTL(context) ) {
                    strURL = Constants.urlTermsAndConditionsAR;
                }else{
                    strURL = Constants.urlTermsAndConditionsEN;
                }
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra(Key.KEY_URL, Constants.PAGE_TYPE_TERMS);
                startActivity(intent);
            }
        });
        getSpinnersData();
    }

    private void getSpinnersData() {
        if (Constants.spinners == null) {
            pbProgress.setVisibility(View.VISIBLE);
            TazweegApi.getInstance().getTypes().enqueue(new Callback<Type>() {
                @Override
                public void onResponse(Call<Type> call, Response<Type> response) {
                    pbProgress.setVisibility(View.GONE);
                    if (response.body() != null) {
                        int status = response.body().getStatus();
                        if (status == 1) {
                            Type type = response.body();
                            if (type.getDropDowns().size() > 0) {
                                Constants.spinners = Utilities.JSONResponseToDropDowns(type.getDropDowns());  //Assigngin downloaded dropdowns data to constant variable
                                settingSpinnersData();
                                settingSpinnersItemSelection();
                            }
                        } else {
                            Utilities.myToastMessage(getApplicationContext(), getString(R.string.general_error ));
                        }
                    }
                }

                @Override
                public void onFailure(Call<Type> call, Throwable t) {
                    pbProgress.setVisibility(View.GONE);
                    Utilities.myToastMessage(getApplicationContext(), getString(R.string.failure));
                }
            });
        }else{
            settingSpinnersData();
            settingSpinnersItemSelection();
        }

    }

    private void settingSpinnersData() {
        //making new list with the context, to fetch arabic/english value
        List<DropDown> list = new ArrayList<DropDown>();
        for (DropDown item : Constants.spinners.getGender_Type()){
            item.setContext(getApplicationContext());
            list.add(item);
        }
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list);
        sGender.setAdapter(spinnerArrayAdapter);
    }

    private void settingSpinnersItemSelection() {
        sGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGenderIndex = position;
                Utilities.myLogError("Spinned index: %d", position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    //When consultant is signing up.
    private void callGetEmiratesApi(int selectedCountry) {
//        clItems.setVisibility(View.GONE);
        pbProgress.setVisibility(View.VISIBLE);
        TazweegApi.getInstance().getStatesByCountry(selectedCountry).enqueue(new Callback<EmirateResponse>() {
            @Override
            public void onResponse(Call<EmirateResponse> call, Response<EmirateResponse> response) {
                pbProgress.setVisibility(View.GONE);
                if (response.body() != null) {
                    int status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status == 1) {
                        emiratesData = response.body().getEmirates();
                        setEmiratesData();
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

    private void setEmiratesData() {
        rvEmirate.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        Collections.reverse(emiratesData);
        emiratesAdapter = new EmiratesAdapter(this, emiratesData,true);
        emiratesAdapter.setOnItemClickListener(this);
        rvEmirate.setAdapter(emiratesAdapter);
    }

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

    private void callSignupApi() {
        final Resources resources = this.getResources();
        String name = txtName.getText().toString();
        //String phone = txtCountryCode.getText().toString() + txtPhone.getText().toString();
        String phone =  this.mobileNumber;
        String email = txtEmail.getText().toString();
        final int typeID = Utilities.getSelectedUserType(getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE));    ////2 = Admin, 3 = Consultant, 4 = User
        //commaSeparatedSelectedStates is being populated in isInputValid()
        int genderID = Constants.spinners.getGender_Type().get(selectedGenderIndex).getValueId();  //// male = 7 and female = 8
        int setLanguage = Utilities.isRTL(this) == true ? 1 : 0;                            //Arabic = 1 , English = 0
        int consultantID = (selectedConsultant != null ? selectedConsultant.getConsultantId() : 0); //if consultant is signinup then send 0
        dialog.show();  //Please wait dialog
        TazweegApi.getInstance().signUp(name, email,phone,typeID, commaSeparatedSelectedStates, consultantID,genderID,setLanguage).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    int status = response.body().getStatus();
                    if (status == 1) {
                        String successMessage = "";
                        if(selectedCountry.getCountryId() == Constants.COUNTRY_CODE_KSA){
                            //Showing the dialog for  members
                            //IF selectedConsultant is not nil, its mean member is signup so display a message contactent it with selected consultant phone number, else if selectedConsultant is nill, its mean some consultant is signing up so just display message for consultant
                            successMessage = (selectedConsultant != null ? resources.getString(R.string.ksa_success_signup_msg) + selectedConsultant.getMobile() : resources.getString(R.string.ksa_consultant_success_signup_msg));
                        }else {
                            //IF selectedConsultant is not nil, its mean member is signup so display a message contactent it with selected consultant phone number, else if selectedConsultant is nill, its mean some consultant is signing up so just display message for consultant
                            successMessage = (selectedConsultant != null ? resources.getString(R.string.member_success_signup_msg)  : resources.getString(R.string.consultant_success_signup_msg));
                        }
                        AlertDialog alertDialog = new AlertDialog.Builder(CommonSignupActivity.this).create();
                        alertDialog.setTitle(resources.getString(R.string.information));
                        alertDialog.setMessage(successMessage);
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(CommonSignupActivity.this, LoginActivity.class);
                                        //If set, and the activity being launched is already running in the current task, then instead of launching a new instance of that activity, all of the other activities on top of it will be closed and this
                                        // Intent will be delivered to the (now on top) old activity as a new Intent.
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        dialog.dismiss(); //<-- change it with ur code
                                    }
                                } );
                        alertDialog.show();
                    }else if (status == 2) {    //User mobile number already exists in our system
                        AlertDialog alertDialog = new AlertDialog.Builder(CommonSignupActivity.this).create();
                        alertDialog.setTitle(resources.getString(R.string.information));
                        alertDialog.setMessage(resources.getString(R.string.duplicate_user));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss(); //<-- change it with ur code
                                        Intent intent = new Intent(CommonSignupActivity.this, LoginActivity.class);
                                        //If set, and the activity being launched is already running in the current task, then instead of launching a new instance of that activity, all of the other activities on top of it will be closed and this
                                        // Intent will be delivered to the (now on top) old activity as a new Intent.
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                } );
                        alertDialog.show();
                    }else {
                        Utilities.myToastMessage(getApplicationContext(), resources.getString(R.string.general_error));
                    }
                }else {
                    Utilities.myToastMessage(getApplicationContext(), resources.getString(R.string.general_error));
                }

            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                dialog.dismiss();
                Utilities.myToastMessage(getApplicationContext(), getString(R.string.failure));
            }
        });
    }

    private void initUIElements() {
        txtName = findViewById(R.id.tvCountryName);
//        txtCountryCode = findViewById(R.id.txtCountryCode);
//        txtPhone = findViewById(R.id.txtMobileNumber);
        txtEmail = findViewById(R.id.txtEmail);
        sGender = findViewById(R.id.spnBrideArrangement);
        cbTerms = findViewById(R.id.cbTerms);
        pbProgress = findViewById(R.id.pbProgress);
        lblSelectEmirates = findViewById(R.id.lblEmirates);
        rvEmirate = findViewById(R.id.rvEmirate);
        dialog = AppUtility.createPleaseWaitDialog(this);
    }

    private boolean isInputValid() {

        Resources resources = this.getResources();

        if (txtName.getText().toString().isEmpty()) {
            txtName.requestFocus();
            txtName.setError(resources.getString(R.string.error_name_required));
            return false;
        }

//        if (!Utilities.isValidPhoneNumber(txtCountryCode.getText().toString()+txtPhone.getText().toString())) {
//            txtPhone.requestFocus();
//            txtPhone.setError(resources.getString(R.string.error_invalid_phone_number));
//            return false;
//        }

        if(!txtEmail.getText().toString().isEmpty() && !Utilities.isValidEmail(txtEmail.getText().toString()))
        {
            txtEmail.requestFocus();
            txtEmail.setError(resources.getString(R.string.invalid_email));
            return false;
        }


        SharedPreferences preferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        if(Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_CONSULTANT) {// but if consultant is trying to signup
            commaSeparatedSelectedStates =  TextUtils.join(",", emiratesAdapter.getSelectedStateIds());
        }else{
            commaSeparatedSelectedStates =  TextUtils.join(",", selectedStateIDs);
        }

        if (commaSeparatedSelectedStates.length() == 0){
            rvEmirate.requestFocus();
            Utilities.myToastMessage(this, resources.getString(R.string.emirate_notValid));
            return false;
        }

        if (!cbTerms.isChecked()){
            cbTerms.setError(resources.getString(R.string.error_accept_terms));
            Utilities.myToastMessage(this, resources.getString(R.string.error_accept_terms));
            return false;
        }
        return true;
    }


    @Override
    public void onStateItemClick(int position, Emirate emirateData) {

    }
}
