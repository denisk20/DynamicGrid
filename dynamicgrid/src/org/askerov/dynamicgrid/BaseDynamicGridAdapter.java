package org.askerov.dynamicgrid;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: alex askerov
 * Date: 9/7/13
 * Time: 10:49 PM
 */
public abstract class BaseDynamicGridAdapter extends AbstractDynamicGridAdapter {
    private Context mContext;

    private ArrayList<Object> mItems = new ArrayList<Object>();
    private int mColumnCount;

    protected BaseDynamicGridAdapter(Context context, int columnCount) {
        this.mContext = context;
        this.mColumnCount = columnCount;
    }

    public BaseDynamicGridAdapter(Context context, List<?> items, int columnCount) {
        mContext = context;
        mColumnCount = columnCount;
        init(items);
    }

    private void init(List<?> items) {
        addAllStableId(items);
        this.mItems.addAll(items);
    }


    public void set(List<?> items) {
        clear();
        init(items);
        notifyDataSetChanged();
    }

    public void setItemAtPosition(Object item, int position) {
        removeStableID(mItems.get(position));
        mItems.set(position, item);
        addStableId(item);
        notifyDataSetChanged();
    }

    public void clear() {
        clearStableIdMap();
        mItems.clear();
        notifyDataSetChanged();
    }

    public void add(Object item) {
        addStableId(item);
        mItems.add(item);
        notifyDataSetChanged();
    }

    public void add(int position, Object item) {
        addStableId(item);
        mItems.add(position, item);
        notifyDataSetChanged();
    }

    public void add(List<?> items) {
        addAllStableId(items);
        this.mItems.addAll(items);
        notifyDataSetChanged();
    }


    public void remove(Object item) {
        mItems.remove(item);
        removeStableID(item);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getColumnCount() {
        return mColumnCount;
    }

    public void setColumnCount(int columnCount) {
        this.mColumnCount = columnCount;
        notifyDataSetChanged();
    }

    @Override
    public void reorderItems(int originalPosition, int newPosition) {
        if (newPosition < getCount()) {
            DynamicGridUtils.reorder(mItems, originalPosition, newPosition);
            notifyDataSetChanged();
        }
    }

    @Override
    public Map<Integer, Integer> getPositiotChangeMap() {
        HashMap<Integer, Integer> positionsChangeMap = new HashMap<Integer, Integer>();
        ArrayList<Object> copy = new ArrayList<Object>(mItems);
        Collections.shuffle(copy);
        for(int i = 0; i < copy.size(); i++) {
            positionsChangeMap.put(i, copy.indexOf(mItems.get(i)));
        }

        //need to call this
        notifyDataSetChanged();

        return positionsChangeMap;
    }

    @Override
    public void doShuffle(Map<Integer, Integer> positionsChangeMap) {
        ArrayList<Object> copy = new ArrayList<Object>(mItems);
        for(Map.Entry<Integer, Integer> pos: positionsChangeMap.entrySet()) {
            mItems.set(pos.getValue(), copy.get(pos.getKey()));
        }

        notifyDataSetChanged();
    }

    public List<Object> getItems() {
        return mItems;
    }

    protected Context getContext() {
        return mContext;
    }
}
