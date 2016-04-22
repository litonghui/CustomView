package com.android.customview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.customview.R;

/**
 * Created by litonghui on 2016/4/20.
 */
public class RectActivity extends Activity {

    private TextView mTxtInfo;
    private String mInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rect);
        mTxtInfo = (TextView) findViewById(R.id.edit_info);
    }
    public void onRoundRectClick(View view){
        mInfo = getResources().getString(R.string.roundrect_info);
        if(!TextUtils.isEmpty(mInfo)) {
            mTxtInfo.setText(mInfo);
        }
    }
    public void onRoundRectPathClick(View view){
        mInfo = getResources().getString(R.string.roundrectpath_info);
        if(!TextUtils.isEmpty(mInfo)) {
            mTxtInfo.setText(mInfo);
        }
    }
    public void RoundRectPathDoubleClick(View view){
        mInfo = getResources().getString(R.string.roundrectpathdouble_info);
        if(!TextUtils.isEmpty(mInfo)) {
            mTxtInfo.setText(mInfo);
        }
    }
    public void ShapRectClick(View view){
        mInfo = getResources().getString(R.string.shaprect_info);
        if(!TextUtils.isEmpty(mInfo)) {
            mTxtInfo.setText(mInfo);
        }
    }
    public void ClipPathClick(View view){
        mInfo = getResources().getString(R.string.clippath_info);
        if(!TextUtils.isEmpty(mInfo)) {
            mTxtInfo.setText(mInfo);
        }
    }
}
