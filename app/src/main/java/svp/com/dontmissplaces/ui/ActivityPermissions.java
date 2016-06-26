package svp.com.dontmissplaces.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.svp.infrastructure.common.PermissionUtils;

import svp.com.dontmissplaces.MainMenuActivity;

public class ActivityPermissions {
    /**
     * Request code for location permission request.
     */
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private boolean isFineLocationGranted;
    /**
     * Flag indicating whether a requested permission has been denied after returning in
     */
    public boolean fineLocationPermissionDenied = false;

    private final FragmentActivity activity;

    public ActivityPermissions(FragmentActivity activity) {
        this.activity = activity;
    }

    public boolean isFineLocationGranted(){
        return isFineLocationGranted;
    }

    public boolean isFineLocationGranted(String[] permissions, int[] grantResults){
        return isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public boolean checkPermissionFineLocation(){
        isFineLocationGranted = checkPermissionFineLocation(activity,Manifest.permission.ACCESS_FINE_LOCATION);
        return isFineLocationGranted;
    }
    public boolean checkPermissionCoarseLocation(){
        return checkPermissionFineLocation(activity,Manifest.permission.ACCESS_COARSE_LOCATION);
    }
    public void checkPermissionExternalStorage() {
        checkPermissionFineLocation(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
    public boolean checkPermissionNetwork() {
        return checkPermissionFineLocation(activity,Manifest.permission.ACCESS_NETWORK_STATE);
    }
    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    public void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(activity.getSupportFragmentManager(), "dialog");
    }

    private static boolean checkPermissionFineLocation(FragmentActivity activity, String manifestPermission){
        int state = ContextCompat.checkSelfPermission(activity, manifestPermission);
        if (state != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_CONTACTS)) {

            }else{
                PermissionUtils.requestPermission(activity, LOCATION_PERMISSION_REQUEST_CODE,
                    manifestPermission, false);
            }
            return false;
        }
        return true;
    }
    private static boolean isPermissionGranted(String[] permissions, int[] grantResults, String manifestPermission){
        return PermissionUtils.isPermissionGranted(permissions, grantResults, manifestPermission);
    }


}
