package com.bulahej.tazweeg.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.activities.MainActivity;
import com.bulahej.tazweeg.activities.SignUpActivity1;
import com.bulahej.tazweeg.activities.SignUpActivity2;
import com.bulahej.tazweeg.activities.SignUpActivity3;
import com.bulahej.tazweeg.activities.SignUpActivity4;
import com.bulahej.tazweeg.activities.SignUpActivity5;
import com.bulahej.tazweeg.activities.WebViewActivity;
import com.bulahej.tazweeg.adapters.MemberDetailAdapter;
import com.bulahej.tazweeg.adapters.MemberMatchingAdapter;
import com.bulahej.tazweeg.apis_responses.TazweegApi;
import com.bulahej.tazweeg.apis_responses.UserResponse.User;
import com.bulahej.tazweeg.apis_responses.UserResponse.UserResponse;
import com.bulahej.tazweeg.constant.Constants;
import com.bulahej.tazweeg.constant.Key;
import com.bulahej.tazweeg.utilties.AppUtility;
import com.bulahej.tazweeg.utilties.Utilities;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//public class ProfileFragment extends Fragment implements MemberMatchingAdapter.OnItemClickListener{
    public class ProfileFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    LinearLayout llSegmentControl;
    ItemTouchHelper mTouchHelper;

    private User currentMember;
    SharedPreferences preferences;

    public AlertDialog getDialog() {
        return dialog;
    }

    private AlertDialog dialog;
    private TextView lblProfile,lblRequired;
//    private TextView lblChoosing, lblMatching,lblMarried;
    private int infoType;
    private ArrayList<User> matchingsData = new ArrayList<>();
    private RecyclerView rvMemberDetail,rvMemberMatching;
    private MemberDetailAdapter memberDetailAdapter;
    private MemberMatchingAdapter memberMatchingAdapter;
    private Resources resources;

    private ArrayList<String> profileInfoKeys = new ArrayList<>();
    private ArrayList<String> profileInfoValues = new ArrayList<>();
    private ArrayList<String> requiredInfoKeys = new ArrayList<>();
    private ArrayList<String> requiredInfoValues = new ArrayList<>();

    private Boolean toShowLimitedProfile;
    private SwipeRefreshLayout swipeRefreshLayout ;
    private ItemTouchHelper.Callback swipeAndDragHelper;
    View view;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setIcon(R.drawable.arabic_flag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        initUIElements();
        resources = getActivity().getResources();
        preferences = getActivity().getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
//        currentMember = Utilities.getLoggedInMember(preferences);

        if (savedInstanceState == null) {
            Bundle extras = getArguments();
            if(extras != null) { //User is coming to sign up
                infoType = (int) extras.get(Key.KEY_INFO_TYPE);
                currentMember = (User)extras.getSerializable(Constants.CURRENT_MEMBER);
                segmentSelectedIndexChanged(infoType,false);
                //If user want to see his own profile OR consultant want to see his own members profile OR logged in user is an Admin
                if (    (Constants.loggedInMember.getId() == currentMember.getId()) ||
                        Constants.loggedInMember.getId() == currentMember.getConsultantId() ||
                        Constants.loggedInMember.getTypeId() == Constants.USER_TYPE_ADMIN
                ){
                    llSegmentControl.setVisibility(View.VISIBLE);
                    toShowLimitedProfile = false;

                }else{
                    llSegmentControl.setVisibility(View.GONE);
                    toShowLimitedProfile = true;
//                    btnEdit.setVisibility(View.GONE);
                }
            }
        }

//        if (currentMember.getImagePath().length()  > 0){
//            String imgURL = currentMember.getImagePath();
//            RequestOptions options = new RequestOptions()
//                    .centerCrop()
//                    .placeholder(R.mipmap.ic_launcher_round)
//                    .error(R.mipmap.ic_launcher_round);
//            Glide.with(this).load(imgURL).apply(options).into(imgView);
//        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Define your task here .
                segmentSelectedIndexChanged(infoType, true);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    private void segmentSelectedIndexChanged(int selectedIndex, Boolean isSilentFetch )
    {
        switch (selectedIndex){
            case Constants.INFO_TYPE_PROFILE:
                infoType = selectedIndex; //infotype is class variable
                lblProfile.setBackgroundColor(getResources().getColor( R.color.colorAccent));
                lblProfile.setTextColor(getResources().getColor( R.color.colorWhite));

                lblRequired.setBackgroundColor(getResources().getColor( R.color.colorLightGrey));
                lblRequired.setTextColor(getResources().getColor( R.color.colorBlack));

                if(profileInfoKeys.size() > 0 || profileInfoValues.size() > 0) {
                    setProfileData();   //set cached data
                }

                getMemberDetail(isSilentFetch); //don't fetch silently
                break;
            case Constants.INFO_TYPE_REQUIRED:
                infoType = selectedIndex; //infotype is class variable
                lblProfile.setBackgroundColor(getResources().getColor( R.color.colorLightGrey));
                lblProfile.setTextColor(getResources().getColor( R.color.colorBlack));

                lblRequired.setBackgroundColor(getResources().getColor( R.color.colorAccent));
                lblRequired.setTextColor(getResources().getColor( R.color.colorWhite));

                if(requiredInfoKeys.size() > 0 || requiredInfoValues.size() > 0) {
                    setProfileData();   //set cached data
                }
                getMemberDetail(isSilentFetch); //don't fetch silently
                break;
        }
    }

    private void completeProfileTapped(){
//        Log.i("completeProfileTapped", "completeProfileTapped onTouch");
        int mobileStatus = Constants.loggedInMember.getMobileStatus();
        int profilePercentComplete = mobileStatus * 20;
        switch (mobileStatus){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                break;
            case 5:
                profilePercentComplete = 90;
                break;
            case 6:
                profilePercentComplete = 100;
                break;
            default:
                break;
        }
        //By passing all steps logic if user has paid.... because paid mean all 5 steps are completed.
        if(Constants.loggedInMember.getPaymentStatusId() == Constants.PAYMENT_TYPE_CHOOSING)
        {
            profilePercentComplete = 100;
        }

        Intent intent = new Intent(getActivity(), SignUpActivity1.class); //default
        switch (profilePercentComplete) {
//            case 0: //Default
//                intent = new Intent(context, SignUpActivity1.class);
//                startActivity(intent);
//                break;
            case 20:
                intent = new Intent(getActivity(), SignUpActivity2.class);
                startActivity(intent);
                break;
            case 40:
                intent = new Intent(getActivity(), SignUpActivity3.class);
                startActivity(intent);
                break;
            case 60:
                intent = new Intent(getActivity(), SignUpActivity4.class);
                startActivity(intent);
                break;
            case 80:
                intent = new Intent(getActivity(), SignUpActivity5.class);
                startActivity(intent);
                break;
            case 90:
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra(Key.KEY_URL, Constants.PAGE_TYPE_PAYMENT);
                startActivity(intent);
                break;
            case 100:
                ((MainActivity)getActivity()).mBottomNavigationView.setSelectedItemId(R.id.action_profile);
//                intent = new Intent(getActivity(), ProfileActivity.class);
//                intent.putExtra(Constants.CURRENT_MEMBER, Constants.loggedInMember);
//                intent.putExtra(Key.KEY_INFO_TYPE, Constants.INFO_TYPE_PROFILE);
//                startActivity(intent);
                break;
            default:
                intent = new Intent(getActivity(), SignUpActivity1.class);
                startActivity(intent);
//                intent = new Intent(context, SignUpActivity5.class);
//                startActivity(intent);
                break;
        }
    }

    private void getMemberDetail(Boolean isSilentFetch)
    {
        int consultantID = 0;   // be default, its mean some member, admin or some other consultant is trying to view the profile of a member, hence is viewed must remain false
        //If loggedin member is a consultant, and he is viewing the profile of his own member then send consultant ID
        if (Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_CONSULTANT  && Constants.loggedInMember.getId() == currentMember.getConsultantId() ){ //
            consultantID = 123456;  //you can send any thing here, but it should not be zero
        }
        if (!isSilentFetch) {
            dialog.show();
        }
        TazweegApi.getInstance().getMemberDetailsByMemberID(currentMember.getId(),consultantID).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    currentMember = response.body().getUser();
                    //if logged in user is a member then save a custom object into userdefaults you have to encode it
                    //To store latest object into userdefaults, so that if member want to edit, it should have latest values
                    //If current user is a member and he is using his own profile then make it consistent else dont persist
                    if(Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_MEMBER &&
                            Constants.loggedInMember.getId() == currentMember.getId()){
                        Constants.loggedInMember = currentMember;  //Storing into constants as it will be required in whole application
                        //Making logged in user record persistent
                        //https://stackoverflow.com/questions/5418160/store-and-retrieve-a-class-object-in-shared-preference
                        Gson gson = new Gson();
                        String json = gson.toJson(currentMember); // myObject - instance of MyObject
                        preferences.edit().putString(Constants.LOGGED_IN_USER, json).apply();
                    }
                    populateProfileAndRequiredArrays();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                dialog.dismiss();
                Utilities.myToastMessage(getActivity(), getString(R.string.failure));
            }
        });
    }

    //this method populates the hashMap list to bind with profile and required tabs
    private void populateProfileAndRequiredArrays()
    {
        profileInfoKeys = new ArrayList<>();
        profileInfoValues = new ArrayList<>();
        requiredInfoKeys = new ArrayList<>();
        requiredInfoValues = new ArrayList<>();

        profileInfoKeys.add(this.getResources().getString(R.string.member_code));
        profileInfoValues.add( currentMember.getUsername() == "" ?  this.getResources().getString(R.string.not_available) : currentMember.getUsername() );

        //this information can only be seen by consultant of his own member and if current user is looking at his own profile
        if (!toShowLimitedProfile){
            profileInfoKeys.add(this.getResources().getString(R.string.full_name));
            profileInfoValues.add(currentMember.getName() == "" ?  this.getResources().getString(R.string.not_available) : currentMember.getName() );

            profileInfoKeys.add(this.getResources().getString(R.string.phone_number));
            profileInfoValues.add(currentMember.getMobile() == "" ?  this.getResources().getString(R.string.not_available) : currentMember.getMobile() );

            profileInfoKeys.add(this.getResources().getString(R.string.email));
            profileInfoValues.add(currentMember.getEmail() == "" ?  this.getResources().getString(R.string.not_available) : currentMember.getEmail() );

            //Emirates ID Number
            profileInfoKeys.add(this.getResources().getString(R.string.emirates_id));
            profileInfoValues.add(currentMember.getEmiratesIdCardNumber() == "" ?  this.getResources().getString(R.string.not_available) : currentMember.getEmiratesIdCardNumber() );
        }
        //Tribe can only bee seen by consultants/Admin and a member of his/her own.
        if (    Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_CONSULTANT ||
                Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_ADMIN ||
                Constants.loggedInMember.getId() == currentMember.getId() ||
                currentMember.getIsFamilyShow()
        ){
            profileInfoKeys.add(this.getResources().getString(R.string.family));
            profileInfoValues.add(currentMember.getFamily() == "" ?  this.getResources().getString(R.string.not_available) : currentMember.getFamily() );


            profileInfoKeys.add(this.getResources().getString(R.string.family_name_to_show));
            profileInfoValues.add(currentMember.getIsFamilyShow() == true ?  this.getResources().getString(R.string.yes) : this.getResources().getString(R.string.no) );
        }
        //this information can only be seen by consultant of his own member and if current user is looking at his own profile
        if (!toShowLimitedProfile){
            profileInfoKeys.add(this.getResources().getString(R.string.place_of_birth));
            profileInfoValues.add(currentMember.getBirthPlace() == "" ?  this.getResources().getString(R.string.not_available) : currentMember.getBirthPlace() );
        }

        int age = Utilities.getAge(currentMember.getBirthDate(),Constants.formatFromServer);
        profileInfoKeys.add(this.getResources().getString(R.string.age));
        profileInfoValues.add(String.valueOf(age));

        //this information can only be seen by consultant of his own member and if current user is looking at his own profile
        if (!toShowLimitedProfile){
            profileInfoKeys.add(this.getResources().getString(R.string.address));
            profileInfoValues.add(currentMember.getAddress());
        }

        if(Utilities.isRTL(getActivity())){
            //Step 1
            profileInfoKeys.add(this.getResources().getString(R.string.country));
            profileInfoValues.add(currentMember.getCountryAR());

            profileInfoKeys.add(this.getResources().getString(R.string.state));
            profileInfoValues.add(currentMember.getStateAR());

            profileInfoKeys.add(this.getResources().getString(R.string.residence));
            profileInfoValues.add(currentMember.getResidenceTypeAR());
            //Consultant and Admin can see the ethnicity only
            if (Utilities.getSelectedUserType(preferences) != Constants.USER_TYPE_MEMBER){
                profileInfoKeys.add(this.getResources().getString(R.string.ethnicity));
                profileInfoValues.add(currentMember.getEthnicityAR());
            }
            profileInfoKeys.add(this.getResources().getString(R.string.mother_nationality));
            profileInfoValues.add(currentMember.getMotherNationalityAR());

            //this information can only be seen by consultant of his own member and if current user is looking at his own profile
            if (!toShowLimitedProfile){
                //Showing this field on step 2 only for females
                if(currentMember.getGenderId() == Constants.GENDER_FEMALE){

                    profileInfoKeys.add(this.getResources().getString(R.string.accepting_polygamy));
                    profileInfoValues.add(currentMember.getIsPolygamy() == true ?  this.getResources().getString(R.string.yes) : this.getResources().getString(R.string.no) );
                    if (currentMember.getIsPolygamy() == false) {
                        profileInfoKeys.add(this.getResources().getString(R.string.bride_arrangement));
                        profileInfoValues.add(currentMember.getSbrideArrangmentAR());
                    }

                }
                profileInfoKeys.add(this.getResources().getString(R.string.gender));
                profileInfoValues.add(currentMember.getGenderAR());
                ////Step 2
                profileInfoKeys.add(this.getResources().getString(R.string.is_smoking));
                profileInfoValues.add(currentMember.getIsSmokeAR());
            }

            profileInfoKeys.add(this.getResources().getString(R.string.skin_color));
            profileInfoValues.add(currentMember.getSkinColorAR());

            profileInfoKeys.add(this.getResources().getString(R.string.hair_color));
            profileInfoValues.add(currentMember.getHairColorAR());

            //this information can only be seen by consultant of his own member and if current user is looking at his own profile
            if (!toShowLimitedProfile){
                profileInfoKeys.add(this.getResources().getString(R.string.hair_type));
                profileInfoValues.add(currentMember.getHairTypeAR());

                profileInfoKeys.add(this.getResources().getString(R.string.eye_color));
                profileInfoValues.add(currentMember.getEyeColorAR());
            }

            profileInfoKeys.add(this.getResources().getString(R.string.height));
            profileInfoValues.add(currentMember.getHeightAR());

            //this information can only be seen by consultant of his own member and if current user is looking at his own profile
            if (!toShowLimitedProfile){
                profileInfoKeys.add(this.getResources().getString(R.string.body_type));
                profileInfoValues.add(currentMember.getBodyTypeAR());

                profileInfoKeys.add(this.getResources().getString(R.string.body_weight));
                profileInfoValues.add(currentMember.getMemberWeightAR());

                profileInfoKeys.add(this.getResources().getString(R.string.sect_type));
                profileInfoValues.add(currentMember.getSectAR());

                //Showing this field on step 2 only for females
                if(currentMember.getGenderId() == Constants.GENDER_FEMALE){
                    profileInfoKeys.add(this.getResources().getString(R.string.veil));
                    profileInfoValues.add(currentMember.getsCondemnBrideAR());
                }
            }

            profileInfoKeys.add(this.getResources().getString(R.string.has_driving_license));
            profileInfoValues.add(currentMember.getMemberLicenseIdAR());


            //Step 3
            //this information can only be seen by consultant of his own member and if current user is looking at his own profile
            if (!toShowLimitedProfile){
                profileInfoKeys.add(this.getResources().getString(R.string.education_level));
                profileInfoValues.add(currentMember.getEducationLevelAR());

                profileInfoKeys.add(this.getResources().getString(R.string.religion_commitment));
                profileInfoValues.add(currentMember.getReligionCommitmentAR());

                profileInfoKeys.add(this.getResources().getString(R.string.financial_status));
                profileInfoValues.add(currentMember.getFinancialStatusAR());
            }
            profileInfoKeys.add(this.getResources().getString(R.string.social_status));
            profileInfoValues.add(currentMember.getSocialStatusAR());

            profileInfoKeys.add(this.getResources().getString(R.string.is_working));
            profileInfoValues.add(currentMember.getIsWorkingAR());

            //this information can only be seen by consultant of his own member and if current user is looking at his own profile
            if (!toShowLimitedProfile){
                //If social status is not equal to single then ask or haschildren, numberOfChildren and reproductions
                if ( currentMember.getSocialStatusId() != Constants.SOCIAL_STATUS_SINGLE){
                    profileInfoKeys.add(this.getResources().getString(R.string.has_children));
                    profileInfoValues.add(currentMember.getMemberHasChildIdAR());
                    //If member has childs then ask for number of child else ask for reproduction
                    if (currentMember.getMemberHasChildId() == Constants.IS_SMOKING_YES){
                        profileInfoKeys.add(this.getResources().getString(R.string.number_of_children));
                        profileInfoValues.add(currentMember.getMemberNoOfChildrenIdAR());
                    }else{
                        profileInfoKeys.add(this.getResources().getString(R.string.reproduction)); //Is Working
                        profileInfoValues.add(currentMember.getMemberReproductionIdAR());
                    }
                }
                //if a person is not working then hide its occupation , jobtype and jobTitle
                if (currentMember.getIsWorkingId() == Constants.IS_WORKING_WORKING){
                    profileInfoKeys.add(this.getResources().getString(R.string.occupation));
                    profileInfoValues.add(currentMember.getOccupation());

                    profileInfoKeys.add(this.getResources().getString(R.string.job_type));
                    profileInfoValues.add(currentMember.getJobTypeAR());

                    profileInfoKeys.add(this.getResources().getString(R.string.job_title));
                    profileInfoValues.add(currentMember.getJobTitle());
                }
                profileInfoKeys.add(this.getResources().getString(R.string.annual_income));
                profileInfoValues.add(currentMember.getAnnualIncomeAR());

                profileInfoKeys.add(this.getResources().getString(R.string.any_disease));
                profileInfoValues.add(currentMember.getIsDiseaseAR());

                profileInfoKeys.add(this.getResources().getString(R.string.disease_name));
                profileInfoValues.add(currentMember.getDiseaseName());

                // Step 4
                //Showing this field on step 4 only for males
                if(currentMember.getGenderId() == Constants.GENDER_MALE){
                    requiredInfoKeys.add(this.getResources().getString(R.string.bride_arrangement));
                    requiredInfoValues.add(currentMember.getSbrideArrangmentAR());
                }

                requiredInfoKeys.add(this.getResources().getString(R.string.gcc_check));
                requiredInfoValues.add( currentMember.getGCCMarriage() == true ? this.getResources().getString(R.string.yes) : this.getResources().getString(R.string.no));
                if (currentMember.getGCCMarriage() == false) {  //show gcc country infor if gcc marriage is false
                    requiredInfoKeys.add(this.getResources().getString(R.string.country));
                    requiredInfoValues.add(currentMember.getsCountryAR());
                }

                requiredInfoKeys.add( currentMember.getGenderId() == Constants.GENDER_MALE ? this.getResources().getString(R.string.male_social_status_check) : this.getResources().getString(R.string.female_social_status_check));
                requiredInfoValues.add( currentMember.getAcceptDMW() == true ? this.getResources().getString(R.string.yes) : this.getResources().getString(R.string.no));
                if (currentMember.getAcceptDMW() == false) {  //show social statue infor if AcceptDMW marriage is false
                    requiredInfoKeys.add(this.getResources().getString(R.string.social_status));
                    requiredInfoValues.add(currentMember.getsSocialStatusAR());
                }
                //If social status is not equal to single then ask or haschildren, numberOfChildren and reproductions
                if (currentMember.getsSocialStatusId() != Constants.SOCIAL_STATUS_SINGLE){
                    requiredInfoKeys.add(this.getResources().getString(R.string.has_children));
                    requiredInfoValues.add(currentMember.getsHasChildIdAR());
                    //If member has childs then ask for number of child else ask for reproduction
                    if (currentMember.getsHasChildId() == Constants.IS_SMOKING_YES){
                        requiredInfoKeys.add(this.getResources().getString(R.string.number_of_children));
                        requiredInfoValues.add(currentMember.getsNoOfChildrenAR());
                    }else{
                        requiredInfoKeys.add(this.getResources().getString(R.string.reproduction));
                        requiredInfoValues.add(currentMember.getsReproductionStatusAR());
                    }
                }
                requiredInfoKeys.add(this.getResources().getString(R.string.age));
                requiredInfoValues.add(currentMember.getsAgeAR());

                requiredInfoKeys.add(this.getResources().getString(R.string.height));
                requiredInfoValues.add(currentMember.getsHeightAR());

                requiredInfoKeys.add(this.getResources().getString(R.string.body_type));
                requiredInfoValues.add(currentMember.getsBodyTypeAR());

                requiredInfoKeys.add(this.getResources().getString(R.string.skin_color));
                requiredInfoValues.add(currentMember.getsSkinColorAR());

                requiredInfoKeys.add(this.getResources().getString(R.string.education_level));
                requiredInfoValues.add(currentMember.getsEducationLevelAR());

                requiredInfoKeys.add(this.getResources().getString(R.string.is_working));
                requiredInfoValues.add(currentMember.getsIsWorkingAR());

                //if a person is not working then hide its jobtype
                if (currentMember.getsIsWorkingId() == Constants.IS_WORKING_WORKING){
                    requiredInfoKeys.add(this.getResources().getString(R.string.job_type));
                    requiredInfoValues.add(currentMember.getsJobTypeAR());
                }
                requiredInfoKeys.add(this.getResources().getString(R.string.live_with_spouse));
                requiredInfoValues.add(currentMember.getSpouseStateToLiveAR());
            }
            //Showing this field on step 4 only for males
            if(currentMember.getGenderId() == Constants.GENDER_MALE){
                requiredInfoKeys.add(this.getResources().getString(R.string.veil));
                requiredInfoValues.add(currentMember.getsCondemnBrideAR());
            }
            requiredInfoKeys.add(this.getResources().getString(R.string.has_driving_license));
            requiredInfoValues.add(currentMember.getsDrivingLicenseAR());

            requiredInfoKeys.add(this.getResources().getString(R.string.has_person_refer));
            if ((currentMember.getReferName() != null && currentMember.getReferName().length() > 0) && (currentMember.getReferMobile() != null && currentMember.getReferMobile().length() > 0 )) {
                requiredInfoValues.add(this.getResources().getString(R.string.yes));
                requiredInfoKeys.add(this.getResources().getString(R.string.name));
                requiredInfoValues.add(currentMember.getReferName());

                requiredInfoKeys.add(this.getResources().getString(R.string.phone_number));
                requiredInfoValues.add(currentMember.getReferMobile());
            }else{
                requiredInfoValues.add(this.getResources().getString(R.string.no));
            }

            //this information can only be seen by consultant of his own member and if current user is looking at his own profile
            if (!toShowLimitedProfile){
                //Showing this field on step 4 only for males
                if(currentMember.getGenderId() == Constants.GENDER_MALE){
                    requiredInfoKeys.add(this.getResources().getString(R.string.will_pay_to_bride));
                    requiredInfoValues.add(currentMember.getWillPayToBrideAR());
                }
                //Step 5
                profileInfoKeys.add(this.getResources().getString(R.string.first_relative));
                profileInfoValues.add(currentMember.getFirstRelative());

                profileInfoKeys.add(this.getResources().getString(R.string.first_relative_number));
                profileInfoValues.add(currentMember.getFirstRelativeNumber());

                profileInfoKeys.add(this.getResources().getString(R.string.first_relative_relation));
                profileInfoValues.add(currentMember.getFirstRelativeRelationAR());

                profileInfoKeys.add(this.getResources().getString(R.string.second_relative));
                profileInfoValues.add(currentMember.getSecondRelative());

                profileInfoKeys.add(this.getResources().getString(R.string.second_relative_number));
                profileInfoValues.add(currentMember.getSecondRelativeNumber());

                profileInfoKeys.add(this.getResources().getString(R.string.second_relative_relation));
                profileInfoValues.add(currentMember.getSecondRelativeRelationAR());

                profileInfoKeys.add(this.getResources().getString(R.string.applicant_description));
                profileInfoValues.add(currentMember.getApplicantDescription());

                //Extra
                profileInfoKeys.add(this.getResources().getString(R.string.profile_status));
                profileInfoValues.add(currentMember.getPaymentStatusAR());
            }
        }else{ //ENGLISH
            //Step 1
            profileInfoKeys.add(this.getResources().getString(R.string.country));
            profileInfoValues.add(currentMember.getCountryEN());

            profileInfoKeys.add(this.getResources().getString(R.string.state));
            profileInfoValues.add(currentMember.getStateEN());

            profileInfoKeys.add(this.getResources().getString(R.string.residence));
            profileInfoValues.add(currentMember.getResidenceTypeEN());
            //Consultant and Admin can see the ethnicity only
            if ( Utilities.getSelectedUserType(preferences) != Constants.USER_TYPE_MEMBER){
                profileInfoKeys.add(this.getResources().getString(R.string.ethnicity));
                profileInfoValues.add(currentMember.getEthnicityEN());
            }
            profileInfoKeys.add(this.getResources().getString(R.string.mother_nationality));
            profileInfoValues.add(currentMember.getMotherNationalityEN());

            //this information can only be seen by consultant of his own member and if current user is looking at his own profile
            if (!toShowLimitedProfile){
                profileInfoKeys.add(this.getResources().getString(R.string.gender));
                profileInfoValues.add(currentMember.getGenderEN());

                //Showing this field on step 2 only for females
                if(currentMember.getGenderId() == Constants.GENDER_FEMALE){

                    profileInfoKeys.add(this.getResources().getString(R.string.accepting_polygamy));
                    profileInfoValues.add(currentMember.getIsPolygamy() == true ?  this.getResources().getString(R.string.yes) : this.getResources().getString(R.string.no) );
                    if (currentMember.getIsPolygamy() == false) {
                        profileInfoKeys.add(this.getResources().getString(R.string.bride_arrangement));
                        profileInfoValues.add(currentMember.getSbrideArrangmentEN());
                    }
                }
                //Step 2
                profileInfoKeys.add(this.getResources().getString(R.string.is_smoking));
                profileInfoValues.add(currentMember.getIsSmokeEN());

            }
            profileInfoKeys.add(this.getResources().getString(R.string.skin_color));
            profileInfoValues.add(currentMember.getSkinColorEN());

            profileInfoKeys.add(this.getResources().getString(R.string.hair_color));
            profileInfoValues.add(currentMember.getHairColorEN());

            //this information can only be seen by consultant of his own member and if current user is looking at his own profile
            if (!toShowLimitedProfile){
                profileInfoKeys.add(this.getResources().getString(R.string.hair_type));
                profileInfoValues.add(currentMember.getHairTypeEN());

                profileInfoKeys.add(this.getResources().getString(R.string.eye_color));
                profileInfoValues.add(currentMember.getEyeColorEN());
            }

            profileInfoKeys.add(this.getResources().getString(R.string.height));
            profileInfoValues.add(currentMember.getHeightEN());

            //this information can only be seen by consultant of his own member and if current user is looking at his own profile
            if (!toShowLimitedProfile){
                profileInfoKeys.add(this.getResources().getString(R.string.body_type));
                profileInfoValues.add(currentMember.getBodyTypeEN());

                profileInfoKeys.add(this.getResources().getString(R.string.body_weight));
                profileInfoValues.add(currentMember.getMemberWeightEN());

                profileInfoKeys.add(this.getResources().getString(R.string.sect_type));
                profileInfoValues.add(currentMember.getSectEN());

                //Showing this field on step 2 only for females
                if(currentMember.getGenderId() == Constants.GENDER_FEMALE){
                    profileInfoKeys.add(this.getResources().getString(R.string.veil));
                    profileInfoValues.add(currentMember.getsCondemnBrideEN());
                }
            }
            profileInfoKeys.add(this.getResources().getString(R.string.has_driving_license));
            profileInfoValues.add(currentMember.getMemberLicenseIdEN());
            //Step 3
            profileInfoKeys.add(this.getResources().getString(R.string.education_level));
            profileInfoValues.add(currentMember.getEducationLevelEN());

            //this information can only be seen by consultant of his own member and if current user is looking at his own profile
            if (!toShowLimitedProfile){
                profileInfoKeys.add(this.getResources().getString(R.string.religion_commitment));
                profileInfoValues.add(currentMember.getReligionCommitmentEN());

                profileInfoKeys.add(this.getResources().getString(R.string.financial_status));
                profileInfoValues.add(currentMember.getFinancialStatusEN());
            }

            profileInfoKeys.add(this.getResources().getString(R.string.social_status));
            profileInfoValues.add(currentMember.getSocialStatusEN());

            profileInfoKeys.add(this.getResources().getString(R.string.is_working));
            profileInfoValues.add(currentMember.getIsWorkingEN());

            //this information can only be seen by consultant of his own member and if current user is looking at his own profile
            if (!toShowLimitedProfile){
                //If social status is not equal to single then ask or haschildren, numberOfChildren and reproductions
                if (currentMember.getSocialStatusId() != Constants.SOCIAL_STATUS_SINGLE ){
                    profileInfoKeys.add(this.getResources().getString(R.string.has_children));
                    profileInfoValues.add(currentMember.getMemberHasChildIdEN());
                    //If member has childs then ask for number of child else ask for reproduction
                    if (currentMember.getMemberHasChildId() == Constants.IS_SMOKING_YES){
                        profileInfoKeys.add(this.getResources().getString(R.string.number_of_children));
                        profileInfoValues.add(currentMember.getMemberNoOfChildrenIdEN());
                    }else{
                        profileInfoKeys.add(this.getResources().getString(R.string.reproduction));
                        profileInfoValues.add(currentMember.getMemberReproductionIdEN());
                    }
                }
                //if a person is not working then hide its occupation , jobtype and jobTitle
                if (currentMember.getIsWorkingId() == Constants.IS_WORKING_WORKING){
                    profileInfoKeys.add(this.getResources().getString(R.string.occupation));
                    profileInfoValues.add(currentMember.getOccupation());

                    profileInfoKeys.add(this.getResources().getString(R.string.job_type));
                    profileInfoValues.add(currentMember.getJobTypeEN());

                    profileInfoKeys.add(this.getResources().getString(R.string.job_title));
                    profileInfoValues.add(currentMember.getJobTitle());
                }
                profileInfoKeys.add(this.getResources().getString(R.string.annual_income));
                profileInfoValues.add(currentMember.getAnnualIncomeEN());

                profileInfoKeys.add(this.getResources().getString(R.string.any_disease));
                profileInfoValues.add(currentMember.getIsDiseaseEN());

                profileInfoKeys.add(this.getResources().getString(R.string.disease_name));
                profileInfoValues.add(currentMember.getDiseaseName());

                // Step 4
                //Showing this field on step 4 only for males
                if(currentMember.getGenderId() == Constants.GENDER_MALE){
                    requiredInfoKeys.add(this.getResources().getString(R.string.bride_arrangement));
                    requiredInfoValues.add(currentMember.getSbrideArrangmentEN());
                }
                requiredInfoKeys.add(this.getResources().getString(R.string.gcc_check));
                requiredInfoValues.add( currentMember.getGCCMarriage() == true ? this.getResources().getString(R.string.yes) : this.getResources().getString(R.string.no));
                if (currentMember.getGCCMarriage() == false) {  //show gcc country infor if gcc marriage is false
                    requiredInfoKeys.add(this.getResources().getString(R.string.country));
                    requiredInfoValues.add(currentMember.getsCountryEN());
                }

                requiredInfoKeys.add( currentMember.getGenderId() == Constants.GENDER_MALE ? this.getResources().getString(R.string.male_social_status_check) : this.getResources().getString(R.string.female_social_status_check));
                requiredInfoValues.add( currentMember.getAcceptDMW() == true ? this.getResources().getString(R.string.yes) : this.getResources().getString(R.string.no));
                if (currentMember.getAcceptDMW() == false) {  //show social statue infor if AcceptDMW marriage is false
                    requiredInfoKeys.add(this.getResources().getString(R.string.social_status));
                    requiredInfoValues.add(currentMember.getsSocialStatusEN());
                }

                //If social status is not equal to single then ask or haschildren, numberOfChildren and reproductions
                if (currentMember.getsSocialStatusId() != Constants.SOCIAL_STATUS_SINGLE){
                    requiredInfoKeys.add(this.getResources().getString(R.string.has_children));
                    requiredInfoValues.add(currentMember.getsHasChildIdEN());
                    //If member has childs then ask for number of child else ask for reproduction
                    if (currentMember.getsHasChildId() == Constants.IS_SMOKING_YES){
                        requiredInfoKeys.add(this.getResources().getString(R.string.number_of_children));
                        requiredInfoValues.add(currentMember.getsNoOfChildrenEN());
                    }else{
                        requiredInfoKeys.add(this.getResources().getString(R.string.reproduction));
                        requiredInfoValues.add(currentMember.getsReproductionStatusEN());
                    }
                }

                requiredInfoKeys.add(this.getResources().getString(R.string.age));
                requiredInfoValues.add(currentMember.getsAgeEN());

                requiredInfoKeys.add(this.getResources().getString(R.string.height));
                requiredInfoValues.add(currentMember.getsHeightEN());

                requiredInfoKeys.add(this.getResources().getString(R.string.body_type));
                requiredInfoValues.add(currentMember.getsBodyTypeEN());

                requiredInfoKeys.add(this.getResources().getString(R.string.skin_color));
                requiredInfoValues.add(currentMember.getsSkinColorEN());

                requiredInfoKeys.add(this.getResources().getString(R.string.education_level));
                requiredInfoValues.add(currentMember.getsEducationLevelEN());

                requiredInfoKeys.add(this.getResources().getString(R.string.is_working));
                requiredInfoValues.add(currentMember.getsIsWorkingEN());

                //if a person is not working then hide its jobtype
                if ( currentMember.getsIsWorkingId() == Constants.IS_WORKING_WORKING ){
                    requiredInfoKeys.add(this.getResources().getString(R.string.job_type));
                    requiredInfoValues.add(currentMember.getsJobTypeEN());
                }
                requiredInfoKeys.add(this.getResources().getString(R.string.live_with_spouse));
                requiredInfoValues.add(currentMember.getSpouseStateToLiveEN());
            }
            //Showing this field on step 4 only for males
            if(currentMember.getGenderId() == Constants.GENDER_MALE ){
                requiredInfoKeys.add(this.getResources().getString(R.string.veil));
                requiredInfoValues.add(currentMember.getsCondemnBrideEN());
            }
            requiredInfoKeys.add(this.getResources().getString(R.string.has_driving_license));
            requiredInfoValues.add(currentMember.getsDrivingLicenseEN());

            requiredInfoKeys.add(this.getResources().getString(R.string.has_person_refer));
            if ((currentMember.getReferName() != null && currentMember.getReferName().length() > 0) && (currentMember.getReferMobile() != null && currentMember.getReferMobile().length() > 0 )) {
                requiredInfoValues.add(this.getResources().getString(R.string.yes));
                requiredInfoKeys.add(this.getResources().getString(R.string.name));
                requiredInfoValues.add(currentMember.getReferName());

                requiredInfoKeys.add(this.getResources().getString(R.string.phone_number));
                requiredInfoValues.add(currentMember.getReferMobile());
            }else{
                requiredInfoValues.add(this.getResources().getString(R.string.no));
            }

            //this information can only be seen by consultant of his own member and if current user is looking at his own profile
            if (!toShowLimitedProfile){
                //Showing this field on step 4 only for males
                if(currentMember.getGenderId() == Constants.GENDER_MALE ){
                    requiredInfoKeys.add(this.getResources().getString(R.string.will_pay_to_bride));
                    requiredInfoValues.add(currentMember.getWillPayToBrideEN());
                }
                // Step 5
                profileInfoKeys.add(this.getResources().getString(R.string.first_relative));
                profileInfoValues.add(currentMember.getFirstRelative());

                profileInfoKeys.add(this.getResources().getString(R.string.first_relative_number));
                profileInfoValues.add(currentMember.getFirstRelativeNumber());

                profileInfoKeys.add(this.getResources().getString(R.string.first_relative_relation));
                profileInfoValues.add(currentMember.getFirstRelativeRelationEN());

                profileInfoKeys.add(this.getResources().getString(R.string.second_relative));
                profileInfoValues.add(currentMember.getSecondRelative());

                profileInfoKeys.add(this.getResources().getString(R.string.second_relative_number));
                profileInfoValues.add(currentMember.getSecondRelativeNumber());

                profileInfoKeys.add(this.getResources().getString(R.string.second_relative_relation));
                profileInfoValues.add(currentMember.getSecondRelativeRelationEN());

                profileInfoKeys.add(this.getResources().getString(R.string.applicant_description));
                profileInfoValues.add(currentMember.getApplicantDescription());

                //Extra
                profileInfoKeys.add(this.getResources().getString(R.string.profile_status));
                profileInfoValues.add(currentMember.getPaymentStatusEN());
            }
        }
        setProfileData();
    }

    private void setProfileData(  ) {
        if (matchingsData == null)  {
            matchingsData = new ArrayList<>();
        }
        rvMemberDetail.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (infoType == Constants.INFO_TYPE_PROFILE) {
            memberDetailAdapter = new MemberDetailAdapter(getActivity(), profileInfoKeys,profileInfoValues, infoType);
        }else if(infoType == Constants.INFO_TYPE_REQUIRED) {
            memberDetailAdapter = new MemberDetailAdapter(getActivity(), requiredInfoKeys,requiredInfoValues, infoType);
        }
        rvMemberDetail.setAdapter(memberDetailAdapter);

        if (mTouchHelper != null){
            mTouchHelper.attachToRecyclerView(null); //Trying to detach
        }
    }


    private void initUIElements() {
        llSegmentControl = view.findViewById(R.id.llSegmentControl);
        lblProfile = view.findViewById(R.id.lblProfile);
        lblRequired = view.findViewById(R.id.lblRequired);
        rvMemberDetail = view.findViewById(R.id.rvMemberDetails);
        rvMemberMatching = view.findViewById(R.id.rvMemberDetails);
        swipeRefreshLayout = view.findViewById(R.id.swipe_view);


        //Tapping on segment control
        lblProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isSilentFetch = false;
                if(profileInfoKeys.size() > 0 || profileInfoValues.size() > 0){   //Data already exists, so fetch silently
                    isSilentFetch = true;
                }
                segmentSelectedIndexChanged(Constants.INFO_TYPE_PROFILE,isSilentFetch);
            }
        });
        lblRequired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isSilentFetch = false;
                if(requiredInfoKeys.size() > 0 || requiredInfoValues.size() > 0){   //Data already exists, so fetch silently
                    isSilentFetch = true;
                }
                segmentSelectedIndexChanged(Constants.INFO_TYPE_REQUIRED,isSilentFetch);
            }
        });
        dialog = AppUtility.createPleaseWaitDialog(getActivity());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

//    @Override
//    public void onPhoneIconClick(int position, User consultant) {
//
//    }

    //this method is called when user will click on recycle view item on member details ui
//    @Override
//    public void onItemClick(int position, User item) {
//        Intent intent = new Intent(getActivity(), MemberDetailActivity.class);
//        intent.putExtra(Constants.CURRENT_MEMBER, item);
//        intent.putExtra(Key.KEY_INFO_TYPE, Constants.INFO_TYPE_PROFILE);
//        startActivity(intent);
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        if (Constants.loggedInMember.getId() == currentMember.getId()){ //user is watching his own profile so dont display call button but edit button
            MenuItem menuItem = menu.findItem(R.id.action_call);
            menuItem.setVisible(false);
        }else{ //user is watching some other user profile so  display call button but dont display edit button
            MenuItem menuItem = menu.findItem(R.id.action_edit);
            menuItem.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_edit){
            Intent intent = new Intent(getActivity(), SignUpActivity1.class);
            startActivity(intent);
            return true;
        }else if(id == R.id.action_call){
            Utilities.phoneCallIntent(getActivity(),Constants.loggedInMember.getConsultantPhone());
        }

        return super.onOptionsItemSelected(item);
    }
}
