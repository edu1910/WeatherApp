package br.com.ceducarneiro.weatherapp.view.places;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import br.com.ceducarneiro.weatherapp.R;
import br.com.ceducarneiro.weatherapp.controller.WeatherListener;
import br.com.ceducarneiro.weatherapp.ws.OpenWeather;
import butterknife.InjectView;
import br.com.ceducarneiro.weatherapp.base.ToolBarActvitiy;
import br.com.ceducarneiro.weatherapp.controller.ListenerController;
import br.com.ceducarneiro.weatherapp.controller.WeatherController;

public class AddPlaceActivity extends ToolBarActvitiy implements OnMapReadyCallback, GoogleMap.OnMapClickListener, WeatherListener {

    @InjectView(R.id.edPlace)
    protected EditText edPlace;

    @InjectView(R.id.edCountry)
    protected EditText edCountry;

    @InjectView(R.id.btSend)
    protected ImageButton btSend;

    GoogleMap googleMap;
    OpenWeather openWeather = null;

    boolean manualSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_place_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edCountry.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /* Empty */
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btSend.setVisibility((edPlace.length() > 0 && edCountry.length() > 0) ?
                        View.VISIBLE : View.GONE);
                openWeather = null;
            }

            @Override
            public void afterTextChanged(Editable s) {
                /* Empty */
            }
        };

        edPlace.addTextChangedListener(watcher);
        edCountry.addTextChangedListener(watcher);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ListenerController.getInstance().addWeatherListener(this);
    }

    @Override
    protected void onDestroy() {
        ListenerController.getInstance().removeWeatherListener(this);
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        LatLng myLocation = new LatLng(-23.5, -46.6);

        if (location != null) {
            myLocation = new LatLng(location.getLatitude(),
                    location.getLongitude());
        }

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 11));
        googleMap.setOnMapClickListener(this);

        onMapClick(myLocation);
    }

    public void onSendClick(View v) {
        if (openWeather != null) {
            addAndFinish();
        } else {
            String place = edPlace.getText().toString();
            String country = edCountry.getText().toString();

            manualSearch = true;

            WeatherController.getInstance().getWeatherByPlace(place, country);
        }
    }

    private void addAndFinish() {
        WeatherController.getInstance().createOrUpdatePlace(openWeather);

        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        WeatherController.getInstance().getWeatherByLatLng(latLng);
    }

    @Override
    public void onGetWeatherStart(List args) {
        showLoading(true);
    }

    @Override
    public void onGetWeatherFinish(List args) {
        showLoading(false);

        boolean error = true;

        if (args != null && args.size() == 1) {
            OpenWeather weather = (OpenWeather) args.get(0);

            if (weather != null) {
                String place = weather.name;
                String country = weather.system.country;

                if (!place.isEmpty() && country.length() == 2) {
                    edPlace.setText(place);
                    edCountry.setText(country);

                    error = false;
                    openWeather = weather;

                    LatLng location = new LatLng(weather.coordinates.latitude,
                            weather.coordinates.longitude);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 11));
                }
            }
        }

        if (manualSearch) {
            if (error) {
                Toast.makeText(this, getString(R.string.place_not_found), Toast.LENGTH_LONG).show();
            } else {
                addAndFinish();
            }
        }

        manualSearch = false;
    }
}
