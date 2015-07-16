package br.com.ceducarneiro.weatherapp.view.places;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import br.com.ceducarneiro.weatherapp.R;
import br.com.ceducarneiro.weatherapp.view.base.ToolBarFragment;
import br.com.ceducarneiro.weatherapp.model.bean.Place;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

public class PlacesFragment extends ToolBarFragment implements SearchView.OnQueryTextListener {

    @InjectView(R.id.listPlaces)
    ListView listPlaces;

    SearchView searchView;
    MenuItem searchItem;

    private PlacesListener listener;

    public static PlacesFragment newInstance() {
        return new PlacesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.places_list, null);

        ButterKnife.inject(this, view);
        setPlaceholderMessage(getString(R.string.places_placeholder_message));

        if (isTablet())
            placeholder.setBackgroundColor(0xCCCCCC);

        updatePlacesList();

        return view;
    }

    public void updatePlacesList() {
        String filter = "";

        if (searchView != null) {
            filter = searchView.getQuery().toString();
        }

        updatePlacesList(Place.filterByCity(filter));
    }

    private void updatePlacesList(List<Place> placeList) {
        if (placeList.size() > 0) {
            listPlaces.setAdapter(new PlaceAdapter(placeList));
            setPlaceholderVisible(false);
        } else {
            listPlaces.setAdapter(null);
            setPlaceholderVisible(true);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        listener = (PlacesListener) getActivity();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.places_menu, menu);
        searchItem = menu.findItem(R.id.action_search);

        if (searchItem != null) {
            searchView = (SearchView) MenuItemCompat
                    .getActionView(searchItem);

            if (searchView != null)
                searchView.setOnQueryTextListener(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = false;

        if (item.getItemId() == R.id.action_add) {
            listener.onAddClicked();
            result = true;
        }

        return result;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        updatePlacesList(Place.filterByCity(s));
        return true;
    }

    public interface PlacesListener {
        void onPlaceClicked(Place place);
        void onPlaceLongClicked(Place place);
        void onAddClicked();
    }

    @OnItemClick(R.id.listPlaces)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.setSelected(true);
        listener.onPlaceClicked(Place.findById(Place.class, id));
    }

    @OnItemLongClick(R.id.listPlaces)
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        listener.onPlaceLongClicked(Place.findById(Place.class, id));
        return true;
    }

}
