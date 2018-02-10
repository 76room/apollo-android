package org.room76.apollo.util;

import android.annotation.SuppressLint;

import java.util.concurrent.TimeUnit;

/**
 * Created by a.zatsepin on 03/02/2018.
 */

public final class Utils {

    @SuppressLint("DefaultLocale")
    public static String convertTime(int time) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
    }
}
