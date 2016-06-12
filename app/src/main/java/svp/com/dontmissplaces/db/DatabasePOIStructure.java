package svp.com.dontmissplaces.db;

public interface DatabasePOIStructure {
    /*
    * http://stackoverflow.com/questions/6132442/android-sqlite-r-tree-how-to-install-module
    * NSString *locationQuery = @"SELECT name, city, state, region, country, lat, lon FROM clubs WHERE (lat <= %f AND lat >= %f) AND (lon <= %f AND lon >= %f)";
    * */
    interface POIBoxColumns extends DatabaseStructure.Base {

        String ZOOM_LEVEL = "zoom_level";
        String ZOOM_LEVEL_TYPE = _INTEGER_NOT_NULL;

        //N:48922424; E:2356224; S:48806788; W:2232627
        String BOUNDING_BOXE6 = "bounding_box";
        String BOUNDING_BOXE6_TYPE = _TEXT_TYPE;

    }
    //park, nightclub, theatre, toilets, wifi, youth_centre
    //-entertainment

    //place_of_worship(Church), monument/memorial
    //-monuments

    //taxi,
    interface POITypeStations extends POITypeColumns{
        String TABLE = "poi_type_stations";
        String CREATE_STATEMENT ="CREATE TABLE " + TABLE + "(" +
                " " + _ID               + " " + _ID_TYPE +
                "," + POI_ID            + " " + POI_ID_TYPE +
                "," + FACILITY          + " " + FACILITY_TYPE +
                ");";
    }
    //	fast_food, restaurant, bar, pub, cafe, marketplace, market, supermarket
    interface POITypeFood extends POITypeColumns{
        String TABLE = "poi_type_food";
        String CREATE_STATEMENT ="";
    }

    interface POITypeColumns extends DatabaseStructure.Base{
        //poi_id -> poi.id
        String POI_ID = "poi_id";
        String POI_ID_TYPE = _INTEGER_NOT_NULL;

        String FACILITY = "facility";
        String FACILITY_TYPE = _TEXT_TYPE;
    }
    /*
    {   "place_id":"75413380",
        "licence":"Data © OpenStreetMap contributors, ODbL 1.0. http:\/\/www.openstreetmap.org\/copyright",
        "osm_type":"way",
        "osm_id":"54657155",
        "boundingbox":["48.8687437","48.8695361","2.3407995","2.3419343"],
        "lat":"48.8691395","lon":"2.34136737919504",
        "display_name":"Palais Brongniart, place de la Bourse, Vivienne, 2nd Arrondissement, Paris, Ile-de-France, Metropolitan France, 75002, France",
        "class":"historic",
        "type":"monument",
        "importance":0.29244504582027,
        "icon":"http:\/\/nominatim.openstreetmap.org\/images\/mapicons\/tourist_monument.p.20.png"}
    */

    interface POI extends DatabaseStructure.Places{
        String TABLE = "poi";
        String CREATE_STATEMENT ="CREATE TABLE " + TABLE + "(" +
                CREATE_COLUMNS +
                ");";
    }
}
