package com.android.customview.activity;

import android.app.Activity;
import android.os.Bundle;

import com.android.customview.R;
import com.android.customview.tools.DownloadUtils;
import com.android.customview.view.BackdropLayout;

/**
 * Created by litonghui on 2016/5/23.
 */
public class BackDropActivity extends Activity {

    private BackdropLayout mBackdropLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backdrop);
        mBackdropLayout = (BackdropLayout) findViewById(R.id.backdroply);
        mBackdropLayout.setLayout(R.mipmap.small_icon);
        DownloadUtils downloadUtils = new DownloadUtils(this);
        downloadUtils.download("http://appcdn.123.sogou.com/haha/SogouHaha_v3.1_normal.apk",
                "SogouHaha/V3.1.apk");
    }
}
