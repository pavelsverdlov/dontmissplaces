package svp.com.dontmissplaces.ui.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.ui.ActivityCommutator;

public class SearchPlacesActivity extends AppCompatActivity
        implements ActivityCommutator.ICommutativeElement {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_places);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchplace_menu, menu);
        initSearch(menu);
        return true;
    }

    private void initSearch(Menu menu){
        SearchManager searchManager =(SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        MenuItem item = menu.findItem(R.id.search_place_menu_action_search);
        SearchView searchView =(SearchView) MenuItemCompat.getActionView(item);
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        searchView.setQuery("bla", false);
        searchView.setIconified(false);
//        item.collapseActionView();
//        item.expandActionView();

      //  searchManager.triggerSearch();

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // doSearch(newText);
//                return true;
//            }
//        });
//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                //TODO: show all action btns
//                return false;
//            }
//        });
//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //TODO: hide all action btns
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
