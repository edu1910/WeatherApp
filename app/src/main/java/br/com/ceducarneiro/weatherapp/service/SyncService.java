package br.com.ceducarneiro.weatherapp.service;

import android.app.IntentService;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import br.com.ceducarneiro.weatherapp.R;
import br.com.ceducarneiro.weatherapp.WeatherApplication;
import br.com.ceducarneiro.weatherapp.controller.SyncListener;
import br.com.ceducarneiro.weatherapp.model.bean.Place;
import br.com.ceducarneiro.weatherapp.ws.OpenWeather;
import br.com.ceducarneiro.weatherapp.ws.OpenWeatherList;
import br.com.ceducarneiro.weatherapp.ws.OpenWeatherWS;
import br.com.ceducarneiro.weatherapp.controller.ListenerController;
import br.com.ceducarneiro.weatherapp.controller.WeatherController;

public class SyncService extends IntentService {

    private static boolean running = false;

    public SyncService() {
        super("SyncService");
    }

    public static boolean isRunning() {
        return running;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        running = true;

        ListenerController.getInstance().notifySyncListeners(
                SyncListener.ON_SYNC_START, null);

        OpenWeatherWS openWeatherWS = OpenWeatherWS.getInstance();

        List<Place> places = Place.listAll(Place.class);

        if (places.size() > 0) {
            List<Long> ids = new ArrayList<>();

            for (Place place : places) {
                ids.add(place.getOwId());
            }

            OpenWeatherList weathers = openWeatherWS.getWeatherByIds(WeatherApplication
                    .getInstance().getString(R.string.default_units), ids);

            if (weathers != null) {
                for (OpenWeather weather : weathers.list) {
                    WeatherController.getInstance().createOrUpdatePlace(weather);
                }
            }
        }

        ListenerController.getInstance().notifySyncListeners(
                SyncListener.ON_SYNC_FINISH, null);

        running = false;
    }
}
