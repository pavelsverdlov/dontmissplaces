package svp.com.dontmissstation.ui.activities;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

public class ActivityOperationResult {
    public static final ActivityOperationItem Undefined= new ActivityOperationItem(0);
    public static final ActivityOperationItem Main = new ActivityOperationItem(1);
    public static final ActivityOperationItem EditNewSubway = new ActivityOperationItem(2);
    public static final ActivityOperationItem EditSubwayLine = new ActivityOperationItem(3);
    public static final ActivityOperationItem EditSubwayStation = new ActivityOperationItem(4);
    public static final ActivityOperationItem ListSubways = new ActivityOperationItem(5);
    public static final ActivityOperationItem PickOnMap = new ActivityOperationItem(6);
    public static final ActivityOperationItem Route = new ActivityOperationItem(7);

    public static ActivityOperationItem get(int resultCode) {
        switch (resultCode){
            case 1:
                return Main;
            case 2:
                return EditNewSubway;
            case 3:
                return EditSubwayLine;
            case 4:
                return EditSubwayStation;
            case 5:
                return ListSubways;
            case 6:
                return PickOnMap;
            case 7:
                return Route;
            default:
                return Undefined;
        }
    }
}
