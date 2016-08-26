package svp.com.dontmissplaces.model;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

/**
 * https://developers.google.com/places/android-api/placepicker?hl=ru#introduction
 */
public class PlacePickerTest extends Activity{
    public PlacePickerTest(){
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();

        Intent intent = null;
        try {
            intent = intentBuilder.build(this);
            // Start the Intent by requesting a result, identified by a request code.
            startActivityForResult(intent, 1);

        } catch (GooglePlayServicesRepairableException e) {
            GooglePlayServicesUtil
                    .getErrorDialog(e.getConnectionStatusCode(), this, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(this, "Google Play Services is not available.",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // BEGIN_INCLUDE(activity_result)
        if (requestCode == 1) {

            if (resultCode == Activity.RESULT_OK) {
                /* User has picked a place, extract data.
                   Data is extracted from the returned intent by retrieving a place object from
                   the PlacePicker.
                 */
                final Place place = PlacePicker.getPlace(data, this);


                /* A place object contains details about that place, such as its name, address
                and phone number. Extract the name, address, phone number, place ID and place types.
                 */
                final CharSequence name = place.getName();
                final CharSequence address = place.getAddress();
                final CharSequence phone = place.getPhoneNumber();
                final String placeId = place.getId();
                String attribution = PlacePicker.getAttributions(data);
                if(attribution == null){
                    attribution = "";
                }

                // Update data on card.


                // Print data to debug log
                //  Log.d(TAG, "place selected: " + placeId + " (" + name.toString() + ")");

                // Show the card.


            } else {
                // User has not selected a place, hide the card.
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
