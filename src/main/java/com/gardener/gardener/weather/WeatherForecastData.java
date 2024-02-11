package com.gardener.gardener.weather;

import com.google.gson.Gson;

public class WeatherForecastData {
    private String cod;
    private int message;
    private int cnt;
    private WeatherData[] list;

    public static WeatherForecastData fromJson(String json) {
        return new Gson().fromJson(json, WeatherForecastData.class);
    }

    public String getCod() {
        return cod;
    }

    public int getMessage() {
        return message;
    }

    public int getCnt() {
        return cnt;
    }

    public WeatherData[] getList() {
        return list;
    }
}
