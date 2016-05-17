package com.android.customview.view;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.customview.tools.Utils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by litonghui on 2016/3/15.
 */
public class AppRectView extends LinearLayout {
    private final static int IMAGE_SIZE = 68;
    private String mAppName;
    private Drawable mDrawable;

    public AppRectView(Context context) {
        super(context);
        init();
    }

    public AppRectView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppRectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void init(){
        setOrientation(VERTICAL);
        int top = (int) Utils.dp2px(getContext(), 9);
        int left = (int) Utils.dp2px(getContext(), 12);
        setPadding(left, top, left, top);
        setOrientation(VERTICAL);
    }
    public void setApp(String packname) {
        if (TextUtils.isEmpty(packname)) return;

        PackageManager packageManager = getContext().getPackageManager();
        if(null == packageManager)
            return;
        try{
            ApplicationInfo info = packageManager.getApplicationInfo(packname, 0);
            if(null == info)
                return;
            mAppName = info.loadLabel(packageManager).toString();
            mDrawable = packageManager.getApplicationIcon(packname);
            if(mAppName!=null && mAppName.length()>6){
                mAppName = mAppName.substring(0,6) + ".....";
            }
        }catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if( null == mDrawable) return;
        int width = (int) Utils.dp2px(getContext(), IMAGE_SIZE);
        ImageView view = new ImageView(getContext());
        view.setBackground(mDrawable);
        addView(view,new LayoutParams(width,width));

        if(!TextUtils.isEmpty(mAppName)){
            LayoutParams lp1 = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            lp1.topMargin = (int) Utils.dp2px(getContext(),8);
            TextView textView = Utils.generateTextView(getContext(), mAppName, 0xFF333333, 17.f);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            addView(textView, lp1);
        }

        String appSize = "14.3MB";
        if(!TextUtils.isEmpty(appSize)){
            LayoutParams lp = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            lp.topMargin = (int) Utils.dp2px(getContext(), 8);
            TextView textView = Utils.generateTextView(getContext(), appSize, 0xF7AFAFAF, 12.f);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            addView(textView, lp);
        }
    }


}
