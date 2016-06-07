package svp.com.dontmissplaces.ui.map;

/**
 * Created by Pasha on 6/1/2016.
 */
public enum MapViewTypes {
    None(0),
    Osmdroid(1),
    Google(2);

    private final int code;

    MapViewTypes(int code) {
        this.code = code;
    }
    public int toInt() {
        return code;
    }

    public static MapViewTypes get(int key) {
        MapViewTypes[] values = MapViewTypes.values();
        if(key < 0 || key > values.length){
            return None;
        }
        return values[key];
    }
}
