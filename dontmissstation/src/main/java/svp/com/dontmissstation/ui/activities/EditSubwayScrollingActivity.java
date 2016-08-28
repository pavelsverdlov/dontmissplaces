package svp.com.dontmissstation.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import java.util.Collection;
import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.com.dontmissstation.R;
import svp.com.dontmissstation.presenters.AddNewSubwayPresenter;
import svp.com.dontmissstation.ui.RecyclerViewEx;
import svp.com.dontmissstation.ui.model.SubwayLineView;
import svp.com.dontmissstation.ui.model.SubwayView;

public class EditSubwayScrollingActivity extends AppCompatActivityView<AddNewSubwayPresenter> implements ICommutativeElement, View.OnClickListener {

    @Override
    public ActivityOperationItem getOperation() {
        return ActivityOperationResult.AddNewSubway;
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

//        public ActivityPermissions getPermissions() {
//            return view.permissions;
//        }
    }

    public class SubwayLineRecyclerAdapter extends RecyclerView.Adapter<SubwayLineRecyclerAdapter.ViewHolder> {
        private final Context context;
        private final Vector<SubwayLineView> lines;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View v) {
                super(v);
                ((TextView)v.findViewById(R.id.activity_add_new_subway_line_template_startStation))
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

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
//            holder.mTextView.setText(mDataset[position]);
        }
        @Override
        public int getItemCount() {
            return lines.size();
        }
    }

//    @Bind(R.id.add_new_subway_actv_addNewSubwayLine) Button addNewSubwayLineBtn;
    @Bind(R.id.add_new_subway_actv_subwayLines) RecyclerView subwayLinesRecyclerView;
    @Bind(R.id.activity_edit_subway_country_edittext) EditText countryText;
    @Bind(R.id.activity_edit_subway_city_edittext) EditText cityText;


    private SubwayLineRecyclerAdapter linesAdapter;
    private SubwayView subway;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subway_scrolling);

        ButterKnife.bind(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(" ");
        AppBarLayout mAppBarLayout = (AppBarLayout)findViewById(R.id.app_bar);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(verticalOffset < -350){
                    collapsingToolbar.setTitle(cityText.getText() + " " + countryText.getText());
                } else {
                    collapsingToolbar.setTitle(" ");
                }
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        addNewSubwayLineBtn.setOnClickListener(this);

        RecyclerViewEx.setCustomSettings(subwayLinesRecyclerView,this);
    }

    public static AppBarLayout.ScrollingViewBehavior from(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params)
                .getBehavior();
        if (!(behavior instanceof AppBarLayout.ScrollingViewBehavior)) {
            throw new IllegalArgumentException(
                    "The view is not associated with BottomSheetBehavior");
        }
        return (AppBarLayout.ScrollingViewBehavior) behavior;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_new_subway_actv_addNewSubwayLine:
                getPresenter().openAddLineActivity();
                break;
        }
    }

    public void onStart(){
        super.onStart();
        subway = getPresenter().getSubway();
        linesAdapter = new SubwayLineRecyclerAdapter(this,subway.getLines());
        subwayLinesRecyclerView.setAdapter(linesAdapter);
    }
}
