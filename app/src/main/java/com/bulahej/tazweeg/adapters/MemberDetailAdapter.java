package com.bulahej.tazweeg.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.apis_responses.UserResponse.User;

import java.util.ArrayList;
import java.util.List;


public class MemberDetailAdapter extends RecyclerView.Adapter<MemberDetailAdapter.CustomViewHolder> {

    private Context mContext;
    private List<User> data;
    private ArrayList<String> keys,values;
    private int infoType;

//    public MemberDetailAdapter(Context context, List<User> data, int infoType) {
//        this.mContext = context;
//        this.data = data;
////        this.infoType = infoType;
//    }

    public MemberDetailAdapter(Context context, ArrayList<String> keys, ArrayList<String> values, int infoType) {
//    public MemberDetailAdapter(Context context, ArrayList<String> keys, ArrayList<String> values) {
        this.mContext = context;
        this.keys = keys;
        this.values = values;
        this.infoType = infoType;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_detail, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder customViewHolder, int i) {
        //Setting the text alignment
//        if (Utilities.isRTL(mContext)){
//            customViewHolder.lblTitle.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
//        }else{
//            customViewHolder.lblTitle.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
//        }
//        if (infoType == Constants.INFO_TYPE_CHOOSING) {
//            customViewHolder.lblTitle.setText(data.get(i).getUsername());
//            customViewHolder.lblDetails.setText(data.get(i).getMobile());
//        }else
//            if (infoType == Constants.INFO_TYPE_PROFILE || infoType == Constants.INFO_TYPE_REQUIRED) {
            customViewHolder.lblTitle.setText(keys.get(i));
            customViewHolder.lblDetails.setText(values.get(i));
//        }
        Log.i( customViewHolder.lblTitle.getText().toString() , mContext.getResources().getString(R.string.has_person_refer));
        if (customViewHolder.lblTitle.getText().toString().equals(mContext.getResources().getString(R.string.has_person_refer))){
            customViewHolder.lblTitle.setTextColor(mContext.getResources().getColor(R.color.colorRed));
            customViewHolder.lblDetails.setTextColor(mContext.getResources().getColor(R.color.colorRed));
        }else{
            customViewHolder.lblTitle.setTextColor(mContext.getResources().getColor(R.color.colorBlack));
            customViewHolder.lblDetails.setTextColor(mContext.getResources().getColor(R.color.colorBlack));
        }
    }

    @Override
    public int getItemCount() {
//        if (infoType == Constants.INFO_TYPE_CHOOSING) {
//            return data.size();
//        }else if (infoType == Constants.INFO_TYPE_PROFILE || infoType == Constants.INFO_TYPE_REQUIRED) {
            if (keys.size() == values.size()){
                return keys.size();
            }else
                return 0;
//        }else
//            return 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView lblTitle, lblDetails;

        CustomViewHolder(View view) {
            super(view);
            lblTitle = view.findViewById(R.id.lblTitle);
            lblDetails = view.findViewById(R.id.lblDetails);
        }
    }
}