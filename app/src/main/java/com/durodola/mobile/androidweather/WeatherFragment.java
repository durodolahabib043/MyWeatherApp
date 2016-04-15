package com.durodola.mobile.androidweather;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by mobile on 2016-04-12.
 */
public class WeatherFragment extends AbstractWeatherFragment {
    TextView cityName, cityTime, currentTime, maxTemp, minTemp, weatherDescription;
    EditText enterCity;
    ImageView iconH;
    // i used virgin mail and olawepo as username
    private static String API_KEY = "4e0587d5eaf712ce4a3c358a79d7e591";
    private static String BASE_URL = "http://openweathermap.org/img/w/";
    private String urlLink;
    String test_link = "http://api.openweathermap.org/data/2.5/forecast?q=ibadan,ca&mode=json&appid=4e0587d5eaf712ce4a3c358a79d7e591";
    private static String TAG = "WeatherFragment";
    LinearLayoutManager llm;
    RecyclerView rv;
    // myList.setLayoutManager(layoutManager);
    ArrayList<HashMap<String, String>> weatherArrayList;
    // adapter
    WeatherAdapter weatherAdapter;
    ProgressBar progressbar;
    // map
    Map.Entry<String, String> entry;
    String key, value;
    HashMap<String, String> contact;
    Weather weatherObj;
    double valueMax, valueMin = 0;
    private Integer[] mThumbIds;
    Random random;

    public WeatherFragment() {
        // Required empty public constructor
    }

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }


    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.weather_fragment, container, false);
        weatherObj = Weather.getInstance();
        cityName = (TextView) view.findViewById(R.id.citytxtview);
        cityTime = (TextView) view.findViewById(R.id.timeR);
        currentTime = (TextView) view.findViewById(R.id.currentTime);
        maxTemp = (TextView) view.findViewById(R.id.buttom_max_temp);
        minTemp = (TextView) view.findViewById(R.id.buttom_min_temp);
        weatherDescription = (TextView) view.findViewById(R.id.button_haze);
        enterCity = (EditText) view.findViewById(R.id.entercity);

       // new DownloadTask().execute(test_link);
        // test api call
     /*   weatherDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //contact.clear();
                contact.clear();
                weatherArrayList.clear();
                urlLink = "http://api.openweathermap.org/data/2.5/forecast?q=" + enterCity.getText().toString() + ",ca&mode=json&appid=" + API_KEY;

                showSpinner(progressbar);
                new DownloadTask().execute(urlLink);
                cityName.setText(enterCity.getText().toString());

                hideKeyBoard(view);

            }
        });*/

        progressbar = (ProgressBar) view.findViewById(R.id.progressbar1);
        enterCity.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    contact.clear();
                    weatherArrayList.clear();
                    urlLink = "http://api.openweathermap.org/data/2.5/forecast?q=" + enterCity.getText().toString() + ",ca&mode=json&appid=" + API_KEY;
                    showSpinner(progressbar);
                    new DownloadTask().execute(urlLink);
                    cityName.setText(enterCity.getText().toString().toUpperCase());
                    enterCity.setText("");

                    hideKeyBoard(view);
                    return true;
                }
                return false;
            }
        });
        //  hideSoftKeyboard();

        iconH = (ImageView) view.findViewById(R.id.button_icon);
        weatherArrayList = new ArrayList<HashMap<String, String>>();
        llm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);
        // change back ground
        mThumbIds = new Integer[]{R.drawable.clearbg, R.drawable.background,
                R.drawable.cloudbg, R.drawable.dayclear, R.drawable.tropical};
        random = new Random();
        return view;
    }

    private void jsonParser(String in) {

        JSONObject reader = null, row, main, weather;
        double temp_double = 273.15;
        JSONArray temp_array, weatherArray;
        double temp_celcius = 0;
        String temp = null, temp_min, temp_max, icon = null, weather_main = null, weather_description = null, contractor, lng, lat, dt, dt_txt;


        try {
            reader = new JSONObject(in);
            temp_array = reader.getJSONArray("list");
            //JSONArray array = new JSONArray(in);
            for (int i = 0; i < temp_array.length(); i++) {
                row = temp_array.getJSONObject(i);
                dt = row.getString(weatherObj.getDt());
                main = row.getJSONObject(weatherObj.getMain());
                temp = main.getString(weatherObj.getTemp());
                temp_celcius = Double.parseDouble(temp) - temp_double;

                temp_min = main.getString(weatherObj.getTemp_min());
                temp_max = main.getString(weatherObj.getTemp_max());

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
                contact = new HashMap<String, String>();


                contact.put(weatherObj.getTemp_min(), temp_min);
                contact.put(weatherObj.getTemp_max(), temp_max);
                contact.put(weatherObj.getMain(), weather_main);
                contact.put(weatherObj.getDescription(), weather_description);
                contact.put(weatherObj.getDt(), dt);
                contact.put(weatherObj.getIcon(), icon);
                contact.put(weatherObj.getDt_txt(), weekdayConverter(dt_txt));
                contact.put(weatherObj.getTemp(), Double.toString(Math.round(temp_celcius)));

                entry = contact.entrySet().iterator().next();
                Log.e(" value ", "2@@@@@@@@@@@@@@@@@@@@@@@ ");
                Log.e(" value ", " " + contact);
                Log.e(" value ", "2@@@@@@@@@@@@@@@@@@@@@@@ ");
                weatherArrayList.add(contact);
            }
            // refactor
            valueMax = Double.parseDouble(weatherArrayList.get(0).get(weatherObj.getTemp_max()));
            valueMin = Double.parseDouble(weatherArrayList.get(0).get(weatherObj.getTemp_min()));
            double maxTempCelcius = valueMax - temp_double;
            double minTempCelius = valueMin - temp_double;
            // Double.valueOf(s).intValue();
            currentTime.setText(Double.valueOf(weatherArrayList.get(0).get(weatherObj.getTemp())).intValue() + "C");

            maxTemp.setText(Double.toString(Math.round(maxTempCelcius)));
            minTemp.setText(Double.toString(Math.round(minTempCelius)));
            weatherDescription.setText(weatherArrayList.get(0).get(weatherObj.getDescription()));
            Picasso.with(this.getContext()).load(BASE_URL + weatherArrayList.get(0).get(weatherObj.getIcon()) + ".png").into(iconH);
            Log.e(TAG, " " + maxTempCelcius);

            for (int i = 0; i <= 4; i++) {
                WeatherFragment.this.getView().setBackgroundResource(mThumbIds[random.nextInt(4 - 0 + 1) + 0]);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //    contact.clear();
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {
        String readStream;


        @Override
        protected String doInBackground(String... url) {
            //    progressbar.setVisibility(View.VISIBLE);
            // progressBar.setVisibility(View.VISIBLE);

            try {
                //  progressbar.setVisibility(View.VISIBLE);
                URL url1 = new URL(url[0]);
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
            progressbar.setVisibility(View.INVISIBLE);


        }
    }

}
