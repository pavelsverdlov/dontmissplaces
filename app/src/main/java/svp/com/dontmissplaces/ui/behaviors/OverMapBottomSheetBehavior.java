package svp.com.dontmissplaces.ui.behaviors;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * temporary implementation
 * */
public class OverMapBottomSheetBehavior<V extends View> extends BottomSheetBehavior<V> {

    CoordinatorLayout parent;
    V child;
    int layoutDirection;

    public OverMapBottomSheetBehavior(Context context, AttributeSet attrs){
        super(context,attrs);

        super.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if(newState != BottomSheetBehavior.STATE_SETTLING){
                    if(parent != null) {
                        onLayoutChild(parent, child, layoutDirection);
                    }
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
                if(getState() != BottomSheetBehavior.STATE_SETTLING){
                    if(parent != null) {
                        onLayoutChild(parent, child, layoutDirection);
                    }
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, V child, MotionEvent event) {
        return super.onInterceptTouchEvent(parent,child,event);
    }
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        this.parent = parent;
        this.child = child;
        this.layoutDirection = layoutDirection;
        return super.onLayoutChild(parent,child,layoutDirection);
        /*
        int mState = super.getState();
        int mPeekHeight = super.getPeekHeight();

        //return super.onLayoutChild(parent,child,layoutDirection);
        // First let the parent lay it out
        if (mState != STATE_DRAGGING && mState != STATE_SETTLING) {
            parent.onLayoutChild(child, layoutDirection);
        }
        // Offset the bottom sheet
        int mParentHeight = parent.getHeight();
        int mMinOffset = Math.max(0, mParentHeight - child.getHeight());

        int mMaxOffset = mParentHeight - mPeekHeight;
        boolean mHideable = false;
        if (mState == STATE_EXPANDED) {
            ViewCompat.offsetTopAndBottom(child, mMinOffset);
        } else if (mHideable && mState == STATE_HIDDEN) {
            ViewCompat.offsetTopAndBottom(child, mParentHeight);
        } else if (mState == STATE_COLLAPSED) {
            ViewCompat.offsetTopAndBottom(child, mMaxOffset);
        }
//        if (mViewDragHelper == null) {
//            mViewDragHelper = ViewDragHelper.create(parent, mDragCallback);
//        }
//        mViewRef = new WeakReference<>(child);
//        mNestedScrollingChildRef = new WeakReference<>(findScrollingChild(child));
        return true;
        */
    }

    public void setState(int state, int peekHeight) {
        super.setPeekHeight(peekHeight);
        super.setState(state);
    }

    public static <V extends View> OverMapBottomSheetBehavior<V> from(V view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params)
                .getBehavior();
        if (!(behavior instanceof OverMapBottomSheetBehavior)) {
            throw new IllegalArgumentException(
                    "The view is not associated with BottomSheetBehavior");
        }
        return (OverMapBottomSheetBehavior<V>) behavior;
    }
}
