package br.com.ceducarneiro.weatherapp.model.bean;

import br.com.ceducarneiro.weatherapp.R;
import br.com.ceducarneiro.weatherapp.WeatherApplication;

public enum AlertOption {
    Temperature,
    Humidity,
    Pressure,
    WindSpeed;

    public String getText() {
        WeatherApplication app = WeatherApplication.getInstance();
        return app.getResources().getStringArray(
                R.array.alerts_options_array)[ordinal()];
    }
}