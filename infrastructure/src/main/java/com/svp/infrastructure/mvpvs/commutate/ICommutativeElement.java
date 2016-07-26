package com.svp.infrastructure.mvpvs.commutate;


import android.app.Activity;

public interface ICommutativeElement{
    ActivityOperationItem getOperation();
    Activity getActivity();
}