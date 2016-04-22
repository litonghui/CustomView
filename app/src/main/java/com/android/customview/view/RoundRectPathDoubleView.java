package com.android.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by litonghui on 2016/4/19.
 *  通过Path.addRoundRect 和 canvas.drawPath 绘制一个STROKE边框矩形
 * 问题：设置setStrokeWidth 之后发现圆角出现加深现象
 *  * 解决方案：
 *    用path 构建一个圆角矩形
 *    想通过截取方式，接着画第二个矩形
*/
public class RoundRectPathDoubleView extends View {


    public RoundRectPathDoubleView(Context context) {
        this(context, null);
    }

    public RoundRectPathDoubleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundRectPathDoubleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private float mDensity;

    private final RectF mRoundRect = new RectF();

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final Path mPath = new Path();

    private void init() {
        mDensity = getResources().getDisplayMetrics().density;
        mPaint.setStrokeWidth(4f);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
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
        mPaint.setColor(Color.RED);
        mPath.addRoundRect(mRoundRect, r, r, Path.Direction.CW);
        canvas.drawPath(mPath, mPaint);
        canvas.save();

        float dxy = 2;
        mRoundRect.left =dxy;
        mRoundRect.top = dxy;
        mRoundRect.right = w - dxy;
        mRoundRect.bottom = h- dxy;
        mPaint.setColor(Color.RED);
        mPath.addRoundRect(mRoundRect, r, r, Path.Direction.CW);
        canvas.drawPath(mPath, mPaint);

    }
}