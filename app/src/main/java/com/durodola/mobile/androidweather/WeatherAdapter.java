package com.durodola.mobile.androidweather;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mobile on 2016-04-01.
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.PersonViewHolder> {

    ArrayList<HashMap<String, String>> weatherArrayList;
    // private static MyItemClickListener mItemClickListener;


    public WeatherAdapter(ArrayList<HashMap<String, String>> arraylist) {
        this.weatherArrayList = arraylist;
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
        //  holder.personPhoto.setImageResource(persons.get(position).photoId);


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return weatherArrayList.size();
    }

/*
    public void animateTo(ArrayList<HashMap<String, String>> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }


    private void applyAndAnimateRemovals(ArrayList<HashMap<String, String>> newModels) {
        for (int i = weatherArrayList.size() - 1; i >= 0; i--) {
            final HashMap<String, String> model = weatherArrayList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(ArrayList<HashMap<String, String>> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final HashMap<String, String> model = newModels.get(i);
            if (!weatherArrayList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(ArrayList<HashMap<String, String>> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final HashMap<String, String> model = newModels.get(toPosition);
            final int fromPosition = weatherArrayList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public HashMap<String, String> removeItem(int position) {
        final HashMap<String, String> model = weatherArrayList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, HashMap<String, String> model) {
        weatherArrayList.add(position, model);
        notifyItemInserted(position);

    }

    public void moveItem(int fromPosition, int toPosition) {
        final HashMap<String, String> model = weatherArrayList.remove(fromPosition);
        weatherArrayList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);

    }
*/


/*
    public interface MyItemClickListener {
        public void onItemClick(int position, View v);

    }

    public ArrayList<HashMap<String, String>> filter(ArrayList<HashMap<String, String>> models, ArrayList<HashMap<String, String>> completeList, String query) {
        query = query.toLowerCase(Locale.getDefault());
        final ArrayList<HashMap<String, String>> filteredModelList = new ArrayList<HashMap<String, String>>();


        if (query.length() == 0) {
            filteredModelList.clear();

            filteredModelList.addAll(completeList);

        } else {
            for (int i = 0, l = models.size(); i < l; i++) {
                final HashMap<String, String> p = models.get(i);
                if (p.toString().toLowerCase().contains(query))
                    filteredModelList.add(p);

            }
        }
        notifyDataSetChanged();

        return filteredModelList;
    }
*/


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
/*
        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(getAdapterPosition(), v);

            }

        }

    }

    public void SetOnItemCLickListener(MyItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }*/


    }
}