package com.pengrad.raspkursk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import rx.Observable;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ThreadActivity extends AppCompatActivity {

    public static final String INTENT_ACTION = "SHOW_ROUTE";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_UID = "uid";
    public static final String EXTRA_FROM = "from";
    public static final String EXTRA_TO = "to";
    private SwipeRefreshLayout mRefreshLayout;
    private String mUid;
    private YandexRaspApi mYandexRaspApi;
    private ThreadListAdapter mThreadListAdapter;
    private Subscription mSubscription;
    private StationManager mStationManager;

    public static Intent getIntent(Context context, String title, String uid, Station stationFrom, Station stationTo) {
        return new Intent(context, ThreadActivity.class)
                .setAction(INTENT_ACTION)
                .putExtra(EXTRA_TITLE, title)
                .putExtra(EXTRA_UID, uid)
                .putExtra(EXTRA_FROM, stationFrom.code)
                .putExtra(EXTRA_TO, stationTo.code);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent == null || !intent.getAction().equals(INTENT_ACTION)) {
            return;
        }

        String title = intent.getStringExtra(EXTRA_TITLE);
        mUid = intent.getStringExtra(EXTRA_UID);
        String codeFrom = intent.getStringExtra(EXTRA_FROM);
        String codeTo = intent.getStringExtra(EXTRA_TO);

        setTitle(title);

        mStationManager = new StationManager(getResources());
        Station stationFrom = mStationManager.getStationByCode(codeFrom);
        Station stationTo = mStationManager.getStationByCode(codeTo);

        mYandexRaspApi = App.getYandexRaspApi();

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mRefreshLayout.setOnRefreshListener(this::doGetThreadInfo);
        mRefreshLayout.post(this::doGetThreadInfo);

        mThreadListAdapter = new ThreadListAdapter(this, stationFrom, stationTo);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(mThreadListAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unsubscribe();
    }

    private void doGetThreadInfo() {
        unsubscribe();
        mRefreshLayout.setRefreshing(true);
        mSubscription = AppObservable.bindActivity(this, threadRequest())
                .map(response -> response.updateTitles(mStationManager.getStationsMap()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(throwable -> new ThreadResponse())
                .subscribe(this::onThreadResponse);
    }

    private void onThreadResponse(ThreadResponse response) {
        mRefreshLayout.setRefreshing(false);
        mThreadListAdapter.setStops(response.stops);
    }

    private Observable<ThreadResponse> threadRequest() {
        return mYandexRaspApi.thread(mUid);
    }

    private void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
