package svp.com.dontmissstation.ui.model;

public enum RouteTypes {
    None(0),
    Train(1),
    Stairs(2),
    Escalator(3),
    Walk(4);

    private final int code;

    RouteTypes(int code) {
        this.code = code;
    }
    public int toInt() {
        return code;
    }

    public static RouteTypes get(int key) {
        RouteTypes[] values = RouteTypes.values();
        if(key < 0 || key > values.length){
            return None;
        }
        return values[key];
    }
}
