package com.svp.infrastructure.mvpvs.bundle;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Pasha on 5/8/2016.
 */
public interface IBundleProvider {
    void putInto(Intent intent);

    CharSequence getPreviousActionText();
    IBundleProvider putActionText(CharSequence text);
}
