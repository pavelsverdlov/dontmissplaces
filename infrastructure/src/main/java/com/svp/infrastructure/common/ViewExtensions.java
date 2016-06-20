package com.svp.infrastructure.common;

import android.app.Activity;
import android.view.View;

public class ViewExtensions {
    public static <T extends View> T findViewById(View view,int id) {
        return (T)view.findViewById(id);
    }
    public static <T extends View> T findViewById(Activity view, int id) {
        return (T)view.findViewById(id);
    }
    public static <T extends View> T setOnClickListener(Activity view, int id, View.OnClickListener l) {
        T v = (T)view.findViewById(id);
        v.setOnClickListener(l);
        return v;
    }
    public static <T extends View> T setOnLongClickListener(Activity view, int id, View.OnLongClickListener l) {
        T v = (T)view.findViewById(id);
        v.setOnLongClickListener(l);
        return v;
    }
}
