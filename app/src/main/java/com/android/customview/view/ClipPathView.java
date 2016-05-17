package com.android.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

import com.android.customview.R;
import com.android.customview.tools.Utils;

/**
 * Created by litonghui on 2016/4/19.
 * 需求在stroke边框矩形绘制一个进度的状态，通过path 先后画两个矩形，取相交部分，
 * 实现左边为边框矩形，右边为直角矩形，成功绘制进度条
 */
public class ClipPathView extends View {


    public ClipPathView(Context context) {
        this(context, null);
    }

    public ClipPathView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipPathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private float mDensity;

    private final RectF mRoundRect = new RectF();

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final Path mPath = new Path();

    private Context mContext;

    private void init() {
        mDensity = getResources().getDisplayMetrics().density;
        setBackgroundResource(R.drawable.shap_stroke);
        mContext = getContext();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0)
            return;
        mRoundRect.left = Utils.dx2dp(mContext,2f);
        mRoundRect.top = Utils.dx2dp(mContext,2f);
        mRoundRect.right = w - Utils.dx2dp(mContext,2f);
        mRoundRect.bottom = h - Utils.dx2dp(mContext,2f);
        float r = 2.5f * mDensity;
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0xFFF26E66);
        canvas.drawRoundRect(mRoundRect, r, r, mPaint);
        canvas.save();   //save：用来保存Canvas的状态。save之后，可以调用Canvas的平移、放缩、旋转、错切、裁剪等操作。
        mPaint.reset();  //重置绘制路线，即隐藏之前绘制的轨迹
        mPath.addRoundRect(mRoundRect, r, r, Path.Direction.CW);
        canvas.clipPath(mPath, Region.Op.INTERSECT);
        /**
         *
         * clipRect的绘制范围设为A，clipRect设定的范围设为B
         *
         * Op.DIFFERENCE，实际上就是求得的A和B的差集范围，即A－B，只有在此范围内的绘制内容才会被显示；
         *
         * Op.REVERSE_DIFFERENCE，实际上就是求得的B和A的差集范围，即B－A，只有在此范围内的绘制内容才会被显示；；
         *
         * Op.INTERSECT，即A和B的交集范围，只有在此范围内的绘制内容才会被显示；
         *
         * Op.REPLACE，不论A和B的集合状况，B的范围将全部进行显示，如果和A有交集，则将覆盖A的交集范围；
         *
         * Op.UNION，即A和B的并集范围，即两者所包括的范围的绘制内容都会被显示；
         *
         * Op.XOR，A和B的补集范围，此例中即A除去B以外的范围，只有在此范围内的绘制内容才会被显示；
         */
        mRoundRect.left = Utils.dx2dp(mContext,2f);
        mRoundRect.top = Utils.dx2dp(mContext,2f);
        mRoundRect.right = w / 2 - Utils.dx2dp(mContext,2f);
        mRoundRect.bottom = h - Utils.dx2dp(mContext,2f);
        mPaint.setAntiAlias(true);
        mPaint.setColor(0xFFF26E66);
        canvas.drawRect(mRoundRect, mPaint);
        canvas.restore();  //restore：用来恢复Canvas之前保存的状态。防止save后对Canvas执行的操作对后续的绘制有影响。
        // save和restore要配对使用（restore可以比save少，但不能多），如果restore调用次数比save多，会引发Error。
    }
}
