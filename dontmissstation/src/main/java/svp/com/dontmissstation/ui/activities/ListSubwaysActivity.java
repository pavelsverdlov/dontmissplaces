package svp.com.dontmissstation.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import svp.com.dontmissstation.ui.model.SubwayView;

public class ListSubwaysActivity extends AppCompatActivityView<ListSubwaysPresenter> implements ICommutativeElement {
    @Override
    public ActivityOperationItem getOperation() {
        return null;
    }

    @Override
    public Activity getActivity() {
        return null;
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

    public class RecyclerCursorAdapter extends RecyclerView.Adapter<RecyclerCursorAdapter.ViewHolder> {
        private final Context context;
        private final SavedPlacesCursorAdapter cursorAdapter;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View v) {
                super(v);
            }
        }

        public RecyclerCursorAdapter(Context context, Cursor c) {
            this.context = context;
            cursorAdapter = new SavedPlacesCursorAdapter(context, c);
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = cursorAdapter.newView(context, cursorAdapter.getCursor(), parent);
            return new ViewHolder(v);
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            cursorAdapter.getCursor().moveToPosition(position);
            cursorAdapter.bindView(holder.itemView, context, cursorAdapter.getCursor());

        }
        @Override
        public int getItemCount() {
            return cursorAdapter.getCount();
        }
    }
    public class SavedPlacesCursorAdapter extends BaseCursorAdapter<SubwayCursorView> {
        public SavedPlacesCursorAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public ICursorParcelable createParcelableObject() {
            return new SubwayCursorView();
        }

        @Override
        public View getView(LayoutInflater inflater, ViewGroup parent) {
            return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        @Override
        public void onItemClick(View view, SubwayCursorView item) {
            ListSubwaysActivity.this.getPresenter().subwaySelected(item.getSubway());
        }
    }
    public static class SubwayCursorView implements ICursorParcelable {
        private TextView title;
        @Override
        public void parse(Cursor cursor) {
         //   this.update(new Place(cursor));
            title.setText("11");
        }

        @Override
        public void initView(View view) {
            title = ViewExtensions.findViewById(view, R.id.text1);
        }

        public SubwayView getSubway() {
            return null;
        }
    }

    @Bind(R.id.activity_list_subways_recyclerView) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_subways);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
        RecyclerCursorAdapter mAdapter = new RecyclerCursorAdapter(this, getPresenter().getSavedPlace());
        recyclerView.setAdapter(mAdapter);
    }

}
