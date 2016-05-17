package com.android.customview;

import android.app.Application;

import com.android.customview.model.LocalPackageManager;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
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
}