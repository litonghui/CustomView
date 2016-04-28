package com.android.customview.model;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.android.customview.tools.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by litonghui on 2016/3/16.
 */
public class LocalPackageManager {

    public List<LocalPackageInfo> mLocalInfos = new ArrayList<>();
    public ConcurrentHashMap<String, LocalPackageInfo> mKvLocalInfos = new ConcurrentHashMap<>();

    private static LocalPackageManager mPackageManager;
    public static LocalPackageManager getInstance(){
        if(null == mPackageManager){
            mPackageManager = new LocalPackageManager();
        }
        return mPackageManager;
    }
    public void refreshLocalInfos(Context context) {
        if (null == context) return;

        List<PackageInfo> packageInfos = null;
        try {
            packageInfos = context.getPackageManager().getInstalledPackages(PackageManager.GET_SIGNATURES);
        } catch (Exception e) {
        }
        if(null == packageInfos) return;

         List<LocalPackageInfo> localInfos = new ArrayList<>();
         ConcurrentHashMap<String, LocalPackageInfo> kvlocalInfos = new ConcurrentHashMap<>();

        for (PackageInfo pkinfo : packageInfos){
            if(null == pkinfo )
                continue;
            LocalPackageInfo lcinfo = new LocalPackageInfo(context,pkinfo);

            if(null != lcinfo) {
                if (Utils.filterApp(pkinfo.applicationInfo.flags)) {
                    if("com.android.admin.projectsgit".equals(pkinfo.packageName))
                        continue;
                    localInfos.add(lcinfo);
                    kvlocalInfos.put(pkinfo.packageName, lcinfo);
                }
            }
        }
        if(null != localInfos)
            mLocalInfos = localInfos;
        if(null != kvlocalInfos)
            mKvLocalInfos = kvlocalInfos;
    }
    public boolean isExistPackage(String pkg){
        if(TextUtils.isEmpty(pkg))
            return  false;
        if(null == mKvLocalInfos)
            return  false;
        return mKvLocalInfos.get(pkg)!=null?true:false;
    }
    public boolean removePackage(String pkg){
        if(TextUtils.isEmpty(pkg))
            return  false;
        if(null == mKvLocalInfos || null == mLocalInfos)
            return  false;
        LocalPackageInfo info =mKvLocalInfos.get(pkg);
        if(null == info)
            return  false;
        else
        {
            mLocalInfos.remove(info);
            mKvLocalInfos.remove(pkg);
            return true;
        }
    }
    public boolean addPackage(Context context,String pkg) {
        boolean isok = false;
        if (context == null || TextUtils.isEmpty(pkg))
            isok = false;
        PackageManager pm = context.getPackageManager();
        PackageInfo pInfo;
        try {
            pInfo = pm.getPackageInfo(pkg, PackageManager.GET_META_DATA);
            LocalPackageInfo lcinfo = new LocalPackageInfo(context, pInfo);
            if (null != lcinfo && Utils.filterApp(pInfo.applicationInfo.flags)) {
                if (!"com.android.admin.projectsgit".equals(pInfo.packageName)) {
                    mLocalInfos.add(lcinfo);
                    mKvLocalInfos.put(pInfo.packageName, lcinfo);
                    isok = true;
                }
            }
        } catch (Exception e) {
            isok = false;
        }
        return isok;
    }



}
