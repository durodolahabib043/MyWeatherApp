package com.durodola.mobile.androidweather;

import android.support.v4.app.Fragment;

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

}
