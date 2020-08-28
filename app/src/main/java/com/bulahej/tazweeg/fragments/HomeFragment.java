package com.bulahej.tazweeg.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.bulahej.tazweeg.constant.Constants;
import com.bulahej.tazweeg.constant.Key;
import com.bulahej.tazweeg.utilties.Utilities;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    //Fragments variable
    private LinearLayout llYourProfile,llMatchCount;
    private ImageView imgPercentComplete,imgRightArrow,imgWhatsApp;
    private TextView lblPercentComplete,lblMemberName,lblMemberCode,lblProfileStatus,lblConsultantName,lblConsultantNumber,lblConsultantStates,lblMatchCount;
    SharedPreferences preferences;
    private int profilePercentComplete = 0;
    private Resources resources;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
//                context = this;
        initUIElements(view);
        setMemberValues();
        return  view;
    }

    private void initUIElements(View view) {
        llYourProfile = view.findViewById(R.id.llYourProfile);
        imgPercentComplete = view.findViewById(R.id.imgPercentComplete);
        lblPercentComplete = view.findViewById(R.id.lblPercentComplete);
        imgRightArrow = view.findViewById(R.id.imgRightArrow_profile);
        imgWhatsApp = view.findViewById(R.id.imgWhatsApp);

        lblMemberName = view.findViewById(R.id.lblMemberName);
        lblMemberCode = view.findViewById(R.id.lblMemberCode);
        lblProfileStatus = view.findViewById(R.id.lblProfileStatus);
        lblConsultantName = view.findViewById(R.id.lblConsultantName);
        lblConsultantNumber = view.findViewById(R.id.lblConsultantNumber);
        lblConsultantStates = view.findViewById(R.id.lblConsultantStates);
        lblMatchCount = view.findViewById(R.id.lblMatchCount);
        llMatchCount = view.findViewById(R.id.llMatchCount);
        preferences = getActivity().getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        resources = getActivity().getResources();

        llYourProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeProfileTapped();
            }
        });
        llMatchCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgMatchingCountTapped();
            }
        });

        imgWhatsApp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String url = "https://api.whatsapp.com/send?phone=" + lblConsultantNumber.getText();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return false;
            }
        });

        if (Utilities.isRTL(getActivity()) ) {
            imgRightArrow.setImageResource(R.drawable.left_arrow);
        }else{
            imgRightArrow.setImageResource(R.drawable.right_arrow);
        }
    }

    private void setMemberValues(){
        if (Constants.loggedInMember == null)
            Constants.loggedInMember = Utilities.getLoggedInMember(getActivity().getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE));

        int mobileStatus = Constants.loggedInMember.getMobileStatus();
        profilePercentComplete = mobileStatus * 20;
        switch (mobileStatus){
            case 0:
                imgPercentComplete.setImageResource(R.drawable.zero_percent);
                break;
            case 1:
                imgPercentComplete.setImageResource(R.drawable.twenty_percent);
                break;
            case 2:
                imgPercentComplete.setImageResource(R.drawable.forty_percent);
                break;
            case 3:
                imgPercentComplete.setImageResource(R.drawable.sixty_percent);
                break;
            case 4:
                imgPercentComplete.setImageResource(R.drawable.eighty_percent);
                break;
            case 5:
                profilePercentComplete = 90;
                imgPercentComplete.setImageResource(R.drawable.ninety_percent);
                break;
            case 6:
                profilePercentComplete = 100;
                imgPercentComplete.setImageResource(R.drawable.hundred_percent);
                break;
            default:
                imgPercentComplete.setImageResource(R.drawable.zero_percent);
                break;
        }
        //By passing all steps logic if user has paid.... because paid mean all 5 steps are completed.
        if(Constants.loggedInMember.getPaymentStatusId() == Constants.PAYMENT_TYPE_CHOOSING)
        {
            imgPercentComplete.setImageResource(R.drawable.hundred_percent);
            profilePercentComplete = 100;
        }

        //Loading labels
        lblPercentComplete.setText(profilePercentComplete + resources.getString(R.string.percentage_complete) );
        lblMemberName.setText(Constants.loggedInMember.getname());
        lblMemberCode.setText( Constants.loggedInMember.getUsername());
        if (Utilities.isRTL(getActivity())) {
            lblProfileStatus.setText( Constants.loggedInMember.getPaymentStatusAR());
        }
        else{
            lblProfileStatus.setText(Constants.loggedInMember.getPaymentStatusEN());
        }
        lblConsultantName.setText( Constants.loggedInMember.getConsultantName());
        lblConsultantNumber.setText( Constants.loggedInMember.getConsultantPhone());
        lblConsultantStates.setText( Constants.loggedInMember.getStateServeIn());
        lblMatchCount.setText( String.valueOf(Constants.loggedInMember.getMatchCount()));
    }


    private void imgMatchingCountTapped(){
        if (Constants.loggedInMember.getPaymentStatusId() == Constants.PAYMENT_TYPE_CHOOSING){  //paid the fee(profile completed)
            ((MainActivity)getActivity()).mBottomNavigationView.setSelectedItemId(R.id.action_choosing);
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

        Intent intent; //default
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
                break;
            default:
                intent = new Intent(getActivity(), SignUpActivity1.class);
                startActivity(intent);
                break;
        }
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
}
