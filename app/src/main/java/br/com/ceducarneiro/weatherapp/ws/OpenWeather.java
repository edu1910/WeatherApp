package br.com.ceducarneiro.weatherapp.ws;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenWeather {

    @JsonProperty("coord")
    public Coord coordinates;

    @JsonProperty("sys")
    public Sys system;

    @JsonProperty("weather")
    public List<Weather> weathers;

    @JsonProperty("main")
    public Main main;

    @JsonProperty("wind")
    public Wind wind;

    @JsonProperty("dt")
    public long timestamp;

    public long id;

    public String name;

    @JsonProperty("cod")
    public int retCode;

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class Coord {
        @JsonProperty("lon")
        public double longitude;
        @JsonProperty("lat")
        public double latitude;
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class Sys {
        public String country;
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class Weather {
        @JsonProperty("id")
        public int code;
        public String icon;
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class Main {
        public double temp;
        public double pressure;
        public int humidity;
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class Wind {
        public double speed;
        @JsonProperty("deg")
        public double degree;
    }
}
