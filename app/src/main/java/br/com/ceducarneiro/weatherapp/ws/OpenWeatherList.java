package br.com.ceducarneiro.weatherapp.ws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenWeatherList {

    public List<OpenWeather> list;

}
