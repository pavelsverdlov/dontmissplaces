package svp.com.dontmissplaces.db;

public class DatabasePOIStructure {
    /*
    * http://stackoverflow.com/questions/6132442/android-sqlite-r-tree-how-to-install-module
    * NSString *locationQuery = @"SELECT name, city, state, region, country, lat, lon FROM clubs WHERE (lat <= %f AND lat >= %f) AND (lon <= %f AND lon >= %f)";
    * */
    public interface POIBoxColumns extends DatabaseStructure.Base {

        String ZOOM_LEVEL = "zoom_level";
        String ZOOM_LEVEL_TYPE = _INTEGER_TYPE;

        //N:48922424; E:2356224; S:48806788; W:2232627
        String BOUNDING_BOXE6 = "bounding_box";
        String BOUNDING_BOXE6_TYPE = _TEXT_TYPE;

    }
}
