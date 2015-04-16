package br.com.ceducarneiro.weatherapp.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;

import br.com.ceducarneiro.weatherapp.R;
import br.com.ceducarneiro.weatherapp.WeatherApplication;
import br.com.ceducarneiro.weatherapp.broadcast.AlarmReceiver;
import br.com.ceducarneiro.weatherapp.controller.WeatherController;
import br.com.ceducarneiro.weatherapp.model.bean.Alert;
import br.com.ceducarneiro.weatherapp.model.bean.AlertCondition;
import br.com.ceducarneiro.weatherapp.model.bean.AlertOption;
import br.com.ceducarneiro.weatherapp.model.bean.Place;
import br.com.ceducarneiro.weatherapp.view.main.MainActivity;
import br.com.ceducarneiro.weatherapp.ws.OpenWeather;
import br.com.ceducarneiro.weatherapp.ws.OpenWeatherList;
import br.com.ceducarneiro.weatherapp.ws.OpenWeatherWS;

public class AlertService extends IntentService {

    private static boolean running = false;

    public AlertService() {
        super("i");
    }

    public static boolean isRunning() {
        return running;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        running = true;

        List<Place> places = Place.listAllWithAlert();

        if (!places.isEmpty()) {
            List<Long> ids = new ArrayList<>();

            for (Place place : places) {
                ids.add(place.getId());
            }

            OpenWeatherList weathers = OpenWeatherWS.getInstance()
                    .getWeatherByIds(WeatherApplication.getInstance()
                            .getString(R.string.default_units), ids);

            if (weathers != null) {
                for (OpenWeather weather : weathers.list) {
                    WeatherController.getInstance().createOrUpdatePlace(weather);
                }
            }
        }

        List<Alert> alerts = Alert.listAll(Alert.class);

        for (Alert alert : alerts) {
            checkAlert(alert);
        }

        if (alerts.isEmpty()) {
            AlarmReceiver.cancelAlarm(this);
        }

        running = false;
    }

    private void checkAlert(Alert alert) {
        Place place = alert.getPlace();

        AlertOption option = alert.getOption();
        AlertCondition condition = alert.getCondition();
        double value = alert.getValue();
        double valueToCheck = 0;

        switch (option) {
            case Humidity: valueToCheck = place.getWeather().getHumidity(); break;
            case Pressure: valueToCheck = place.getWeather().getPressure(); break;
            case Temperature: valueToCheck = place.getWeather().getTemperature(); break;
            case WindSpeed: valueToCheck = place.getWeather().getWindSpeed(); break;
            default: /* Empty */
        }

        boolean needAlert;

        if (condition == AlertCondition.GreaterThan) {
            needAlert = valueToCheck > value;
        } else {
            needAlert = valueToCheck < value;
        }

        if (needAlert) {
            sendNotification(place);
        }
    }

    private void sendNotification(Place place) {
        String contentText = String.format("%s: %s",
                getString(R.string.climate_alert), place.getCity());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(contentText)
                .setTicker(contentText);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        Intent it = new Intent(this, MainActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        it.putExtra(MainActivity.ALERT_KEY, true);
        it.putExtra(MainActivity.PLACE_KEY, place.getId());

        PendingIntent pendingIntent = PendingIntent.getActivity(this, place.getId().intValue(),
                it, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(place.getId().intValue(), notification);
    }
}
