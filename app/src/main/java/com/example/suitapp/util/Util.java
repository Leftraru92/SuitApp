package com.example.suitapp.util;

import android.content.res.Resources;

public class Util {

    public static int getPxByDensity(Resources resources, int dp) {
        final float scale = resources.getDisplayMetrics().density;
        int padding_in_px = (int) (dp * scale + 0.5f);
        return padding_in_px;
    }
}
