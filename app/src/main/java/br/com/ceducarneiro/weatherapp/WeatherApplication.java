package br.com.ceducarneiro.weatherapp;

import com.orm.SugarApp;

public class WeatherApplication extends SugarApp {

    private static WeatherApplication instance;

    public WeatherApplication() {
        instance = this;
    }

    public static synchronized WeatherApplication getInstance() {
        if (instance == null) {
            new WeatherApplication();
        }

        return instance;
    }

}
