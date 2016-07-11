package svp.com.dontmissplaces.ui;

import android.app.Activity;
import android.content.ClipboardManager;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;
import android.widget.TextView;

import com.svp.infrastructure.common.ViewExtensions;
import com.svp.infrastructure.mvpvs.viewstate.IViewState;
import butterknife.Bind;
import butterknife.ButterKnife;
import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.model.nominatim.PhraseProvider;
import svp.com.dontmissplaces.ui.behaviors.OverMapBottomSheetBehavior;
import svp.com.dontmissplaces.ui.model.IPOIView;
import svp.com.dontmissplaces.ui.model.PlaceView;

public class PlaceInfoLayoutView {
    private final int bottomPanelHeight = 224;

    private final Activity activity;

    private OverMapBottomSheetBehavior behavior;
    View placeInfoHeader;

//    @Bind(R.id.select_place_show_near_info) TextView nearInfo;
//    @Bind(R.id.select_place_show_title) TextView title;
//    @Bind(R.id.select_place_show_placetype) TextView placetype;
//    @Bind(R.id.select_place_content_location) TextView contentLocation;



    public PlaceInfoLayoutView(Activity activity,View placeInfoHeader){

        this.activity = activity;
        this.placeInfoHeader = placeInfoHeader;

        behavior = OverMapBottomSheetBehavior.from(activity.findViewById(R.id.select_place_scrolling_act_content_view));
        behavior.setPeekHeight(bottomPanelHeight);
    }

    public void show(final IViewState viewState, IPOIView place){
        String name = place.getName();
        String type = place.getType();

        showPlaceInfoLayout();

//        ButterKnife.bind(activity);

        ViewExtensions.<TextView>findViewById(activity,R.id.select_place_show_near_info).setText(place.getAccuracyDistance());

        ViewExtensions.<TextView>setOnLongClickListener(activity,R.id.select_place_show_title,
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        CharSequence text = ((TextView) v).getText();
                        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Activity.CLIPBOARD_SERVICE);
                        clipboard.setText(text);
                        viewState.getSnackbar("Name was copied to clipboard.");
                        return true;
                    }
                })
                .setText(name);

        switch (PhraseProvider.getType(type)) {
            case Food:
                String cuisine = place.getExtraTags().getCuisine();
                if (!cuisine.isEmpty()) {
                    type = type + " (" + cuisine + ")";
                }
                break;
        }

        ViewExtensions.<TextView>findViewById(activity,R.id.select_place_show_placetype).setText(type);
        ViewExtensions.<TextView>findViewById(activity,R.id.select_place_content_location).setText(place.getLocationStringFormat());
        ViewExtensions.<TextView>findViewById(activity,R.id.select_place_show_address)
                .setText(place.getAddress());
    }

    private void showPlaceInfoLayout() {
        invertStatePlaceInfoLayout(BottomSheetBehavior.STATE_EXPANDED);
    }
    public void invertState() {
        invertStatePlaceInfoLayout(behavior.getState());
    }


    private void invertStatePlaceInfoLayout(int state) {
        if (state == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(
                    BottomSheetBehavior.STATE_COLLAPSED,
                    bottomPanelHeight + placeInfoHeader.getMeasuredHeight());
        } else {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }
}
