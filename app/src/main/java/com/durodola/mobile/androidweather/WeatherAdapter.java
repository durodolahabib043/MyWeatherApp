package com.durodola.mobile.androidweather;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mobile on 2016-04-01.
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.PersonViewHolder> {

    ArrayList<HashMap<String, String>> weatherArrayList;
    Context context;
    // private static MyItemClickListener mItemClickListener;


    public WeatherAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
        this.weatherArrayList = arraylist;
        this.context = context;
    }


    // private final View.OnClickListener mOnClickListener = new MyOnClickListener();

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_layout, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.weekdays.setText(weatherArrayList.get(position).get("dt_txt"));
        holder.weekdaysTemp.setText(weatherArrayList.get(position).get("temp"));

        //
        //  holder.weatherIcom.setImageResource(Integer.parseInt(weatherArrayList.get(position).get("icon")));
        Picasso.with(this.context).load("http://openweathermap.org/img/w/" + weatherArrayList.get(position).get("icon") + ".png").into(holder.weatherIcom);
        // Picasso.with(c).load(url).fit().into(imageView);


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return weatherArrayList.size();
    }


    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView weekdays;
        TextView weekdaysTemp;
        ImageView weatherIcom;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            weekdays = (TextView) itemView.findViewById(R.id.single_id);
            weekdaysTemp = (TextView) itemView.findViewById(R.id.row_temp);
            weatherIcom = (ImageView) itemView.findViewById(R.id.icon_row);

        }


    }
}