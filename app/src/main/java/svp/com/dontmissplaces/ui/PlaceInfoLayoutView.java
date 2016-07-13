package svp.com.dontmissplaces.ui;

import android.app.Activity;
import android.content.ClipboardManager;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;
import android.widget.TextView;

import com.svp.infrastructure.mvpvs.viewstate.IViewState;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.model.nominatim.PhraseProvider;
import svp.com.dontmissplaces.ui.behaviors.OverMapBottomSheetBehavior;
import svp.com.dontmissplaces.ui.model.IPOIView;

public class PlaceInfoLayoutView implements View.OnClickListener {
    private int bottomPanelHeight = 224;
    private final Activity activity;

    private BottomSheetBehavior behavior;
    private boolean isShowed;

    @Bind(R.id.select_place_show_near_info) TextView nearInfo;
    @Bind(R.id.select_place_show_title) TextView title;
    @Bind(R.id.select_place_show_placetype) TextView placetype;
    @Bind(R.id.select_place_content_location) TextView contentLocation;
    @Bind(R.id.select_place_show_address) TextView address;
    @Bind(R.id.select_place_header_layout) View placeInfoHeader;
    @Bind(R.id.main_action_btns_toolbar) View actiontoolbar;


    public PlaceInfoLayoutView(Activity activity){
        this.activity = activity;

        behavior = BottomSheetBehavior.from(activity.findViewById(R.id.select_place_scrolling_act_content_view));


        ButterKnife.bind(this, activity);
//        placeInfoHeader = coordinatorLayout.findViewById(R.id.select_place_header_layout);
        placeInfoHeader.setOnClickListener(this);

        actiontoolbar.post(new Runnable() {
            @Override
            public void run() {
                bottomPanelHeight = (int)(actiontoolbar.getMeasuredHeight()* 1.5);
                behavior.setPeekHeight(bottomPanelHeight);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_place_header_layout:
                invertState();
                break;
        }
    }

    public void show(final IViewState viewState, IPOIView place){
        isShowed = true;
        String name = place.getName();
        String type = place.getType();
        String accuracyDistance = place.getAccuracyDistance();

        showPlaceInfoLayout();

        if(accuracyDistance.isEmpty()) {
            nearInfo.setVisibility(View.GONE);
            nearInfo.clearComposingText();
            nearInfo.setText(null);
        }else{
            nearInfo.setVisibility(View.VISIBLE);
            nearInfo.setText(accuracyDistance);
        }


        switch (PhraseProvider.getType(type)) {
            case Food:
                String cuisine = place.getExtraTags().getCuisine();
                if (!cuisine.isEmpty()) {
                    type = type + " (" + cuisine + ")";
                }
                break;
        }

        title.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        CharSequence text = ((TextView) v).getText();
                        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Activity.CLIPBOARD_SERVICE);
                        clipboard.setText(text);
                        viewState.getSnackbar("Name was copied to clipboard.");
                        return true;
                    }
                });

        title.setText(name);
        placetype.setText(type);
        contentLocation.setText(place.getLocationStringFormat());
        address.setText(place.getAddress());

        behavior.setPeekHeight(bottomPanelHeight + (int)(placeInfoHeader.getMeasuredHeight()*0.8));
    }

    private void showPlaceInfoLayout() {
        invertStatePlaceInfoLayout(BottomSheetBehavior.STATE_EXPANDED);
    }
    public void invertState() {
        invertStatePlaceInfoLayout(behavior.getState());
    }


    private void invertStatePlaceInfoLayout(int state) {
        if (state == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            behavior.setState(
//                    BottomSheetBehavior.STATE_COLLAPSED,
//                    bottomPanelHeight + placeInfoHeader.getMeasuredHeight());
        } else {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public boolean isShow() {
        return isShowed;
    }

    public void hide() {
        behavior.setPeekHeight(bottomPanelHeight);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        isShowed = false;
    }


}
