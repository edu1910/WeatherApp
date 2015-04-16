package br.com.ceducarneiro.weatherapp.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ListenerController {

    private static ListenerController instance;

    private List<WeatherListener> weatherListeners;
    private List<SyncListener> syncListeners;

    private ListenerController() {
        weatherListeners = new ArrayList<>();
        syncListeners = new ArrayList<>();
    }

    public static synchronized ListenerController getInstance() {
        if (instance == null) {
            instance = new ListenerController();
        }

        return instance;
    }

    public void addWeatherListener(WeatherListener listener) {
        weatherListeners.remove(listener);
        weatherListeners.add(listener);
    }

    public void removeWeatherListener(WeatherListener listener) {
        weatherListeners.remove(listener);
    }

    public void addSyncListener(SyncListener listener) {
        syncListeners.remove(listener);
        syncListeners.add(listener);
    }

    public void removeSyncListener(SyncListener listener) {
        syncListeners.remove(listener);
    }

    public void notifyWeatherListeners(String event, Object... args) {
        List param = new ArrayList();

        if (args != null) {
            for (Object arg : args) {
                param.add(arg);
            }
        }

        for (WeatherListener listener : weatherListeners) {
            callMethod(listener, event, param);
        }
    }

    public void notifySyncListeners(String event, Object... args) {
        List param = new ArrayList();

        if (args != null) {
            for (Object arg : args) {
                param.add(arg);
            }
        }

        for (SyncListener listener : syncListeners) {
            callMethod(listener, event, param);
        }
    }

    private void callMethod(Object obj, String methodName, List param) {
        try {
            Method method = obj.getClass().getMethod(methodName, List.class);
            method.invoke(obj, param);
        } catch (Exception ignored) {
            /* Empty */
        }
    }

}
