package com.durodola.mobile.androidweather;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by mobile on 2016-04-12.
 */
public class WeatherFragment extends AbstractWeatherFragment implements TextView.OnEditorActionListener {
    TextView cityName, cityTime, currentTime, maxTemp, minTemp, weatherDescription;
    EditText enterCity;
    ImageView iconH;
    // i used virginmail and olawepo as username
    private String urlLink;
    private static String TAG = "WeatherFragment";
    LinearLayoutManager llm;
    RecyclerView rv;
    ArrayList<HashMap<String, String>> weatherArrayList;
    WeatherAdapter weatherAdapter;
    ProgressBar progressbar;
    HashMap<String, String> contact;
    Weather weatherObj;
    private Integer[] mThumbIds;
    Random random;
    float latitudeN, longitudeN;
    String urlLink2, city = null;

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
        enterCity = (EditText) view.findViewById(R.id.entercity);
        weatherDescription = (TextView) view.findViewById(R.id.button_haze);
        progressbar = (ProgressBar) view.findViewById(R.id.progressbar1);
        setHasOptionsMenu(true);
        iconH = (ImageView) view.findViewById(R.id.button_icon);
        weatherArrayList = new ArrayList<HashMap<String, String>>();
        llm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);
        mThumbIds = new Integer[]{R.drawable.clearbg, R.drawable.background,
                R.drawable.cloudbg, R.drawable.dayclear, R.drawable.tropical};
        random = new Random();
        enterCity.setOnEditorActionListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (isConnected() == true) {
            new DownloadTask().execute(getCurrentLocation(progressbar, latitudeN, longitudeN, city, urlLink2, Constant.API_KEY));
        } else {
            noNetworkAlert();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        cityNames(city);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //  return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_edit:
                enterCity.setVisibility(View.VISIBLE);
                enterCity.requestFocus();
                showKeyboard(enterCity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }//end switch

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.frag_menu, menu);


    }

    private void cityNames(String cityname) {
        ((MainActivity) getActivity())
                .setActionBarTitle(cityname);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (contact != null) {
                contact.clear();
                weatherArrayList.clear();
            }
            urlLink = Constant.URL_WAETHER + enterCity.getText().toString() + Constant.URL_PARAM + Constant.API_KEY;
            //showSpinner(progressbar);
            new DownloadTask().execute(urlLink);
            cityNames(enterCity.getText().toString().toUpperCase());
            enterCity.setText("");
            hideKeyBoard(v);
            return true;
        }

        return false;
    }


    private class DownloadTask extends AsyncTask<String, Integer, String> {
        String readStream;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showDialog(DIALOG_DOWNLOAD_PROGRESS);
            progressbar.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(String... url) {
            //    progressbar.setVisibility(View.VISIBLE);
            progressbar.setVisibility(View.VISIBLE);
            //   getCurrentLocation();

            try {
                  progressbar.setVisibility(View.VISIBLE);
                URL url1 = new URL(url[0]);
                HttpURLConnection con = (HttpURLConnection) url1.openConnection();
                readStream = readStream(con.getInputStream());
                // Give output for the command line
                System.out.println(readStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // getCurrentLocation();
            return readStream;
        }

        @Override
        protected void onPostExecute(String result) {
            //  Log.e(TAG, result);


            if (result == null) {
                result = "empty string";
                Log.e(TAG, result);
            } else {
                getCurrentLocation(progressbar, latitudeN, longitudeN, city, urlLink2, Constant.API_KEY);

                for (int i = 0; i <= 4; i++) {
                    WeatherFragment.this.getView().setBackgroundResource(mThumbIds[random.nextInt(4 - 0 + 1) + 0]);
                }
                weatherAdapter = new WeatherAdapter(getContext(), jsonParser(result, currentTime, maxTemp, minTemp, weatherDescription, iconH));
                rv.setAdapter(weatherAdapter);
            }
            progressbar.setVisibility(View.INVISIBLE);


        }
    }

}
