package com.durodola.mobile.androidweather;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by mobile on 2016-04-12.
 */
public abstract class AbstractWeatherFragment extends Fragment {
    GPSService mGPSService;
    private static String TAG = "AbstractWeatherFragment";

    protected static String readStream(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in));) {

            String nextLine = "";
            while ((nextLine = reader.readLine()) != null) {
                sb.append(nextLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    protected void hideKeyBoard(TextView view) {
        // View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void hideSoftKeyboard() {
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

/*    public String time(String dt_txt) {
        String formattedDate = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date;
        try {
            date = df.parse(dt_txt);
            df.setTimeZone(TimeZone.getDefault());
            formattedDate = df.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(formattedDate);
        return formattedDate.substring(11, 18);

    }*/

    protected String weekdayConverter(String dt_txt) {
        // String dt = "2016-04-17 03:00:00" ;
        // convert utc to est

        String formattedDate = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date;
        try {
            date = df.parse(dt_txt);
            df.setTimeZone(TimeZone.getDefault());
            formattedDate = df.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(formattedDate);


        // convert to days of the week
        String s = formattedDate.substring(0, 10), day = null;
        String r = s.replace("-", "/");
        String finalDay = null;
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");
        Date dt1;
        try {
            dt1 = format1.parse(r);
            DateFormat format2 = new SimpleDateFormat("EE");
            finalDay = format2.format(dt1);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println(finalDay.substring(0, 3));
        return "    " + finalDay.substring(0, 3) + '\n' + formattedDate.substring(11, 18);

    }

    protected void showSpinner(final ProgressBar progressBar) {
        final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                handler.postDelayed(this, 4000);
                progressBar.setVisibility(View.INVISIBLE);
            }
        };

        handler.postDelayed(r, 1000);

    }


    public boolean isConnected() {
        ConnectivityManager connectivity = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public void noNetworkAlert() {
        // display dialog when no internet
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Uh Oh");
        builder.setMessage("We couldn't retrieve the data. Please check your network connection and try again.");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);

            }

        });
        builder.show();
    }


    public String getCurrentLocation(ProgressBar progressbar, float latitudeN, float longitudeN, String city, String urlLin2, String API_KEY) {
        mGPSService = new GPSService(getContext());
        mGPSService.getLocation();

        if (mGPSService.isLocationAvailable == false) {

            // Here you can ask the user to try again, using return; for that
            Toast.makeText(getActivity(), "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
            return "no location";

            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // address = "Location not available";
        } else {
            // Getting location co-ordinates
            progressbar.setVisibility(View.VISIBLE);
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getContext(), Locale.getDefault());
            latitudeN = (float) mGPSService.getLatitude();
            longitudeN = (float) mGPSService.getLongitude();
            String testadrres = mGPSService.getLocationAddress();

            Log.e("long", " " + longitudeN);
            Log.e("lat", " " + latitudeN);
            Log.e("addresss", " " + testadrres);


            try {
                addresses = geocoder.getFromLocation(latitudeN, longitudeN, 1);
                Log.e("address", " " + " " + addresses);

                city = addresses.get(0).getSubLocality();
                Log.e("city", " " + city);

                urlLin2 = "http://api.openweathermap.org/data/2.5/forecast?q=" + city.trim().replaceAll("\\s+", "") + ",ca&mode=json&appid=" + API_KEY;
                Log.e("link 2 ", urlLin2);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return urlLin2;

    }

    public void showKeyboard(EditText entercity) {
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.showSoftInput(entercity, 0);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


    public ArrayList jsonParser(String in, TextView currentTime, TextView maxTemp, TextView minTemp, TextView weatherDescription, ImageView iconH) {
        double valueMax, valueMin = 0;
        HashMap<String, String> contact;
        ArrayList<HashMap<String, String>> weatherArrayList;
        weatherArrayList = new ArrayList<HashMap<String, String>>();
        Weather weatherObj;
        weatherObj = Weather.getInstance();

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
               // contact.put(weatherObj.getTime(), time(dt_txt));
                contact.put(weatherObj.getTemp(), Double.toString(Math.round(temp_celcius)));

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
            // weatherDescription.setText(weatherArrayList.get(0).get(weatherObj.getTime()));
            Picasso.with(this.getContext()).load(Constant.BASE_URL + weatherArrayList.get(0).get(weatherObj.getIcon()) + ".png").into(iconH);
            Log.e(TAG, " " + maxTempCelcius);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weatherArrayList;

        //    contact.clear();
    }


}
