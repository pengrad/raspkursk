package com.pengrad.raspkursk.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * stas
 * 7/23/15
 */
public abstract class RecyclerViewHolder<T> extends RecyclerView.ViewHolder {

    public RecyclerViewHolder(View itemView) {
        super(itemView);
    }

    public void onBind(T item, ItemClickListener<T> itemClickListener) {
        if (itemClickListener != null) {
            super.itemView.setOnClickListener(v -> itemClickListener.onItemClick(item));
        }
        onBindItem(item);
    }

    public abstract void onBindItem(T item);
}
