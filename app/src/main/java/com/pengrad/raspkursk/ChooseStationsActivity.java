package com.pengrad.raspkursk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;

import java.util.List;

import rx.Observable;

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
    private List<Station> mStationList;

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

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        StationManager stationManager = new StationManager(getResources());
        mStationList = stationManager.getAllStations();

        Intent intent = getIntent();
        String stationFromCode, stationToCode;
        if (intent != null && intent.getAction().equals(ACTION)) {
            stationFromCode = intent.getStringExtra(EXTRA_STATION_FROM);
            stationToCode = intent.getStringExtra(EXTRA_STATION_TO);
        } else {
            stationFromCode = stationManager.getDefaultFromStation().code;
            stationToCode = stationManager.getDefaultToStation().code;
        }

        initPickers(stationManager, stationFromCode, stationToCode);
    }

    private void initPickers(StationManager stationManager, String stationFromCode, String stationToCode) {
        Station stationFrom = stationManager.getStationByCode(stationFromCode);
        Station stationTo = stationManager.getStationByCode(stationToCode);

        mPickerFrom = (NumberPicker) findViewById(R.id.picker_from);
        mPickerTo = (NumberPicker) findViewById(R.id.picker_to);

        mPickerFrom.setMinValue(0);
        mPickerFrom.setMaxValue(mStationList.size() - 1);
        for (View vv : mPickerFrom.getTouchables()) {
            vv.setFocusable(false);
        }

        mPickerTo.setMinValue(0);
        mPickerTo.setMaxValue(mStationList.size() - 1);
        for (View vv : mPickerTo.getTouchables()) {
            vv.setFocusable(false);
        }

        Observable.from(mStationList)
                .map(station -> station.title)
                .toList()
                .subscribe(titles -> {
                    String[] names = titles.toArray(new String[titles.size()]);
                    mPickerFrom.setDisplayedValues(names);
                    mPickerTo.setDisplayedValues(names);
                });

        mPickerFrom.setValue(mStationList.indexOf(stationFrom));
        mPickerTo.setValue(mStationList.indexOf(stationTo));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_choose_stations, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                doReturnStations();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void doReturnStations() {
        Station stationFrom = mStationList.get(mPickerFrom.getValue());
        Station stationTo = mStationList.get(mPickerTo.getValue());
        Intent intent = setIntentExtra(new Intent(), stationFrom.code, stationTo.code);
        setResult(RESULT_OK, intent);
        finish();
    }
}
