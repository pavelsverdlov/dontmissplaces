package svp.com.dontmissplaces.model.Map.google;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

public class GoogleApi {

    public void test(Activity activity){
         /*
                    Note: The Google Places API for Android enforces a default limit of 1 000 requests per 24 hour period.
                    There is a further checkpoint when the app reaches 150 000 requests.
                     To prevent your app from failing when it exceeds these limits, follow the instructions in the usage limits guide.
                    * */
        GoogleApiClient mGoogleApiClient = new GoogleApiClient
                .Builder(activity)
                .addApi(Places.GEO_DATA_API)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage((FragmentActivity) activity, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {

                    }
                })
                .build();
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(mGoogleApiClient, null);

        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                Log.i("Place", "onResult");
                if (likelyPlaces.getCount() <= 0) {
//                                Toast.makeText(PlaceLikelihoodActivity.this, "No place found", Toast.LENGTH_SHORT).show();
                }
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    Log.i("Place", String.format("Place '%s' has likelihood: %g",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));
                }
                likelyPlaces.release();
            }
        });

    }
}
