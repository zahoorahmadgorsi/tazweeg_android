package com.bulahej.tazweeg.activities;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.bulahej.tazweeg.utilties.MaskWatcher;
import com.bulahej.tazweeg.utilties.Utilities;
import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bulahej.tazweeg.constant.Constants.COUNTRY_CODE_UAE;
import static com.bulahej.tazweeg.constant.Constants.CURRENT_MEMBER;
import static com.bulahej.tazweeg.constant.Constants.LOGGED_IN_USER;
import static com.bulahej.tazweeg.constant.Constants.PREF_NAME;
import static com.bulahej.tazweeg.constant.Constants.USER_TYPE_ADMIN;
import static com.bulahej.tazweeg.constant.Constants.USER_TYPE_CONSULTANT;
import static com.bulahej.tazweeg.constant.Constants.USER_TYPE_MEMBER;
import static com.bulahej.tazweeg.constant.Constants.formatFromServer;
import static com.bulahej.tazweeg.constant.Constants.formatOnDevice;
import static com.bulahej.tazweeg.constant.Constants.formatToServer;
import static com.bulahej.tazweeg.constant.Constants.loggedInMember;
import static com.bulahej.tazweeg.constant.Constants.spinners;

public class SignUpActivity1 extends AppCompatActivity {
    private ScrollView svItems;
    private EditText txtName, txtMobileNumber,txtEmail, txtEmirateIdCard, txtFamilyName, txtDateOfBirth, txtPlaceOfBirth, txtAddress;
    private CheckBox cbIsFamilyShow;
    private Button btnNext;
    private int emiratesIDLength = 15 + 3, memberID,selectedCountryIndex = 0,selectedEmirateIndex = 0,selectedResidenceIndex = 0,selectedEthnicityIndex = 0,selectedMotherNationalityIndex = 0;
    private User currentMember;     //whose data is being filled in this step
    private Boolean isFamilyShow = true;    //by default it will be true
    private Spinner spnCountry,spnEmirate,spnResidence,spnEthnicity,spnMotherNationality;
    SharedPreferences preferences;
    private LinearLayout llEthnicity;
    List<DropDown> countryList,emiratesList, residenceTypeList, ethnicityList,motherNationalityList;
    private AlertDialog dialog;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_1);
        context = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.step_one);
        toolbar.setTitleTextColor(0xFF000000);

        initUIElements();
        settingViewAndClicks();
        getSpinnersData();

        preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        if(Utilities.getSelectedUserType(preferences) == USER_TYPE_MEMBER){// but if consultant is trying to signup
            llEthnicity.setVisibility(View.GONE);//hidden for member
            if (loggedInMember != null){
                currentMember = loggedInMember;
                setMemberValues();
            }
        }else if (Utilities.getSelectedUserType(preferences) == USER_TYPE_ADMIN ||
                Utilities.getSelectedUserType(preferences) == USER_TYPE_CONSULTANT){//consultant coming for edit
//            getMemberDetail(isRefresh: false); To be implemented
        }

//        txtEmail.setText("Zahoor.Ahmad@Gorsi.Android");
//        txtEmirateIdCard.setText("784-1983-7504292-0");
//        txtFamilyName.setText("Gorsi Gujjar");
//        txtDateOfBirth.setText("18-Apr-1983");
//        txtPlaceOfBirth.setText("Lahore, Pakistan.");
//        txtAddress.setText("1605, Bank al Saderat Building, Abu Dhabi");
    }

    //***********
    //populating all textfields
    //if consultant is editing then it will be called from getMemberDetail() to set the values of the member which is fetched from server
    //if Member is editing then it will be called from viewDidLoad to set the values of the member which is loaded from localStorage i.e. got at the time of login.
    //***********
    private void setMemberValues() {
        txtName.setText(currentMember.getName());
        txtMobileNumber.setText(currentMember.getMobile());
        txtEmail.setText( currentMember.getEmail());
        if (currentMember != null && currentMember.getCountryId() == Constants.COUNTRY_CODE_UAE) {
            txtEmirateIdCard.addTextChangedListener(new MaskWatcher("###-####-#######-#"));
        }
        txtEmirateIdCard.setText( currentMember.getEmiratesIdCardNumber());
        //If valid emirates ID exist (length >= 18) and logged in user is a member then Txt emiratesID is not editable
        if (txtEmirateIdCard.getText().length() >= emiratesIDLength && Utilities.getSelectedUserType(preferences) == USER_TYPE_MEMBER){// //if emirates ID Exist
            txtEmirateIdCard.setEnabled(false);
        }
        txtFamilyName.setText( currentMember.getFamily());
        isFamilyShow = currentMember.getIsFamilyShow();
        cbIsFamilyShow.setChecked(isFamilyShow);

        String dob = "";
        try {
            dob = Utilities.changeDateFormats(currentMember.getBirthDate(), formatFromServer, formatOnDevice);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txtDateOfBirth.setText(dob);
        txtPlaceOfBirth.setText( currentMember.getBirthPlace());
        int index = Utilities.getIndexByID(countryList,currentMember.getCountryId());
        if (index >= 0) {
            spnCountry.setSelection(index);
        }
        String[] stateIds =  currentMember.getStateId().split(",");
        if (stateIds.length > 0 && stateIds[0].length() > 0) {
            index = Utilities.getIndexByID(emiratesList, Integer.parseInt(stateIds[0]));
            if (index >= 0) {
                spnEmirate.setSelection(index);
            }
        }
        txtAddress.setText( currentMember.getAddress());
        index = Utilities.getIndexByID(residenceTypeList,currentMember.getResidenceTypeId());
        if (index >= 0) {
            spnResidence.setSelection(index);
        }
        index = Utilities.getIndexByID(ethnicityList,currentMember.getEthnicityId());
        if (index >= 0) {
            spnEthnicity.setSelection(index);
        }
        index = Utilities.getIndexByID(motherNationalityList,currentMember.getMotherNationalityId());
        if (index >= 0) {
            spnMotherNationality.setSelection(index);
        }
    }

    private void getSpinnersData() {
        if (spinners == null) {
            TazweegApi.getInstance().getTypes().enqueue(new Callback<Type>() {
                @Override
                public void onResponse(Call<Type> call, Response<Type> response) {
                    if (response.body() != null) {
                        int status = response.body().getStatus();
                        if (status == 1) {
                            Type type = response.body();
                            if (type.getDropDowns().size() > 0) {
                                spinners = Utilities.JSONResponseToDropDowns(type.getDropDowns());  //Assigngin downloaded dropdowns data to constant variable
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

                    Utilities.myToastMessage(getApplicationContext(), getString(R.string.failure));
                }

            });
        }else{
            settingSpinnersData();
            settingSpinnersItemSelection();
        }

    }

    private void settingSpinnersData() {
        if (spinners != null) {
            //COUNTRIES
            countryList = new ArrayList<DropDown>();
            for (DropDown item : spinners.getCountries()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                countryList.add(item);
            }
            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, countryList);
            spnCountry.setAdapter(spinnerArrayAdapter);
            int index = Utilities.getIndexByID(countryList,218); //UAE has 218 ID
            if (index >= 0) {
                spnCountry.setSelection(index);
            }
            //EMIRATES
            emiratesList = new ArrayList<DropDown>();
            for (DropDown item : spinners.getStates()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                emiratesList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, emiratesList);
            spnEmirate.setAdapter(spinnerArrayAdapter);
            //RESIDENCE
            residenceTypeList = new ArrayList<DropDown>();
            for (DropDown item : spinners.getResidenceType()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                residenceTypeList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, residenceTypeList);
            spnResidence.setAdapter(spinnerArrayAdapter);
            //ETHNICITY
            ethnicityList = new ArrayList<DropDown>();
            for (DropDown item : spinners.getEthnicity()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                ethnicityList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ethnicityList);
            spnEthnicity.setAdapter(spinnerArrayAdapter);
            //MOTHER NATIONALITY
            motherNationalityList = new ArrayList<DropDown>();
            for (DropDown item : spinners.getCountries()) {
                item.setContext(getApplicationContext());   //Setting the context, because we have override toString() method on classs DropDown to bind localized value with the spinner
                motherNationalityList.add(item);
            }
            spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, motherNationalityList);
            spnMotherNationality.setAdapter(spinnerArrayAdapter);
            if (index >= 0) {
                spnMotherNationality.setSelection(index);
            }
        }
    }

    private void settingSpinnersItemSelection() {
        //COUNTRY
        spnCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountryIndex = position;
                Utilities.myLogError("selectedCountryIndex: %d", position);
                if (countryList.get(position).getValueId() != COUNTRY_CODE_UAE){    //If country is other then UAE then state must be OTHERS
                    spnEmirate.setSelection(9); //at 9th index we have state Others
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //EMIRATES
        spnEmirate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedEmirateIndex = position;
                Utilities.myLogError("selectedEmirateIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //RESIDENCE TYPE
        spnResidence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedResidenceIndex = position;
                Utilities.myLogError("selectedResidenceIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //ETHNICITY
        spnEthnicity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedEthnicityIndex = position;
                Utilities.myLogError("selectedEthnicityIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        //MOTHER NATIONALITY
        spnMotherNationality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMotherNationalityIndex = position;
                Utilities.myLogError("selectedMotherNationalityIndex: %d", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
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

    //This method is used to implement localization
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    private void settingViewAndClicks() {
//        txtName.requestFocus();
        txtDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePicker = new DatePickerDialog(SignUpActivity1.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String monthStr;
                        String dayStr;

                        if (month <= 8) {
                            monthStr = "0" + (month + 1);
                        } else {
                            monthStr = "" + (month + 1);
                        }

                        if (dayOfMonth <= 9) {
                            dayStr = "0" + dayOfMonth;
                        }
                        else {
                            dayStr = "" + dayOfMonth;
                        }
                        String strDate = dayStr + "-" + monthStr + "-" + year;
                        try {
                            txtDateOfBirth.setText( Utilities.changeDateFormats(strDate , "dd-MM-yyyy", Constants.formatOnDevice) );
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }, calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1), calendar.get(Calendar.DAY_OF_MONTH));
                datePicker.getDatePicker().setMaxDate(System.currentTimeMillis() - 568025136000L);  //setting date to 18 years in past
//                String str =  String.valueOf(Calendar.YEAR +" "+Calendar.MONTH + " " + calendar.DATE);
//                Log.e("year = " , str);
//                datePicker.updateDate(Calendar.YEAR,Calendar.MONTH, calendar.DATE);
                datePicker.show();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextTap();
            }
        });
    }

    private void nextTap(){
        final Resources resources = this.getResources();

        int userId = -1 ,ethnicityId = -1, userUpdateId = -1;
        String email = txtEmail.getText().toString();
        String idCardNum = txtEmirateIdCard.getText().toString();
        String family = txtFamilyName.getText().toString();
        String dateOfBirth  = null;
        try {
            dateOfBirth = Utilities.changeDateFormats(txtDateOfBirth.getText().toString(), formatOnDevice, formatToServer);
            if (dateOfBirth.isEmpty() ) {
                txtDateOfBirth.requestFocus();
                txtDateOfBirth.setError(resources.getString(R.string.invalid_dob));
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            txtDateOfBirth.requestFocus();
            txtDateOfBirth.setError(resources.getString(R.string.invalid_dob));
        }
        String placeOfBirth = txtPlaceOfBirth.getText().toString();
        String address = txtAddress.getText().toString();


        if(!Utilities.isValidEmail(email)) {    //is empty check is already applied in isValidEmail
            txtEmail.requestFocus();
            txtEmail.setError(resources.getString(R.string.invalid_email));
            return;
        }//else if (idCardNum.isEmpty() || idCardNum.length()<18) {
        else if ((currentMember.getCountryId() == COUNTRY_CODE_UAE && (idCardNum.length() < emiratesIDLength - 3 ||  //without dashes, to handle legacy data
                idCardNum.length() > emiratesIDLength)  ) ||
            (currentMember.getCountryId() != COUNTRY_CODE_UAE && idCardNum.length() < 9)   //9 is min national ID length for bahrain, KSA = 10 , Kuwait=12
        )    {
            txtEmirateIdCard.requestFocus();
            txtEmirateIdCard.setError(resources.getString(R.string.invalid_emirates_id));
            return;
        }else if (family.isEmpty() ) {
            txtFamilyName.requestFocus();
            txtFamilyName.setError(resources.getString(R.string.invalid_family_name));
            return;
        }
        //DOB check is applied at top, because we have to handle exception
//        else if (dateOfBirth.isEmpty() ) {
//            txtDateOfBirth.requestFocus();
//            txtDateOfBirth.setError(resources.getString(R.string.invalid_dob));
//            return;
//        }
        else if (placeOfBirth.isEmpty() ) {
            txtPlaceOfBirth.requestFocus();
            txtPlaceOfBirth.setError(resources.getString(R.string.invalid_pob));
            return;
        }else if (address.isEmpty() ) {
            txtAddress.requestFocus();
            txtAddress.setError(resources.getString(R.string.invalid_address));
            return;
        }

        if(Utilities.getSelectedUserType(preferences) == USER_TYPE_MEMBER) {// but if consultant is trying to signup
            userId = loggedInMember.getId();
        }else{
            userId = currentMember.getId();
            ethnicityId = ethnicityList.get(selectedEthnicityIndex).getValueId();
        }
        userUpdateId = loggedInMember.getId();
        dialog.show();  //Please wait dialog
        TazweegApi.getInstance().stepOne(userId, email, idCardNum,family,cbIsFamilyShow.isChecked(),dateOfBirth,placeOfBirth
                ,countryList.get(selectedCountryIndex).getValueId()
                ,emiratesList.get(selectedEmirateIndex).getValueId()
                ,address
                ,residenceTypeList.get(selectedResidenceIndex).getValueId()
                ,ethnicityId
                ,motherNationalityList.get(selectedMotherNationalityIndex).getValueId()
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
                        if(Utilities.getSelectedUserType(preferences) == USER_TYPE_MEMBER){
                            loggedInMember = currentMember;  //Storing into constants as it will be required in whole application
                            //Making logged in user record persistent
                            //https://stackoverflow.com/questions/5418160/store-and-retrieve-a-class-object-in-shared-preference
                            Gson gson = new Gson();
                            String json = gson.toJson(currentMember); // myObject - instance of MyObject
                            preferences.edit().putString(LOGGED_IN_USER, json).apply();
                        }
                        Intent intent = new Intent(context, SignUpActivity2.class);
                        intent.putExtra(CURRENT_MEMBER, currentMember);
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

    private void initUIElements() {
        svItems = findViewById(R.id.svItems);
        txtName = findViewById(R.id.tvCountryName);
        txtEmail = findViewById(R.id.txtEmail);
        txtMobileNumber = findViewById(R.id.txtMobileNumber);
        txtEmirateIdCard = findViewById(R.id.txtEmiratesIdCardNumber);

        txtDateOfBirth = findViewById(R.id.txtDateOfBirth);
        txtAddress = findViewById(R.id.txtAddress);
        txtFamilyName = findViewById(R.id.txtFamilyName);
        cbIsFamilyShow = findViewById(R.id.cbIsFamilyShow);
        txtPlaceOfBirth = findViewById(R.id.txtPlaceOfBirth);

        spnCountry = findViewById(R.id.spnCountry);
        spnEmirate = findViewById(R.id.spnEmirate);
        spnResidence = findViewById(R.id.spnResidence);
        llEthnicity = findViewById(R.id.llEthnicity);
        spnEthnicity = findViewById(R.id.spnEthnicity);
        spnMotherNationality = findViewById(R.id.spnMotherNationality);

        btnNext = findViewById(R.id.bNext);
        dialog = AppUtility.createPleaseWaitDialog(this);
    }

}
