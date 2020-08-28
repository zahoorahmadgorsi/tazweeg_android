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
import com.bulahej.tazweeg.apis_responses.list_countries.Country;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {
//public class CountryAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
//    private ArrayList<Country> countries;
    private List<Country> data;
    private CountryAdapter.OnItemClickListener clickListener;
    private int selectedItemIndex = -1 ;


    public CountryAdapter (Context context, ArrayList<Country> countries) {
        this.context = context;
        this.data = countries;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }
//    @Override
//    public int getCount() {
//        return countries.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return countries.get(position);
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_country, viewGroup, false);
        return new CountryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String imgURL = "https://www.tazweeg.ae/assets/img/gcc/flags/" + data.get(i).getImg();
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(context).load(imgURL).apply(options).into(viewHolder.imgCountry);
        viewHolder.tvCountryName.setText(data.get(i).getName(context));
        viewHolder.tvCountryCode.setText( "+" + data.get(i).getCode());
        //viewHolder.tvCountryCode.setText("971589108662");
    }

//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }

    @Override
    public int getItemCount() {
        return data.size();
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View itemView = inflater.inflate(R.layout.item_country, parent, false);
//
//        ivGallery = (ImageView) itemView.findViewById(R.id.imgCountry);
//        textView = (TextView) itemView.findViewById(R.id.txtName);
//
//        String imgURL = "https://www.tazweeg.ae/assets/img/gcc/flags/" + countries.get(position).getImg();
//        RequestOptions options = new RequestOptions()
//            .centerCrop()
//            .placeholder(R.mipmap.ic_launcher_round)
//            .error(R.mipmap.ic_launcher_round);
//        Glide.with(context).load(imgURL).apply(options).into(ivGallery);
//        textView.setText(countries.get(position).getName(context));
//
//        return itemView;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgCountry;
        private TextView tvCountryName;
        private TextView tvCountryCode;

        private ViewHolder(View view) {
            super(view);
            imgCountry = view.findViewById(R.id.cImgCountry);
            tvCountryName = view.findViewById(R.id.tvCountryName);
            tvCountryCode = view.findViewById(R.id.tvCountryCode);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                selectedItemIndex = getLayoutPosition();
//                notifyItemChanged(selectedItemIndex);
                clickListener.onItemClick(getLayoutPosition(), data.get(getLayoutPosition()));
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Country country);
    }
}