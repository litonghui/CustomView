package com.android.customview.model;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.android.customview.R;

import java.io.File;

/**
 * Created by litonghui on 2016/3/16.
 */
public class LocalPackageInfo implements NonProguard {
    public String packagename;
    public String name;
    public String size;
    public String md5;
    public String version;
    public String versioncode;
    public String channel;

    public LocalPackageInfo(final Context context, PackageInfo info) {
        if (null == context || null == info) return;
        name = info.applicationInfo.loadLabel(context.getPackageManager()).toString().trim();
        packagename = info.packageName;
        version = info.versionName;
        versioncode = String.valueOf(info.versionCode);
        File file = new File(info.applicationInfo.publicSourceDir);
        size = String.valueOf(file.length());
        if (info.applicationInfo != null && info.applicationInfo.metaData!=null) {
            channel = String.valueOf(info.applicationInfo.metaData.get("CHANNEL"));
        }
    }
    public Drawable getIcon(Context context) {
       // Context context = MainApplication.getInstance().getContext();
        if(null == context)  return null;
        Drawable drawable = context.getResources().getDrawable(R.mipmap.icon_mobilletool);
        try {
            if(null != packagename) {
                drawable = context.getPackageManager()
                        .getApplicationIcon(packagename);
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        return drawable;
    }
}
