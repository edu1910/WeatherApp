package br.com.ceducarneiro.weatherapp.view.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import br.com.ceducarneiro.weatherapp.R;
import br.com.ceducarneiro.weatherapp.WeatherApplication;
import br.com.ceducarneiro.weatherapp.model.bean.Place;
import br.com.ceducarneiro.weatherapp.view.places.PlacesFragment;
import br.com.ceducarneiro.weatherapp.view.weather.WeatherFragment;

class PlacesPagerAdapter extends FragmentPagerAdapter {
    private PlacesFragment placesFragment;
    private WeatherFragment weatherFragment;

    public PlacesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return (position == 0) ? PlacesFragment.newInstance()
                : WeatherFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (position == 0) ? WeatherApplication.getInstance().getString(R.string.places)
                : WeatherApplication.getInstance().getString(R.string.weather);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        if (position == 0) placesFragment = (PlacesFragment) fragment;
        if (position == 1) weatherFragment = (WeatherFragment) fragment;
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position == 0) placesFragment = null;
        if (position == 1) weatherFragment = null;
        super.destroyItem(container, position, object);
    }

    public void updatePlacesList() {
        if (placesFragment != null)
            placesFragment.updatePlacesList();
    }

    public void updateWeatherDetail() {
        if (weatherFragment != null)
            weatherFragment.updateWeatherDetail();
    }

    public void updateWeatherDetail(Place place) {
        if (weatherFragment != null)
            weatherFragment.updateWeatherDetail(place);
    }

}
