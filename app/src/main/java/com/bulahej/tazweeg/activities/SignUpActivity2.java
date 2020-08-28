package com.bulahej.tazweeg.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.apis_responses.TazweegApi;
import com.bulahej.tazweeg.apis_responses.UserResponse.User;
import com.bulahej.tazweeg.apis_responses.UserResponse.UserResponse;
import com.bulahej.tazweeg.apis_responses.registration_form_new.DropDown;
import com.bulahej.tazweeg.apis_responses.registration_form_new.Type;
import com.bulahej.tazweeg.constant.Constants;
import com.bulahej.tazweeg.utilties.AppUtility;
import com.bulahej.tazweeg.utilties.LocaleHelper;
import com.bulahej.tazweeg.utilties.Utilities;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity2 extends AppCompatActivity {

    private Spinner spnBrideArrangement, spnIsSmoking, spnSkinColor, spnHairColor, spnHairType, spnEyeColor, spnHeight, spnBodyType, spnBodyWeight, spnSectType, spnVeil, spnHasDrivingLicense;
    List<DropDown> brideArrangementList, isSmokingList, skinColorList, hairColorList, hairTypeList, eyeColorList, heightList, bodyTypeList, bodyWeightList, sectTypeList, veilList, hasDrivingLicenseList;
    private int selectedBrideArrangementIndex, selectedIsSmokingIndex, selectedSkinColorIndex, selectedHairColorIndex, selectedHairTypeIndex, selectedEyeColorIndex, selectedHeightIndex, selectedBodyTypeIndex, selectedBodyWeightIndex
            , selectedSectTypeIndex, selectedVeilIndex, selectedHasDrivingLicenseIndex;
    private Button btnNext,btnPrevious;
    private AlertDialog dialog;
    Context context;
    SharedPreferences preferences;
    private LinearLayout llBrideArrangement, llVeil;
    private CheckBox cbIsPolygammy;
    private User currentMember; //whose data is being filled in this step

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_2);
        context = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.step_two);
        toolbar.setTitleTextColor(0xFF000000);

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
        // For Female its visible on step 2
        // For male its hidden on step 2
        // If logged in user is a member and he is male OR if logged in user is a consultant and consultant is editing a male
        if (    (Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_MEMBER && Constants.loggedInMember.getGenderId() == Constants.GENDER_MALE ) ||
                ((Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_CONSULTANT || Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_ADMIN )  &&
                        Constants.loggedInMember.getGenderId() == Constants.GENDER_MALE)){
            llBrideArrangement.setVisibility(View.GONE);
            cbIsPolygammy.setVisibility(View.GONE);
            llVeil.setVisibility(View.GONE);
        }
        setMemberValues();

    }

    //***********
    //populating all textfields
    //if consultant is editing then it will be called from getMemberDetail() to set the values of the member which is fetched from server
    //if Member is editing then it will be called from viewDidLoad to set the values of the member which is loaded from localStorage i.e. got at the time of login.
    //***********
    private void setMemberValues() {
        int index = Utilities.getIndexByID(brideArrangementList,currentMember.getSbrideArrangmentId());
        if (index >= 0) {
            spnBrideArrangement.setSelection(index);
        }
        cbIsPolygammy.setChecked(currentMember.getIsPolygamy());
        index = Utilities.getIndexByID(isSmokingList,currentMember.getIsSmokingId());
        if (index >= 0) {
            spnIsSmoking.setSelection(index);
        }
        index = Utilities.getIndexByID(skinColorList,currentMember.getSkinColorId());
        if (index >= 0) {
            spnSkinColor.setSelection(index);
        }
        index = Utilities.getIndexByID(hairColorList,currentMember.getHairColorId());
        if (index >= 0) {
            spnHairColor.setSelection(index);
        }
        index = Utilities.getIndexByID(hairTypeList,currentMember.getHairTypeId());
        if (index >= 0) {
            spnHairType.setSelection(index);
        }
        index = Utilities.getIndexByID(eyeColorList,currentMember.getEyeColorId());
        if (index >= 0) {
            spnEyeColor.setSelection(index);
        }
        index = Utilities.getIndexByID(heightList,currentMember.getHeightId());
        if (index >= 0) {
            spnHeight.setSelection(index);
        }
        index = Utilities.getIndexByID(bodyTypeList,currentMember.getBodyTypeId());
        if (index >= 0) {
            spnBodyType.setSelection(index);
        }
        index = Utilities.getIndexByID(bodyWeightList,currentMember.getMemberWeightId());
        if (index >= 0) {
            spnBodyWeight.setSelection(index);
        }
        index = Utilities.getIndexByID(sectTypeList,currentMember.getSectTypeId());
        if (index >= 0) {
            spnSectType.setSelection(index);
        }
        index = Utilities.getIndexByID(veilList,currentMember.getsCondemnBrideId());
        if (index >= 0) {
            spnVeil.setSelection(index);
        }
        index = Utilities.getIndexByID(hasDrivingLicenseList,currentMember.getsDrivingLicenseId());
        if (index >= 0) {
            spnHasDrivingLicense.setSelection(index);
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

    private void settingSpinnersItemSelection() {
        //BRIDE ARRANGEMENT
        spnBrideArrangement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBrideArrangementIndex = position;
                Utilities.myLogError("selectedBrideArrangementIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //IS SMOKING
        spnIsSmoking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedIsSmokingIndex = position;
                Utilities.myLogError("selectedIsSmokingIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //SKIN COLOR
        spnSkinColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSkinColorIndex = position;
                Utilities.myLogError("selectedSkinColorIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //HAIR COLOR
        spnHairColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedHairColorIndex = position;
                Utilities.myLogError("selectedHairColorIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //HAIR TYPE
        spnHairType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedHairTypeIndex = position;
                Utilities.myLogError("selectedHairTypeIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //EYE COLOR
        spnEyeColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedEyeColorIndex = position;
                Utilities.myLogError("selectedEyeColorIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //HEIGHT
        spnHeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedHeightIndex = position;
                Utilities.myLogError("selectedHeightIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //BODY TYPE
        spnBodyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBodyTypeIndex = position;
                Utilities.myLogError("selectedBodyTypeIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //BODY WEIGHT
        spnBodyWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBodyWeightIndex = position;
                Utilities.myLogError("selectedBodyWeightIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //SECT TYPE
        spnSectType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSectTypeIndex = position;
                Utilities.myLogError("selectedSectTypeIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //VEIL
        spnVeil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedVeilIndex = position;
                Utilities.myLogError("selectedVeilIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //HAS DRIVING LICENSE
        spnHasDrivingLicense.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedHasDrivingLicenseIndex = position;
                Utilities.myLogError("selectedEmirateIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void settingSpinnersData() {
        if (Constants.spinners != null) {
            //BRIDE ARRANGEMENT
            brideArrangementList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getBride_Arrangement()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                brideArrangementList.add(item);
            }
            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, brideArrangementList);
            spnBrideArrangement.setAdapter(spinnerArrayAdapter);
            //IS SMOKING
            isSmokingList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getIs_Smoking()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                isSmokingList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, isSmokingList);
            spnIsSmoking.setAdapter(spinnerArrayAdapter);
            //SKIN COLOR
            skinColorList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getSkin_Color()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                skinColorList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, skinColorList);
            spnSkinColor.setAdapter(spinnerArrayAdapter);
            //HAIR COLOR
            hairColorList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getHair_Color()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                hairColorList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, hairColorList);
            spnHairColor.setAdapter(spinnerArrayAdapter);
            //HAIR TYPE
            hairTypeList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getHair_Type()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                hairTypeList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, hairTypeList);
            spnHairType.setAdapter(spinnerArrayAdapter);
            //EYE COLOR
            eyeColorList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getEye_Color()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                eyeColorList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, eyeColorList);
            spnEyeColor.setAdapter(spinnerArrayAdapter);
            //HEIGHT
            heightList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getHeight()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                heightList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, heightList);
            spnHeight.setAdapter(spinnerArrayAdapter);
            //BODY TYPE
            bodyTypeList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getBody_Type()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                bodyTypeList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, bodyTypeList);
            spnBodyType.setAdapter(spinnerArrayAdapter);
            //BODY WEIGHT
            bodyWeightList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getWeight()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                bodyWeightList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, bodyWeightList);
            spnBodyWeight.setAdapter(spinnerArrayAdapter);
            //SECT TYPE
            sectTypeList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getSect_Type()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                sectTypeList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sectTypeList);
            spnSectType.setAdapter(spinnerArrayAdapter);
            //VEIL
            veilList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getCondemningBride()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                veilList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, veilList);
            spnVeil.setAdapter(spinnerArrayAdapter);
            //HAS DRIVING LICENSE
            hasDrivingLicenseList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getHasLicense()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                hasDrivingLicenseList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, hasDrivingLicenseList);
            spnHasDrivingLicense.setAdapter(spinnerArrayAdapter);
        }

    }

    private void initUIElements() {
        llBrideArrangement = findViewById(R.id.llBrideArrangement);
        spnBrideArrangement = findViewById(R.id.spnBrideArrangement);
        cbIsPolygammy = findViewById(R.id.cbIsPolygammy);
        spnIsSmoking = findViewById(R.id.spnIsSmoking);
        spnSkinColor = findViewById(R.id.spnSkinColor);
        spnHairColor = findViewById(R.id.spnHairColor);
        spnHairType = findViewById(R.id.spnHairType);
        spnEyeColor = findViewById(R.id.spnEyeColor);
        spnHeight = findViewById(R.id.spnHeight);
        spnBodyType = findViewById(R.id.spnBodyType);
        spnBodyWeight = findViewById(R.id.spnBodyWeight);
        spnSectType = findViewById(R.id.spnSectType);
        spnSectType = findViewById(R.id.spnSectType);
        spnVeil = findViewById(R.id.spnVeil);
        llVeil = findViewById(R.id.llVeil);
        spnHasDrivingLicense = findViewById(R.id.spnHasDrivingLicense);

        btnNext = findViewById(R.id.bNext);
        btnPrevious = findViewById(R.id.bPrevious);

        cbIsPolygammy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if ( isChecked == true ){
                    llBrideArrangement.setVisibility(View.GONE);
                }else{
                    llBrideArrangement.setVisibility(View.VISIBLE);
                }
            }
        });

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
        dialog = AppUtility.createPleaseWaitDialog(this);
    }

    public void nextTap(View view) {
        final Resources resources = this.getResources();
        int userId = -1 ,ethnicityId = -1, userUpdateId = -1;
        userId = currentMember.getId(); //if logged in person is a member then current member is logged in member else if logged in member is consultant then current member is the member whose data is being entered
        userUpdateId = Constants.loggedInMember.getId();
//        Utilities.myLogError("selectedBrideArrangementIndex: %d", firstRelativeList.get(selectedBrideArrangementIndex).getValueId());
//        Utilities.myLogError("selectedBodyWeightIndex: %d", bodyWeightList.get(selectedBodyWeightIndex).getValueId());
//        Utilities.myLogError("selectedHasDrivingLicenseIndex: %d", hasDrivingLicenseList.get(selectedHasDrivingLicenseIndex).getValueId());
//        Utilities.myLogError("cbIsPolygammy: %b", cbIsPolygammy.isChecked());
        dialog.show();  //Please wait dialog
        TazweegApi.getInstance().stepTwo(userId
                , isSmokingList.get(selectedIsSmokingIndex).getValueId()
                , skinColorList.get(selectedSkinColorIndex).getValueId()
                , hairColorList.get(selectedHairColorIndex).getValueId()
                , hairTypeList.get(selectedHairTypeIndex).getValueId()
                , eyeColorList.get(selectedEyeColorIndex).getValueId()
                , heightList.get(selectedHeightIndex).getValueId()
                , bodyTypeList.get(selectedBodyTypeIndex).getValueId()
                , bodyWeightList.get(selectedBodyWeightIndex).getValueId()
                , sectTypeList.get(selectedSectTypeIndex).getValueId()
                , hasDrivingLicenseList.get(selectedHasDrivingLicenseIndex).getValueId()
                , brideArrangementList.get(selectedBrideArrangementIndex).getValueId()      //just sending but will not be updated for males as this is step 2
                , cbIsPolygammy.isChecked() == true ? "true" : "false"                      //just sending but will not be updated for males as this is step 2
                , veilList.get(selectedVeilIndex).getValueId()
                ,userUpdateId
        ).enqueue(new Callback<UserResponse>() {
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
                        Intent intent = new Intent(context, SignUpActivity3.class);
                        intent.putExtra(Constants.CURRENT_MEMBER, currentMember);
                        startActivity(intent);
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

}
