package svp.com.dontmissplaces.ui.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.svp.infrastructure.common.ViewExtensions;
import com.svp.infrastructure.common.view.BaseCursorAdapter;
import com.svp.infrastructure.common.view.ICursorParcelable;
import com.svp.infrastructure.mvpvs.bundle.BundleProvider;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.bonuspack.utils.WebImageCache;

import java.util.Date;
import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.db.Place;
import svp.com.dontmissplaces.model.Map.Point2D;
import svp.com.dontmissplaces.presenters.SearchPlacesPresenter;
import svp.com.dontmissplaces.ui.ActivityCommutator;
import svp.com.dontmissplaces.ui.adapters.PlaceSearchAdapter;
import svp.com.dontmissplaces.ui.model.PlaceView;

public class SearchPlacesActivity extends AppCompatActivityView<SearchPlacesPresenter>
        implements ActivityCommutator.ICommutativeElement, PlaceSearchAdapter.OnClickListener {

    public static class SearchPlacesBundleProvider extends BundleProvider {
        private static final java.lang.String KEY = "QUERY_KEY";
        private static final java.lang.String FOUND_PLACE_KEY = "FOUND_PLACE_KEY";

        public SearchPlacesBundleProvider(Intent intent) {
            super(intent);
        }

        public SearchPlacesBundleProvider() {
            this(new Bundle());
        }

        public SearchPlacesBundleProvider(Bundle b) {
            super(b);
        }

        public String getQuery() {
            return bundle.getString(KEY);
        }

        public SearchPlacesBundleProvider putQuery(String query) {
            bundle.putString(KEY, query);
            return this;
        }

        public long getFoundPlaceId() {
            return bundle.getLong(FOUND_PLACE_KEY);
        }

        public SearchPlacesBundleProvider putFoundPlaceId(long id) {
            bundle.putLong(FOUND_PLACE_KEY, id);
            return this;
        }
    }

    public static class PlaceSearchResult extends PlaceView {
        ImageButton image;

        //public final String PLACE_KEY = "PLACE_KEY";
        public PlaceSearchResult(Place p) {
            super(p, Point2D.empty());
        }

        public Place getPlace(){
            return place;
        }

        public void initView(View view) {
            ViewExtensions.<TextView>findViewById(view, R.id.history_tracks_item_text)
                    .setText(getAddress());

            image = ViewExtensions.findViewById(view, R.id.history_tracks_item_image);

            new ThumbnailTask().execute(image);
        }

        class ThumbnailTask extends AsyncTask<ImageView, Void, ImageView> {
            Bitmap btm;

            @Override
            protected ImageView doInBackground(ImageView... params) {
                WebImageCache mThumbnailCache = new WebImageCache(300);
                btm = mThumbnailCache.get(place.iconUrl);
//                image.setImageBitmap(btm);
//                image.setVisibility(View.VISIBLE);
                return params[0];
            }

            @Override
            protected void onPostExecute(ImageView iv) {
                if (btm != null) {
                    iv.setImageBitmap(btm);
                    iv.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    /*public static class PlacesCursorAdapter extends BaseCursorAdapter<PlaceSearchResult> {

        private LayoutInflater mInflater;

        public PlacesCursorAdapter(Context context, Cursor c) {
            super(context, c);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public ICursorParcelable createParcelableObject() {
            return new PlaceSearchResult();
        }

        @Override
        public View getView(LayoutInflater inflater, ViewGroup parent) {
            return mInflater.inflate(R.layout.activity_history_tracks_item_template, parent, false);
        }

        @Override
        public void onItemClick(View view, PlaceSearchResult item) {

        }
    }*/

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<SearchPlacesActivity> {
        public ViewState(SearchPlacesActivity view) {
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

        public void setSearchQuery(final String query) {
            view.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view.searchView.setQuery(query, false);
                    view.searchView.setIconified(false);
                }
            });
        }

        public void addPlaces(final Vector<PlaceSearchResult> places) {
            view.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PlaceSearchAdapter adapter = new PlaceSearchAdapter(view.getLayoutInflater(), places);
                    adapter.setOnClickListener(view);
                    view.placesView.setAdapter(adapter);
                }
            });
        }

        public void addPlaces(final Cursor places) {
            view.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //PlacesCursorAdapter cursorAdapter = new PlacesCursorAdapter(view, places);
//            cursorAdapter.setOnItemClickListener(new HistoryCursorAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(View view, TrackView track) {
//                    view.setSelected(true);
//                    getPresenter().openTrack(track);
////                Snackbar.make(view, "selected " + track.getHeader().toString(), Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//                }
//            });
                    // view.placesView.setAdapter(cursorAdapter);
                }
            });
        }
    }

    private SearchView searchView;
    @Bind(R.id.search_places_list_view) ListView placesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_places);

        ButterKnife.bind(this);


    }

    @Override
    public void onClick(PlaceSearchResult result) {
        getPresenter().placeSelected(result);
    }

    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchplace_menu, menu);
        initSearch(menu);

        getPresenter().startSearch(null);

        return true;
    }

    @Override
    public void onBackPressed() {

    }

    private void initSearch(Menu menu) {
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        MenuItem item = menu.findItem(R.id.search_place_menu_action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));

//        item.collapseActionView();
//        item.expandActionView();

        //  searchManager.triggerSearch();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            long prevTime;
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                long d = new Date().getTime();
                if(prevTime == 0){
                    prevTime = d;
                }
                long change = prevTime - d;
                if(change > 100) {
                    getPresenter().startSearch(newText);
                }
                prevTime = d;
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
    }

    /*
    * Commutative
    * */
    @Override
    public ActivityCommutator.ActivityOperationResult getOperation() {
        return ActivityCommutator.ActivityOperationResult.SearchPlaces;
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
