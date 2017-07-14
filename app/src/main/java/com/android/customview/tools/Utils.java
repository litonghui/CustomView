package com.android.customview.tools;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.TextView;

import com.android.customview.MyApplication;

import java.security.MessageDigest;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by litonghui on 2016/4/27.
 */
public class Utils {

    public static int getScreemWidth(Context context){
        return context!=null?context.getResources().getDisplayMetrics().widthPixels:0;
    }

    public static int getScreenHeight(Context context){
        return context!=null?context.getResources().getDisplayMetrics().heightPixels:0;
    }


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

    public static String md5(String pwd){
        if(!TextUtils.isEmpty(pwd)){
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(pwd.getBytes());
                byte bytes[] = md.digest();
                int i;
                StringBuilder buf = new StringBuilder("");
                for (int offset = 0; offset < bytes.length; offset++) {
                    i = bytes[offset];
                    if (i < 0)
                        i += 256;
                    if (i < 16)
                        buf.append("0");
                    buf.append(Integer.toHexString(i));
                }
                return buf.toString();
            } catch (Exception e) {
                return null;
            }
        }else {
            return null;
        }
    }
    private  static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            int newValue = result + 1;
            if (newValue > 0x00ffffff)
                newValue = 1;
            if(sNextGeneratedId.compareAndSet(result,newValue))
                return result;
        }
    }

    public static int getVersionCode() {
        Context context = MyApplication.getInstance();
        if(null != context) {
            try {
                PackageManager pm = context.getPackageManager();
                PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
                return pi.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return -1;
            }
        }else
            return -1;
    }
    public static String getChannel(){
        Context context = MyApplication.getInstance();
        if(null != context) {
            try {
               ApplicationInfo appInfo = context.getPackageManager().
                       getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                return appInfo.metaData.getString("CHANNEL");
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return "pc";
            }
        }else
            return "pc";
    }
}
