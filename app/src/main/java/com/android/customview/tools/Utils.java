package com.android.customview.tools;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.TextView;

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
    public static boolean filterApp(int flags) {
        if ((flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
        } else if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        }
        return false;
    }
    public static TextView generateTextView(Context context, String text, int color, float size) {
        final TextView tv = new TextView(context);
        tv.setTextColor(color);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        tv.setText(text);
        return tv;
    }
    public static void unInstallApk(Context context , String pkg){
        if(TextUtils.isEmpty(pkg))
            return;
        Uri uri = Uri.parse("package:"+pkg);
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(uri);
        context.startActivity(intent);
    }

}
