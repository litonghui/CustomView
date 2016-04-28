package com.android.customview.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.customview.tools.Utils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by litonghui on 2016/3/15.
 */
public class AppHorizontalLayout extends LinearLayout {

    private LinearLayout mContainerLy;
    private TextView mLableView;

    public AppHorizontalLayout(Context context) {
        super(context);
        init();
    }

    public AppHorizontalLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppHorizontalLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void init(){
        setOrientation(VERTICAL);

        //标题
        LinearLayout titleLayout = new LinearLayout(getContext());
        titleLayout.setPadding((int)Utils.dp2px(getContext(), 12), 0, (int)Utils.dp2px(getContext(), 12), 0);
        addView(titleLayout, new LayoutParams(MATCH_PARENT, (int)Utils.dp2px(getContext(), 64)));

        LinearLayout.LayoutParams lp = new LayoutParams(MATCH_PARENT,WRAP_CONTENT);
        mLableView = Utils.generateTextView(getContext(),"谁与争辉", Color.BLACK,18.f);
        mLableView.setSingleLine(true);
        mLableView.setEllipsize(TextUtils.TruncateAt.END);
        titleLayout.addView(mLableView, lp);
        //应用
        HorizontalScrollView scrollView = new HorizontalScrollView(getContext());
        scrollView.setHorizontalFadingEdgeEnabled(false);//不显示两边阴影
        scrollView.setHorizontalScrollBarEnabled(false);//不显示滚动条
        addView(scrollView, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));

        mContainerLy = new LinearLayout(getContext());
        mContainerLy.setOrientation(LinearLayout.HORIZONTAL);

        scrollView.addView(mContainerLy, new ViewGroup.LayoutParams(MATCH_PARENT,WRAP_CONTENT));
    }
    public void setAppArray(String[] array) {
        if (null == array || array.length == 0)
            return;
        mContainerLy.removeAllViews();
        for (String app:array){
            LayoutParams lp = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            lp.rightMargin = (int) Utils.dp2px(getContext(),9);
            AppRectView view = new AppRectView(getContext());
            view.setApp(app);
            mContainerLy.addView(view, lp);
        }
    }

}
