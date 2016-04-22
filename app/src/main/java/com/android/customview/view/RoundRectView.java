package com.android.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by litonghui on 2016/4/19.
 * 通过drawRoundRect 绘制一个STROKE边框矩形
 * 问题：设置setStrokeWidth 之后发现圆角出现加深现象
 */
public class RoundRectView extends View {


    private float mDensity;

    private final RectF mRoundRect = new RectF();

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public RoundRectView(Context context) {
        this(context, null);
    }
    public RoundRectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public RoundRectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDensity = getResources().getDisplayMetrics().density;
        mPaint.setStrokeWidth(4f);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0)
            return;
        mRoundRect.left = 0;
        mRoundRect.top = 0;
        mRoundRect.right = w;
        mRoundRect.bottom = h;
        float r = 5 * mDensity;
        canvas.drawRoundRect(mRoundRect, r, r, mPaint);
    }
}