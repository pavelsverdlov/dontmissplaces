package com.svp.infrastructure.common.view;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseCursorAdapter<T extends ICursorParcelable > extends CursorAdapter {
    private final LayoutInflater inflater;

    public BaseCursorAdapter(Context context, Cursor c) {
        super(context, c);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final View item = getView(inflater,parent);
        ICursorParcelable obj = createParcelableObject();
        obj.initView(item);
        item.setTag(obj);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v,(T)v.getTag());
            }
        });

        return item;
    }

    @Override
    public void bindView(View convertView, Context context, Cursor cursor) {
        ICursorParcelable obj = (ICursorParcelable) convertView.getTag();
        obj.parse(cursor);
    }

    public abstract ICursorParcelable createParcelableObject();
    //inflater.inflate(R.layout.activity_history_tracks_item_template, parent, false)
    public abstract View getView(LayoutInflater inflater, ViewGroup parent);

    public abstract void onItemClick(View view,T item);
}
