package com.android.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

import com.android.customview.R;

/**
 * Created by litonghui on 2016/4/19.
 * 通过xml 的shap设置矩形边框
 * 成功
 */
public class ShapRectView extends View {



    public ShapRectView(Context context) {
        this(context, null);
    }

    public ShapRectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapRectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0)
            return;
        setBackgroundResource(R.drawable.shap_stroke);
    }
}
