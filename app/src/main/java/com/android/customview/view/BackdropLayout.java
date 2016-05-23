package com.android.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.customview.tools.Utils;

/**
 * Created by litonghui on 2016/5/23.
 */
public class BackdropLayout extends RelativeLayout {

    private Context mContext;

    private ImageView mBackView;

    private ImageView mSmallView;

    public BackdropLayout(Context context) {
        this(context,null);
    }

    public BackdropLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BackdropLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }
    private void init(){
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mBackView  = new ImageView(mContext);
        mBackView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(mBackView, lp);

        View coverView = new View(mContext);
        coverView.setBackgroundColor(0X80000000);
        addView(coverView, lp);

        int width = (int) Utils.dp2px(mContext, 80);
        int iconViewId = Utils.generateViewId();
        mSmallView = new ImageView(mContext);
        mSmallView.setId(iconViewId);
        LayoutParams slp =new LayoutParams(width,width);
        slp.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(mSmallView, slp);
    }

    private void setBackView(Bitmap bitmap){
        if(null != bitmap ){
            int w = bitmap.getWidth();
            int setoff = (int) (w * 0.1);
            int side = w - setoff *2 ;
            Bitmap cropBmp = Bitmap.createBitmap(bitmap,setoff,setoff,side,side);
            Bitmap blurBmp = Bitmap.createBitmap(side,side, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(blurBmp);
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(cropBmp, 0, 0, null);
            cropBmp.recycle();

            blurBmp = Blur.doBlur(blurBmp, 16, true);//图片模糊处理
            if(blurBmp !=null){
                 mBackView.setImageBitmap(blurBmp);
            }
            mSmallView.setImageBitmap(bitmap);
        }

    }
    public void setLayout(int resId){
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resId);
        setBackView(bmp);
    }


}
