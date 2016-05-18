package com.android.customview.tools;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

/**
 * Created by litonghui on 2016/5/18.
 */
public class Preferences {

    private final static String CONFIG_MAIN="config_main";

    private final static String LOCKPASSWORD = "lock_password";

    private final static String LOCKTYPE = "lock_type";


    public static void setLockPassword(Context context,String strpwd){
        if(null != context && !TextUtils.isEmpty(strpwd)) {
            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(LOCKPASSWORD,strpwd);
            editor.commit();
        }
    }
    public static String getLockPassword(Context context){
        if(null != context ) {
            SharedPreferences sp = getSharedPreferences(context);
            return sp.getString(LOCKPASSWORD,"1111");
        }
        return "1111";
    }

    public static String getLockType(Context context){
        if(null != context ) {
            SharedPreferences sp = getSharedPreferences(context);
            return sp.getString(LOCKTYPE,"1111");
        }
        return "1111";
    }
    public static void setLockType(Context context, String type){
        if(null != context && !TextUtils.isEmpty(type)) {
            SharedPreferences.Editor editor = getSharedPreferences(context).edit();
            editor.putString(LOCKTYPE,type);
            editor.commit();
        }
    }


    @TargetApi(11)
    private static SharedPreferences getSharedPreferences(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return context.getSharedPreferences(CONFIG_MAIN,
                    Context.MODE_MULTI_PROCESS);
        } else {
            return context.getSharedPreferences(CONFIG_MAIN,
                    Context.MODE_APPEND);
        }
    }
}
