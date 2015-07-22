package com.pengrad.raspkursk.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * stas
 * 7/23/15
 */
public abstract class RecyclerViewListAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder<T>> {

    private ItemClickListener<T> itemClickListener;
    private List<T> data;

    public RecyclerViewListAdapter(ItemClickListener<T> itemClickListener) {
        this.itemClickListener = itemClickListener;
        setDataImpl(null);
    }

    private void setDataImpl(List<T> data) {
        this.data = data != null ? data : new ArrayList<>(0);
    }

    public RecyclerViewListAdapter<T> setData(List<T> data) {
        setDataImpl(data);
        notifyDataSetChanged();
        return this;
    }

    @Override
    public abstract RecyclerViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(RecyclerViewHolder<T> holder, int position) {
        holder.onBind(data.get(position), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
