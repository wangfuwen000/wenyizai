package com.wenyizai.wangfuwen.wenyizai.adapter;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by wangfuwen on 16/10/30.
 */

public abstract class PulmBaseAdapter<T> extends BaseAdapter {
    public List<T> items;

    public PulmBaseAdapter() {
        this.items = new ArrayList<>();
    }

    public PulmBaseAdapter(List<T> items) {
        this.items = items;
    }

    public void  addMoreItems(List<?> newItems, boolean isFirstLoad){
        if(isFirstLoad){
            this.items.clear();
        }
        this.items.addAll((Collection<? extends T>) newItems);
        notifyDataSetChanged();
    }

    public void  removedAllItems(){
        this.items.clear();
        notifyDataSetChanged();
    }
}
