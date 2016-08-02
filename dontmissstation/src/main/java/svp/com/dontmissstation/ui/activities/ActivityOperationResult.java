package svp.com.dontmissstation.ui.activities;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

public class ActivityOperationResult {
    public static final ActivityOperationItem Undefined= new ActivityOperationItem(0);
    public static final ActivityOperationItem Main = new ActivityOperationItem(1);
    public static ActivityOperationItem get(int resultCode) {
        switch (resultCode){
            case 1:
                return Main;
            default:
                return Undefined;
        }
    }
}
