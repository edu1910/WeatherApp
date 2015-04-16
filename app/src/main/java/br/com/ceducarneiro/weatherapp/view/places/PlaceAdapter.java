package br.com.ceducarneiro.weatherapp.view.places;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import br.com.ceducarneiro.weatherapp.R;
import butterknife.ButterKnife;
import br.com.ceducarneiro.weatherapp.model.bean.Place;

public class PlaceAdapter extends BaseAdapter {

    private List<Place> placeList;

    public PlaceAdapter(List<Place> placeList) {
        this.placeList = placeList;
    }

    @Override
    public int getCount() {
        return placeList.size();
    }

    @Override
    public Object getItem(int position) {
        return placeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return placeList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Place place = (Place) getItem(position);
        ViewHolder holder;


        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            convertView = inflater.inflate(R.layout.place_item, parent, false);

            holder = new ViewHolder();
            holder.imgWeather = ButterKnife.findById(convertView, R.id.imgWeather);
            holder.txCity = ButterKnife.findById(convertView, R.id.txCity);
            holder.txWeather = ButterKnife.findById(convertView, R.id.txWeather);
            holder.txTemperature = ButterKnife.findById(convertView, R.id.txTemperature);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imgWeather.setImageResource(place.getWeather().getResIcon());
        holder.txCity.setText(place.getCity()+ ", " + place.getCountry().toUpperCase());
        holder.txWeather.setText(place.getWeather().getDescription());
        holder.txTemperature.setText(String.format(Locale.getDefault(), "%.1fº", place.getWeather().getTemperature()));

        return convertView;
    }

    static class ViewHolder {
        ImageView imgWeather;
        TextView txCity;
        TextView txWeather;
        TextView txTemperature;
    }

}
