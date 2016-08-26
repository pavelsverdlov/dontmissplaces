package svp.com.dontmissstation.ui.activities;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

public class ActivityOperationResult {
    public static final ActivityOperationItem Undefined= new ActivityOperationItem(0);
    public static final ActivityOperationItem Main = new ActivityOperationItem(1);
    public static final ActivityOperationItem AddNewSubway = new ActivityOperationItem(2);
    public static final ActivityOperationItem AddSubwayLine = new ActivityOperationItem(3);

    public static ActivityOperationItem get(int resultCode) {
        switch (resultCode){
            case 1:
                return Main;
            case 2:
                return AddNewSubway;
            case 3:
                return AddSubwayLine;
            default:
                return Undefined;
        }
    }
}
