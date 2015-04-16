package br.com.ceducarneiro.weatherapp.model.bean;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

public class Place extends BaseBean<Place> {

    private Long owId;
    private String city;
    private String country;
    private Double latitude;
    private Double longitude;
    private Weather weather;

    public Place() {
        weather = new Weather();
    }

    public Long getOwId() {
        return owId;
    }

    public void setOwId(Long owId) {
        this.owId = owId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public static List<Place> listAllWithAlert() {
        List<Place> places = new ArrayList<>();

        List<Alert> alerts = Alert.listAll(Alert.class);
        for (Alert alert : alerts) {
            places.add(alerts.get(0).getPlace());
        }

        return places;
    }

    public static List<Place> filterByCity(String city) {
        return Select.from(Place.class).where(Condition.prop("city")
                .like("%" + city + "%")).list();
    }

    public static Place findByOwId(long owId) {
        return Select.from(Place.class).where(Condition.prop("ow_id")
                .eq(String.valueOf(owId))).first();
    }

}
