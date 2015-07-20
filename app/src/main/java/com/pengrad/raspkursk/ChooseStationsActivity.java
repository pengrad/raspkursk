package com.pengrad.raspkursk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;

import java.util.Collection;

/**
 * stas
 * 7/20/15
 */
public class ChooseStationsActivity extends AppCompatActivity {

    public static final String ACTION = "choose_stations";
    public static final String EXTRA_STATION_FROM = "stationFrom";
    public static final String EXTRA_STATION_TO = "stationTo";
    private NumberPicker mPickerFrom;
    private NumberPicker mPickerTo;
    private Station[] mStations;

    public static Intent getIntent(Context context, Station stationFrom, Station stationTo) {
        Intent intent = new Intent(context, ChooseStationsActivity.class).setAction(ACTION);
        return setIntentExtra(intent, stationFrom.code, stationTo.code);
    }

    public static Intent setIntentExtra(Intent intent, String stationFrom, String stationTo) {
        return intent.putExtra(EXTRA_STATION_FROM, stationFrom).putExtra(EXTRA_STATION_TO, stationTo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Выбор станций");
        setContentView(R.layout.activity_choose_stations);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StationManager stationManager = new StationManager(getResources());

        Intent intent = getIntent();
        String stationFromCode, stationToCode;
        if (intent != null && intent.getAction().equals(ACTION)) {
            stationFromCode = intent.getStringExtra(EXTRA_STATION_FROM);
            stationToCode = intent.getStringExtra(EXTRA_STATION_TO);
        } else {
            stationFromCode = stationManager.getDefaultFromStation().code;
            stationToCode = stationManager.getDefaultToStation().code;
        }
        Collection<Station> stationCollection = stationManager.getAllStations();
        mStations = stationCollection.toArray(new Station[stationCollection.size()]);

        int stationFromIndex = 0, stationToIndex = 0;

        String[] stationNames = new String[mStations.length];
        for (int i = 0; i < mStations.length; i++) {
            stationNames[i] = mStations[i].title;
            String code = mStations[i].code;
            if (code.equals(stationFromCode)) {
                stationFromIndex = i;
            } else if (code.equals(stationToCode)) {
                stationToIndex = i;
            }
        }

        mPickerFrom = (NumberPicker) findViewById(R.id.picker_from);
        mPickerTo = (NumberPicker) findViewById(R.id.picker_to);

        mPickerFrom.setMinValue(0);
        mPickerFrom.setMaxValue(stationNames.length - 1);
        mPickerFrom.setValue(stationFromIndex);
        mPickerFrom.setDisplayedValues(stationNames);
        for (View vv : mPickerFrom.getTouchables()) {
            vv.setFocusable(false);
        }

        mPickerTo.setMinValue(0);
        mPickerTo.setMaxValue(stationNames.length - 1);
        mPickerTo.setValue(stationToIndex);
        mPickerTo.setDisplayedValues(stationNames);
        for (View vv : mPickerTo.getTouchables()) {
            vv.setFocusable(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_choose_stations, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_done) {
            doReturnStations();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doReturnStations() {
        int stationFromIndex = mPickerFrom.getValue();
        int stationToIndex = mPickerTo.getValue();
        Intent intent = setIntentExtra(new Intent(), mStations[stationFromIndex].code, mStations[stationToIndex].code);
        setResult(RESULT_OK, intent);
        finish();
    }
}
