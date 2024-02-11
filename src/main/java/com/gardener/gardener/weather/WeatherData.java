package com.gardener.gardener.weather;

import com.google.gson.Gson;

public class WeatherData {
    private Coord coord;
    private Weather[] weather;
    private String base;
    private Main main;
    // Другие поля


    public static WeatherData fromJson(String json) {
        return new Gson().fromJson(json, WeatherData.class);
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }
}

