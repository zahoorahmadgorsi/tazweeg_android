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

public class EmiratesAdapter extends RecyclerView.Adapter<EmiratesAdapter.ViewHolder> {

    private Context context;
    private OnItemClickListener itemClickListener;
    private int selectedItemIndex = -1 ;
    private List<Integer> selectedStateIndicies = new ArrayList<Integer>();
    private List<Emirate> data;
    private boolean isConsultantLogin = false;

    public EmiratesAdapter(Context context, List<Emirate> data, Boolean isConsultantLogin) {
        this.context = context;
        this.data = data;
        this.isConsultantLogin = isConsultantLogin;
        if (!isConsultantLogin){
            selectedItemIndex = 0;
            selectedStateIndicies.add(selectedItemIndex); //adding it for member
        }
    }


    public List<Integer> getSelectedStateIds() {
        List<Integer> selectedStateIds = new ArrayList<Integer>();
        for(int i : selectedStateIndicies)
        {
            selectedStateIds.add(this.data.get(i).getStateId());
        }
        return selectedStateIds;
    }
    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_emirate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        if (selectedStateIndicies.contains(position)){
            viewHolder.vLine.setVisibility(View.VISIBLE);
        }else{
            viewHolder.vLine.setVisibility(View.INVISIBLE);
        }
        String imgURL = data.get(position).getImageURL();
//        String imgURL = "https://54.196.95.55/assets/states/abudhabi.png";
//        String imgURL = "https://www.tazweeg.ae/assets/states/abudhabi.png";
//        String imgURL = "https://www.tazweeg.ae//assets/gender/female.png";   //loading perfectly fine
//        Glide.with(context).load(imgURL).into(viewHolder.ivImage);
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
        private View vLine;

        private ViewHolder(View view) {
            super(view);
            ivImage = view.findViewById(R.id.imgEmirate);
            tvName = view.findViewById(R.id.tvCountryName);
            vLine = view.findViewById(R.id.vLine);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                int previousSelectedIndex = selectedItemIndex;
                selectedItemIndex = getLayoutPosition();
                if (selectedStateIndicies.contains(selectedItemIndex)){
                    if (isConsultantLogin){
                        //list.remove(1) removes the object at position 1 and remove(new Integer(1)) removes the first occurrence of the specified element from this list.
                        selectedStateIndicies.remove( new Integer(selectedItemIndex) );
                    }
                }else{
                    if (!isConsultantLogin) {
                        selectedStateIndicies.clear();  //for member clearing all then adding
                    }
                    selectedStateIndicies.add(selectedItemIndex);
                }
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