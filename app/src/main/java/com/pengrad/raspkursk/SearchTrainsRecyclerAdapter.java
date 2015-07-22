package com.pengrad.raspkursk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pengrad.raspkursk.recyclerview.ItemClickListener;
import com.pengrad.raspkursk.recyclerview.RecyclerViewHolder;
import com.pengrad.raspkursk.recyclerview.RecyclerViewListAdapter;

/**
 * stas
 * 7/20/15
 */
public class SearchTrainsRecyclerAdapter extends RecyclerViewListAdapter<SearchResponse.Thread> {

    public static final int ITEM_LAYOUT_ID = R.layout.cardview_train;

    public SearchTrainsRecyclerAdapter(ItemClickListener<SearchResponse.Thread> itemClickListener) {
        super(itemClickListener);
    }

    @Override
    public RecyclerViewHolder<SearchResponse.Thread> onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(ITEM_LAYOUT_ID, parent, false);
        return new ViewHolder(v);
    }

    public static class ViewHolder extends RecyclerViewHolder<SearchResponse.Thread> {
        TextView textTitle, textDeparture, textArrival;

        public ViewHolder(View itemView) {
            super(itemView);
            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            textDeparture = (TextView) itemView.findViewById(R.id.text_departure);
            textArrival = (TextView) itemView.findViewById(R.id.text_arrival);
        }

        @Override
        public void onBindItem(SearchResponse.Thread thread) {
            textTitle.setText(thread.title());
            textDeparture.setText(DateParser.timeNoSecs(thread.departure));
            textArrival.setText(DateParser.timeNoSecs(thread.arrival));
        }
    }
}
