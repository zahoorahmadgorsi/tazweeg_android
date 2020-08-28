package com.bulahej.tazweeg.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.apis_responses.MobileSignUpResponse;
import com.bulahej.tazweeg.apis_responses.SignUpResponseMessage;
import com.bulahej.tazweeg.apis_responses.TazweegApi;
import com.bulahej.tazweeg.apis_responses.list_countries.Country;
import com.bulahej.tazweeg.constant.Constants;
import com.bulahej.tazweeg.constant.Key;
import com.bulahej.tazweeg.utilties.AppUtility;
import com.bulahej.tazweeg.utilties.LocaleHelper;
import com.bulahej.tazweeg.utilties.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bulahej.tazweeg.constant.Constants.COUNTRY_CODE_BAHRAIN;
import static com.bulahej.tazweeg.constant.Constants.COUNTRY_CODE_KSA;
import static com.bulahej.tazweeg.constant.Constants.COUNTRY_CODE_KUWAIT;
import static com.bulahej.tazweeg.constant.Constants.COUNTRY_CODE_OMAN;
import static com.bulahej.tazweeg.constant.Constants.COUNTRY_CODE_UAE;
import static com.bulahej.tazweeg.constant.Constants.USER_TYPE_MEMBER;

public class PhoneActivity extends AppCompatActivity {

    ImageView imgCountryFlag;
    EditText txtCountryCode, txtPhoneNumber;
    Context context;
    CheckBox cbTerms;
    private AlertDialog dialog;
    Country selectedCountry = new Country(COUNTRY_CODE_UAE,"United Arab Emirates", "الإمارات","uae.png", "971");    //default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    callSignUpApi();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.signup);
        toolbar.setTitleTextColor(0xFF000000);
        context = this;

        initUIElements();

    }

    private void initUIElements() {
        //assigning tap events on snapChat image
        imgCountryFlag = findViewById(R.id.imgCountryFlag);
        imgCountryFlag.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("imgCountryFlag", "imgCountryFlag onTouch");
                Intent intent = new Intent(context, CountriesActivity.class);
                startActivityForResult(intent,1);
            }
        });
        txtCountryCode = findViewById(R.id.txtCountryCode);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        cbTerms = findViewById(R.id.cbTerms);
        dialog = AppUtility.createPleaseWaitDialog(this);
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
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    private boolean isInputValid() {
        Resources resources = this.getResources();

        if (!Utilities.isValidPhoneNumber(txtCountryCode.getText().toString() + txtPhoneNumber.getText().toString())) {
            txtPhoneNumber.requestFocus();
            txtPhoneNumber.setError(resources.getString(R.string.error_invalid_phone_number));
            return false;
        }

        if (!cbTerms.isChecked()){
            cbTerms.setError(resources.getString(R.string.error_accept_terms));
            Utilities.myToastMessage(this, resources.getString(R.string.error_accept_terms));
            return false;
        }
        return true;
    }


    private void callSignUpApi() {
        final Resources resources = this.getResources();
        final int loggingInUserType = Utilities.getSelectedUserType(getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE));    ////2 = Admin, 3 = Consultant, 4 = User
        final String mobileNumber = txtCountryCode.getText().toString() + txtPhoneNumber.getText().toString();
        int countryID = (selectedCountry != null ? selectedCountry.getCountryId() : 218); //if consultant is signinup then send 0
        dialog.show();  //Please wait dialog
        TazweegApi.getInstance().mobileVerification(mobileNumber, countryID).enqueue(new Callback<MobileSignUpResponse>() {
            @Override
            public void onResponse(Call<MobileSignUpResponse> call, Response<MobileSignUpResponse> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    int status = response.body().getStatus();
                    SignUpResponseMessage message =  response.body().getMessage();
                    if (status == 1) {
                        if(selectedCountry.getCountryId() == Constants.COUNTRY_CODE_KSA){   //No OTP for KSA
                            if (loggingInUserType == USER_TYPE_MEMBER){
                                Intent intent = new Intent(PhoneActivity.this , StateAndConsultantActivity.class);
                                intent.putExtra(Key.KEY_SELECTED_COUNTRY, selectedCountry);
                                intent.putExtra(Key.KEY_MOBILE_NUMBER, mobileNumber);
                                intent.putExtra(Key.KEY_USER_ID, 123);  //will be used in case of transfer
                                intent.putExtra(Key.KEY_PAGE_TYPE, Constants.PAGE_TYPE_SIGNUP);
                                startActivity(intent);
                            }else{  //Consultant will go to common sign up directly
                                Intent intent = new Intent(PhoneActivity.this , CommonSignupActivity.class);
                                intent.putExtra(Key.KEY_SELECTED_COUNTRY, selectedCountry);
                                intent.putExtra(Key.KEY_MOBILE_NUMBER, mobileNumber);
                                intent.putExtra(Key.KEY_USER_ID, 123);  //will be used in case of transfer
                                intent.putExtra(Key.KEY_PAGE_TYPE, Constants.PAGE_TYPE_SIGNUP);
                                startActivity(intent);
                            }
                        }else { // NON KSA
                            Intent intent = new Intent(context, OTPVerificationActivity.class);
                            intent.putExtra(Key.KEY_MOBILE_NUMBER, mobileNumber);  //Mobile number is required for verification code web API
                            intent.putExtra(Key.KEY_SELECTED_COUNTRY, selectedCountry);
                            intent.putExtra(Key.KEY_OTP, message.getPin());
                            intent.putExtra(Key.KEY_OTP_EXPIRY, message.getOtpExpiry());
                            startActivity(intent);
                        }
                    }else if (status == 2) {    //User mobile number already exists in our system
                        AlertDialog alertDialog = new AlertDialog.Builder(PhoneActivity.this).create();
                        alertDialog.setTitle(resources.getString(R.string.information));
                        alertDialog.setMessage(resources.getString(R.string.duplicate_user));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss(); //<-- change it with ur code
                                        Intent intent = new Intent(PhoneActivity.this, LoginActivity.class);
                                        //If set, and the activity being launched is already running in the current task, then instead of launching a new instance of that activity, all of the other activities on top of it will be closed and this
                                        // Intent will be delivered to the (now on top) old activity as a new Intent.
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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
            public void onFailure(Call<MobileSignUpResponse> call, Throwable t) {
                dialog.dismiss();
                Utilities.myToastMessage(getApplicationContext(), getString(R.string.failure));
            }
        });
    }


    //Loading countries
//    private void callGetCountriesApi() {
//        TazweegApi.getInstance().getListOfCountries().enqueue(new Callback<CountryResponse>() {
//            @Override
//            public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {
//                if (response.body() != null) {
//                    int status = response.body().getStatus();
//                    if (status == 1) {
//                        countriesData = new ArrayList<>(response.body().getCountries());
//                        if (countriesData != null && countriesData.size() > 0){
//                            selectedCountry = countriesData.get(0); //country at index 0 will be default country
//                            setCountry(selectedCountry);
//                        }
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CountryResponse> call, Throwable t) {
//            }
//
//        });
//    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                selectedCountry = (Country) data.getSerializableExtra(Key.KEY_SELECTED_COUNTRY);
                setCountry(selectedCountry);
            }
        }
    }

    private void setCountry(Country country){
        if (country != null){
            this.txtCountryCode.setText(country.getCode());
            switch (selectedCountry.getCountryId()){
                case COUNTRY_CODE_UAE:
                    imgCountryFlag.setImageResource(R.drawable.uae_flag);
                    break;
                case COUNTRY_CODE_BAHRAIN:
                    imgCountryFlag.setImageResource(R.drawable.bahrain_flag);
                    break;
                case COUNTRY_CODE_KUWAIT:
                    imgCountryFlag.setImageResource(R.drawable.kuwait_flag);
                    break;
                case COUNTRY_CODE_OMAN:
                    imgCountryFlag.setImageResource(R.drawable.oman_flag);
                    break;
                case COUNTRY_CODE_KSA:
                    imgCountryFlag.setImageResource(R.drawable.saudi_flag);
                    break;
            }
        }
    }

}
