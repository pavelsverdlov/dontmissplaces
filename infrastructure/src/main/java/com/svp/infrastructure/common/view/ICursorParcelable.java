package com.svp.infrastructure.common.view;

import android.database.Cursor;
import android.view.View;

public interface ICursorParcelable {
    void parse(Cursor cursor);
    void initView(View view);
}
