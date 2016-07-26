package com.svp.infrastructure.mvpvs.commutate;

public final class ActivityOperationItem {
//    Undefined(0);

    private final int code;

    public ActivityOperationItem(int code) {
        this.code = code;
    }
    public int toInt() {
        return code;
    }
    public boolean is(int key) {
        return code == key;
    }
    public boolean is(ActivityOperationItem item) {
        return item.code == code;
    }
}
