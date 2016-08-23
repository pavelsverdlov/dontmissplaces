package svp.com.dontmissstation.ui.activities;

import android.app.Activity;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;

import svp.com.dontmissstation.MainActivity;


public class ActivityCommutator extends com.svp.infrastructure.mvpvs.commutate.ActivityCommutator{
    public ActivityCommutator(ICommutativeElement element) {
        super(element);
    }

    public static void register(ActivityOperationItem key, Class<?> value) {
        activities.put(key,value);
    }
}
