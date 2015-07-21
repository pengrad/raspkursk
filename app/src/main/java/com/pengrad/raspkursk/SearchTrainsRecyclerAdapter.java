package com.pengrad.raspkursk;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * stas
 * 7/20/15
 */
public class SearchTrainsRecyclerAdapter extends RecyclerView.Adapter<SearchTrainsRecyclerAdapter.ViewHolder> {

    public static final int ITEM_LAYOUT_ID = R.layout.cardview_train;
    private List<SearchResponse.Thread> data;

    public SearchTrainsRecyclerAdapter() {
        setDataImpl(null);
    }

    public SearchTrainsRecyclerAdapter(@NonNull SearchResponse searchResponse) {
        setDataImpl(searchResponse.threads);
    }

    private void setDataImpl(List<SearchResponse.Thread> data) {
        this.data = data != null ? data : new ArrayList<>(0);
    }

    public void setData(SearchResponse searchResponse) {
        setDataImpl(searchResponse.threads);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(ITEM_LAYOUT_ID, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.onBind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textDeparture, textArrival;

        public ViewHolder(View itemView) {
            super(itemView);
            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            textDeparture = (TextView) itemView.findViewById(R.id.text_departure);
            textArrival = (TextView) itemView.findViewById(R.id.text_arrival);
        }

        public void onBind(SearchResponse.Thread thread) {
            textTitle.setText(thread.title());
            textDeparture.setText(thread.departure);
            textArrival.setText(thread.arrival);
        }
    }
}
