package svp.com.dontmissstation.presenters;

import android.content.Intent;
import android.util.Log;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

import svp.com.dontmissstation.db.Repository;
import svp.com.dontmissstation.model.BundleRepository;
import svp.com.dontmissstation.model.SubwayGraph;
import svp.com.dontmissstation.ui.activities.PickOnMapActivity;
import svp.com.dontmissstation.ui.activities.SearchNewRouteActivity;
import svp.com.dontmissstation.ui.model.SubwayLineView;
import svp.com.dontmissstation.ui.model.SubwayStationView;
import svp.com.dontmissstation.ui.model.SubwayView;

public class SearchNewRoutePresenter extends CommutativePreferencePresenter<SearchNewRouteActivity,SearchNewRouteActivity.ViewState>{
    public static class RouteView{
        private final Vector<SubwayStationView> route;
        private final HashSet<SubwayLineView> lines;

        public RouteView(Vector<SubwayStationView> route){
            this.route = route;
            lines = new HashSet<>();
            for (SubwayStationView s : route){
                lines.addAll(s.getLines());
            }
        }

        public String getTitle() {
            return route.firstElement().getName() + " -> " + route.lastElement().getName();
        }

        public int getCountStations() {
            return route.size();
        }
        public int getLines(){
            return lines.size();
        }
    }

    private final Repository repository;
    private SubwayView subway;

    public SearchNewRoutePresenter(Repository repository) {
        this.repository = repository;
    }

    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {
        subway = BundleRepository.getSubway(data,repository);
    }
    @Override
    protected void onAttachedView(SearchNewRouteActivity view, Intent intent) {
        super.onAttachedView(view, intent);
        subway = BundleRepository.getSubway(intent,repository);
    }

    public void searchNewRoute(String from, String to) {
        long fromId = -1;
        long toId = -1;
        HashMap<Long,SubwayStationView>stations = new HashMap<>();
        for (SubwayStationView s : subway.getAllStations()){
            stations.put(s.getId(),s);
            if(s.getName().equals(from)){
                fromId = s.getId();
            }
            if(s.getName().equals(to)){
                toId = s.getId();
            }
        }
        Vector<Repository.StationRoute> stationRoutes = repository.getStationRoutes(subway.getId());

        try {
            SubwayGraph graph = new SubwayGraph();
            for (Repository.StationRoute route : stationRoutes) {
                graph.addNode(stations.get(route.fromStationId), stations.get(route.toStationId), route.length);
            }
            Vector<RouteView> routes = new Vector<>();
            routes.add(new RouteView(graph.getRoute(stations.get(fromId), stations.get(toId))));
            state.addRoutes(routes);
        } catch (Exception ex) {
            Log.e("SearchNewRoutePresenter","",ex);
        }
    }

    public Collection<SubwayStationView> getSubwayStations() {
        return subway.getAllStations();
    }
}
