package svp.com.dontmissplaces.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Utilities for creating intents.
 *
 * @author Jimmy Shih
 */
public class IntentUtils {

    public static final String TEXT_PLAIN_TYPE = "text/plain";
    private static final String BLUETOOTH_PACKAGE_NAME = "com.android.bluetooth";
    private static final String TWITTER_PACKAGE_NAME = "com.twitter.android";

    private IntentUtils() {}

    /**
     * Creates an intent with {@link Intent#FLAG_ACTIVITY_CLEAR_TOP} and
     * {@link Intent#FLAG_ACTIVITY_NEW_TASK}.
     *
     * @param context the context
     * @param cls the class
     */
    public static final Intent newIntent(Context context, Class<?> cls) {
        return new Intent(context, cls).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    }


}