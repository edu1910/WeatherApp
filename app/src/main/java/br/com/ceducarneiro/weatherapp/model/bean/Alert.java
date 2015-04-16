package br.com.ceducarneiro.weatherapp.model.bean;

import java.util.List;

import br.com.ceducarneiro.weatherapp.R;
import br.com.ceducarneiro.weatherapp.WeatherApplication;

public class Alert extends BaseBean<Alert> {

    private AlertOption option;
    private AlertCondition condition;
    private double value;
    private Place place;

    public Alert() {
        place = new Place();
    }

    public AlertOption getOption() {
        return option;
    }

    public void setOption(AlertOption option) {
        this.option = option;
    }

    public AlertCondition getCondition() {
        return condition;
    }

    public void setCondition(AlertCondition condition) {
        this.condition = condition;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public static List<Alert> listAllWithPlace(long placeId) {
        return Alert.find(Alert.class, "place = ?", String.valueOf(placeId));
    }

}
