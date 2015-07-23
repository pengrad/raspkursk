package com.pengrad.raspkursk;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * stas
 * 7/24/15
 */
public class ThreadListAdapter extends BaseAdapter {

    private List<ThreadResponse.Stop> mStops;
    private LayoutInflater mInflater;

    private Station mStationFrom, mStationTo;
    private int positionFrom, positionTo;

    private int colorEnabled, colorDisabled;

    public ThreadListAdapter(Context context, Station stationFrom, Station stationTo) {
        mInflater = LayoutInflater.from(context);
        mStationFrom = stationFrom;
        mStationTo = stationTo;

        Resources resources = context.getResources();
        colorEnabled = resources.getColor(R.color.primary_text);
        colorDisabled = resources.getColor(R.color.secondary_text);
        setStops(null);
    }

    public void setStops(List<ThreadResponse.Stop> stops) {
        this.mStops = stops != null ? stops : new ArrayList<>(0);
        refreshPositions();
        notifyDataSetChanged();
    }

    private void refreshPositions() {
        for (int i = 0; i < mStops.size(); i++) {
            if (mStops.get(i).code().equals(mStationFrom.code)) positionFrom = i;
            if (mStops.get(i).code().equals(mStationTo.code)) positionTo = i;
        }
    }

    private boolean isEnabledStop(int position) {
        return position >= positionFrom && position <= positionTo;
    }

    @Override
    public int getCount() {
        return mStops.size();
    }

    @Override
    public Object getItem(int position) {
        return mStops.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_thread_stop, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.viewPoint = convertView.findViewById(R.id.view_point);
            viewHolder.textTime = (TextView) convertView.findViewById(R.id.text_time);
            viewHolder.textTitle = (TextView) convertView.findViewById(R.id.text_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ThreadResponse.Stop stop = mStops.get(position);
        String time = !TextUtils.isEmpty(stop.arrival) ? stop.arrival : stop.departure;
        viewHolder.textTime.setText(DateParser.timeNoSecs(time));
        viewHolder.textTitle.setText(stop.title());

        if (isEnabledStop(position)) {
            viewHolder.viewPoint.setEnabled(true);
            viewHolder.textTime.setTextColor(colorEnabled);
            viewHolder.textTitle.setTextColor(colorEnabled);
        } else {
            viewHolder.viewPoint.setEnabled(false);
            viewHolder.textTime.setTextColor(colorDisabled);
            viewHolder.textTitle.setTextColor(colorDisabled);
        }

        return convertView;
    }

    static class ViewHolder {
        View viewPoint;
        TextView textTime;
        TextView textTitle;
    }
}
