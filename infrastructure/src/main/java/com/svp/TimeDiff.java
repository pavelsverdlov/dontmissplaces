package com.svp;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Pasha on 4/2/2016.
 */
public class TimeDiff {

    public static long getTimeDifference(Date d1, Date d2, TimeField field) {
        return TimeDiff.getTimeDifference(d1, d2)[field.ordinal()];
    }

    /**
     * Calculate the absolute difference between two Date without
     * regard for time offsets
     *
     * @param d1 Date one
     * @param d2 Date two
     * @return The fields day, hour, minute, second and millisecond
     */
    public static long[] getTimeDifference(Date d1, Date d2) {
        long[] result = new long[5];
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTime(d1);

        long t1 = cal.getTimeInMillis();
        cal.setTime(d2);

        long diff = Math.abs(cal.getTimeInMillis() - t1);
        final int ONE_DAY = 1000 * 60 * 60 * 24;
        final int ONE_HOUR = ONE_DAY / 24;
        final int ONE_MINUTE = ONE_HOUR / 60;
        final int ONE_SECOND = ONE_MINUTE / 60;

        long d = diff / ONE_DAY;
        diff %= ONE_DAY;

        long h = diff / ONE_HOUR;
        diff %= ONE_HOUR;

        long m = diff / ONE_MINUTE;
        diff %= ONE_MINUTE;

        long s = diff / ONE_SECOND;
        long ms = diff % ONE_SECOND;
        result[0] = d;
        result[1] = h;
        result[2] = m;
        result[3] = s;
        result[4] = ms;

        return result;
    }


    public static enum TimeField {DAY,
        HOUR,
        MINUTE,
        SECOND,
        MILLISECOND;
    }
}
