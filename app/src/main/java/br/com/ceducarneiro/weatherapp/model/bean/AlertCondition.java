package br.com.ceducarneiro.weatherapp.model.bean;

import br.com.ceducarneiro.weatherapp.R;
import br.com.ceducarneiro.weatherapp.WeatherApplication;

public enum AlertCondition {
    LessThan,
    GreaterThan;

    public String getText() {
        WeatherApplication app = WeatherApplication.getInstance();
        return app.getResources().getStringArray(
                R.array.alerts_conditions_array)[ordinal()];
    }
}
