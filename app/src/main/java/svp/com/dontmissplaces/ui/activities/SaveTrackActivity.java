package svp.com.dontmissplaces.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.svp.infrastructure.mvpvs.bundle.IBundleProvider;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.presenters.SaveTrackPresenter;
import svp.com.dontmissplaces.ui.ActivityCommutator;
import svp.com.dontmissplaces.ui.BaseBundleProvider;

import com.svp.infrastructure.mvpvs.bundle.BundleProvider;

public class SaveTrackActivity extends AppCompatActivityView<SaveTrackPresenter>
    implements ActivityCommutator.ICommutativeElement{

    @Override
    public ActivityCommutator.ActivityOperationResult getOperation() {
        return ActivityCommutator.ActivityOperationResult.SaveTrack;
    }

    @Override
    public Activity getActivity() { return this; }

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<SaveTrackActivity> {

        public ViewState(SaveTrackActivity view) {
            super(view);
        }

        public void setTrackName(CharSequence text){
            view.name.setText(text);
        }

        @Override
        protected void restore() { }

        @Override
        public Activity getActivity() {
            return view;
        }
    }

    @Bind(R.id.track_save_fab) FloatingActionButton fab;
    @Bind(R.id.activity_track_save_name_track) EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_track);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_track_save_toolbar);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().save();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_track_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_track_menu_delete_track:
                getPresenter().delete();
                break;
        }
        return true;
    }

}
