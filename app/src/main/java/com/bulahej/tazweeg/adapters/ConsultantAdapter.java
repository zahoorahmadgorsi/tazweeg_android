package com.bulahej.tazweeg.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.apis_responses.UserResponse.User;
import com.bulahej.tazweeg.utilties.Utilities;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ConsultantAdapter extends RecyclerView.Adapter<ConsultantAdapter.CustomViewHolder> {

    private Context mContext;
    private List<User> data;

    private OnConsultItemClickListener clickListener;

    public ConsultantAdapter(Context context, List<User> data) {
        this.mContext = context;
        this.data = data;
    }

    public void setOnConsulItemClickListener(OnConsultItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consultant, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder customViewHolder, int i) {
        String imgURL = data.get(i).getImagePath();
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(mContext).load(imgURL).apply(options).into(customViewHolder.ciImage);
        //Setting the text alignment
        if (Utilities.isRTL(mContext)){
            customViewHolder.tvName.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        }else{
            customViewHolder.tvName.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        }
        customViewHolder.tvName.setText(data.get(i).getname());
        customViewHolder.tvPhoneNumber.setText(data.get(i).getMobile());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView ciImage;
        private TextView tvName, tvPhoneNumber;

        CustomViewHolder(View view) {
            super(view);

            ciImage = view.findViewById(R.id.cImgCountry);
            tvName = view.findViewById(R.id.tvCountryName);
            tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
//            tvService = view.findViewById(R.id.tvService);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onConsultItemClick(getLayoutPosition(), data.get(getLayoutPosition()));
                }
            });
        }
    }

    public interface OnConsultItemClickListener {
        void onConsultItemClick(int position, User consultant);
    }
}