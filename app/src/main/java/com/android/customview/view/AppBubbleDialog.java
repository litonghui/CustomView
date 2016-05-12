package com.android.customview.view;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;

import com.android.customview.R;

/**
 * Created by litonghui on 2016/5/12.
 */
public class AppBubbleDialog {

    private Activity mActivity;
    private BaseDialog mDialog;

    public AppBubbleDialog(Activity activity){
        mActivity = activity;
    }
    public void initView(){
        View view = mActivity.getLayoutInflater().inflate(R.layout.app_dialog,null);
        mDialog = new BaseDialog(mActivity,R.style.DialogStyle,view);
        mDialog.setDialogSize(Gravity.BOTTOM,0,0);
        mDialog.setAnimation(R.style.AppAnimBottom);
    }

    public void show(){
        if(null != mDialog){
            mDialog.show();
        }
    }


}
