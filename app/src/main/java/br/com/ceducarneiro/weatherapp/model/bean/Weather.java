package br.com.ceducarneiro.weatherapp.model.bean;

import java.util.Date;

import br.com.ceducarneiro.weatherapp.R;
import br.com.ceducarneiro.weatherapp.WeatherApplication;

public class Weather extends BaseBean<Weather> {

    private int code;
    private String icon;
    private double temperature;
    private int humidity;
    private double pressure;
    private double windDegree;
    private double windSpeed;
    private Date lastUpdate;

    public Weather() {
        lastUpdate = new Date();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(double windDegree) {
        this.windDegree = windDegree;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getDescription() {
        String description = "";

        if (icon != null && icon.length() == 3) {
            description = String.format("condition_%s", icon.substring(0, 2));
            description = WeatherApplication.getInstance().getString(
                    WeatherApplication.getInstance().getResources()
                            .getIdentifier(description, "string",
                                    WeatherApplication.getInstance().getPackageName()));
        }

        return description;
    }

    public int getResIcon() {
        String iconName = String.format("ic_condition_%s", icon);
        return WeatherApplication.getInstance().getResources()
                .getIdentifier(iconName, "drawable",
                        WeatherApplication.getInstance().getPackageName());
    }

    public int getResBackground() {
        int res = 0;

        if (icon != null && icon.length() == 3) {
            int iconValue = Integer.parseInt(icon.substring(0, 2));

            if (iconValue == 1)
                res = R.drawable.clear;
            else if (iconValue == 2 || iconValue == 3)
                res = R.drawable.clouds;
            else if (iconValue == 4 || iconValue == 50)
                res = R.drawable.cloudy;
            else
                res = R.drawable.rain;
        }

        return res;
    }
}
