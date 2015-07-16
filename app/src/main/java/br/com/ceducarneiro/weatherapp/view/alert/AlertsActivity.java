package br.com.ceducarneiro.weatherapp.view.alert;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.List;

import br.com.ceducarneiro.weatherapp.R;
import br.com.ceducarneiro.weatherapp.broadcast.AlarmReceiver;
import br.com.ceducarneiro.weatherapp.model.bean.AlertCondition;
import br.com.ceducarneiro.weatherapp.model.bean.AlertOption;
import butterknife.InjectView;
import butterknife.OnItemLongClick;
import butterknife.OnItemSelected;
import br.com.ceducarneiro.weatherapp.view.base.ToolBarActvitiy;
import br.com.ceducarneiro.weatherapp.model.bean.Alert;
import br.com.ceducarneiro.weatherapp.model.bean.Place;

public class AlertsActivity extends ToolBarActvitiy {

    public static final String PLACE_KEY = "place_id_key";

    @InjectView(R.id.optionsSpinner)
    Spinner optionsSpinner;

    @InjectView(R.id.conditionsSpinner)
    Spinner conditionsSpinner;

    @InjectView(R.id.edValue)
    EditText edValue;

    @InjectView(R.id.btAdd)
    ImageButton btAdd;

    @InjectView(R.id.listAlerts)
    ListView listAlerts;

    private int optionSelected;
    private int conditionSelected;
    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alerts_activity);

        place = Place.findById(Place.class, getIntent().getLongExtra(PLACE_KEY, 0));

        setPlaceholderMessage(R.string.alerts_placeholder_message);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.alerts_options_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionsSpinner.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this,
                R.array.alerts_conditions_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionsSpinner.setAdapter(adapter);

        edValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /* Empty */
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btAdd.setVisibility(edValue.getText().length() > 0 ?
                        View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                /* Empty */
            }
        });

        updateAlertsList(Alert.listAllWithPlace(place.getId()));
    }

    @OnItemLongClick(R.id.listAlerts)
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Alert alert = Alert.findById(Alert.class, id);

        AlertDialog dialog = new AlertDialog.Builder(
                new ContextThemeWrapper(this, R.style.AppTheme)).create();
        dialog.setMessage(getString(R.string.alert_exclude_confirmation));
        dialog.setCancelable(true);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
                alert.delete();
                updateAlertsList(Alert.listAllWithPlace(place.getId()));
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
                /* Empty */
            }
        });

        dialog.show();

        return true;
    }

    private void updateAlertsList(List<Alert> alertsList) {
        if (alertsList.size() > 0) {
            listAlerts.setAdapter(new AlertAdapter(alertsList));
            setPlaceholderVisible(false);
        } else {
            setPlaceholderVisible(true);
        }
    }

    @OnItemSelected(R.id.optionsSpinner)
    public void onOptionsItemSelected(AdapterView<?> parent, View view, int position, long id) {
        optionSelected = position;
    }

    @OnItemSelected(R.id.conditionsSpinner)
    public void onConditionItemSelected(AdapterView<?> parent, View view, int position, long id) {
        conditionSelected = position;
    }

    public void onAddClick(View v) {
        Alert alert = new Alert();

        alert.setOption(AlertOption.values()[optionSelected]);
        alert.setCondition(AlertCondition.values()[conditionSelected]);
        alert.setValue(Double.parseDouble(edValue.getText().toString()));
        alert.setPlace(place);

        alert.save();
        AlarmReceiver.scheduleAlarm(this);
        updateAlertsList(Alert.listAllWithPlace(place.getId()));
    }

}
