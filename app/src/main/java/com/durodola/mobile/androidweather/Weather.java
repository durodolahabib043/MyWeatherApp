package com.durodola.mobile.androidweather;

/**
 * Created by mobile on 2016-04-13.
 */
public class Weather {
    private String temp = "temp";
    private String temp_min = "temp_min";
    private String temp_max = "temp_max";
    private String main = "main";
    private String description = "description";
    private String dt = "dt";
    private String icon = "icon";
    private String dt_txt = "dt_txt";


    private String time = "dt_txt";


    private static Weather weather = new Weather();

    /* A private Constructor prevents any other
     * class from instantiating.
     */
    private Weather() {
    }

    /* Static 'instance' method */
    public static Weather getInstance() {
        return weather;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTemp() {

        return temp;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(String temp_min) {
        this.temp_min = temp_min;
    }

    public String getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(String temp_max) {
        this.temp_max = temp_max;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }


}
