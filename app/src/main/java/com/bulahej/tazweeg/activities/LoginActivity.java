package com.bulahej.tazweeg.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.apis_responses.TazweegApi;
import com.bulahej.tazweeg.apis_responses.UserResponse.User;
import com.bulahej.tazweeg.apis_responses.UserResponse.UserResponse;
import com.bulahej.tazweeg.constant.Constants;
import com.bulahej.tazweeg.utilties.AppUtility;
import com.bulahej.tazweeg.utilties.LocaleHelper;
import com.bulahej.tazweeg.utilties.Utilities;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    Button btnCross;
    TextView lblWelcome;
    EditText txtUserName,txtPassword;
    Button btnForgotPassword,btnLogin,btnLoginViaSmartpass;
    TextView lblVersionCode;
    Context context;
    private AlertDialog dialog;
    SharedPreferences preferences;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        preferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        resources = this.getResources();

        initUIElements();

        if ( Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_MEMBER){
            lblWelcome.setText(resources.getString(R.string.member_login));
            txtUserName.setText("Tazweeg_");

//            Demo Account @ Live Server
//            txtUserName.text = "Tazweeg_1606"
//            txtPassword.text = "tazweeg786"

//            txtUserName.setText("Tazweeg_1608"); //Female
//            txtPassword.setText("tazweeg786");

//            txtUserName.setText( "Tazweeg_1917");   //Male
//            txtPassword.setText( "398981");
//            Mr Humaid
//            txtUserName.text = "Tazweeg_2"
//            txtPassword.text = "787796"
//            My User on ahsan local
//            txtUserName.text = "Tazweeg_1496"
//            txtPassword.text = "884460"
        }else{
            lblWelcome.setText(resources.getString(R.string.consultant_login));

            //Test Data
//            Demo Account @ Demo Consultant
//            txtUserName.text = "Tazweeg_1605"
//            txtPassword.text = "tazweeg786"

//            txtUserName.setText("Tazweeg_1607");
//            txtPassword.setText("tazweeg786");

//            txtUserName.text = "Admin"
//            txtPassword.text = "admin786"

//            Mr humaid
//            txtUserName.text = "AUH3@Tazweeg.com"
//            txtPassword.text = "auh3654321"
            //            My User on ahsan local
//            txtUserName.text = "AUH3@tazweeg.com"
//            txtPassword.text = "auh3654321"
        }


        //Actions on each UI widget
        btnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent forgotPassword = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(forgotPassword);
                if (isInputValid()) {
                    callLoginApi();
                }
            }
        });
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPassword = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(forgotPassword);
            }
        });
        btnLoginViaSmartpass.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Snackbar.make(findViewById(android.R.id.content), "Login via smart pass is coming soon.", Snackbar.LENGTH_LONG)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                        finish();
                            }
                        }).show();
            }
        });
        lblVersionCode.setText(Utilities.getAppVersion(getApplicationContext()));
    }

    //This method changes the activity language according to the chosen language
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    private void callLoginApi() {
        String userName = txtUserName.getText().toString();
        String password = txtPassword.getText().toString();
        dialog.show();  //Please wait dialog
        TazweegApi.getInstance().login(userName,password).enqueue(new Callback<UserResponse>() {

            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    int status = response.body().getStatus();
                    if (status == 1) {
                        User loggedInUser = response.body().getUser();
                        Constants.loggedInMember = loggedInUser;  //Storing into constants as it will be required in whole application
                        if(Utilities.getSelectedUserType(preferences) == loggedInUser.getTypeId() || loggedInUser.getTypeId() == Constants.USER_TYPE_ADMIN ){ //If logged in user is according to UserSelection as done at very first page OR logged in user is an admin
                            //Making logged in user record persistent
                            //https://stackoverflow.com/questions/5418160/store-and-retrieve-a-class-object-in-shared-preference
                            Gson gson = new Gson();
                            String json = gson.toJson(loggedInUser); // myObject - instance of MyObject
                            preferences.edit().putString(Constants.LOGGED_IN_USER, json).apply();

                            if (loggedInUser.getTypeId() == Constants.USER_TYPE_ADMIN){ //Storing current usertype as admin, because in user selection page we are only storing member or consultant
                                preferences.edit().putInt(Constants.CURRENT_USER,Constants.USER_TYPE_ADMIN).apply();
                            }

                            if (loggedInUser.getTypeId() == Constants.USER_TYPE_MEMBER){
                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
                            }else{  //if logged in user is consultant or admin
                                Snackbar.make(findViewById(android.R.id.content), "Consultant logged in successfully.", Snackbar.LENGTH_LONG)
                                .setAction("Success", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
//                                        finish();
                                    }
                                }).show();
                            }
                        }else if(loggedInUser.getTypeId() == Constants.USER_TYPE_MEMBER){
                            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                            alertDialog.setTitle(resources.getString(R.string.verification));
                            alertDialog.setMessage(resources.getString(R.string.invalid_user_member));
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss(); //<-- change it with ur code
                                            finish();
                                        }
                                    } );
                            alertDialog.show();
//                            Snackbar.make(findViewById(android.R.id.content), resources.getString(R.string.invalid_user_consultant), Snackbar.LENGTH_LONG)
//                                .setAction(resources.getString(R.string.ok), new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        finish();
//                                    }
//                                }).show();
                        }else if(loggedInUser.getTypeId() == Constants.USER_TYPE_CONSULTANT){
                            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                            alertDialog.setTitle(resources.getString(R.string.verification));
                            alertDialog.setMessage(resources.getString(R.string.invalid_user_consultant));
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss(); //<-- change it with ur code
                                            finish();
                                        }
                                    } );
                            alertDialog.show();
//                            Snackbar.make(findViewById(android.R.id.content), resources.getString(R.string.invalid_user_member), Snackbar.LENGTH_LONG)
//                                    .setAction(resources.getString(R.string.ok), new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            finish();
//                                        }
//                                    }).show();
                        }

                    }else {    //User mobile number already exists in our system
                        Utilities.myToastMessage(getApplicationContext(), resources.getString(R.string.verification_failed));

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
        //Loading local variables
        btnCross =  findViewById(R.id.btnCross);
        lblWelcome =  findViewById(R.id.lblWelcome);
        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginViaSmartpass = findViewById(R.id.btnLoginViaSmartPass);
        lblVersionCode =  findViewById(R.id.lblVersionBuild);
        dialog = AppUtility.createPleaseWaitDialog(this);
    }

    private boolean isInputValid() {

        Resources resources = this.getResources();

        if (txtUserName.getText().toString().isEmpty()) {
            txtUserName.requestFocus();
            txtUserName.setError(resources.getString(R.string.invalid_username));
            return false;
        }

        if (txtPassword.getText().toString().isEmpty()) {
            txtPassword.requestFocus();
            txtPassword.setError(resources.getString(R.string.invalid_password));
            return false;
        }

        return true;
    }
}
