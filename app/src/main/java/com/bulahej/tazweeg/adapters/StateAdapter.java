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
import com.bulahej.tazweeg.apis_responses.list_emirates.Emirate;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.ViewHolder> {

    private Context context;
    private OnItemClickListener itemClickListener;
    private int selectedItemIndex = -1 ;
    private List<Emirate> data;
    private boolean isConsultantLogin = false;

    public StateAdapter(Context context, List<Emirate> data, Boolean isConsultantLogin) {
        this.context = context;
        this.data = data;
        this.isConsultantLogin = isConsultantLogin;
        if (!isConsultantLogin){
            selectedItemIndex = 0;
        }
    }


    public List<Integer> getSelectedStateIds() {
        List<Integer> selectedStateIds = new ArrayList<Integer>();
        return selectedStateIds;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_state, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        String imgURL = data.get(position).getImageURL();
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(context).load(imgURL).apply(options).into(viewHolder.ivImage);
        viewHolder.tvName.setText(data.get(position).getName(context));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivImage;
        private TextView tvName;

        private ViewHolder(View view) {
            super(view);
            ivImage = view.findViewById(R.id.imgEmirate);
            tvName = view.findViewById(R.id.tvCountryName);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                int previousSelectedIndex = selectedItemIndex;
                selectedItemIndex = getLayoutPosition();
                notifyItemChanged(previousSelectedIndex);//un highlighting previous selection
                notifyItemChanged(selectedItemIndex);
                itemClickListener.onStateItemClick(getLayoutPosition(), data.get(getLayoutPosition()));
            }
        }
    }

    public interface OnItemClickListener {
        void onStateItemClick(int position, Emirate emirateData);
    }

}