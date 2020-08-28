package com.bulahej.tazweeg.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.utilties.Utilities;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ContactUsAdapter extends RecyclerView.Adapter<ContactUsAdapter.CustomViewHolder> {

    private Context mContext;
    private ArrayList<String> titlesArray;
    private ArrayList<Integer> imagesArray;
    private OnItemClickListener clickListener;

    public ContactUsAdapter(Context context, ArrayList<String> titlesArray, ArrayList<Integer> imagesArray) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_us, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder customViewHolder, int i) {
        if (Utilities.isRTL(mContext)){
            customViewHolder.lblTitle.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        }else{
            customViewHolder.lblTitle.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        }
        customViewHolder.lblTitle.setText(titlesArray.get(i));
        customViewHolder.imgView.setImageResource(imagesArray.get(i));
    }

    @Override
    public int getItemCount() {
        return titlesArray.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView lblTitle;
        private CircleImageView imgView;

        CustomViewHolder(View view) {
            super(view);

            lblTitle = view.findViewById(R.id.lblTitle);
            imgView = view.findViewById(R.id.imgView);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getLayoutPosition());
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}