package com.svp.infrastructure.common;

import android.view.View;

public class ViewExtensions {
    public static <T extends View> T findViewById(View view,int id) {
        return (T)view.findViewById(id);
    }
}
