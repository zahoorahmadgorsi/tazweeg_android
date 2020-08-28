package com.bulahej.tazweeg.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.apis_responses.TazweegApi;
import com.bulahej.tazweeg.apis_responses.UserResponse.User;
import com.bulahej.tazweeg.apis_responses.UserResponse.UserResponse;
import com.bulahej.tazweeg.apis_responses.registration_form_new.DropDown;
import com.bulahej.tazweeg.apis_responses.registration_form_new.Type;
import com.bulahej.tazweeg.constant.Constants;
import com.bulahej.tazweeg.constant.Key;
import com.bulahej.tazweeg.dialogs.SignatureFragment;
import com.bulahej.tazweeg.utilties.AppUtility;
import com.bulahej.tazweeg.utilties.LocaleHelper;
import com.bulahej.tazweeg.utilties.Utilities;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity5 extends AppCompatActivity implements SignatureFragment.OnGetSignatureClickListener {

    //UI elements
    private EditText txtFirstRelativeName, txtFirstRelativePhone, txtSecondRelativeName , txtSecondRelativePhone, txtApplicantDescription;
    private Spinner spnFirstRelativeRelation, spnSecondRelativeRelation;
    List<DropDown> firstRelativeList, secondRelativeList;
    private int selectedFirstRelativeIndex, selectedSecondRelativeIndex;
    private TextView tvSignature;

    private Button btnNext,btnPrevious;
    private AlertDialog dialog;
    Context context;
    SharedPreferences preferences;
    private CheckBox cbArabic, cbEnglish, cbOther, cbTermsAndConditions;
    private FrameLayout flSignature;
    private ImageView imgSignature;
    private SignatureFragment signatureFragment;
    private User currentMember; //whose data is being filled in this step

    private String[] selLanguages = new String[3];
    private Bitmap signaturePic = null;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_5);
        context = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.step_five);
        toolbar.setTitleTextColor(0xFF000000);

        resources = this.getResources();

        initUIElements();
        getSpinnersData();

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

        setMemberValues();

//        txtFirstRelativeName.setText("Haseeb ali gorsi");
//        txtFirstRelativePhone.setText("971589108663");
//        txtSecondRelativeName.setText("Numair ali gorsi");
//        txtSecondRelativePhone.setText("971589108664");
//        txtApplicantDescription.setText("Quit playing games with my heart. With my heart. I should have known from the start");
    }

    //***********
    //populating all textfields
    //if consultant is editing then it will be called from getMemberDetail() to set the values of the member which is fetched from server
    //if Member is editing then it will be called from viewDidLoad to set the values of the member which is loaded from localStorage i.e. got at the time of login.
    //***********
    private void setMemberValues() {

        txtFirstRelativeName.setText(currentMember.getFirstRelative());
        txtFirstRelativePhone.setText(currentMember.getFirstRelativeNumber());

        txtSecondRelativeName.setText(currentMember.getSecondRelative());
        txtSecondRelativePhone.setText(currentMember.getSecondRelativeNumber());

        int index = Utilities.getIndexByID(firstRelativeList,currentMember.getFirstRelativeRelationId());
        if (index >= 0) {
            spnFirstRelativeRelation.setSelection(index);
        }
        index = Utilities.getIndexByID(secondRelativeList,currentMember.getSecondRelativeRelationId());
        if (index >= 0) {
            spnSecondRelativeRelation.setSelection(index);
        }
        txtApplicantDescription.setText(currentMember.getApplicantDescription());
        //Languages
        String[] languagesArray =  currentMember.getLanguagesId().split(",");
        if (languagesArray.length > 0 ){
            for(String language : languagesArray){
                if (language.length() > 0){ //incase of empty languagesArray, languagesArray length will be 1 but empty language
                    DropDown val = Constants.spinners.getLanguages().get(0);     //English
                    int langID = val.getValueId();
                    if ( Integer.valueOf(language) == langID) {
                        cbEnglish.setChecked(true);
                        continue;
                    }
                    val = Constants.spinners.getLanguages().get(1);     //Arabic
                    langID = val.getValueId();
                    if (Integer.valueOf(language) == langID) {
                        cbArabic.setChecked(true);
                        continue;
                    }
                    val = Constants.spinners.getLanguages().get(2);     //Others
                    langID = val.getValueId();
                    if (Integer.valueOf(language) == langID) {
                        cbOther.setChecked(true);
                        continue;
                    }
                }
            }
        }
        //signature
        if (currentMember.getSignature() != null){
            imgSignature.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imgSignature.setImageBitmap(Utilities.getBitmapFromEncoded64ImageString(currentMember.getSignature()));
            tvSignature.setVisibility(View.GONE);
        }

    }

    private void getSpinnersData() {
        if (Constants.spinners == null) { // very rare are the chances that this spinner will be null as we are already loading it on previous screen i.e. profile
            dialog.show();  //Please wait dialog
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
                    dialog.dismiss();
                    Utilities.myToastMessage(getApplicationContext(), getString(R.string.failure));
                }

            });
        }else{
            settingSpinnersData();
            settingSpinnersItemSelection();
        }
    }


    private void settingSpinnersItemSelection() {
        //FIRST RELATIVE
        spnFirstRelativeRelation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFirstRelativeIndex = position;
                Utilities.myLogError("selectedFirstRelativeIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //SECOND RELATIVE
        spnSecondRelativeRelation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSecondRelativeIndex = position;
                Utilities.myLogError("selectedSecondRelativeIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void settingSpinnersData() {
        if (Constants.spinners != null) {
            //FIRST RELATIVE
            firstRelativeList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getRelation()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                firstRelativeList.add(item);
            }
            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, firstRelativeList);
            spnFirstRelativeRelation.setAdapter(spinnerArrayAdapter);
            //SECOND RELATIVE
            secondRelativeList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getRelation()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                secondRelativeList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, secondRelativeList);
            spnSecondRelativeRelation.setAdapter(spinnerArrayAdapter);
            if (secondRelativeList.size() > 0)
                spnSecondRelativeRelation.setSelection(1);//making 2nd index as default selection
            //English Check box
            if (Constants.spinners.getLanguages().size() > 0){
                DropDown item = Constants.spinners.getLanguages().get(0);
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                cbEnglish.setText(item.toString());
                cbEnglish.setTag(item.getValueId());
            }
            //Arabic Check box
            if (Constants.spinners.getLanguages().size() > 1){
                DropDown item = Constants.spinners.getLanguages().get(1);
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                cbArabic.setText(item.toString());
                cbArabic.setTag(item.getValueId());
            }
            //Other Check box
            if (Constants.spinners.getLanguages().size() > 2){
                DropDown item = Constants.spinners.getLanguages().get(2);
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                cbOther.setText(item.toString());
                cbOther.setTag(item.getValueId());
            }
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
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
    
    private void initUIElements() {
        txtFirstRelativeName = findViewById(R.id.txtFirstRelativeName);
        txtFirstRelativePhone = findViewById(R.id.txtFirstRelativePhone);
        spnFirstRelativeRelation = findViewById(R.id.spnFirstRelativeRelation);

        txtSecondRelativeName = findViewById(R.id.txtSecondRelativeName);
        txtSecondRelativePhone = findViewById(R.id.txtSecondRelativePhone);
        spnSecondRelativeRelation = findViewById(R.id.spnSecondRelativeRelation);

        txtApplicantDescription = findViewById(R.id.txtApplicantDescription);

        cbArabic = findViewById(R.id.cbArabic);
        cbEnglish  = findViewById(R.id.cbEnglish);
        cbOther  = findViewById(R.id.cbOther);
        cbTermsAndConditions  = findViewById(R.id.cbTermsAndConditions);

        flSignature = findViewById(R.id.flSignature);
        signatureFragment = new SignatureFragment();
        tvSignature = findViewById(R.id.tvSignature);
        imgSignature = findViewById(R.id.imgSignature);

        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextTap(v);
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPrevious(v);
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

        dialog = AppUtility.createPleaseWaitDialog(this);

    }

    public void nextTap(View view) {
        final Resources resources = this.getResources();
        int userId = -1 , userUpdateId = -1;
        String firstRelativeName = txtFirstRelativeName.getText().toString();
        String firstRelativePhone  = txtFirstRelativePhone.getText().toString();

        String secondRelativeName = txtSecondRelativeName.getText().toString();
        String secondRelativePhone  = txtSecondRelativePhone.getText().toString();

        if (cbArabic.isChecked() == false && cbEnglish.isChecked() == false && cbOther.isChecked() == false){
            cbArabic.setError(resources.getString(R.string.language_required));
            Utilities.myToastMessage(this, resources.getString(R.string.language_required));
            return;
        }

        if(imgSignature.getDrawable() == null){
            Utilities.myToastMessage(this, resources.getString(R.string.sign_required));
            return;
        }

        if (!cbTermsAndConditions.isChecked()){
            cbTermsAndConditions.setError(resources.getString(R.string.error_accept_terms));
            Utilities.myToastMessage(this, resources.getString(R.string.error_accept_terms));
            return ;
        }

//        HashMap<String, String> dynamicParams = new HashMap<>();
        userId = currentMember.getId(); //if logged in person is a member then current member is logged in member else if logged in member is consultant then current member is the member whose data is being entered
        userUpdateId = Constants.loggedInMember.getId();

        List<Integer> selectedLanguages = new ArrayList<Integer>();
        if (cbArabic.isChecked()){
            selectedLanguages.add((Integer) cbArabic.getTag());
        }
        if (cbEnglish.isChecked()){
            selectedLanguages.add((Integer) cbEnglish.getTag());
        }
        if (cbOther.isChecked()){
            selectedLanguages.add((Integer) cbOther.getTag());
        }

        String commaSeperatedLanguages = TextUtils.join(",", selectedLanguages);

//        dynamicParams.put("languagesId",commaSeperatedLanguages);

        BitmapDrawable drawable = (BitmapDrawable) imgSignature.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        String strBase64 = Utilities.getEncoded64ImageStringFromBitmap(bitmap);
//        dynamicParams.put("signature",strBase64);
//        dynamicParams.put("userUpdateId",String.valueOf(userUpdateId));
//        Utilities.myLogError("Step 5 Params: %s", dynamicParams.toString());
        dialog.show();  //Please wait dialog
        TazweegApi.getInstance().stepFive( userId
                ,firstRelativeName
                ,firstRelativePhone
                ,firstRelativeList.get(selectedFirstRelativeIndex).getValueId()
                ,secondRelativeName
                ,secondRelativePhone
                ,secondRelativeList.get(selectedSecondRelativeIndex).getValueId()
                ,txtApplicantDescription.getText().toString()
                ,commaSeperatedLanguages
                ,strBase64
                ,userUpdateId
        ).enqueue(new Callback<UserResponse>() {
//        TazweegApi.getInstance().stepFive(dynamicParams
//        ).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    int status = response.body().getStatus();
                    if (status == 1) {
                        currentMember = response.body().getUser();
                        //if logged in user is a member then save a custom object into userdefaults you have to encode it
                        if(Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_MEMBER){
                            Constants.loggedInMember = currentMember;  //Storing into constants as it will be required in whole application
                            //Making logged in user record persistent
                            //https://stackoverflow.com/questions/5418160/store-and-retrieve-a-class-object-in-shared-preference
                            Gson gson = new Gson();
                            String json = gson.toJson(currentMember); // myObject - instance of MyObject
                            preferences.edit().putString(Constants.LOGGED_IN_USER, json).apply();
                        }
                        if (currentMember.getPaymentStatusId() != Constants.PROFILE_STATUS_PAID) {  //if profile status is not paid then take it to paid
                            Intent intent = new Intent(context, WebViewActivity.class);
                            intent.putExtra(Constants.CURRENT_MEMBER, currentMember);
                            intent.putExtra(Key.KEY_URL, Constants.PAGE_TYPE_PAYMENT);
                            startActivity(intent);
                        }
                        else{
                            Intent intent = new Intent(SignUpActivity5.this, MainActivity.class);
                            //If set, and the activity being launched is already running in the current task, then instead of launching a new instance of that activity, all of the other activities on top of it will be closed and this
                            // Intent will be delivered to the (now on top) old activity as a new Intent.
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
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

    public void onClickPrevious(View view) {
        onBackPressed();
    }

    public void onClickSignature(View view) {
        signatureFragment.show(getSupportFragmentManager(), "SigFragment");
        signatureFragment.setOnGetSignatureClickListener(this);
    }

    @Override
    public void onGetSignature(Bitmap bitmap) {
        signaturePic = bitmap;
        tvSignature.setVisibility(View.GONE);
        imgSignature.setImageBitmap(bitmap);
    }

}
