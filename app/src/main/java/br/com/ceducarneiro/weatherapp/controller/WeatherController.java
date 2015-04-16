package br.com.ceducarneiro.weatherapp.controller;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import br.com.ceducarneiro.weatherapp.R;
import br.com.ceducarneiro.weatherapp.WeatherApplication;
import br.com.ceducarneiro.weatherapp.model.bean.Place;
import br.com.ceducarneiro.weatherapp.ws.OpenWeather;
import br.com.ceducarneiro.weatherapp.ws.OpenWeatherWS;

public class WeatherController {

    private static WeatherController instance;

    private WeatherController() {

    }

    public static synchronized WeatherController getInstance() {
        if (instance == null) {
            instance = new WeatherController();
        }

        return instance;
    }

    public void getWeatherByLatLng(LatLng latLng) {
        new WeatherLatLngTask().execute(latLng);
    }

    public void getWeatherByPlace(String place, String country) {
        new WeatherPlaceTask().execute(place, country);
    }

    public Place createOrUpdatePlace(OpenWeather openWeather) {
        Place place = Place.findByOwId(openWeather.id);

        if (place == null) {
            place = new Place();
        }

        if (openWeather.weathers.size() > 0) {
            place.getWeather().setCode(openWeather.weathers.get(0).code);
            place.getWeather().setIcon(openWeather.weathers.get(0).icon);
        }
        place.getWeather().setTemperature(openWeather.main.temp);
        place.getWeather().setHumidity(openWeather.main.humidity);
        place.getWeather().setPressure(openWeather.main.pressure);
        place.getWeather().setWindDegree(openWeather.wind.degree);
        place.getWeather().setWindSpeed(openWeather.wind.speed);
        place.getWeather().setLastUpdate(new Date(openWeather.timestamp *1000));

        place.setOwId(openWeather.id);
        place.setCity(openWeather.name);
        place.setCountry(openWeather.system.country);
        place.setLatitude(openWeather.coordinates.latitude);
        place.setLongitude(openWeather.coordinates.longitude);

        place.getWeather().save();
        place.save();

        return place;
    }

    private class WeatherLatLngTask extends WeatherTask<LatLng, Void> {

        @Override
        protected OpenWeather doInBackground(LatLng... params) {
            return OpenWeatherWS.getInstance().getWeatherByLatLng(
                    WeatherApplication.getInstance().getString(R.string.default_units),
                    params[0].latitude, params[0].longitude);
        }
    }

    private class WeatherPlaceTask extends WeatherTask<String, Void> {

        @Override
        protected OpenWeather doInBackground(String... params) {
            return OpenWeatherWS.getInstance().getWeatherByPlace(
                    WeatherApplication.getInstance().getString(R.string.default_units),
                    params[0], params[1]);
        }
    }

    private abstract class WeatherTask<a, b> extends AsyncTask<a, b, OpenWeather> {

        @Override
        protected void onPreExecute() {
            ListenerController.getInstance().notifyWeatherListeners(
                    WeatherListener.ON_GET_WEATHER_START, null);
        }

        @Override
        protected void onPostExecute(OpenWeather weather) {
            ListenerController.getInstance().notifyWeatherListeners(
                    WeatherListener.ON_GET_WEATHER_FINISH, weather);
        }
    }

}
