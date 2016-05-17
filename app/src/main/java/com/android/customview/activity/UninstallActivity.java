package com.android.customview.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.android.customview.R;
import com.android.customview.adapter.UnInstallAdapter;
import com.android.customview.model.LocalPackageManager;

/**
 * Created by litonghui on 2016/4/29.
 */
public class UninstallActivity extends Activity {

    private Context mContext;

    private UnInstallAdapter mAdapter;

    private RecyclerView mRecycleView;

    private BootBroadcastReceiver mBootReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uninstall);
        init();
        initView();
    }
    private void init(){
        mContext = this ;
        mBootReceiver = new BootBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(mBootReceiver,filter);
    }
    private void initView(){
        mAdapter = new UnInstallAdapter(mContext);
        mAdapter.setData(LocalPackageManager.getInstance().mLocalInfos);
        mRecycleView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setAdapter(mAdapter);
    }
    private class BootBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            handleBroadCastReceive(intent);
        }
    }

    private void handleBroadCastReceive(final Intent intent) {
        Handler mainThread = new Handler(Looper.getMainLooper());
        mainThread.post(new Runnable() {
            @Override
            public void run() {
                String action = intent.getAction();
                Uri uri = intent.getData();
                String pkg = uri.getSchemeSpecificPart();
                if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
                    if (TextUtils.isEmpty(pkg))
                        return;
                    if (LocalPackageManager.getInstance().isExistPackage(pkg)) {
                        if (LocalPackageManager.getInstance().removePackage(pkg)) {
                            //刷新
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                } else if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
                    if (TextUtils.isEmpty(pkg))
                        return;
                    if (LocalPackageManager.getInstance().addPackage(mContext, pkg)) {
                        mAdapter.notifyDataSetChanged();
                    }

                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBootReceiver);
    }
}
