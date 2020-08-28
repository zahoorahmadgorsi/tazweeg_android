package com.bulahej.tazweeg.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.apis_responses.TazweegApi;
import com.bulahej.tazweeg.apis_responses.UserResponse.UserResponse;
import com.bulahej.tazweeg.constant.Constants;
import com.bulahej.tazweeg.constant.Key;
import com.bulahej.tazweeg.utilties.AppUtility;
import com.bulahej.tazweeg.utilties.LocaleHelper;
import com.bulahej.tazweeg.utilties.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText txtPhoneNumber,txtEmail;
    SharedPreferences preferences;
    Resources resources;
    Context context;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    btnSubmitTap();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.forgot_password);
        toolbar.setTitleTextColor(0xFF000000);

        context = this;
        preferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        resources = this.getResources();
        initUIElements();

//        txtPhoneNumber.setText("971589108662");
//        txtEmail.setText("zahoor.gorsi@gmail.com");
    }

    private void initUIElements() {
        //Loading local variables
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        txtEmail = findViewById(R.id.txtEmail);
        dialog = AppUtility.createPleaseWaitDialog(this);
    }

    private boolean isInputValid() {

        Resources resources = this.getResources();

        if ((txtPhoneNumber.getText().length() == 0  && txtEmail.getText().length() == 0) ||    //Both are empty
                (txtPhoneNumber.getText().length() > 0  && txtEmail.getText().length() > 0)         //Both are filled
        )  {
            Utilities.myToastMessage(getApplicationContext(), resources.getString(R.string.one_is_required));
            return false;
        }else if (txtPhoneNumber.getText().length() > 0  && !Utilities.isValidPhoneNumber(txtPhoneNumber.getText().toString())) {
            txtPhoneNumber.requestFocus();
            txtPhoneNumber.setError(resources.getString(R.string.error_invalid_phone_number));
            return false;
        }else if( txtPhoneNumber.getText().length() > 0 && !txtEmail.getText().toString().isEmpty() && !Utilities.isValidEmail(txtEmail.getText().toString()))
        {
            txtEmail.requestFocus();
            txtEmail.setError(resources.getString(R.string.invalid_email));
            return false;
        }


        return true;
    }

    private void btnSubmitTap(){
        final Resources resources = this.getResources();
        String forgotType , value = "";
        String email = txtEmail.getText().toString();
        if (txtPhoneNumber.getText().toString().length() > 0){
            forgotType = "sms";
            value = txtPhoneNumber.getText().toString();
        }else{
            forgotType = "email";
            value = txtEmail.getText().toString();
        }

        dialog.show();  //Please wait dialog
        TazweegApi.getInstance().forgotPassword(forgotType, value).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, final Response<UserResponse> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    int status = response.body().getStatus();
                    if (status == 1) {
                        AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordActivity.this).create();
                        alertDialog.setTitle(resources.getString(R.string.verification));
                        alertDialog.setMessage(resources.getString(R.string.check_phone_email));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss(); //<-- change it with ur code
                                        finish();
                                    }
                                } );
                        alertDialog.show();
                    }else if (status == 3) {    //Not Exist
                        AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordActivity.this).create();
                        alertDialog.setTitle(resources.getString(R.string.verification));
                        alertDialog.setMessage(resources.getString(R.string.emailPhoneNotExist));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss(); //<-- change it with ur code
                                        finish();
                                    }
                                } );
                        alertDialog.show();
                    } else if (status == 4) {    //User is Inactive, redirect him to verification page
                        AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordActivity.this).create();
                        alertDialog.setTitle(resources.getString(R.string.verification));
                        alertDialog.setMessage(resources.getString(R.string.check_phone));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss(); //<-- change it with ur code
                                        Intent intent = new Intent(context, OTPVerificationActivity.class);
                                        String phone = response.body().getPhone();
                                        if (txtPhoneNumber.getText().toString().length() > 0){
                                            phone = txtPhoneNumber.getText().toString();
                                        }else if( response.body().getPhone().length() > 0 ) { //Incase of forgot password via email
                                            phone = response.body().getPhone();
                                        }
                                        intent.putExtra(Key.KEY_MOBILE_NUMBER, phone);  //Mobile number is required for verification code web API
                                        startActivity(intent);
                                        finish();
                                    }
                                } );
                        alertDialog.show();
                    }else {
                        Utilities.myToastMessage(getApplicationContext(), resources.getString(R.string.general_error));
                    }
                }

            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                dialog.dismiss();
                Utilities.myToastMessage(getApplicationContext(), getString(R.string.failure));
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                // todo: goto back activity from here
//                Intent intent = new Intent(context.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

}
