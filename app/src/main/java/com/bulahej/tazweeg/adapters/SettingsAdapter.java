package com.bulahej.tazweeg.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.activities.WebViewActivity;
import com.bulahej.tazweeg.constant.Constants;
import com.bulahej.tazweeg.constant.Key;
import com.bulahej.tazweeg.utilties.Utilities;

import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bulahej.tazweeg.constant.Constants.loggedInMember;


public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.CustomViewHolder> {

    private Context mContext;
    private ArrayList<String> titlesArray;
    private ArrayList<Integer> imagesArray;
    private OnItemClickListener clickListener;

    public SettingsAdapter(Context context, ArrayList<String> titlesArray, ArrayList<Integer> imagesArray) {
        this.mContext = context;
        this.titlesArray = titlesArray;
        this.imagesArray = imagesArray;
    }

    public void setItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_settings, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder customViewHolder, int i) {

        //Setting the text alignment
        if (Utilities.isRTL(mContext)){
            customViewHolder.imgRightArrow.setImageResource(R.drawable.left_arrow);
        }else{
        }

        if ( i == 10 ) {    //hiding settings for contact US
            customViewHolder.llTop.setVisibility(View.GONE);
            customViewHolder.llContactUs.setVisibility(View.VISIBLE);
        }else{
            customViewHolder.llTop.setVisibility(View.VISIBLE);
            customViewHolder.llContactUs.setVisibility(View.GONE);

            if (titlesArray.get(i).length() > 0) {
                if (i == 8 ){   //hiding right arrow for sign out
                    customViewHolder.imgRightArrow.setVisibility(View.GONE);
                }else{
                    customViewHolder.imgRightArrow.setVisibility(View.VISIBLE);
                }

                customViewHolder.lblTitle.setText(titlesArray.get(i));
                if (imagesArray.get(i) != null) {
                    customViewHolder.imgView.setImageResource(imagesArray.get(i));
                }else{
                    customViewHolder.imgView.setVisibility(View.GONE);  //hiding for address and phone
                    customViewHolder.imgRightArrow.setVisibility(View.GONE);
                }
            }else{
                customViewHolder.lblTitle.setVisibility(View.GONE);
                customViewHolder.imgView.setVisibility(View.GONE);
                customViewHolder.imgRightArrow.setVisibility(View.GONE);
                customViewHolder.itemView.setBackgroundColor( mContext.getResources().getColor(R.color.colorLightGrey)) ;
            }
        }

    }

    @Override
    public int getItemCount() {
        return titlesArray.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView lblTitle;
        private CircleImageView imgView;
        private View viewGrayLine;
        private ImageView imgRightArrow;
        private ImageView imgFacebook, imgInstagram, imgTwitter, imgTelegram,imgPinterest,imgEmail;
        private LinearLayout llTop;
        private LinearLayout llContactUs;
        CustomViewHolder(View view) {
            super(view);

            llTop = view.findViewById(R.id.llTop);
            lblTitle = view.findViewById(R.id.lblTitle);
            imgView = view.findViewById(R.id.imgView);
            imgRightArrow = view.findViewById(R.id.imgRightArrow);
            viewGrayLine = view.findViewById(R.id.viewGrayLine);

            llContactUs = view.findViewById(R.id.llContactUs);
            imgFacebook = view.findViewById(R.id.imgFacebook);
            imgInstagram = view.findViewById(R.id.imgInstagram);
            imgTwitter = view.findViewById(R.id.imgTwitter);
            imgTelegram = view.findViewById(R.id.imgTelegram);
            imgPinterest = view.findViewById(R.id.imgPinterest);
            imgEmail = view.findViewById(R.id.imgEmail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getLayoutPosition());
                }
            });

            imgFacebook.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra(Key.KEY_URL, Constants.PAGE_TYPE_FB);
                    mContext.startActivity(intent);
                }
            });

            imgInstagram.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra(Key.KEY_URL, Constants.PAGE_TYPE_INSTA);
                    mContext.startActivity(intent);
                }
            });

            imgTwitter.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra(Key.KEY_URL, Constants.PAGE_TYPE_TWITTER);
                    mContext.startActivity(intent);
                }
            });

            imgTelegram.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra(Key.KEY_URL, Constants.PAGE_TYPE_TELEGRAM);
                    mContext.startActivity(intent);
                }
            });

            imgPinterest.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra(Key.KEY_URL, Constants.PAGE_TYPE_PINTEREST);
                    mContext.startActivity(intent);
                }
            });

            imgEmail.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    sendEmail();
                }
            });
        }
    }

    private  void sendEmail(){
        String strEmailBody = "\n\n\n" + mContext.getResources().getString(R.string.user_information);
        strEmailBody += "\n" + mContext.getResources().getString(R.string.user_name) +" : " + loggedInMember.getUsername();
        strEmailBody += "\n" + mContext.getResources().getString(R.string.user_phone_number) + loggedInMember.getMobile();
        strEmailBody += "\n\n" + mContext.getResources().getString(R.string.app_information) + "\n" + Utilities.getAppVersion(mContext);
        strEmailBody += "\n\n" + mContext.getResources().getString(R.string.device_information);
        strEmailBody += "\n" + mContext.getResources().getString(R.string.device_model)+ Utilities.getDeviceName();
        strEmailBody += "\n" + mContext.getResources().getString(R.string.android_version) + android.os.Build.VERSION.RELEASE;
        strEmailBody += "\n" + mContext.getResources().getString(R.string.device_language) + Resources.getSystem().getConfiguration().locale.getLanguage();
        strEmailBody += "\n" + mContext.getResources().getString(R.string.app_language) + Locale.getDefault().getDisplayLanguage();
        TelephonyManager manager = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String carrierName = manager.getNetworkOperatorName();
        strEmailBody += "\n" + mContext.getResources().getString(R.string.carrier) + carrierName;
        strEmailBody += "\n" + mContext.getResources().getString(R.string.timezone) + TimeZone.getDefault().getDisplayName();
        strEmailBody += "\n" + mContext.getResources().getString(R.string.connection_status) + Utilities.getNetworkClass(mContext);

        Utilities.sendEmailIntent(((Activity)mContext),mContext.getResources().getString(R.string.emailClientTitle),mContext.getResources().getString(R.string.tazweeg_email),mContext.getResources().getString(R.string.emailSubject) , strEmailBody);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}