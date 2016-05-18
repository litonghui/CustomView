package com.android.customview.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.android.customview.R;

/**
 * Created by litonghui on 2016/5/16.
 */
public abstract class VerifyPasswordActivity extends FragmentActivity {

    public final  static int TYPE_VERIFICATION = 100;

    public final static int TYPE_PD_SETTING = 101;

    protected  int mType;

    protected TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        initView();
        addFragment();
    }
    private void initView(){
        mTvTitle = (TextView) findViewById(R.id.tv_title);
    }
    private void addFragment(){
        setTvTitle();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = createFragment();
        ft.replace(R.id.ll_pattern,fragment);
        ft.commit();

    }

    protected  abstract Fragment createFragment();

    protected  abstract void setTvTitle();
}
