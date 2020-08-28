package com.bulahej.tazweeg.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bulahej.tazweeg.R;
import com.bulahej.tazweeg.utilties.Utilities;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChangeLanguageAdapter extends RecyclerView.Adapter<ChangeLanguageAdapter.CustomViewHolder> {

    private Context mContext;
    private ArrayList<String> titlesArray;
    private ArrayList<Integer> imagesArray;
    private OnItemClickListener clickListener;

    public ChangeLanguageAdapter(Context context, ArrayList<String> titlesArray, ArrayList<Integer> imagesArray) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_change_language, parent, false);
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
        customViewHolder.lblTitle.setText(titlesArray.get(i));
        customViewHolder.imgViewLeft.setImageResource(imagesArray.get(i));
        //Highlighting the selcted language
        if (i == 0 && !Utilities.isRTL(mContext)){
            customViewHolder.imgViewRight.setImageResource(R.drawable.tick_icon);
        }else if (i == 1 && Utilities.isRTL(mContext)){
            customViewHolder.imgViewRight.setImageResource(R.drawable.tick_icon);
        }else{
            customViewHolder.imgViewRight.setImageResource(android.R.color.transparent);
        }
    }

    @Override
    public int getItemCount() {
        return titlesArray.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView lblTitle;
        private CircleImageView imgViewLeft;
        private ImageView imgViewRight;

        CustomViewHolder(View view) {
            super(view);

            lblTitle = view.findViewById(R.id.lblTitle);
            imgViewLeft = view.findViewById(R.id.imgViewLeft);
            imgViewRight = view.findViewById(R.id.imgViewRight);
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