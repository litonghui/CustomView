package com.android.customview.tools;

import android.content.Context;
import android.util.Log;

/**
 * Created by litonghui on 2016/4/27.
 */
public class Utils {
    public static float dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return dp * density + 0.5f;
    }

    public static float dx2dp(Context context, float dx) {
        float density = context.getResources().getDisplayMetrics().density;
        return dx / density + 0.5f;
    }
}
