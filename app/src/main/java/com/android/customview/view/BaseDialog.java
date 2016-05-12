package com.android.customview.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.customview.tools.Utils;

/**
 * Created by litonghui on 2016/5/12.
 */
public class BaseDialog extends Dialog {

    private Context mContext;
    private View mView;
    private Window mWindow;

    public BaseDialog(Context context,int theme,View view){
        super(context,theme);
        mContext = context;
        mView = view;
        mWindow = getWindow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
        int width = Utils.getScreemWidth(mContext);
        setDialogSize(0,width,0);
    }

    @Override
    public void show() {
        try {
            if(!isActivityValid())
                return;
            super.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        try {
            if(!isShowing())
                return;
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void setDialogSize(int gravity,int width,int height){
        if(null != mWindow) {
            WindowManager.LayoutParams lp = mWindow.getAttributes();
            lp.width = width;
            if (height > 0) {
                lp.height = height;
            }
            if (gravity != 0) {
                lp.gravity = gravity;
            }
            mWindow.setAttributes(lp);
        }
    }


    public void setAnimation(int resId){
        if(null != mWindow && resId !=0){
            mWindow.setWindowAnimations(resId);
        }
    }

    private boolean isActivityValid() {
        if (null != mContext && mContext instanceof Activity) {
            Activity activity = (Activity) mContext;
            if (!activity.isFinishing()) {
                return true;
            } else
                return false;
        } else
            return false;
    }
}
