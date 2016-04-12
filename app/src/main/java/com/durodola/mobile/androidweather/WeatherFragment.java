package com.durodola.mobile.androidweather;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mobile on 2016-04-12.
 */
public class WeatherFragment extends AbstractWeatherFragment {
    TextView cityName, cityTime, currentTime;
    EditText enterCity;
    private static String API_KEY = "f79974fabd0e9c2c9ae5301b0b059a14";
    private String urlLink = "http://api.openweathermap.org/data/2.5/forecast?q=mumbai" + ",us&mode=json&appid=" + API_KEY;
    private static String TAG = "WeatherFragment";


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
        new DownloadTask().execute();

        return view;
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
            Log.e(TAG, result);
            // getCurrentLocation();


      /*      if (result == null) {
                Log.e("result is null ", " result is null");
            } else {
                Log.d(" url string", result);
                jsonParserr(result);
                adapter = new RVAdapter(towList);
                rv.setAdapter(adapter);

                if (adapter != null) {
                    adapter.SetOnItemCLickListener(new RVAdapter.MyItemClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {

                            Switchfragment(R.id.mylayout, mapFragment, Float.parseFloat(towList.get(position).get(TAG_LNG)),
                                    Float.parseFloat(towList.get(position).get(TAG_LAT)), towList.get(position).get(TAG_NAME),
                                    towList.get(position).get(TAG_CONTRACTOR), latitudeN, longitudeN, address);

                        }
                    });

                }

            }
            progressbar.setVisibility(View.INVISIBLE);
*/
        }
    }

}
