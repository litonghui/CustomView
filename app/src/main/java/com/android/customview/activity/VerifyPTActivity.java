package com.android.customview.activity;


import android.support.v4.app.Fragment;

import com.android.customview.fragment.DigitalPasswordFragment;
import com.android.customview.fragment.PatternPasswordFragment;

/**
 * Created by litonghui on 2016/5/16.
 */
public  class VerifyPTActivity extends VerifyPasswordActivity {

    @Override
    protected Fragment createFragment() {
        return new PatternPasswordFragment();
    }

    @Override
    protected void setTvTitle() {
        if(mType == TYPE_VERIFICATION){
            mTvTitle.setText("验证密码");
        }else{
            mTvTitle.setText("设置密码");
        }

    }
}
