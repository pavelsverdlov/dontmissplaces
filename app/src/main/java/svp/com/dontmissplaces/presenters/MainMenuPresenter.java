package svp.com.dontmissplaces.presenters;

import android.content.Intent;
import android.location.Location;
import android.os.RemoteException;
import android.util.Log;

import com.svp.infrastructure.mvpvs.bundle.IBundleProvider;
import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import svp.app.map.android.gps.GPSService;
import svp.app.map.android.gps.IGPSLocationListener;
import svp.app.map.android.gps.IGPSProvider;
import svp.com.dontmissplaces.MainMenuActivity;
import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.db.Place;
import svp.com.dontmissplaces.db.Repository;
import svp.com.dontmissplaces.db.SessionTrack;
import svp.com.dontmissplaces.db.Track;
import svp.com.dontmissplaces.db.Waypoint;
import svp.app.map.model.Point2D;
import svp.app.map.android.gps.GPSServiceProvider;
import svp.app.map.android.gps.OnLocationChangeListener;
import svp.com.dontmissplaces.model.PlaceProvider;
import svp.com.dontmissplaces.model.gps.GPSProvider;
import svp.com.dontmissplaces.model.nominatim.PhraseProvider;
import svp.com.dontmissplaces.model.nominatim.PlaceByPoint;
import svp.com.dontmissplaces.model.nominatim.PointsOfInterestInsiteBoxTask;
import svp.com.dontmissplaces.ui.ActivityCommutator;
import svp.com.dontmissplaces.ui.BaseBundleProvider;
import svp.com.dontmissplaces.ui.activities.SearchPlacesActivity;
import svp.app.map.MapViewTypes;
import svp.com.dontmissplaces.ui.model.IPOIView;
import svp.com.dontmissplaces.ui.model.PlaceView;
import svp.com.dontmissplaces.ui.model.SessionView;
import svp.com.dontmissplaces.ui.model.TrackView;

import static svp.com.dontmissplaces.ui.ActivityCommutator.ActivityOperationResult.HistoryTracks;
import static svp.com.dontmissplaces.ui.ActivityCommutator.ActivityOperationResult.SavedPlaces;

public class MainMenuPresenter extends CommutativePresenter<MainMenuActivity,MainMenuActivity.ViewState>
    implements OnLocationChangeListener {
    private final String TAG ="MainMenuPresenter";
    private TrackTimer timer;
    private final Repository repository;
    private Track recordingTrack;

    public IGPSProvider gps;
    private Location prevLocation;

    private SessionTrack recordingSession;
    private MapViewTypes mapViewType;

    public MainMenuPresenter(Repository repository) {
        this.repository = repository;
        mapViewType = MapViewTypes.Osmdroid;
    }

    public void startNewTrackSession() {
        if(timer != null){
            timer.stop();
            timer = null;
        }
        timer = new TrackTimer(1000);

        if(recordingTrack == null){
            Date date = new Date();
            recordingTrack = new Track(-1,date.toString(),date.getTime());
        }

        timer.start();

        recordingTrack = repository.track.getOrInsertTrack(recordingTrack);
        recordingSession = repository.track.insertSession(recordingTrack);

        state.startTrackRecording(new SessionView(recordingSession,new Vector<Waypoint>()));
    }
    public void pauseTrackRecording() {
        timer.pause();
    }
    public void stopTrackRecording() {
        timer.cancel();
        state.slideOutFabTrackRecordingToolbar();

        IBundleProvider bp = new BaseBundleProvider().putTrack(recordingTrack);
        commutator.goTo(ActivityCommutator.ActivityOperationResult.SaveTrack, bp);
    }
    public void resumeTrackRecording() {
        timer.resume();
    }

    public void openHistoryTracks() {
        commutator.goTo(HistoryTracks);
    }
    public void openSettings() {
        commutator.goTo(ActivityCommutator.ActivityOperationResult.Settings);
    }
    public void openSearch(String query) {
        IBundleProvider bp = new SearchPlacesActivity.SearchPlacesBundleProvider().putQuery(query);
        commutator.goTo(ActivityCommutator.ActivityOperationResult.SearchPlaces,bp);
    }
    public void openSavedPlaces() {
        commutator.goTo(SavedPlaces);
    }

    public void onMoveToMyLocation() {
        Point2D point = Point2D.empty();
        try {
            point = new Point2D(gps.getLocation());
        } catch (Exception e) {
            Log.e(TAG,"onMoveToMyLocation",e);
        }
        state.MapCameraMoveTo(point);
    }
    public void pinPlace(IPOIView poi) {
        Place p = poi.getPlace();
        poi.update(repository.place.insert(p));
    }
    public void beenInPlace(IPOIView poi) {
        Place p = poi.getPlace();
        p.setBeenHere();
        poi.update(repository.place.insert(p));
    }

    public void permissionFineLocationReceived() {
        if(prevLocation == null) {
            try {
                prevLocation = gps.getLocation();
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.e(TAG,"",e);
            }
        }
    }
    @Override
    public void OnLocationChanged(Location location) {
        if(prevLocation == null && location != null){
            state.MapCameraMoveTo(new Point2D(location));
        }
        prevLocation = location;
    }

    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {
        if(ActivityCommutator.ActivityOperationResult.HistoryTracks.is(from)){
            BaseBundleProvider bp = new BaseBundleProvider(data);
            recordingTrack = bp.getTrack();

            Vector<SessionView> sessions = new Vector<>();
            for (SessionTrack session : repository.track.getSessions(recordingTrack)){
                sessions.add(new SessionView(session,repository.track.getWaypoints(session)));
            }

            state.displayTrackOnMap(new TrackView(recordingTrack, sessions));
        }else if(ActivityCommutator.ActivityOperationResult.SearchPlaces.is(from)){
            SearchPlacesActivity.SearchPlacesBundleProvider sbp = new SearchPlacesActivity.SearchPlacesBundleProvider(data);
            long id = sbp.getFoundPlaceId();
            Place place = repository.poi.getPlaceById(id);
            state.showPlaceInfo(new PlaceView(place, Point2D.empty()), Point2D.empty());
            //state.showMarker();
        }
    }
    @Override
    protected void onAttachedView(MainMenuActivity view, Intent intent) {
        super.onAttachedView(view,intent);
        try {
            gps = GPSProvider.create(state.getActivity(),state.getPermissions(), new GPSLocationListener());
        } catch (Exception e) {
            Log.e(TAG,"onAttachedView", e);
        }
    }

    /** POI **/
    public void showPlaceInfoNearPoint(Point2D point) {
        if(point.isEmpty()){
            point = new Point2D(prevLocation);
        }
        //for search nearest POI to point we should check found point this original
        showPlaceInfo(point, point);
    }
    public void showPlaceInfoByPoint(final Point2D point) {
//        PlaceProvider pp = new PlaceProvider(state.getActivity());
//        Place p = pp.getPlace(point.getLatLng());
//        place p = pp.getPlace(new LatLng(46.4708294,30.7043384))
        showPlaceInfo(point, Point2D.empty());
    }

    private void showPlaceInfo(final Point2D point, final Point2D originalPoint) {
        if(state.getPermissions().checkPermissionNetwork()) {
            switch (getMapViewType()) {
                case Google:
                    PlaceProvider pp = new PlaceProvider(state.getActivity());
                    Place place = pp.getPlace(point.getLatLng());
                    state.showPlaceInfo(new PlaceView(place,originalPoint), null);
                    repository.poi.insert(place);
                    break;
                case Osmdroid:
                    new PlaceByPoint(){
                        @Override
                        public void processing(ArrayList<Place> poi) {
                            if(poi.size() > 0){
                                state.showPlaceInfo(new PlaceView(poi.get(0),originalPoint), null);
                                repository.poi.insertMany(poi);
                            }
                        }
                    }.execute(point);
                    break;
            }
        }else{
            int maxD = 100;
            GeoPoint p = point.getGeoPoint();
            BoundingBoxE6 bb = new BoundingBoxE6(p.getLatitudeE6()+maxD,
                    p.getLongitudeE6()+maxD,
                    p.getLatitudeE6()-maxD,
                    p.getLongitudeE6()-maxD);

            Vector<Place> pois = repository.poi.get(bb);

            if(pois.size() > 0) {
                state.showPlaceInfo(new PlaceView(pois.get(0), originalPoint), point);
            }
        }

        if(state.getPermissions().checkPermissionNetwork()){

        }else{

        }
    }

    public void searchPOI(int zoom, BoundingBoxE6 box) {
        if(!state.getPermissions().checkPermissionNetwork()){
            return;
        }
        PhraseProvider pp = new PhraseProvider();

        ArrayList<PointsOfInterestInsiteBoxTask.InputData> datas = new ArrayList<>();
        for (String phrase : pp.getPhrases(zoom)){
            datas.add(new PointsOfInterestInsiteBoxTask.InputData(box,phrase,50));
        }

        if(datas.size() == 0){
            return;
        }

        new PointsOfInterestInsiteBoxTask(){
            @Override
            protected void processing(ArrayList<Place> poi, InputData data){
                repository.poi.insertMany(poi);
            }
        }.execute(datas);
    }

    /* end POI */

    public MapViewTypes getMapViewType() {
        mapViewType = userSettings.getMapProvider();
//        return mapViewType;
        return MapViewTypes.Google;
    }




    private class TrackTimer extends TimerTask{
        private long elapsedMses;
        private long intervalMsec;

        private boolean isPause;
        private boolean isStop;

        public TrackTimer(long countDownInterval) {
            intervalMsec = countDownInterval;
        }

        public void start(){
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(this, intervalMsec, intervalMsec);
        }

        public void pause(){
            isPause = true;
        }
        public void resume() {
            isPause = false;
        }

        public void stop(){
            isStop = true;
            this.cancel();
        }

        @Override
        public void run() {
            if(isPause || isStop){
                return;
            }
            elapsedMses += intervalMsec;
            state.updateTrackTime(elapsedMses);

//            Location loc = state.getLocation();
//            if(loc != null) {
//                state.updateTrackDispance(loc.toString());
//            }
        }
    }

    public class GPSLocationListener extends IGPSLocationListener.Stub {
        @Override
        public void onLocationChanged(Location location) throws RemoteException {
            OnLocationChanged(location);
        }
    }
}
