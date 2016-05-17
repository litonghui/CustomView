package com.android.customview.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.customview.R;

/**
 * Created by litonghui on 2016/5/16.
 */
public class SeleterView extends LinearLayout{

    private Context mContext;

    private TextView mTitleTv;

    private TextView mDescripTv;

    public SeleterView(Context context) {
        this(context, null);
    }

    public SeleterView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SeleterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }
    private void init(){
        LayoutInflater.from(mContext)
                .inflate(R.layout.item_selecter, this, true);
        mTitleTv = (TextView) findViewById(R.id.tv_title);
        mDescripTv = (TextView) findViewById(R.id.tv_descrip);
    }

    /**----------外部调用接口----------**/
    public void setTitle(String title){
        if(!TextUtils.isEmpty(title)){
            mTitleTv.setText(title);
        }
    }
    public void setTitle(int resId){
        if(null != mContext){
            setTitle(mContext.getResources().getString(resId));
        }
    }
    public void setDescrip(String title){
        if(!TextUtils.isEmpty(title)){
            mDescripTv.setText(title);
        }
    }
    public void setDescrip(int resId){
        if(null != mContext){
            setDescrip(mContext.getResources().getString(resId));
        }
    }

}
