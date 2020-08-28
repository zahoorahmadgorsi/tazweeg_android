package com.bulahej.tazweeg.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.apis_responses.TazweegApi;
import com.bulahej.tazweeg.apis_responses.UserResponse.UserResponse;
import com.bulahej.tazweeg.constant.Constants;
import com.bulahej.tazweeg.utilties.AppUtility;
import com.bulahej.tazweeg.utilties.LocaleHelper;
import com.bulahej.tazweeg.utilties.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText txtOldPassword,txtNewPassword,txtConfirmNewPassword;
    SharedPreferences preferences;
    Context context;
    private Resources resources;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        context = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.change_password);
        toolbar.setTitleTextColor(0xFF000000);
        resources = this.getResources();
        initUIElements();

//        txtOldPassword.setText("tazweeg786");
//        txtNewPassword.setText("tazweeg786");
//        txtConfirmNewPassword.setText("tazweeg786");
    }

    //This method is used to implement localization
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    //When back button is pressed
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

    private void initUIElements() {
        //Loading local variables
        Button fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    btnSubmitTap();
                }
            }
        });

        txtOldPassword = findViewById(R.id.txtOldPassword);
        txtNewPassword = findViewById(R.id.txtNewPassword);
        txtConfirmNewPassword = findViewById(R.id.txtConfirmNewPassword);
        dialog = AppUtility.createPleaseWaitDialog(this);
    }

    private boolean isInputValid() {

        Resources resources = this.getResources();

        if (txtOldPassword.getText().toString().isEmpty()) {
            txtOldPassword.requestFocus();
            txtOldPassword.setError(resources.getString(R.string.old_pass_missing));
            return false;
        }else if (txtNewPassword.getText().toString().isEmpty()) {
            txtNewPassword.requestFocus();
            txtNewPassword.setError(resources.getString(R.string.new_pass_missing));
            return false;
        }else if (txtNewPassword.getText().toString().length() < 6) {
            txtNewPassword.requestFocus();
            txtNewPassword.setError(resources.getString(R.string.new_pass_length));
            return false;
        }
        else if (!txtNewPassword.getText().toString().equals(txtConfirmNewPassword.getText().toString())) {
            txtConfirmNewPassword.requestFocus();
            txtConfirmNewPassword.setError(resources.getString(R.string.passwords_not_match));
            return false;
        }


        return true;
    }

    private void btnSubmitTap(){
        final Resources resources = this.getResources();
        dialog.show();  //Please wait dialog
        TazweegApi.getInstance().changePassword(Constants.loggedInMember.getId(),
                txtNewPassword.getText().toString(),
                txtOldPassword.getText().toString()).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, final Response<UserResponse> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    int status = response.body().getStatus();
                    if (status == 1) {
                        AlertDialog alertDialog = new AlertDialog.Builder(ChangePasswordActivity.this).create();
                        alertDialog.setTitle(resources.getString(R.string.success));
                        alertDialog.setMessage(resources.getString(R.string.changePasswordSuccess));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss(); //<-- change it with ur code
                                        finish();
                                    }
                                } );
                        alertDialog.show();
                    }else if (status == 3) {    //Not Exist
                        AlertDialog alertDialog = new AlertDialog.Builder(ChangePasswordActivity.this).create();
                        alertDialog.setTitle(resources.getString(R.string.verification));
                        alertDialog.setMessage(resources.getString(R.string.passwordIsMisMatched));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss(); //<-- change it with ur code
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
}
