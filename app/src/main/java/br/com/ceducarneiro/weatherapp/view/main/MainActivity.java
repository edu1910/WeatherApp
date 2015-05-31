package br.com.ceducarneiro.weatherapp.view.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;

import br.com.ceducarneiro.weatherapp.model.bean.Alert;
import io.fabric.sdk.android.Fabric;
import java.util.List;

import br.com.ceducarneiro.weatherapp.R;
import br.com.ceducarneiro.weatherapp.broadcast.AlarmReceiver;
import br.com.ceducarneiro.weatherapp.controller.SyncListener;
import br.com.ceducarneiro.weatherapp.model.bean.Place;
import br.com.ceducarneiro.weatherapp.view.alert.AlertsActivity;
import butterknife.InjectView;
import butterknife.Optional;
import br.com.ceducarneiro.weatherapp.base.ToolBarActvitiy;
import br.com.ceducarneiro.weatherapp.controller.ListenerController;
import br.com.ceducarneiro.weatherapp.service.SyncService;
import br.com.ceducarneiro.weatherapp.view.places.AddPlaceActivity;
import br.com.ceducarneiro.weatherapp.view.places.PlacesFragment;
import br.com.ceducarneiro.weatherapp.view.weather.WeatherFragment;

public class MainActivity extends ToolBarActvitiy implements PlacesFragment.PlacesListener,
        WeatherFragment.WeatherListener, SyncListener {

    public static final String ALERT_KEY = "alert_key";
    public static final String PLACE_KEY = "place_key";

    private static final int ADD_PLACE_REQUEST = 1;

    @Optional
    @InjectView(R.id.pager)
    protected ViewPager pager;

    private PlacesFragment placesFragment;
    private WeatherFragment weatherFragment;
    private PlacesPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fabric.with(this, new Crashlytics());

        super.onCreate(savedInstanceState);
        ListenerController.getInstance().addSyncListener(this);

        setContentView(R.layout.main_activity);

        loadView();

        boolean isAlert = getIntent().getBooleanExtra(ALERT_KEY, false);

        if (isAlert && (savedInstanceState == null)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Place place = Place.findById(Place.class, getIntent().getLongExtra(PLACE_KEY, 0));
                    onPlaceClicked(place);
                }
            }, 500);
        }

        AlarmReceiver.scheduleAlarm(this);
    }

    @Override
    protected void onDestroy() {
        ListenerController.getInstance().removeSyncListener(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_update) {
            if (!SyncService.isRunning()) {
                startService(new Intent(this, SyncService.class));
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadView() {
        if (isTablet()) {
            loadTablet();
        } else {
            loadSmartphone();
        }
    }

    private void loadSmartphone() {
        pagerAdapter = new PlacesPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
    }

    private void loadTablet() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.list, (placesFragment = PlacesFragment.newInstance()))
                .commitAllowingStateLoss();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail, (weatherFragment = WeatherFragment.newInstance()))
                .commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (!isTablet()) {
            if (pager.getCurrentItem() == 0) {
                super.onBackPressed();
            } else {
                pager.setCurrentItem(pager.getCurrentItem() - 1);
            }
        }
    }

    @Override
    public void onPlaceClicked(Place place) {
        if (isTablet()) {
            weatherFragment.updateWeatherDetail(place);
        } else {
            pagerAdapter.updateWeatherDetail(place);
            pager.setCurrentItem(1);
        }
    }

    @Override
    public void onPlaceLongClicked(final Place place) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setMessage(getString(R.string.place_exclude_confirmation));
        dialog.setCancelable(true);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {

                List<Alert> alerts = Alert.listAllWithPlace(place.getId());

                if (alerts != null) {
                    for (Alert alert : alerts) {
                        alert.delete();
                    }
                }

                place.delete();

                if (isTablet()) {
                    placesFragment.updatePlacesList();
                    weatherFragment.updateWeatherDetail();
                } else {
                    pagerAdapter.updatePlacesList();
                    pagerAdapter.updateWeatherDetail();
                }
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
                /* Empty */
            }
        });

        dialog.show();
    }

    @Override
    public void onAddClicked() {
        Intent it = new Intent(this, AddPlaceActivity.class);
        startActivityForResult(it, ADD_PLACE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_PLACE_REQUEST && resultCode == RESULT_OK) {
            if (isTablet()) {
                placesFragment.updatePlacesList();
            } else {
                pagerAdapter.updatePlacesList();
            }
        }
    }

    @Override
    public void onAlertsClicked(Place place) {
        Intent it = new Intent(this, AlertsActivity.class);

        it.putExtra(AlertsActivity.PLACE_KEY, place.getId());

        startActivity(it);
    }

    @Override
    public void onSyncStart(List args) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showLoading(true);
            }
        });
    }

    @Override
    public void onSyncFinish(List args) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isTablet()) {
                    placesFragment.updatePlacesList();
                    weatherFragment.updateWeatherDetail();
                } else {
                    pagerAdapter.updatePlacesList();
                    pagerAdapter.updateWeatherDetail();
                }

                showLoading(false);
            }
        });
    }

}
