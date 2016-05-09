package svp.com.dontmissplaces.presenters;

import android.database.Cursor;
import android.widget.Toast;

import com.svp.infrastructure.mvpvs.presenter.Presenter;

import svp.com.dontmissplaces.db.Repository;
import svp.com.dontmissplaces.ui.activities.HistoryTracksActivity;

public class HistoryTracksPresenter extends Presenter<HistoryTracksActivity,HistoryTracksActivity.ViewState> {

    private final Repository repository;

    public HistoryTracksPresenter(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void onAttachedView(HistoryTracksActivity view) {
        CharSequence text = state.getBundle().getPreviousActionText();
        if(text != null) {
            state.getToast(text);
        }
//        super.attachView(view);
//
////        repository.clearTracks();
////
////        for (String name : new String[]{ "Track 1","Track 2","Track 3","Track 4" }){
////            repository.insertTrack(name);
////        }
    }


    public Cursor getCursorTracks(){
        return repository.getCursorTracks();
    }


}
