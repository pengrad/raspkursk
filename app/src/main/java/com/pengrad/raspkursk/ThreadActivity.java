package com.pengrad.raspkursk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class ThreadActivity extends AppCompatActivity {

    public static final String INTENT_ACTION = "SHOW_ROUTE";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_UID = "uid";
    public static final String EXTRA_FROM = "from";
    public static final String EXTRA_TO = "to";

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
        String uid = intent.getStringExtra(EXTRA_UID);
        String codeFrom = intent.getStringExtra(EXTRA_FROM);
        String codeTo = intent.getStringExtra(EXTRA_TO);

        setTitle(title);

        StationManager stationManager = new StationManager(getResources());

        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText(uid + "  " + stationManager.getStationByCode(codeFrom).title + "  " + stationManager.getStationByCode(codeTo).title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
