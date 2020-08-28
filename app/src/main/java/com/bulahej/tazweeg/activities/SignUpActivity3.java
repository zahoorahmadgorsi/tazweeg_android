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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.apis_responses.TazweegApi;
import com.bulahej.tazweeg.apis_responses.UserResponse.User;
import com.bulahej.tazweeg.apis_responses.registration_form_new.DropDown;
import com.bulahej.tazweeg.apis_responses.registration_form_new.Type;
import com.bulahej.tazweeg.apis_responses.UserResponse.UserResponse;
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

public class SignUpActivity3 extends AppCompatActivity {

    //UI elements
    private EditText txtOccupation, txtJobTitle, txtDiseaseName;
    private Spinner spnEducationLevel, spnCommitmentToReligion, spnFinancialStatus, spnSocialStatus, spnHasChildren, spnNumberOfChildren, spnReproduction, spnIsWorking, spnJobType, spnAnnualIncome, spnAnyDisease;
    List<DropDown> educationLevelList, commitmentToReligionList, financialStatusList, socialStatusList, hasChildrenList, numberOfChildrenList, reproductionList, isWorkingList, jobTypeList, annualIncomeList, anyDiseaseList;
    private int selectedEducationLevelIndex, selectedCommitmentToReligionIndex, selectedFinancialStatusIndex, selectedSocialStatusIndex, selectedHasChildrenIndex,selectedReproductionIndex, selectedNumberOfChildrenIndex, selectedIsWorkingIndex, selectedJobTypeIndex, selectedAnnualIncomeIndex
            , selectedAnyDiseaseIndex;
    private Button btnNext,btnPrevious;
    private AlertDialog dialog;
    Context context;
    SharedPreferences preferences;
    private LinearLayout llHasChildren, llNumberOfChildren, llReproduction,llJobDetails, llDiseaseDescription;
    private User currentMember; //whose data is being filled in this step
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_3);
        context = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.step_three);
        toolbar.setTitleTextColor(0xFF000000);

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
        getSpinnersData();
        setMemberValues();

//        txtDiseaseName.setText("Allergy");
//        txtOccupation.setText("Mobile App Developer");
//        txtJobTitle.setText("Mobile App Developer asdf");
    }

    //***********
    //populating all textfields
    //if consultant is editing then it will be called from getMemberDetail() to set the values of the member which is fetched from server
    //if Member is editing then it will be called from viewDidLoad to set the values of the member which is loaded from localStorage i.e. got at the time of login.
    //***********
    private void setMemberValues() {

        txtDiseaseName.setText(currentMember.getDiseaseName());
        txtOccupation.setText(currentMember.getOccupation());
        txtJobTitle.setText(currentMember.getJobTitle());

        int index = Utilities.getIndexByID(educationLevelList,currentMember.getEducationLevelId());
        if (index >= 0) {
            spnEducationLevel.setSelection(index);
        }
        index = Utilities.getIndexByID(commitmentToReligionList,currentMember.getReligionCommitmentId());
        if (index >= 0) {
            spnCommitmentToReligion.setSelection(index);
        }
        index = Utilities.getIndexByID(financialStatusList,currentMember.getFinancialStatusId());
        if (index >= 0) {
            spnFinancialStatus.setSelection(index);
        }
        index = Utilities.getIndexByID(socialStatusList,currentMember.getSocialStatusId());
        if (index >= 0) {
            spnSocialStatus.setSelection(index);
        }
        index = Utilities.getIndexByID(hasChildrenList,currentMember.getMemberHasChildId());
        if (index >= 0) {
            spnHasChildren.setSelection(index);
        }
        index = Utilities.getIndexByID(numberOfChildrenList,currentMember.getMemberNoOfChildrenId());
        if (index >= 0) {
            spnNumberOfChildren.setSelection(index);
        }
        index = Utilities.getIndexByID(reproductionList,currentMember.getMemberReproductionId());
        if (index >= 0) {
            spnReproduction.setSelection(index);
        }
        index = Utilities.getIndexByID(isWorkingList,currentMember.getIsWorkingId());
        if (index >= 0) {
            spnIsWorking.setSelection(index);
        }
        index = Utilities.getIndexByID(jobTypeList,currentMember.getJobTypeId());
        if (index >= 0) {
            spnJobType.setSelection(index);
        }
        index = Utilities.getIndexByID(annualIncomeList,currentMember.getMemberWeightId());
        if (index >= 0) {
            spnAnnualIncome.setSelection(index);
        }
        index = Utilities.getIndexByID(anyDiseaseList,currentMember.getIsDiseaseId());
        if (index >= 0) {
            spnAnyDisease.setSelection(index);
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
        //RELIGION COMMITMENT
        spnCommitmentToReligion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCommitmentToReligionIndex = position;
                Utilities.myLogError("selectedCommitmentToReligionIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //FINANCIAL STATUS
        spnFinancialStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFinancialStatusIndex = position;
                Utilities.myLogError("selectedFinancialStatusIndex: %d", position);
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
        //ANNUAL INCOME
        spnAnnualIncome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAnnualIncomeIndex = position;
                Utilities.myLogError("selectedAnnualIncomeIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //ANY DISEASE
        spnAnyDisease.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAnyDiseaseIndex = position;
                if(selectedAnyDiseaseIndex == 0 ){ //if not disease
                    llDiseaseDescription.setVisibility(View.GONE);
                }else{  //dont have children
                    llDiseaseDescription.setVisibility(View.VISIBLE);
                }
                Utilities.myLogError("selectedAnyDiseaseIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void settingSpinnersData() {
        if (Constants.spinners != null) {
            //EDUCATION LEVEL
            educationLevelList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getEducation_Level()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                educationLevelList.add(item);
            }
            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, educationLevelList);
            spnEducationLevel.setAdapter(spinnerArrayAdapter);
            //COMMITMENT TO RELIGION
            commitmentToReligionList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getReligion_Commitment()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                commitmentToReligionList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, commitmentToReligionList);
            spnCommitmentToReligion.setAdapter(spinnerArrayAdapter);
            //FINANCIAL STATUS
            financialStatusList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getFinancial_Condition()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                financialStatusList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, financialStatusList);
            spnFinancialStatus.setAdapter(spinnerArrayAdapter);
            //SOCIAL STATUS
            socialStatusList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getSocial_Status()) {
                if (item.getValueId() == Constants.SOCIAL_STATUS_MARRIED && currentMember.getGenderId() == Constants.GENDER_FEMALE) {   //Married female can not be part of tazweeg
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
            //NUMBER OF CHILDRED
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
            //ANNUAL INCOME
            annualIncomeList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getAnnual_Income()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                annualIncomeList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, annualIncomeList);
            spnAnnualIncome.setAdapter(spinnerArrayAdapter);
            //ANY DISEASE
            anyDiseaseList = new ArrayList<DropDown>();
            for (DropDown item : Constants.spinners.getIs_Smoking()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                anyDiseaseList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, anyDiseaseList);
            spnAnyDisease.setAdapter(spinnerArrayAdapter);
        }
    }

    private void initUIElements() {
        spnEducationLevel = findViewById(R.id.spnEducationLevel);
        spnCommitmentToReligion = findViewById(R.id.spnCommitmentToReligion);
        spnFinancialStatus = findViewById(R.id.spnFinancialStatus);
        spnSocialStatus = findViewById(R.id.spnSocialStatus);

        llHasChildren = findViewById(R.id.llHasChildren);
        spnHasChildren = findViewById(R.id.spnHasChildren);

        llNumberOfChildren = findViewById(R.id.llNumberOfChildren);
        spnNumberOfChildren = findViewById(R.id.spnNumberOfChildren);

        llReproduction = findViewById(R.id.llReproduction);
        spnReproduction = findViewById(R.id.spnReproduction);

        llJobDetails = findViewById(R.id.llJobDetails);
        spnIsWorking = findViewById(R.id.spnIsWorking);
        spnJobType = findViewById(R.id.spnJobType);
        txtOccupation = findViewById(R.id.txtOccupation);
        txtJobTitle = findViewById(R.id.txtJobTitle);
        spnAnnualIncome = findViewById(R.id.spnAnnualIncome);

        spnAnyDisease = findViewById(R.id.spnAnyDisease);
        txtDiseaseName = findViewById(R.id.txtDiseaseName);
        llDiseaseDescription = findViewById(R.id.llDiseaseDescription);

        btnNext = findViewById(R.id.bNext);
        btnPrevious = findViewById(R.id.bPrevious);

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
        int userId = -1 ,socialStatusId = -1, hasChildrenId = -1, isWorkingId = -1, anyDiseaseId = -1, userUpdateId = -1;
        String occupation = txtOccupation.getText().toString();
        String jobTitle = txtJobTitle.getText().toString();
        String diseaseName = txtDiseaseName.getText().toString();

        if(selectedIsWorkingIndex == 0 && occupation.isEmpty()) {
            txtOccupation.requestFocus();
            txtOccupation.setError(resources.getString(R.string.invalid_occupation));
            return;
        }else if (selectedIsWorkingIndex == 0 && jobTitle.isEmpty()) {
            txtJobTitle.requestFocus();
            txtJobTitle.setError(resources.getString(R.string.invalid_job_title));
            return;
        }else if (selectedAnyDiseaseIndex != 0 && diseaseName.isEmpty()) {
            txtDiseaseName.requestFocus();
            txtDiseaseName.setError(resources.getString(R.string.invalid_disease_name));
            return;
        }
        HashMap<String, String> dynamicParams = new HashMap<>();
        userId = currentMember.getId(); //if logged in person is a member then current member is logged in member else if logged in member is consultant then current member is the member whose data is being entered
        socialStatusId = socialStatusList.get(selectedSocialStatusIndex).getValueId();
        hasChildrenId = hasChildrenList.get(selectedHasChildrenIndex).getValueId();
        isWorkingId = isWorkingList.get(selectedIsWorkingIndex).getValueId();
        anyDiseaseId = anyDiseaseList.get(selectedAnyDiseaseIndex).getValueId();
        userUpdateId = Constants.loggedInMember.getId();
        dynamicParams.put("userId",String.valueOf(userId));
        dynamicParams.put("educationLevelId",String.valueOf(educationLevelList.get(selectedEducationLevelIndex).getValueId()));
        dynamicParams.put("religionCommitmentId",String.valueOf(commitmentToReligionList.get(selectedCommitmentToReligionIndex).getValueId()));
        dynamicParams.put("financialStatusId",String.valueOf(financialStatusList.get(selectedFinancialStatusIndex).getValueId()));
        dynamicParams.put("socialStatusId",String.valueOf(socialStatusId));
        if (socialStatusId != Constants.SOCIAL_STATUS_SINGLE) { //56 is Single, 57 is Married, 58 is Divorced and 59 is Widowed
            dynamicParams.put("memberHasChildId",String.valueOf(hasChildrenList.get(selectedHasChildrenIndex).getValueId()));
            if (hasChildrenId == Constants.IS_SMOKING_NO) //12 is NO, 13 is yes (using is smoking as we dont have has children)
            {
                dynamicParams.put("memberReproductionId",String.valueOf(reproductionList.get(selectedReproductionIndex).getValueId()));
            }else{
                dynamicParams.put("memberNoOfChildrenId",String.valueOf(numberOfChildrenList.get(selectedNumberOfChildrenIndex).getValueId()));
            }
        }
        dynamicParams.put("isWorkingId",String.valueOf(isWorkingId));
        if (isWorkingId == Constants.IS_WORKING_WORKING) //60 is working, 61 is not working, 62 is doesnt matter
        {
            dynamicParams.put("memberWorking" , txtOccupation.getText().toString());
            dynamicParams.put("jobTypeId" , String.valueOf(jobTypeList.get(selectedJobTypeIndex).getValueId()));
            dynamicParams.put("jobTitle" , txtJobTitle.getText().toString());
        }
        dynamicParams.put("annualIncomeId" , String.valueOf(annualIncomeList.get(selectedAnnualIncomeIndex).getValueId()));
        dynamicParams.put("isDiseaseId",String.valueOf(anyDiseaseId));
        if (anyDiseaseId == Constants.IS_SMOKING_YES) //12 is no, 13 is yes
        {
            dynamicParams.put("diseaseName", txtDiseaseName.getText().toString());
        }
        dynamicParams.put("userUpdateId",String.valueOf(userUpdateId));
        Utilities.myLogError("Step 3 Params: %s", dynamicParams.toString());
        dialog.show();  //Please wait dialog
        TazweegApi.getInstance().stepThree(dynamicParams
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
                        Intent intent = new Intent(context, SignUpActivity4.class);
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
