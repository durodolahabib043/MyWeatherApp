package com.durodola.mobile.androidweather;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by mobile on 2016-04-12.
 */
public class AbstractWeatherFragment extends Fragment {

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

    protected void hideKeyBoard(View view) {
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
        return finalDay.substring(0, 3);

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

}
