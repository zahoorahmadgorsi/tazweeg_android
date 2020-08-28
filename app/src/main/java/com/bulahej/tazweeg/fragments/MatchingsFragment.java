package com.bulahej.tazweeg.fragments;

import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Log;
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
import com.bulahej.tazweeg.activities.ProfileActivity;
import com.bulahej.tazweeg.activities.SignUpActivity1;
import com.bulahej.tazweeg.activities.SignUpActivity2;
import com.bulahej.tazweeg.activities.SignUpActivity3;
import com.bulahej.tazweeg.activities.SignUpActivity4;
import com.bulahej.tazweeg.activities.SignUpActivity5;
import com.bulahej.tazweeg.activities.WebViewActivity;
import com.bulahej.tazweeg.adapters.MemberMatchingAdapter;
import com.bulahej.tazweeg.apis_responses.TazweegApi;
import com.bulahej.tazweeg.apis_responses.UserResponse.User;
import com.bulahej.tazweeg.apis_responses.UserResponse.UserResponse;
import com.bulahej.tazweeg.constant.Constants;
import com.bulahej.tazweeg.constant.Key;
import com.bulahej.tazweeg.utilties.AppUtility;
import com.bulahej.tazweeg.utilties.SwipeAndDragHelper;
import com.bulahej.tazweeg.utilties.Utilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.bulahej.tazweeg.constant.Constants.INFO_TYPE_CHOOSING;
import static com.bulahej.tazweeg.constant.Constants.PROFILE_STATUS_FINISHED;
import static com.bulahej.tazweeg.constant.Constants.PROFILE_STATUS_MATCHING;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatchingsFragment extends Fragment implements MemberMatchingAdapter.OnItemClickListener{
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
    private TextView lblChoosing, lblMatching,lblMarried;
    private int infoType;
    private ArrayList<User> matchingsData = new ArrayList<>();
//    private RecyclerView rvMemberDetail,rvMemberMatching;
//    private MemberDetailAdapter memberDetailAdapter;
private RecyclerView rvMemberMatching;
    private MemberMatchingAdapter memberMatchingAdapter;
    private Resources resources;

    private SwipeRefreshLayout swipeRefreshLayout ;
    private ItemTouchHelper.Callback swipeAndDragHelper;
    View view;

    public MatchingsFragment() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_choosing, container, false);
        initUIElements();
        resources = getActivity().getResources();
        preferences = getActivity().getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        currentMember = Utilities.getLoggedInMember(preferences);

        if (savedInstanceState == null) {
            Bundle extras = getArguments();
            if(extras != null) { //User is coming to sign up
                infoType = (int) extras.get(Key.KEY_INFO_TYPE);
                segmentSelectedIndexChanged(infoType,false);
                //If user want to see his own profile OR consultant want to see his own members profile OR logged in user is an Admin
                if (    (Constants.loggedInMember.getId() == currentMember.getId()) ||
                        Constants.loggedInMember.getId() == currentMember.getConsultantId() ||
                        Constants.loggedInMember.getTypeId() == Constants.USER_TYPE_ADMIN
                ){
                    llSegmentControl.setVisibility(View.VISIBLE);

                }else{
                    llSegmentControl.setVisibility(View.GONE);
                }
            }
        }

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
            case INFO_TYPE_CHOOSING:
                //If logged in user is an admin, consultant or current logged in member has paid the fee
                if ( Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_CONSULTANT  ||
                        Utilities.getSelectedUserType(preferences) == Constants.USER_TYPE_ADMIN  ||
                        Constants.loggedInMember.getPaymentStatusId() == Constants.PAYMENT_TYPE_CHOOSING ){  //paid the fee(profile completed)

                    infoType = selectedIndex; //infotype is class variable

                    lblChoosing.setBackgroundColor(getResources().getColor( R.color.colorAccent));
                    lblChoosing.setTextColor(getResources().getColor( R.color.colorWhite));

                    lblMatching.setBackgroundColor(getResources().getColor( R.color.colorLightGrey));
                    lblMatching.setTextColor(getResources().getColor( R.color.colorBlack));

                    lblMarried.setBackgroundColor(getResources().getColor( R.color.colorLightGrey));
                    lblMarried.setTextColor(getResources().getColor( R.color.colorBlack));
                    if(matchingsData.size() > 0) {//if data is already fetched then just display fetched data and send a silent request to fetch fresh data
                        setMatchingsData(matchingsData);   //set cached data
                    }
                    getChoosings(isSilentFetch); //don't fetch silently
                }else {    //profile is incomplete
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle(resources.getString(R.string.confirm));
                    alertDialog.setMessage(resources.getString(R.string.incomplete_profile));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss(); //<-- change it with ur code
                                    completeProfileTapped();
                                }
                            } );
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.no),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            } );
                    alertDialog.show();
                }
                break;
            case Constants.INFO_TYPE_MATCHING:
                infoType = selectedIndex; //setting info type to 0th tab, so that when user will press back from payment he should see 0th tab

                lblChoosing.setBackgroundColor(getResources().getColor( R.color.colorLightGrey));
                lblChoosing.setTextColor(getResources().getColor( R.color.colorBlack));

                lblMatching.setBackgroundColor(getResources().getColor( R.color.colorAccent));
                lblMatching.setTextColor(getResources().getColor( R.color.colorWhite));

                lblMarried.setBackgroundColor(getResources().getColor( R.color.colorLightGrey));
                lblMarried.setTextColor(getResources().getColor( R.color.colorBlack));
                if(matchingsData.size() > 0) {//if data is already fetched then just display fetched data and send a silent request to fetch fresh data
                    setMatchingsData(matchingsData);   //set cached data
                }
                getChoosings(isSilentFetch); //don't fetch silently
                break;
            case Constants.INFO_TYPE_MARRIED:
                infoType = selectedIndex; //setting info type to 0th tab, so that when user will press back from payment he should see 0th tab

                lblChoosing.setBackgroundColor(getResources().getColor( R.color.colorLightGrey));
                lblChoosing.setTextColor(getResources().getColor( R.color.colorBlack));

                lblMatching.setBackgroundColor(getResources().getColor( R.color.colorLightGrey));
                lblMatching.setTextColor(getResources().getColor( R.color.colorBlack));

                lblMarried.setBackgroundColor(getResources().getColor( R.color.colorAccent));
                lblMarried.setTextColor(getResources().getColor( R.color.colorWhite));

                if(matchingsData.size() > 0) {//if data is already fetched then just display fetched data and send a silent request to fetch fresh data
                    setMatchingsData(matchingsData);   //set cached data
                }
                getChoosings(isSilentFetch); //don't fetch silently
                break;
        }
    }

    private void completeProfileTapped(){
        Log.i("completeProfileTapped", "completeProfileTapped onTouch");
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
//                intent = new Intent(getActivity(), MemberDetailActivity.class);
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

    private void getChoosings(Boolean isSilentFetch){
        if (!isSilentFetch) {
            dialog.show();
        }
        TazweegApi.getInstance().getMatchingsByMemberID(currentMember.getId()).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    int status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status == 1) {
                        matchingsData = response.body().getUsers();
                        setMatchingsData(matchingsData);
                    } else {
                        Utilities.myToastMessage(getActivity(), message);

                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                dialog.dismiss();
                Utilities.myToastMessage(getActivity(), getString(R.string.failure));
            }
        });
    }

    private ArrayList<User> getFilteredData(List<User> users, int profileStatus){
        ArrayList filteredList = new ArrayList<>();
        for (User user : users) {
            Log.i("PaymentStatusId" , Integer.toString(user.getPaymentStatusId()) );
            if (user.getPaymentStatusId() == profileStatus) {
                filteredList.add(user);
            }
        }
        return filteredList;
    }

    private void setMatchingsData(List<User> matchingsData) {
        if (matchingsData == null)  {
            matchingsData = new ArrayList<>();
        }
        rvMemberMatching.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (memberMatchingAdapter == null) {
            memberMatchingAdapter = new MemberMatchingAdapter(getActivity(), matchingsData, infoType);
            memberMatchingAdapter.setItemClickListener(this);
        }
        ArrayList<User> filteredList = new ArrayList<User>();
        if(infoType == INFO_TYPE_CHOOSING){
            //filteredList = getFilteredData(matchingsData,PROFILE_STATUS_PAID);
            filteredList = getFilteredData(matchingsData,0);    //right now WebAPI is not setting payment status
        }else if(infoType == INFO_TYPE_CHOOSING){
            filteredList = getFilteredData(matchingsData,PROFILE_STATUS_MATCHING);
        }else if(infoType == INFO_TYPE_CHOOSING){
            filteredList = getFilteredData(matchingsData,PROFILE_STATUS_FINISHED);
        }

        memberMatchingAdapter.setMatchingsData(filteredList);
        rvMemberMatching.setAdapter(memberMatchingAdapter);

//        to call these appropriate callbacks (onViewMoved,onViewSwiped) from the ItemTouchHelper.Callback class, we will pass the adapter to the class:
        if ( swipeAndDragHelper == null){
            swipeAndDragHelper = new SwipeAndDragHelper(memberMatchingAdapter);  //SwipeAndDragHelper is extendsing ItemTouchHelper.Callback
        }
        if (mTouchHelper == null){
            mTouchHelper = new ItemTouchHelper(swipeAndDragHelper);
        }
        mTouchHelper.attachToRecyclerView(rvMemberMatching); //to integrate this ItemTouchHelper with our RecyclerView, we call attachToRecyclerView() method
    }

    private void initUIElements() {
        llSegmentControl = view.findViewById(R.id.llSegmentControl);
        lblChoosing = view.findViewById(R.id.lblChoosing);
        lblMatching = view.findViewById(R.id.lblMatching);
        lblMarried = view.findViewById(R.id.lblMarried);
        rvMemberMatching = view.findViewById(R.id.rvMemberDetails);
        swipeRefreshLayout = view.findViewById(R.id.swipe_view);

        //Tapping on segment control
        lblChoosing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isSilentFetch = false;
                if(matchingsData.size() > 0 ){   //Data already exists, so fetch silently
                    isSilentFetch = true;
                }
                segmentSelectedIndexChanged(INFO_TYPE_CHOOSING,isSilentFetch);
            }
        });
        lblMatching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                segmentSelectedIndexChanged(Constants.INFO_TYPE_MATCHING,false);
            }
        });
        lblMarried.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                segmentSelectedIndexChanged(Constants.INFO_TYPE_MARRIED,false);

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
    @Override
    public void onItemClick(int position, User item) {
        //Intent intent = new Intent(getActivity(), MemberDetailActivity.class);
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra(Constants.CURRENT_MEMBER, item);
        intent.putExtra(Key.KEY_INFO_TYPE, Constants.INFO_TYPE_PROFILE);
        startActivity(intent);
    }

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
        inflater.inflate(R.menu.choosing_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_call){
            Utilities.phoneCallIntent(getActivity(),currentMember.getConsultantPhone());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
