package com.durodola.mobile.androidweather;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mobile on 2016-04-12.
 */
public class WeatherFragment extends AbstractWeatherFragment {
    TextView cityName, cityTime, currentTime;
    EditText enterCity;
    private static String API_KEY = "f79974fabd0e9c2c9ae5301b0b059a14";
    private String urlLink = "http://api.openweathermap.org/data/2.5/forecast?q=toronto" + ",ca&mode=json&appid=" + API_KEY;
    private static String TAG = "WeatherFragment";

    // json
    private static final String TEMP = "temp";
    private static final String TEMP_MIN = "temp_min";
    private static final String TEMP_MAX = "temp_max";
    private static final String DT = "dt";
    private static final String DT_TXT = "dt_txt";
    private static final String ICON = "icon";
    private static final String TAG_CONTRACTOR = "contractor";
    private static final String TAG_LNG = "lng";
    private static final String TAG_LAT = "lat";

    //
    LinearLayoutManager llm;

    RecyclerView rv;
    // myList.setLayoutManager(layoutManager);
    ArrayList<HashMap<String, String>> weatherArrayList;


    // adapter
    WeatherAdapter weatherAdapter;


    public WeatherFragment() {
        // Required empty public constructor
    }

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }


    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.weather_fragment, container, false);
        cityName = (TextView) view.findViewById(R.id.citytxtview);
        cityTime = (TextView) view.findViewById(R.id.timeR);
        currentTime = (TextView) view.findViewById(R.id.currentTime);
        enterCity = (EditText) view.findViewById(R.id.entercity);
        weatherArrayList = new ArrayList<HashMap<String, String>>();
        llm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);
        new DownloadTask().execute();
        //picasso
    /*    Picasso.with(this)
                .load("YOUR IMAGE URL HERE")
                .into(imageView);
*/
        return view;
    }

    // mathod to parse json
    private void jsonParser(String in) {

        JSONObject reader = null, row, main, weather;
        double temp_double = 273.15;
        JSONArray temp_array, weatherArray;
        String temp, temp_min, temp_max, icon = null, weather_main, weather_description, contractor, lng, lat, dt, dt_txt;
        // getCurrentLocation();


        try {
            reader = new JSONObject(in);
            temp_array = reader.getJSONArray("list");
            //JSONArray array = new JSONArray(in);
            for (int i = 0; i < temp_array.length(); i++) {
                row = temp_array.getJSONObject(i);
                dt = row.getString("dt");
                main = row.getJSONObject("main");


                temp = main.getString("temp");
                double temp_celcius = Double.parseDouble(temp) - temp_double;

                temp_min = main.getString("temp_min");
                temp_max = main.getString("temp_max");


                HashMap<String, String> contact = new HashMap<String, String>();

             /*   contact.put(TAG_CONTRACTOR, contractor);
                contact.put(TAG_LAT, lat);
                contact.put(TAG_LNG, lng);*/
                // contact.get
                Log.e(TAG, " " + temp);

                weatherArray = row.getJSONArray("weather");
                for (int j = 0; j < weatherArray.length(); j++) {
                    weather = weatherArray.getJSONObject(j);
                    weather_main = weather.getString("main");
                    weather_description = weather.getString("description");
                    icon = weather.getString("icon");

                    Log.e(TAG, "weather main  " + weather_description + " icon" + icon);


                }
                dt_txt = row.getString("dt_txt");
                Log.e(TAG, " temp" + temp_double);
                Log.e(TAG, " dt" + dt);
                Log.e(TAG, " dt_txt" + dt_txt);
                contact.put(TEMP, Double.toString(Math.round(temp_celcius)));
                contact.put(TEMP_MIN, temp_min);
                contact.put(TEMP_MAX, temp_max);
                contact.put(DT, dt);
                contact.put(ICON, icon);
                contact.put(DT_TXT, weekdayConverter(dt_txt));
                weatherArrayList.add(contact);

           /*     towList.add(contact);
                completeList.add(contact);
*/
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private class DownloadTask extends AsyncTask<String, Integer, String> {
        String readStream;

        @Override
        protected String doInBackground(String... url) {
            //progressbar.setVisibility(View.VISIBLE);

            try {
                URL url1 = new URL(urlLink);
                HttpURLConnection con = (HttpURLConnection) url1.openConnection();
                readStream = readStream(con.getInputStream());
                // Give output for the command line
                System.out.println(readStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return readStream;
        }

        @Override
        protected void onPostExecute(String result) {
            //  Log.e(TAG, result);
            if (result.equalsIgnoreCase("null")) {
                Log.e(TAG, " download did not happend");

            } else {
                jsonParser(result);
                weatherAdapter = new WeatherAdapter(getContext(), weatherArrayList);
                rv.setAdapter(weatherAdapter);


            }


        }
    }

}
