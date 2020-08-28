package com.bulahej.tazweeg.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.activities.ChangeLanguageActivity;
import com.bulahej.tazweeg.activities.ChangePasswordActivity;
import com.bulahej.tazweeg.activities.UserSelectionActivity;
import com.bulahej.tazweeg.activities.WebViewActivity;
import com.bulahej.tazweeg.adapters.SettingsAdapter;
import com.bulahej.tazweeg.constant.Constants;
import com.bulahej.tazweeg.constant.Key;
import com.bulahej.tazweeg.utilties.Utilities;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements SettingsAdapter.OnItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private ArrayList<String> nameArray = new ArrayList<>();
    private ArrayList<Integer> imageArray = new ArrayList<>();
    private Resources resources;
    private RecyclerView rvSettings;
    private SettingsAdapter settingsAdapter;
    private TextView lblVersionCode;
    private ImageView imgContactUs;
    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        resources = this.getResources();
        initUIElements(view);
        lblVersionCode.setText(Utilities.getAppVersion(getActivity()));
        //populating title array
        nameArray.add("");
        nameArray.add(resources.getString(R.string.change_password));
        nameArray.add(resources.getString(R.string.change_language));
        nameArray.add("");
        nameArray.add(resources.getString(R.string.terms_and_condition));
        nameArray.add(resources.getString(R.string.privacy_policy));
        nameArray.add(resources.getString(R.string.faqs));
        nameArray.add("");
        nameArray.add(resources.getString(R.string.sign_out));
        nameArray.add("");
        nameArray.add(resources.getString(R.string.contact_us));
        nameArray.add("");
        nameArray.add(resources.getString(R.string.tazweeg_address));
        nameArray.add(resources.getString(R.string.tazweeg_phone_number));
        //populating images array
        imageArray.add(null);
        imageArray.add(R.drawable.change_password);
        imageArray.add(R.drawable.change_language);
        imageArray.add(null);
        imageArray.add(R.drawable.terms_and_conditions);
        imageArray.add(R.drawable.privacy_policy);
        imageArray.add(R.drawable.faqs);
        imageArray.add(null);
        imageArray.add(R.drawable.sign_out);
        imageArray.add(null);
        imageArray.add(R.drawable.contact_us);
        imageArray.add(null);
        imageArray.add(null);
        imageArray.add(null);
        setSettingsData();   //set cached data
        return view;

    }


    private void initUIElements(View view) {
        rvSettings = view.findViewById(R.id.rvSettingsDetails);
        lblVersionCode =  view.findViewById(R.id.lblVersionBuild);
    }

    private void setSettingsData(  ) {
        rvSettings.setLayoutManager(new LinearLayoutManager(getActivity()));
        settingsAdapter = new SettingsAdapter(getActivity(), nameArray,imageArray);
        settingsAdapter.setItemClickListener(this);
        rvSettings.setAdapter(settingsAdapter);
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
        }else{
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(int position) {
        if (position == 0){
        }else if (position == 1){
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(intent);
        }else if (position == 2){
            Intent intent = new Intent(getActivity(), ChangeLanguageActivity.class);
            startActivity(intent);
        }else if (position == 3){
        } else if (position == 4){
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra(Key.KEY_URL, Constants.PAGE_TYPE_TERMS);
            startActivity(intent);
        }else if (position == 5){
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra(Key.KEY_URL, Constants.PAGE_TYPE_PRIVACY_POLICY);
            startActivity(intent);
        }else if (position == 6){
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra(Key.KEY_URL, Constants.PAGE_TYPE_FAQ);
            startActivity(intent);
        }else if(position == 7){
        }else if (position == 8){   //Sign out
            ShowSignoutAlert();
        }else if(position == 9){    //Empty
        }else if(position == 10){   //Contact Us
        }else if(position == 11){   //Empty
        }else if(position == 12){   //Address
        }else if(position == 13){   //Phone
            Utilities.phoneCallIntent(getActivity(),resources.getString(R.string.tazweeg_phone_number));
        }
    }

        public void ShowSignoutAlert(){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(resources.getString(R.string.sign_out));
        alertDialog.setMessage(resources.getString(R.string.areYouSure));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //<-- change it with ur code
                        getActivity().getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE).edit().clear().apply();
                        //finishAffinity();
                        Intent intent = new Intent(getActivity(), UserSelectionActivity.class);
                        //If set, and the activity being launched is already running in the current task, then instead of launching a new instance of that activity, all of the other activities on top of it will be closed and this
                        // Intent will be delivered to the (now on top) old activity as a new Intent.
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
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
