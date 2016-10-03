package svp.com.dontmissstation.ui.model;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import svp.com.dontmissstation.R;

public class LineUIView {
    TextView linev;
    public LineUIView(Context activity, SubwayLineView line){
        linev = new TextView( activity);
        linev.setText(line.getName());
        linev.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        linev.setBackgroundColor(line.getColor());
        linev.setTextSize(20);
        linev.setTypeface(null, Typeface.BOLD);
        linev.setPadding(20,2,20,2);
        linev.setTextColor(activity.getResources().getColor(R.color.text_white));
        LinearLayoutCompat.LayoutParams param = new LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        param.setMargins(10,0,10,0);
        linev.setLayoutParams(param);
    }

    public void addTo(ViewGroup group){
        group.addView(linev);
    }
}
