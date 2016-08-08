package svp.app.map.android.gps;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class GPSLocation implements Parcelable {
    public Location location;

    public GPSLocation(Parcel source) {
        readFromParcel(source);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(location,flags);
    }

    public void readFromParcel(Parcel source) {
        this.location = Location.CREATOR.createFromParcel(source);
    }

    public static final Parcelable.Creator<GPSLocation> CREATOR = new Parcelable.Creator<GPSLocation>() {
        @Override
        public GPSLocation[] newArray(int size) {
            return new GPSLocation[size];
        }

        @Override
        public GPSLocation createFromParcel(Parcel source) {
            return new GPSLocation(source);
        }
    };
}
