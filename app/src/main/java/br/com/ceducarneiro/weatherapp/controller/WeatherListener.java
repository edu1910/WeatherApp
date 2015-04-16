package br.com.ceducarneiro.weatherapp.controller;

import java.util.List;

public interface WeatherListener {
    void onGetWeatherStart(List args);
    void onGetWeatherFinish(List args);

    static final String ON_GET_WEATHER_START = "onGetWeatherStart";
    static final String ON_GET_WEATHER_FINISH = "onGetWeatherFinish";
}
