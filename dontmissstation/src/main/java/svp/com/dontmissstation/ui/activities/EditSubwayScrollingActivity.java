package svp.com.dontmissstation.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.svp.infrastructure.common.ViewExtensions;
import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;

import java.util.Collection;
import java.util.Vector;

import butterknife.Bind;
import svp.com.dontmissstation.R;
import svp.com.dontmissstation.presenters.EditSubwayPresenter;
import svp.com.dontmissstation.ui.model.SubwayLineView;
import svp.com.dontmissstation.ui.model.SubwayView;

public class EditSubwayScrollingActivity extends EditScrollingActivity<EditSubwayPresenter> implements ICommutativeElement {

    @Override
    public ActivityOperationItem getOperation() {
        return ActivityOperationResult.EditNewSubway;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<EditSubwayScrollingActivity> {

        public ViewState(EditSubwayScrollingActivity view) {
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

    public class SubwayLineRecyclerAdapter extends RecyclerView.Adapter<SubwayLineRecyclerAdapter.ViewHolder> {
        private final Context context;
        private final Vector<SubwayLineView> lines;

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView endStation;
            private final TextView startStation;
            private final TextView lineName;
            private int position;
            public ViewHolder(View v) {
                super(v);
                startStation = ViewExtensions.findViewById(v,R.id.activity_add_new_subway_line_template_startStation);
                endStation = ViewExtensions.findViewById(v,R.id.activity_add_new_subway_line_template_endStation);
                lineName = ViewExtensions.findViewById(v,R.id.activity_add_new_subway_line_template_line_name);
            }

            public void bind(int position) {
                this.position = position;
                SubwayLineView line = lines.get(position);
                startStation.setText(line.getStartStation().getName());
                startStation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditSubwayScrollingActivity.this.getPresenter()
                                .openEditLineActivity(new SubwayLineView());
                    }
                });
            }
        }
        public SubwayLineRecyclerAdapter(Context context, Collection<SubwayLineView> collection) {
            this.context = context;
            lines = new Vector<>(collection);
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_add_new_subway_line_template, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(position);
//            holder.mTextView.setText(mDataset[position]);
        }
        @Override
        public int getItemCount() {
            return lines.size();
        }
    }

    @Bind(R.id.activity_edit_subway_country_edittext) EditText countryText;
    @Bind(R.id.activity_edit_subway_city_edittext) EditText cityText;

    private SubwayLineRecyclerAdapter linesAdapter;
    private SubwayView subway;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_edit_subway_scrolling);
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void onAddClick(){
        getPresenter().openEditLineActivity(new SubwayLineView());
    }
    @Override
    protected void onApplyChangesClick(){

    }
    @Override
    public String getToolbarTitle(){
        CharSequence city = cityText.getText().length() == 0 ? "city" : cityText.getText();
        CharSequence country = countryText.getText().length() == 0 ? "country" : countryText.getText();
        return city + " " + country;
    }

    @Override
    public int getMaxCollapsingHeight() {
        return 350;
    }

    @Override
    public void onClickRoute(View v) {
        switch (v.getId()){
            case R.id.activity_edit_add_line_fab:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onStart(){
        super.onStart();
        subway = getPresenter().getSubway();
        linesAdapter = new SubwayLineRecyclerAdapter(this,subway.getLines());
        setAdapter(linesAdapter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
