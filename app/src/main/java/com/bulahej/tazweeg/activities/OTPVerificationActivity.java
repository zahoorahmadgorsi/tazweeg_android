package com.bulahej.tazweeg.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bulahej.tazweeg.constant.Constants.USER_TYPE_MEMBER;

public class OTPVerificationActivity extends AppCompatActivity {
    private EditText txtVerificationCode;
    private AlertDialog dialog;
    private String mobileNumber;
    Resources resources;
    private Country selectedCountry;
    private int OTP , OTPExpiry;
    Button btnSubmitOTP, btnResendOTP;
    CountDownTimer mCountDownTimer;
    TextView tvCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.verification_code);
        toolbar.setTitleTextColor(0xFF000000);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) { //User is coming to sign up
                mobileNumber = extras.getString(Key.KEY_MOBILE_NUMBER);
                selectedCountry = (Country) extras.get(Key.KEY_SELECTED_COUNTRY);
                OTP = extras.getInt(Key.KEY_OTP);
                OTPExpiry = extras.getInt(Key.KEY_OTP_EXPIRY);
                if (OTPExpiry > 0){
                    timerStart();
                    mCountDownTimer.start();
                }
            }
        }
        initUIElements();
        resources = this.getResources();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    private void initUIElements() {
        btnSubmitOTP = findViewById(R.id.btnSubmitOTP);
        btnResendOTP = findViewById(R.id.btnResendOTP);
        txtVerificationCode = findViewById(R.id.txtVerificationCode);
        tvCounter = findViewById(R.id.tvCounter);
        dialog = AppUtility.createPleaseWaitDialog(this);

        btnSubmitOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tapOnSubmitOTP();
            }
        });
        btnResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOTPTapped();
            }
        });
    }

    public boolean isInputValid() {
        String verifyCode = txtVerificationCode.getText().toString();
        if (verifyCode.isEmpty() || verifyCode.length() != 4 )  {
            txtVerificationCode.requestFocus();
            txtVerificationCode.setError(resources.getString(R.string.error_invalid_verification_code));
            return false;
        }
        return true;
    }

    private void tapOnSubmitOTP() {
        if (isInputValid() &&  txtVerificationCode.getText().toString().equals(String.valueOf(OTP))){

            AlertDialog alertDialog = new AlertDialog.Builder(OTPVerificationActivity.this).create();
            alertDialog.setTitle(resources.getString(R.string.information));
            alertDialog.setMessage(resources.getString(R.string.otp_verified));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            int loggingInUserType = Utilities.getSelectedUserType(getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE));    ////2 = Admin, 3 = Consultant, 4 = User
                            if (loggingInUserType == USER_TYPE_MEMBER){
                                Intent intent = new Intent(OTPVerificationActivity.this , StateAndConsultantActivity.class);
                                intent.putExtra(Key.KEY_SELECTED_COUNTRY, selectedCountry);
                                intent.putExtra(Key.KEY_MOBILE_NUMBER, mobileNumber);
                                intent.putExtra(Key.KEY_USER_ID, 123);  //will be used in case of transfer
                                intent.putExtra(Key.KEY_PAGE_TYPE, Constants.PAGE_TYPE_SIGNUP);
                                startActivity(intent);
                            }else{  //Consultant will go to common sign up directly
                                Intent intent = new Intent(OTPVerificationActivity.this , CommonSignupActivity.class);
                                intent.putExtra(Key.KEY_SELECTED_COUNTRY, selectedCountry);
                                intent.putExtra(Key.KEY_MOBILE_NUMBER, mobileNumber);
                                intent.putExtra(Key.KEY_USER_ID, 123);  //will be used in case of transfer
                                intent.putExtra(Key.KEY_PAGE_TYPE, Constants.PAGE_TYPE_SIGNUP);
                                startActivity(intent);
                            }
                            mCountDownTimer.cancel(); //Stop timer
                        }
                    } );
            alertDialog.show();
        }else{  //
            AlertDialog alertDialog = new AlertDialog.Builder(OTPVerificationActivity.this).create();
            alertDialog.setTitle(resources.getString(R.string.verification));
            alertDialog.setMessage(resources.getString(R.string.error_invalid_verification_code));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); //<-- change it with ur code
                        }
                    } );
            alertDialog.show();
        }
    }

    private void resendOTPTapped() {
        final Resources resources = this.getResources();
        int countryID = (selectedCountry != null ? selectedCountry.getCountryId() : 218); //if consultant is signingup then send 0
        dialog.show();  //Please wait dialog
        TazweegApi.getInstance().mobileVerification(mobileNumber, countryID).enqueue(new Callback<MobileSignUpResponse>() {
            @Override
            public void onResponse(Call<MobileSignUpResponse> call, Response<MobileSignUpResponse> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    int status = response.body().getStatus();
                    SignUpResponseMessage message =  response.body().getMessage();
                    if (status == 1 && message != null) {
                        OTP = message.getPin();
                        OTPExpiry = message.getOtpExpiry();
                        //resetting the timer now
                        mCountDownTimer.cancel();
                        mCountDownTimer.start();//starting the timer again

                    }else if (status == 2) {    //User mobile number already exists in our system
                        AlertDialog alertDialog = new AlertDialog.Builder(OTPVerificationActivity.this).create();
                        alertDialog.setTitle(resources.getString(R.string.information));
                        alertDialog.setMessage(resources.getString(R.string.duplicate_user));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss(); //<-- change it with ur code
                                        Intent intent = new Intent(OTPVerificationActivity.this, LoginActivity.class);
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

    private void timerStart(){
        mCountDownTimer = new CountDownTimer(OTPExpiry * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (OTPExpiry <= 0){
                    mCountDownTimer.cancel();
                    finish();   //activity finish
                }else {
                    OTPExpiry -= 1;
                    int minutes = OTPExpiry / 60;
                    int seconds = OTPExpiry % 60;
                    //string format like 00:00
                    tvCounter.setText(String.format(Locale.ENGLISH, "%02d", minutes) + ":" + String.format(Locale.ENGLISH, "%02d", seconds));
                }
            }

            @Override
            public void onFinish() {
                finish();   //activity finish
            }
        };
    }
}
