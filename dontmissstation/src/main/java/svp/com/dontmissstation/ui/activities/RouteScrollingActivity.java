package svp.com.dontmissstation.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import svp.com.dontmissstation.R;
import svp.com.dontmissstation.presenters.RouteScrollingPresenter;

public class RouteScrollingActivity extends AppCompatActivityView<RouteScrollingPresenter> implements ICommutativeElement {

    @Override
    public ActivityOperationItem getOperation() {
        return ActivityOperationResult.RouteSelection;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<RouteScrollingActivity> {

        public ViewState(RouteScrollingActivity view) {
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
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_scrolling);//activity_route_toolbar
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
    }
}
