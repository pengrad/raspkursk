package com.pengrad.raspkursk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.pengrad.raspkursk.recyclerview.ItemClickListener;

import rx.Observable;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements ItemClickListener<SearchResponse.Thread> {

    public static final String TAG = "MainActivity";
    public static final int REQUEST_CODE_CHOOSE_STATIONS = 120;
    public static final String KEY_STATION_FROM = "KEY_STATION_FROM";
    public static final String KEY_STATION_TO = "KEY_STATION_TO";

    private YandexRaspApi mYandexRaspApi;
    private SearchTrainsRecyclerAdapter mTrainsAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Station mStationFrom, mStationTo;
    private TextView mEditTextFrom;
    private TextView mEditTextTo;

    private StationManager mStationManager;
    private View mSwitchView;
    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Электрички Курска");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSwitchView = findViewById(R.id.button_switch_stations);
        mSwitchView.setOnClickListener(v -> doSwitchStations());
        findViewById(R.id.button_find).setOnClickListener(v -> doSearch());

        mEditTextFrom = (TextView) findViewById(R.id.edittext_from);
        mEditTextTo = (TextView) findViewById(R.id.edittext_to);
        mEditTextFrom.setOnClickListener(v -> doChooseStations());
        mEditTextTo.setOnClickListener(v -> doChooseStations());

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this::doSearch);

        mTrainsAdapter = new SearchTrainsRecyclerAdapter(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mTrainsAdapter);

        mYandexRaspApi = App.getYandexRaspApi();
        mStationManager = new StationManager(getResources());
        loadLastUsedStations();
        updateStationTitles();
        doSearch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHOOSE_STATIONS && resultCode == RESULT_OK) {
            String stationFrom = data.getStringExtra(ChooseStationsActivity.EXTRA_STATION_FROM);
            String stationTo = data.getStringExtra(ChooseStationsActivity.EXTRA_STATION_TO);

            mStationFrom = mStationManager.getStationByCode(stationFrom);
            mStationTo = mStationManager.getStationByCode(stationTo);
            updateStationTitles();
            doSearch();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unsubscribe();
        saveLastUsedStations();
    }

    private void saveLastUsedStations() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit()
                .putString(KEY_STATION_FROM, mStationFrom.code)
                .putString(KEY_STATION_TO, mStationTo.code)
                .apply();
    }

    private void loadLastUsedStations() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String codeFrom = preferences.getString(KEY_STATION_FROM, mStationManager.getDefaultFromStationCode());
        String codeTo = preferences.getString(KEY_STATION_TO, mStationManager.getDefaultToStationCode());

        mStationFrom = mStationManager.getStationByCode(codeFrom);
        mStationTo = mStationManager.getStationByCode(codeTo);
    }

    private void doChooseStations() {
        Intent intent = ChooseStationsActivity.getIntent(this, mStationFrom, mStationTo);
        startActivityForResult(intent, REQUEST_CODE_CHOOSE_STATIONS);
    }

    private void doSwitchStations() {
        Station _from = mStationFrom;
        mStationFrom = mStationTo;
        mStationTo = _from;
        rotateAnimation(mSwitchView);
        updateStationTitles();
        doSearch();
    }

    private void updateStationTitles() {
        mEditTextFrom.setText(mStationFrom.title);
        mEditTextTo.setText(mStationTo.title);
    }

    private void doSearch() {
        unsubscribe();
        mSwipeRefreshLayout.setRefreshing(true);
        mSubscription = AppObservable.bindActivity(this, searchRequest())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(throwable -> new SearchResponse())
                .subscribe(this::onSearchResponse);
    }

    private void onSearchResponse(SearchResponse searchResponse) {
        mSwipeRefreshLayout.setRefreshing(false);
        mTrainsAdapter.setData(searchResponse.threads);
    }

    private Observable<SearchResponse> searchRequest() {
        return mYandexRaspApi.search(mStationFrom.code, mStationTo.code, null);
    }

    private void rotateAnimation(View view) {
        RotateAnimation animation = new RotateAnimation(0, 180, view.getWidth() / 2, view.getHeight() / 2);
        animation.setDuration(250); // duration in ms
        animation.setRepeatCount(0);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    @Override
    public void onItemClick(SearchResponse.Thread item) {
        startActivity(ThreadActivity.getIntent(this, item.title(), item.uid(), mStationFrom, mStationTo));
    }

    private void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
