package svp.com.dontmissstation.ui.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.svp.infrastructure.mvpvs.presenter.Presenter;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.com.dontmissstation.R;
import svp.com.dontmissstation.ui.RecyclerViewEx;

public abstract class EditScrollingActivity<P extends Presenter> extends AppCompatActivityView<P>  implements
        View.OnClickListener, AppBarLayout.OnOffsetChangedListener, KeyboardVisibilityEventListener {

    @Bind(R.id.activity_edit_collapsing_toolbar_layout) CollapsingToolbarLayout collapsingToolbar;

    @Bind(R.id.activity_edit_add_line_fab) FloatingActionButton addFab;
    @Bind(R.id.activity_edit_apply_changes_fab) FloatingActionButton applyChangesFab;

    @Bind(R.id.actvity_edit_recycle_view) RecyclerView recyclerView;

    @Bind(R.id.activity_edit_subway_scrolling_items_count) AppCompatTextView itemsCountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_route_toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppBarLayout mAppBarLayout = (AppBarLayout)findViewById(R.id.activity_route_app_bar);

        collapsingToolbar.setTitle(" ");

        RecyclerViewEx.setCustomSettings(recyclerView,this);
        KeyboardVisibilityEvent.setEventListener(this,this);
        applyChangesFab.setOnClickListener(this);
        addFab.setOnClickListener(this);
        mAppBarLayout.addOnOffsetChangedListener(this);

    }
    @Override
    public void onVisibilityChanged(boolean isOpen) {
        addFab.setVisibility(isOpen ? View.GONE : View.VISIBLE);
    }
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if(verticalOffset < -getMaxCollapsingHeight()){
            collapsingToolbar.setTitle(getToolbarTitle());
        } else {
            collapsingToolbar.setTitle(" ");
        }
    }
    @Override
    final public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_edit_add_line_fab:
                onAddClick();
                break;
            case R.id.activity_edit_apply_changes_fab:
                onApplyChangesClick();
                break;
            default:
                onClickRoute(v);
                break;
        }
    }

    protected void setAdapter(RecyclerView.Adapter adapter) {
        itemsCountText.setText("("+adapter.getItemCount()+")");
        recyclerView.setAdapter(adapter);
    }

    protected abstract void onApplyChangesClick();

    protected abstract void onAddClick();

    protected abstract void onClickRoute(View v);

    public abstract String getToolbarTitle();

    public abstract int getMaxCollapsingHeight();
}
