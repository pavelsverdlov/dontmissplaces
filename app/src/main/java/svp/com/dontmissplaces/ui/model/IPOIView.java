package svp.com.dontmissplaces.ui.model;

import svp.com.dontmissplaces.db.Place;

public interface IPOIView extends svp.app.map.model.IPOIView  {
    Place getPlace();
    void update(Place place);
}
