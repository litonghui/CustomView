package com.android.customview;

import android.app.Application;
import android.content.Context;

import com.android.customview.model.LocalPackageManager;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class MyApplication extends Application{

    private static Context mInstance ;

    @Override
    public void onCreate() {
        super.onCreate();
        if(mInstance == null)
            mInstance = this;
        load();
    }
    private void load(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                LocalPackageManager.getInstance().refreshLocalInfos(getApplicationContext());
            }
        }).start();

    }
    public static synchronized  Context getInstance(){
        return mInstance ;
    }
}