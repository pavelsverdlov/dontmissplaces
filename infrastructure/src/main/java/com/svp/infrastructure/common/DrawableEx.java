package com.svp.infrastructure.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;

import java.io.InputStream;

/**
 * Created by Pasha on 10/19/2015.
 */
public class DrawableEx {
    public static Drawable getGray(Context context,int resource){
        Drawable ico = context.getResources().getDrawable(resource);
        ico.setAlpha(100);
        return ico;
    }
    public static void changeColor(Drawable drawable, int colorInt){
        int red   = (colorInt & 0xFF0000) / 0xFFFF;
        int green = (colorInt & 0xFF00) / 0xFF;
        int blue  = colorInt & 0xFF;

        float[] matrix = { 0, 0, 0, 0, red,
                0, 0, 0, 0, green,
                0, 0, 0, 0, blue,
                0, 0, 0, 1, 0 };

        ColorFilter colorFilter = new ColorMatrixColorFilter(matrix);
        drawable.setColorFilter(colorFilter);
    }

    public static Bitmap getBitmapFromDrawableRes(Context context, int resId){
        InputStream is = context.getResources().openRawResource(resId);
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }
}
