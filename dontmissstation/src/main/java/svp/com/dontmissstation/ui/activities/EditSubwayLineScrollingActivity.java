package svp.com.dontmissstation.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.svp.infrastructure.common.ViewExtensions;
import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;

import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

import butterknife.Bind;
import svp.com.dontmissstation.R;
import svp.com.dontmissstation.presenters.*;
import svp.com.dontmissstation.ui.model.LineUIView;
import svp.com.dontmissstation.ui.model.SubwayStationView;
import svp.com.dontmissstation.ui.model.SubwayLineView;

public class EditSubwayLineScrollingActivity extends EditScrollingActivity<EditSubwayLinePresenter> implements ICommutativeElement {

    @Override
    public ActivityOperationItem getOperation() {
        return ActivityOperationResult.EditSubwayLine;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<EditSubwayLineScrollingActivity> {

        public ViewState(EditSubwayLineScrollingActivity view) {
            super(view);
        }

        @Override
        protected void restore() {

        }

        @Override
        public void saveState() {

        }

        @Override
        public Activity getActivity() {
            return view;
        }

    }

    public class SubwayStationRecyclerAdapter extends RecyclerView.Adapter<SubwayStationRecyclerAdapter.ViewHolder> {
        private final Context context;
        public final Vector<SubwayStationView> stations;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private final TextView name;
            private final LinearLayout linesLayout;
            private int position;
            View view;

            public ViewHolder(View v) {
                super(v);
                this.view = v;
                name = ViewExtensions.findViewById(v,R.id.activity_add_new_subway_station_name_textview);
                linesLayout = ViewExtensions.findViewById(v,R.id.activity_add_new_subway_line_template_lines_layout);
                ViewExtensions.findViewById(v,R.id.activity_add_new_subway_line_template_up_btn).setOnClickListener(this);
                ViewExtensions.findViewById(v,R.id.activity_add_new_subway_line_template_down).setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int prev = position;
                switch (v.getId()){
                    case R.id.activity_add_new_subway_line_template_up_btn:
                        if (position == 0){ return; }
                        SubwayStationRecyclerAdapter.this.notifyItemMoved(prev,--position);
                        break;
                    case R.id.activity_add_new_subway_line_template_down:
                        if (position == stations.size()-1){ return; }
                        SubwayStationRecyclerAdapter.this.notifyItemMoved(prev,++position);
                        break;
                }
                Collections.swap(SubwayStationRecyclerAdapter.this.stations, prev, position);
                //update position other item
                ((ViewHolder)recyclerView.findViewHolderForAdapterPosition(prev)).position = prev;
            }

            public void bind(int p) {
                this.position = p;
                SubwayStationView station = stations.get(position);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditSubwayLineScrollingActivity.this
                                .getPresenter().openEditStationActivity(stations.get(position));
                    }
                });

                name.setText(station.getName());
                for (SubwayLineView line : station.getLines()) {
                    LineUIView linev = new LineUIView(view.getContext(), line);
                    linev.addTo(linesLayout);
                }
            }


        }
        public SubwayStationRecyclerAdapter(Context context, Collection<SubwayStationView> collection) {
            this.context = context;
            stations = new Vector<>(collection);
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subway_station_template, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(position);
        }
        @Override
        public int getItemCount() {
            return stations.size();
        }
    }

    @Bind(R.id.activity_edit_subway_line_name_edittext) EditText lineNameText;
    @Bind(R.id.activity_edit_subway_line_change_color_dtn) Button changeColorBtn;

    private SubwayLineView line;
    private SubwayStationRecyclerAdapter linesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_edit_subway_line_scrolling);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onApplyChangesClick() {
        getPresenter().saveStations(linesAdapter.stations);
    }

    @Override
    protected void onAddClick() {
        getPresenter().openStationListActivity();
    }

    @Override
    protected void onClickEventRoute(View v) {

    }

    @Override
    public String getToolbarTitle() {
        return lineNameText.getText().length() == 0 ? "Subway line" : lineNameText.getText().toString();
    }

    @Override
    public int getMaxCollapsingHeight() {
        return 450;
    }


    @Override
    public void onStart(){
        super.onStart();
        line = getPresenter().getLine();
        linesAdapter = new SubwayStationRecyclerAdapter(this,line.getStations());
        setAdapter(linesAdapter);

        lineNameText.setText(line.getName());
        changeColorBtn.setBackgroundColor(line.getColor());


    }


}
