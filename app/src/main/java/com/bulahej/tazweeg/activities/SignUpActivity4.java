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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity4 extends AppCompatActivity {

    private Spinner spnBrideArrangement, spnNationality, spnSocialStatus, spnHasChildren, spnNumberOfChildren, spnReproduction, spnAge,spnHeight, spnBodyType, spnSkinColor, spnEducationLevel, spnIsWorking, spnJobType, spnVeil, spnHasDrivingLicense, spnWillPayToBride, spnWhereToLive;
    List<DropDown> brideArrangementList, nationalityList, socialStatusList, hasChildrenList, numberOfChildrenList, reproductionList, ageList, heightList, bodyTypeList, skinColorList, educationLevelList, isWorkingList, jobTypeList , veilList, hasDrivingLicenseList, willPayToBrideList, whereToLiveList;
    private int selectedBrideArrangementIndex, selectedNationalityIndex, selectedSocialStatusIndex, selectedHasChildrenIndex, selectedNumberOfChildrenIndex, selectedReproductionIndex, selectedAgeIndex, selectedHeightIndex, selectedBodyTypeIndex, selectedSkinColorIndex
            , selectedEducationLevelIndex, selectedIsWorkingIndex, selectedJobTypeIndex, selectedVeilIndex, selectedHasDrivingLicenseIndex, selectedWillPayToBrideIndex, selectedWhereToLiveIndex;
    private Button btnNext,btnPrevious;
    private AlertDialog dialog;
    Context context;
    SharedPreferences preferences;
    private LinearLayout llBrideArrangement,llCountry, llSocialStatus, llHasChildren, llNumberOfChildren, llReproduction,llJobDetails,llVeil,llWillPayToBride, llHasRefer;
    private User currentMember; //whose data is being filled in this step
    private TextView lblSpouseData;
    Resources resources;
    private CheckBox cbIsMarryingInGCC, cbAcceptDMW, cbHasRefer;
    private EditText txtReferName,txtReferNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_4);
        context = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.step_four);
        toolbar.setTitleTextColor(0xFF000000);
        resources = this.getResources();

        initUIElements();

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
        // For female its hidden on step 4
        // For male its visible on step 4
        // If logged in user is a member and she is female OR if logged in user is a consultant and consultant is editing a female
        preferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        if (    (Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_MEMBER && Constants.loggedInMember.getGenderId() == Constants.GENDER_FEMALE ) ||
                ((Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_CONSULTANT || Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_ADMIN )  &&
                        Constants.loggedInMember.getGenderId() == Constants.GENDER_FEMALE)){
            llBrideArrangement.setVisibility(View.GONE);
            llVeil.setVisibility(View.GONE);
            llWillPayToBride.setVisibility(View.GONE);
            lblSpouseData.setText(resources.getString(R.string.groom_data));
            cbAcceptDMW.setText(resources.getString(R.string.female_social_status_check));
        }else{
            lblSpouseData.setText(resources.getString(R.string.bride_data));
            cbAcceptDMW.setText(resources.getString(R.string.male_social_status_check));
        }
        getSpinnersData();
        setMemberValues();
    }

    //***********
    //populating all textfields
    //if consultant is editing then it will be called from getMemberDetail() to set the values of the member which is fetched from server
    //if Member is editing then it will be called from viewDidLoad to set the values of the member which is loaded from localStorage i.e. got at the time of login.
    //***********
    private void setMemberValues() {
        cbIsMarryingInGCC.setChecked(currentMember.getGCCMarriage());
        int index = Utilities.getIndexByID(brideArrangementList,currentMember.getSbrideArrangmentId());
        if (index >= 0) {
            spnBrideArrangement.setSelection(index);
        }
        cbAcceptDMW.setChecked(currentMember.getAcceptDMW());
        index = Utilities.getIndexByID(nationalityList,currentMember.getsNationalityId());
        if (index >= 0) {
            spnNationality.setSelection(index);
        }
        index = Utilities.getIndexByID(socialStatusList,currentMember.getsSocialStatusId());
        if (index >= 0) {
            spnSocialStatus.setSelection(index);
        }
        index = Utilities.getIndexByID(hasChildrenList,currentMember.getsHasChildId());
        if (index >= 0) {
            spnHasChildren.setSelection(index);
        }
        index = Utilities.getIndexByID(numberOfChildrenList,currentMember.getSnoOfChildrenId());
        if (index >= 0) {
            spnNumberOfChildren.setSelection(index);
        }
        index = Utilities.getIndexByID(reproductionList,currentMember.getsReproductionId());
        if (index >= 0) {
            spnReproduction.setSelection(index);
        }
        index = Utilities.getIndexByID(ageList,currentMember.getsAgeId());
        if (index >= 0) {
            spnAge.setSelection(index);
        }
        index = Utilities.getIndexByID(heightList,currentMember.getsHeightId());
        if (index >= 0) {
            spnHeight.setSelection(index);
        }
        index = Utilities.getIndexByID(bodyTypeList,currentMember.getsBodyTypeId());
        if (index >= 0) {
            spnBodyType.setSelection(index);
        }
        index = Utilities.getIndexByID(skinColorList,currentMember.getsSkinColorId());
        if (index >= 0) {
            spnSkinColor.setSelection(index);
        }
        index = Utilities.getIndexByID(educationLevelList,currentMember.getsEducationLevelId());
        if (index >= 0) {
            spnEducationLevel.setSelection(index);
        }
        index = Utilities.getIndexByID(isWorkingList,currentMember.getsCondemnBrideId());
        if (index >= 0) {
            spnIsWorking.setSelection(index);
        }
        index = Utilities.getIndexByID(jobTypeList,currentMember.getsDrivingLicenseId());
        if (index >= 0) {
            spnJobType.setSelection(index);
        }
        index = Utilities.getIndexByID(veilList,currentMember.getsCondemnBrideId());
        if (index >= 0) {
            spnVeil.setSelection(index);
        }
        index = Utilities.getIndexByID(hasDrivingLicenseList,currentMember.getsDrivingLicenseId());
        if (index >= 0) {
            spnHasDrivingLicense.setSelection(index);
        }
        index = Utilities.getIndexByID(willPayToBrideList,currentMember.getsWillPayToBrideId());
        if (index >= 0) {
            spnWillPayToBride.setSelection(index);
        }
        index = Utilities.getIndexByID(whereToLiveList,currentMember.getsRequiredStateId());
        if (index >= 0) {
            spnWhereToLive.setSelection(index);
        }
        if((currentMember.getReferName() != null &&  currentMember.getReferName().length() > 0 ) && (currentMember.getReferMobile() != null && currentMember.getReferMobile().length() > 0 )){
            cbHasRefer.setChecked(true);
            txtReferName.setText(currentMember.getReferName());
            txtReferNumber.setText(currentMember.getReferMobile());
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
        //NATIONALITY
        spnNationality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedNationalityIndex = position;
                Utilities.myLogError("selectedNationalityIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //SOCIAL STATUS
        spnSocialStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSocialStatusIndex = position;
                if(selectedSocialStatusIndex == 0 ){ //if single
                    llHasChildren.setVisibility(View.GONE);
                    llNumberOfChildren.setVisibility(View.GONE);
                    llReproduction.setVisibility(View.GONE);
                    //setting default selected index to zero for hidden spinner
                    spnHasChildren.setSelection(0); //it will fire spnHasChildren.setOnItemSelectedListener
                }else{
                    llHasChildren.setVisibility(View.VISIBLE);
                    llReproduction.setVisibility(View.VISIBLE);
                }
                Utilities.myLogError("selectedSocialStatusIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //HAS CHILDREN
        spnHasChildren.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedHasChildrenIndex = position;
                if(selectedHasChildrenIndex == 1 ){ //if has children
                    llNumberOfChildren.setVisibility(View.VISIBLE);
                    llReproduction.setVisibility(View.GONE);
                }else if(selectedSocialStatusIndex != 0 ){  //don't have children and social status is also not single
                    llNumberOfChildren.setVisibility(View.GONE);
                    llReproduction.setVisibility(View.VISIBLE);
                }
                Utilities.myLogError("selectedHasChildrenIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //NUMBER OF CHILDREN
        spnNumberOfChildren.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedNumberOfChildrenIndex = position;
                Utilities.myLogError("selectedNumberOfChildrenIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //REPRODUCTION
        spnReproduction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedReproductionIndex = position;
                Utilities.myLogError("selectedReproductionIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //AGE
        spnAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAgeIndex = position;
                Utilities.myLogError("selectedAgeIndex: %d", position);
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
        //EDUCATION LEVEL
        spnEducationLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedEducationLevelIndex = position;
                Utilities.myLogError("selectedEducationLevelIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //IS WORKING
        spnIsWorking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedIsWorkingIndex = position;
                if(selectedIsWorkingIndex != 0 ){ //if not working
                    llJobDetails.setVisibility(View.GONE);
                }else{  //dont have children
                    llJobDetails.setVisibility(View.VISIBLE);
                }
                Utilities.myLogError("selectedIsWorkingIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //JOB TYPE
        spnJobType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedJobTypeIndex = position;
                Utilities.myLogError("selectedJobTypeIndex: %d", position);
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
                Utilities.myLogError("selectedHasDrivingLicenseIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //WILL PAY TO BRIDE
        spnWillPayToBride.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedWillPayToBrideIndex = position;
                Utilities.myLogError("selectedWillPayToBrideIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //WHERE TO LIVE
        spnWhereToLive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedWhereToLiveIndex = position;
                Utilities.myLogError("selectedWhereToLiveIndex: %d", position);
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
            //NATIONALITY
            nationalityList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getCountries()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                nationalityList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nationalityList);
            spnNationality.setAdapter(spinnerArrayAdapter);
            //SOCIAL STATUS
            socialStatusList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getSocial_Status()) {
                if (item.getValueId() == Constants.SOCIAL_STATUS_MARRIED && currentMember.getGenderId() == Constants.GENDER_MALE) {   //male can not marry an already married woman
                    continue;
                }
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                socialStatusList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, socialStatusList);
            spnSocialStatus.setAdapter(spinnerArrayAdapter);
            //HAS CHILDREN
            hasChildrenList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getIs_Smoking()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                hasChildrenList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, hasChildrenList);
            spnHasChildren.setAdapter(spinnerArrayAdapter);
            //NUMBER OF CHILDREN
            numberOfChildrenList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getNoOfChildren()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                numberOfChildrenList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, numberOfChildrenList);
            spnNumberOfChildren.setAdapter(spinnerArrayAdapter);
            //REPRODUCTION
            reproductionList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getInfertility()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                reproductionList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, reproductionList);
            spnReproduction.setAdapter(spinnerArrayAdapter);
            //AGE
            ageList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getAge()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                ageList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ageList);
            spnAge.setAdapter(spinnerArrayAdapter);
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
            //SKIN COLOR
            skinColorList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getSkin_Color()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                skinColorList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, skinColorList);
            spnSkinColor.setAdapter(spinnerArrayAdapter);
            //EDUCATION LIST
            educationLevelList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getEducation_Level()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                educationLevelList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, educationLevelList);
            spnEducationLevel.setAdapter(spinnerArrayAdapter);
            //IS WORKING
            isWorkingList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getIs_Working()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                isWorkingList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, isWorkingList);
            spnIsWorking.setAdapter(spinnerArrayAdapter);
            //JOB TYPE
            jobTypeList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getJob_Type()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                jobTypeList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, jobTypeList);
            spnJobType.setAdapter(spinnerArrayAdapter);

            //VEIL
            veilList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getCondemningBride()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                veilList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, veilList);
            spnVeil.setAdapter(spinnerArrayAdapter);
            //HAS DRIVING LICENSE
            educationLevelList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getEducation_Level()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                educationLevelList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, educationLevelList);
            spnEducationLevel.setAdapter(spinnerArrayAdapter);
            //HAS DRIVING LICENSE
            hasDrivingLicenseList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getHasLicense()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                hasDrivingLicenseList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, hasDrivingLicenseList);
            spnHasDrivingLicense.setAdapter(spinnerArrayAdapter);
            //WILL PAY TO BRIDE
            willPayToBrideList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getWillPayToBride()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                willPayToBrideList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, willPayToBrideList);
            spnWillPayToBride.setAdapter(spinnerArrayAdapter);
            //WHERE TO LIVE
            whereToLiveList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getStates()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                whereToLiveList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, whereToLiveList);
            spnWhereToLive.setAdapter(spinnerArrayAdapter);
        }
    }

    private void initUIElements() {

        lblSpouseData = findViewById(R.id.lblSpouseData);

        llBrideArrangement = findViewById(R.id.llBrideArrangement);
        spnBrideArrangement = findViewById(R.id.spnBrideArrangement);

        cbIsMarryingInGCC = findViewById(R.id.cbGCC);
        llCountry = findViewById(R.id.llCountry);
        spnNationality = findViewById(R.id.spnNationality);

        llSocialStatus = findViewById(R.id.llSocialStatus);
        spnSocialStatus = findViewById(R.id.spnSocialStatus);
        cbAcceptDMW = findViewById(R.id.cbAcceptDMW);

        llHasChildren = findViewById(R.id.llHasChildren);
        spnHasChildren = findViewById(R.id.spnHasChildren);

        llNumberOfChildren = findViewById(R.id.llNumberOfChildren);
        spnNumberOfChildren = findViewById(R.id.spnNumberOfChildren);

        llReproduction = findViewById(R.id.llReproduction);
        spnReproduction = findViewById(R.id.spnReproduction);

        spnAge = findViewById(R.id.spnAge);
        spnHeight = findViewById(R.id.spnHeight);
        spnBodyType = findViewById(R.id.spnBodyType);
        spnSkinColor = findViewById(R.id.spnSkinColor);
        spnEducationLevel = findViewById(R.id.spnEducationLevel);

        spnIsWorking = findViewById(R.id.spnIsWorking);
        llJobDetails = findViewById(R.id.llJobDetails);
        spnJobType = findViewById(R.id.spnJobType);

        llVeil = findViewById(R.id.llVeil);
        spnVeil = findViewById(R.id.spnVeil);

        spnHasDrivingLicense = findViewById(R.id.spnHasDrivingLicense);

        llWillPayToBride = findViewById(R.id.llWillPayToBride);
        spnWillPayToBride = findViewById(R.id.spnWillPayToBride);

        spnWhereToLive = findViewById(R.id.spnWhereToLive);

        cbHasRefer = findViewById(R.id.cbHasRefer);
        llHasRefer = findViewById(R.id.llHasRefer);
        txtReferName = findViewById(R.id.txtReferName);
        txtReferNumber = findViewById(R.id.txtReferNumber);

        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);

        cbIsMarryingInGCC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if ( isChecked == true ){
                    llCountry.setVisibility(View.GONE);
                }else{
                    llCountry.setVisibility(View.VISIBLE);
                }
            }
        });

        cbAcceptDMW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if ( isChecked == true ){
                    llHasRefer.setVisibility(View.GONE);
                }else{
                    llHasRefer.setVisibility(View.VISIBLE);
                }
            }
        });

        cbHasRefer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if ( isChecked == true ){
                    llHasRefer.setVisibility(View.VISIBLE );
                }else{
                    llHasRefer.setVisibility(View.GONE);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputValid()) {
                    nextTap(v);
                }
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

    private boolean isInputValid() {
        Resources resources = this.getResources();
        if (cbHasRefer.isChecked()) {
            if (txtReferName.getText().toString().isEmpty()) {
                txtReferName.requestFocus();
                txtReferName.setError(resources.getString(R.string.error_name_required));
                return false;
            }

            if (!Utilities.isValidPhoneNumber(txtReferNumber.getText().toString() )) {
                txtReferNumber.requestFocus();
                txtReferNumber.setError(resources.getString(R.string.error_invalid_phone_number));
                return false;
            }
        }

        return true;
    }

    public void nextTap(View view) {

        int userId = -1 ,socialStatusId = -1, hasChildrenId = -1, isWorkingId = -1, userUpdateId = -1;

        HashMap<String, String> dynamicParams = new HashMap<>();
        userId = currentMember.getId(); //if logged in person is a member then current member is logged in member else if logged in member is consultant then current member is the member whose data is being entered
        socialStatusId = socialStatusList.get(selectedSocialStatusIndex).getValueId();
        hasChildrenId = hasChildrenList.get(selectedHasChildrenIndex).getValueId();
        isWorkingId = isWorkingList.get(selectedIsWorkingIndex).getValueId();
        userUpdateId = Constants.loggedInMember.getId();
        dynamicParams.put("userId",String.valueOf(userId));

        // For female its hidden on step 4
        // For male its visible on step 4
        // If logged in user is a member and he is male OR if logged in user is a consultant and consultant is editing a male
        if (    (Constants.loggedInMember.getGenderId() == Constants.GENDER_MALE && Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_MEMBER) ||
                (currentMember.getGenderId() == Constants.GENDER_MALE && ( Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_CONSULTANT ||
                        Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_ADMIN))){
            dynamicParams.put("sBrideArrangmentId", String.valueOf(brideArrangementList.get(selectedBrideArrangementIndex).getValueId()));
            dynamicParams.put("sCondemnBrideId" ,String.valueOf(veilList.get(selectedVeilIndex).getValueId()));
            dynamicParams.put("sWillPayToBrideId" ,String.valueOf(willPayToBrideList.get(selectedWillPayToBrideIndex).getValueId()));
        }

        dynamicParams.put("acceptDMW", cbAcceptDMW.isChecked() == true ? "true" : "false");
        dynamicParams.put("sSocialStatusId",String.valueOf(socialStatusId));

        if (socialStatusId != Constants.SOCIAL_STATUS_SINGLE) { //56 is Single, 57 is Married, 58 is Divorced and 59 is Widowed
            dynamicParams.put("sHasChildId",String.valueOf(hasChildrenList.get(selectedHasChildrenIndex).getValueId()));
            if (hasChildrenId == Constants.IS_SMOKING_NO) //12 is NO, 13 is yes (using is smoking as we dont have has children)
            {
                dynamicParams.put("sReproductionId",String.valueOf(reproductionList.get(selectedReproductionIndex).getValueId()));
            }else{
                dynamicParams.put("sNoOfChildrenId",String.valueOf(numberOfChildrenList.get(selectedNumberOfChildrenIndex).getValueId()));
            }
        }

        dynamicParams.put("GCCMarriage", cbIsMarryingInGCC.isChecked() == true ? "true" : "false");
        dynamicParams.put("sNationalityId" , String.valueOf(nationalityList.get(selectedNationalityIndex).getValueId()));

        dynamicParams.put("sAgeId", String.valueOf(ageList.get(selectedAgeIndex).getValueId()));
        dynamicParams.put("sHeightId" ,String.valueOf(heightList.get(selectedHeightIndex).getValueId()));
        dynamicParams.put("sBodyTypeId" , String.valueOf(bodyTypeList.get(selectedBodyTypeIndex).getValueId()));
        dynamicParams.put("sSkinColorId" , String.valueOf(skinColorList.get(selectedSkinColorIndex).getValueId()));
        dynamicParams.put("sEducationLevelId" , String.valueOf(educationLevelList.get(selectedEducationLevelIndex).getValueId()));

        dynamicParams.put("sIsWorkingId",String.valueOf(isWorkingId));
        if (isWorkingId == Constants.IS_WORKING_WORKING) //60 is working, 61 is not working, 62 is doesnt matter
        {
            dynamicParams.put("sJobTypeId" , String.valueOf(jobTypeList.get(selectedJobTypeIndex).getValueId()));
        }
        dynamicParams.put("sDrivingLicenseId" , String.valueOf(hasDrivingLicenseList.get(selectedHasDrivingLicenseIndex).getValueId()));
        dynamicParams.put("sRequiredStateId", String.valueOf(whereToLiveList.get(selectedWhereToLiveIndex).getValueId()));
        dynamicParams.put("userUpdateId",String.valueOf(userUpdateId));

        if (cbHasRefer.isChecked()){
            dynamicParams.put("nameRefer",txtReferName.getText().toString());
            dynamicParams.put("mobileRefer", txtReferNumber.getText().toString());
        }

//        Utilities.myLogError("Step 4 Params: %s", dynamicParams.toString());
        dialog.show();  //Please wait dialog
        TazweegApi.getInstance().stepFour(dynamicParams
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
                        Intent intent = new Intent(context, SignUpActivity5.class);
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
                Utilities.myToastMessage(getApplicationContext(), resources.getString(R.string.failure));
            }
        });

    }

    public void onClickPrevious(View view) {
        onBackPressed();
    }

}
