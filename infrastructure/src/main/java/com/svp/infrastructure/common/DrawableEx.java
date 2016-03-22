package com.svp.infrastructure.common;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by Pasha on 10/19/2015.
 */
public class DrawableEx {
    public static Drawable getGray(Context context,int resource){
        Drawable ico = context.getResources().getDrawable(resource);
        ico.setAlpha(100);
        return ico;
    }
}
