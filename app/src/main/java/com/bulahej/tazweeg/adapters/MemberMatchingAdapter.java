package com.bulahej.tazweeg.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.activities.MainActivity;
import com.bulahej.tazweeg.apis_responses.TazweegApi;
import com.bulahej.tazweeg.apis_responses.UserResponse.User;
import com.bulahej.tazweeg.apis_responses.UserResponse.UserResponse;
import com.bulahej.tazweeg.constant.Constants;
import com.bulahej.tazweeg.utilties.SwipeAndDragHelper;
import com.bulahej.tazweeg.utilties.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MemberMatchingAdapter extends RecyclerView.Adapter<MemberMatchingAdapter.CustomViewHolder> implements SwipeAndDragHelper.ActionCompletionContract{

    private Context mContext;
    private List<User> data;
    private List<User> tempData;
    private ArrayList<String> keys,values;
    private int infoType;
    private OnItemClickListener clickListener;
    private int sourcePostion, destinationPosition;

    public MemberMatchingAdapter(Context context, List<User> data, int infoType) {
        this.mContext = context;
        this.data = data;
        this.tempData = new ArrayList<User>(data);  //copying by value
        this.infoType = infoType;
    }

    public void setMatchingsData(List<User> data){
        this.data = data;
        this.tempData = new ArrayList<>(data);  //copying by value
        notifyDataSetChanged();
    }

    public void setItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choosing, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder customViewHolder, int i) {
        //Setting the text alignment
        if (Utilities.isRTL(mContext)){
            customViewHolder.lblTitle.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        }else{
            customViewHolder.lblTitle.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        }

        customViewHolder.lblSerialNumber.setText( String.valueOf(i + 1));

        if (infoType == Constants.INFO_TYPE_CHOOSING) {
            customViewHolder.lblTitle.setText(data.get(i).getUsername());
            customViewHolder.lblDetails.setText(data.get(i).getMobile());
        }else if (infoType == Constants.INFO_TYPE_PROFILE || infoType == Constants.INFO_TYPE_REQUIRED) {
            customViewHolder.lblTitle.setText(keys.get(i));
            customViewHolder.lblDetails.setText(values.get(i));
        }


    }

    @Override
    public int getItemCount() {
        if (infoType == Constants.INFO_TYPE_CHOOSING) {
            return data.size();
        }else if (infoType == Constants.INFO_TYPE_PROFILE || infoType == Constants.INFO_TYPE_REQUIRED) {
            if (keys.size() == values.size()){
                return keys.size();
            }else
                return 0;
        }else
            return 0;
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(data, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(data, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(MemberMatchingAdapter.CustomViewHolder myViewHolder) {
        sourcePostion = myViewHolder.getAdapterPosition();
        myViewHolder.lblTitle.setTextColor(Color.GRAY);
    }

    @Override
    public void onRowClear(MemberMatchingAdapter.CustomViewHolder myViewHolder) {
        myViewHolder.lblTitle.setTextColor(Color.BLACK);
        destinationPosition = myViewHolder.getAdapterPosition();
        Log.e("From = " + sourcePostion , "To = " + destinationPosition);
        if (destinationPosition >= 0 && sourcePostion != destinationPosition) { //destinationPosition contains -1 in-case of row removal
            int userId, memberId,priorityId,memberIdShift,priorityIdShift,userUpdateId;
            String deviceInfo,appVersion;
            userId = Utilities.getLoggedInMember(((MainActivity)mContext).getPreferences()).getId();
            memberId = tempData.get(sourcePostion).getId() ;
            priorityId = destinationPosition + 1;
            memberIdShift = tempData.get(destinationPosition).getId();
            priorityIdShift = sourcePostion + 1;
            userUpdateId = Constants.loggedInMember.getId();
            deviceInfo = Utilities.getDeviceName();
            appVersion = Utilities.getAppVersion(mContext);
            ((MainActivity)mContext).getDialog().show();  //Please wait dialog
            TazweegApi.getInstance().changeMatchingsOrder(userId
                    ,memberId
                    ,priorityId
                    ,memberIdShift
                    ,priorityIdShift
                    ,userUpdateId
                    ,deviceInfo
                    ,appVersion
            ).enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    ((MainActivity)mContext).getDialog().dismiss();
                    if (response.body() != null) {
                        int status = response.body().getStatus();
                        if (status == 1) {
                            Collections.swap(tempData, sourcePostion, destinationPosition);
                            setMatchingsData(tempData);
//                            ((MainActivity) mContext).setMatchingsData(new ArrayList<User>(tempData));   //updating activity data
                        }else {
                            Utilities.myToastMessage(mContext, ((MainActivity)mContext).getResources().getString(R.string.general_error));
                        }
                    }
                }
                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    ((MainActivity)mContext).getDialog().dismiss();
                    Utilities.myToastMessage(mContext, ((MainActivity)mContext).getResources().getString(R.string.failure) );
                }
            });
        }
    }

    @Override
    public void onRowSwiped(final int position) {
        int userId, memberId,userUpdateId;
        String deviceInfo,appVersion;
        //userId = ((MainActivity)mContext).getCurrentMember().getId();
        userId = Utilities.getLoggedInMember(((MainActivity)mContext).getPreferences()).getId();
        memberId = tempData.get(position).getId() ;
        userUpdateId = Constants.loggedInMember.getId();
        deviceInfo = Utilities.getDeviceName();
        appVersion = Utilities.getAppVersion(mContext);
        ((MainActivity)mContext).getDialog().show();  //Please wait dialog
        TazweegApi.getInstance().deleteMatching(userId
                ,memberId
                ,userUpdateId
                ,deviceInfo
                ,appVersion
        ).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                ((MainActivity)mContext).getDialog().dismiss();
                if (response.body() != null) {
                    int status = response.body().getStatus();
                    if (status == 1) {
                        tempData.remove(position);
                        data.remove(position);
//                        ((MainActivity) mContext).setMatchingsData(new ArrayList<User>(data));   //updating activity data
                        notifyDataSetChanged();     //to reset the serial number
                    }else {
                        Utilities.myToastMessage(mContext, mContext.getResources().getString(R.string.general_error));
                    }
                }
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                ((MainActivity)mContext).getDialog().dismiss();
                Utilities.myToastMessage(mContext, (mContext).getResources().getString(R.string.failure) );
            }
        });
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView lblSerialNumber,lblTitle, lblDetails;
        private CircleImageView imgPhone;

        CustomViewHolder(View view) {
            super(view);

            lblSerialNumber = view.findViewById(R.id.lblSerialNumber);
            lblTitle = view.findViewById(R.id.lblTitle);
            lblDetails = view.findViewById(R.id.lblDetails);
            imgPhone = view.findViewById(R.id.cImgCountry);

            if (infoType == Constants.INFO_TYPE_CHOOSING) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.onItemClick(getLayoutPosition(), data.get(getLayoutPosition()));
                    }
                });
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, User consultant);
    }
}