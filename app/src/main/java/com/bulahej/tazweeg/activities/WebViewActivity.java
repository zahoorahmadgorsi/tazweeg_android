package com.bulahej.tazweeg.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.apis_responses.TazweegApi;
import com.bulahej.tazweeg.apis_responses.UserResponse.User;
import com.bulahej.tazweeg.apis_responses.UserResponse.UserPaymentResponse;
import com.bulahej.tazweeg.apis_responses.UserResponse.UserResponse;
import com.bulahej.tazweeg.constant.Constants;
import com.bulahej.tazweeg.constant.Key;
import com.bulahej.tazweeg.utilties.AppUtility;
import com.bulahej.tazweeg.utilties.LocaleHelper;
import com.bulahej.tazweeg.utilties.Utilities;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebViewActivity extends AppCompatActivity {

    private static final String[] PERMISSIONS = {
            Manifest.permission.INTERNET
    };
    private ProgressBar pbProgress;
    private WebView webView;
    private int iPageType =-1;
    Context context;
    String WEB_URL;
    private User currentMember; //who is coming on this activity for webview or payment
    SharedPreferences preferences;
    private AlertDialog dialog;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        context = this;
        resources = this.getResources();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setTitleTextColor(0xFF000000);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) { //no URL is passed
                iPageType = extras.getInt(Key.KEY_URL);
            }
        }
        pbProgress = findViewById(R.id.pbProgress);
        dialog = AppUtility.createPleaseWaitDialog(this);
        preferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);

        //Loading current member details from localStorage where it was stored after login as well as after step 1
        if (Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_MEMBER && Constants.loggedInMember != null){//Member
            currentMember = Constants.loggedInMember;
        }else{//loading current member whose data is being filled by consultant.
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if(extras != null) { //User is coming to sign up
                    currentMember = (User) extras.get(Constants.CURRENT_MEMBER);
                }
            }
        }

        WEB_URL = Constants.urlSnapChat;    //default
        switch (iPageType){
            case Constants.PAGE_TYPE_TERMS:
                getSupportActionBar().setTitle(R.string.terms_and_condition);
                if (Utilities.isRTL(context)) {
                    WEB_URL = Constants.urlTermsAndConditionsAR;
                }else{
                    WEB_URL = Constants.urlTermsAndConditionsEN;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMISSIONS, 100);
                } else {
                    loadWebPage(WEB_URL);
                }
                break;
            case Constants.PAGE_TYPE_FAQ:
                getSupportActionBar().setTitle(R.string.faqs);
                if (Utilities.isRTL(context)) {
                    WEB_URL = Constants.urlFrequentylAskedQuestionsAR ;
                }else{
                    WEB_URL = Constants.urlFrequentylAskedQuestionsEN;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMISSIONS, 100);
                } else {
                    loadWebPage(WEB_URL);
                }
                break;
            case Constants.PAGE_TYPE_PRIVACY_POLICY:
                getSupportActionBar().setTitle(R.string.privacy_policy);
                if (Utilities.isRTL(context)) {
                    WEB_URL = Constants.urlPrivacyPolicyAR;
                }else{
                    WEB_URL = Constants.urlPrivacyPolicyEN;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMISSIONS, 100);
                } else {
                    loadWebPage(WEB_URL);
                }
                break;
            case Constants.PAGE_TYPE_PAYMENT:
                getSupportActionBar().setTitle(R.string.payment);
                int age = 36;
                if (currentMember.getBirthDate() != null){ //if age is nill then calculate it from DOB
                    age = Utilities.getAge(currentMember.getBirthDate(),Constants.formatFromServer);
                }
                if (age <= 35){
                    getPaymentURL();
                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(WebViewActivity.this).create();
                    alertDialog.setTitle(resources.getString(R.string.payment));
                    alertDialog.setMessage(resources.getString(R.string.age_check));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss(); //<-- change it with ur code
                                    if (Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_MEMBER) { //member
                                        Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
                                        //If set, and the activity being launched is already running in the current task, then instead of launching a new instance of that activity, all of the other activities on top of it will be closed and this
                                        // Intent will be delivered to the (now on top) old activity as a new Intent.
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                    else{   //consultant
                                        finish();   //go to previous UI in case of consultant
                                    }
                                }
                            } );
                    alertDialog.show();
                }
                break;
            case Constants.PAGE_TYPE_FB:
                getSupportActionBar().setTitle(R.string.tazweeg_facebook);
                WEB_URL = Constants.urlFacebook;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMISSIONS, 100);
                } else {
                    loadWebPage(WEB_URL);
                }
                break;
            case Constants.PAGE_TYPE_INSTA:
                getSupportActionBar().setTitle(R.string.tazweeg_instagram);
                WEB_URL = Constants.urlInstagram;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMISSIONS, 100);
                } else {
                    loadWebPage(WEB_URL);
                }
                break;
            case Constants.PAGE_TYPE_PINTEREST:
                getSupportActionBar().setTitle(R.string.tazweeg_pinterest);
                WEB_URL = Constants.urlPintrest;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMISSIONS, 100);
                } else {
                    loadWebPage(WEB_URL);
                }
                break;
            case Constants.PAGE_TYPE_TELEGRAM:
                getSupportActionBar().setTitle(R.string.tazweeg_telegram);
                WEB_URL = Constants.urlTelegram;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMISSIONS, 100);
                } else {
                    loadWebPage(WEB_URL);
                }
                break;
            case Constants.PAGE_TYPE_TWITTER:
                getSupportActionBar().setTitle(R.string.tazweeg_twitter);
                WEB_URL = Constants.urlTwitter;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMISSIONS, 100);
                } else {
                    loadWebPage(WEB_URL);
                }
                break;
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            loadWebPage(WEB_URL);
        }
    }

    //This method changes the activity language according to the chosen language
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    private void loadWebPage(String url) {

        Utilities.myLogDebug("Url: %s", url);
        webView = findViewById(R.id.wvWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl(url);
        webView.setInitialScale(1);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pbProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pbProgress.setVisibility(View.GONE);
                Utilities.myLogDebug("Url (After loaded): %s", url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView wView, String url) {
                if (url.contains(Constants.urlPaymentRedirect)) //check if that's a url you want to load internally
                {
                    String[] splittedArray = url.split("=");
                    if (splittedArray.length > 0){//minimum one entry should be there
                        String referenceNumber = "";
                        referenceNumber = splittedArray[splittedArray.length - 1];
//                    Confirm the payment status again this reference number
//                    referenceNumber = "52a1789f-7234-44eb-8fef-ece77f209c9e"; // captured
//                    referenceNumber = "41bd4796-edae-49f1-bb50-31ce9a59bf6b"; // captured
                        getPaymentConfirmation( referenceNumber);
                    }
                    return true;
                }
                else
                {
                    return false; //Let the system handle it
                }
            }
        });

    }

    private void getPaymentURL()
    {
        dialog.show();  //Please wait dialog
        String email = currentMember.getId() + "@tazweeg.com";
        TazweegApi.getInstance().getPaymentURL( currentMember.getId()
                ,email
                ,Constants.PAYMENT_TYPE_CHOOSING    //in case of matching payment , i.e. next 2500 we need to change this
        ).enqueue(new Callback<UserPaymentResponse>() {
            @Override
            public void onResponse(Call<UserPaymentResponse> call, Response<UserPaymentResponse> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    int status = response.body().getStatus();
                    if (status == 1) {
                        loadWebPage(response.body().getUrl());
//                        loadWebPage("https://www.google.com");
                    }else if(status == 2){
                        Utilities.myToastMessage(getApplicationContext(), resources.getString(R.string.already_paid));
                    }
                    else {
                        Utilities.myToastMessage(getApplicationContext(), resources.getString(R.string.general_error));
                    }
                }
            }
            @Override
            public void onFailure(Call<UserPaymentResponse> call, Throwable t) {
                dialog.dismiss();
                Utilities.myToastMessage(getApplicationContext(), getString(R.string.failure));
            }
        });
    }

    private void getPaymentConfirmation(String referenceNumber) {
        if (currentMember != null) {
            dialog.show();  //Please wait dialog
            TazweegApi.getInstance().getPaymentConfirmation(referenceNumber
                    , currentMember.getId()
            ).enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    dialog.dismiss();
                    if (response.body() != null) {
                        int status = response.body().getStatus();
                        if (status == 1) { //Captured
                            currentMember = response.body().getUser();
                            if (Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_MEMBER) {
                                Constants.loggedInMember = currentMember;  //Storing into constants as it will be required in whole application
                                //Making logged in user record persistent
                                //https://stackoverflow.com/questions/5418160/store-and-retrieve-a-class-object-in-shared-preference
                                Gson gson = new Gson();
                                String json = gson.toJson(currentMember); // myObject - instance of MyObject
                                preferences.edit().putString(Constants.LOGGED_IN_USER, json).apply();
                            }
                            ShowCompleteProfileAlert();
                        } else {
                            Utilities.myToastMessage(getApplicationContext(), resources.getString(R.string.general_error));
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    dialog.dismiss();
                    Utilities.myToastMessage(getApplicationContext(), getString(R.string.failure));
                    finish();
                }
            });
        }
    }

    public void ShowCompleteProfileAlert(){
        AlertDialog alertDialog = new AlertDialog.Builder(WebViewActivity.this).create();
        alertDialog.setTitle(resources.getString(R.string.payment));
        alertDialog.setMessage(resources.getString(R.string.profile_completed));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //<-- change it with ur code
                        if (Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_MEMBER) { //member
                            Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
                            //If set, and the activity being launched is already running in the current task, then instead of launching a new instance of that activity, all of the other activities on top of it will be closed and this
                            // Intent will be delivered to the (now on top) old activity as a new Intent.
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else{   //consultant
                            finish();   //go to previous UI in case of consultant
                        }
                    }
                } );
        alertDialog.show();
    }
}
