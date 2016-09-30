package svp.com.dontmissstation.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.svp.infrastructure.common.ViewExtensions;
import com.svp.infrastructure.common.view.BaseCursorAdapter;
import com.svp.infrastructure.common.view.ICursorParcelable;
import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.com.dontmissstation.R;
import svp.com.dontmissstation.presenters.ListSubwaysPresenter;
import svp.com.dontmissstation.ui.RecyclerViewEx;
import svp.com.dontmissstation.ui.adapters.RecyclerCursorAdapter;
import svp.com.dontmissstation.ui.model.SubwayLineView;
import svp.com.dontmissstation.ui.model.SubwayView;

public class ListSubwaysActivity extends AppCompatActivityView<ListSubwaysPresenter> implements ICommutativeElement {
    @Override
    public ActivityOperationItem getOperation() {
        return ActivityOperationResult.ListSubways;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<ListSubwaysActivity> {

        public ViewState(ListSubwaysActivity view) {
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


    public class SavedPlacesCursorAdapter extends BaseCursorAdapter<SubwayCursorView> {
        public SavedPlacesCursorAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public ICursorParcelable createParcelableObject() {
            return new SubwayCursorView(ListSubwaysActivity.this.getPresenter().getSubways().get(0));
        }

        @Override
        public View getView(LayoutInflater inflater, ViewGroup parent) {
            return inflater.inflate(R.layout.activity_list_subways_subway_item_template, parent, false);
        }

        @Override
        public void onItemClick(View view, SubwayCursorView item) {
            ListSubwaysActivity.this.getPresenter().subwaySelected(item.getSubway());
        }
        @Override
        public int getCount() {
            return ListSubwaysActivity.this.getPresenter().getSubways().size();
        }
    }
    public static class SubwayCursorView implements ICursorParcelable {
        private final SubwayView subway;
        private TextView title;
        private LinearLayout linesLayout;
        private View view;

        public SubwayCursorView(SubwayView subwayView) {
            this.subway = subwayView;
        }

        public void parse(Cursor cursor) {
         //   this.update(new Place(cursor));
            title.setText(subway.getCity() + " " + subway.getCountry());

            for (SubwayLineView line : subway.getLines()) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMarginStart(10);
                layoutParams.setMarginEnd(10);

                TextView tv = new TextView(view.getContext());
                tv.setText(line.getName());
               // tv.setTextColor(line.getColor());
                tv.setTextSize(16);
                tv.setPadding(15,5,15,5);
                tv.setBackgroundColor(line.getColor());

                linesLayout.addView(tv, layoutParams);
            }
        }

        @Override
        public void initView(View view) {
            this.view = view;
            title = ViewExtensions.findViewById(view, R.id.activity_list_subways_subway_item_template_subway_name);
            linesLayout= ViewExtensions.findViewById(view,R.id.activity_list_subways_subway_item_template_subway_lines);
        }

        public SubwayView getSubway() {
            return subway;
        }
    }

    @Bind(R.id.activity_list_subways_recyclerView) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_subways);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_route_toolbar_id);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_route_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        RecyclerViewEx.setCustomSettings(recyclerView,this);
    }

    @Override
    public void onStart() {
        super.onStart();
        RecyclerCursorAdapter mAdapter = new RecyclerCursorAdapter(this,null,new SavedPlacesCursorAdapter(this, null));
        recyclerView.setAdapter(mAdapter);
    }

}
