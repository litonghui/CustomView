package com.android.customview.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.customview.R;
import com.android.customview.view.AnimateView;
import com.android.customview.view.PathPoint;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by litonghui on 2016/4/25.
 */
public class BezierActivity extends Activity {

    private ImageView iv_small;
    private ImageView iv_big;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);
        iv_big = (ImageView) findViewById(R.id.iv_big);
        iv_small = (ImageView) findViewById(R.id.iv_small);
    }
    public void onFlyClick(View view){
        flyingAnimation(this,view,iv_small);
    }
    /**
     * 缩放，飞行动画
     *
     * @param context
     * @param v
     */
    public void flyingAnimation(Context context, View v,View targetView) {
        Activity activity = (Activity) context;
        if (activity == null)
            return;
        final ImageView animateView = new ImageView(context);
        /*** x,y坐标*/
        final int[] location = new int[2];
        /**矩形Image*/
        final Rect rect = new Rect();
        v.getLocationInWindow(location);
        rect.left = location[0];
        rect.top = location[1];
        rect.right = location[0] + v.getWidth();
        rect.bottom = location[1] + v.getHeight();
        v.destroyDrawingCache();
        v.setDrawingCacheEnabled(true);
        /*设置飞行动画图片*/
        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
        animateView.setImageBitmap(bitmap);
        v.setDrawingCacheEnabled(false);
        final WindowManager windowManager = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        WindowManager.LayoutParams windowparams = new WindowManager.LayoutParams();
        windowparams.height = MATCH_PARENT;
        windowparams.width = MATCH_PARENT;
        windowparams.format = PixelFormat.TRANSLUCENT;
        windowparams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        final FrameLayout container = new FrameLayout(context);
        container.addView(animateView, new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        windowManager.addView(container, windowparams);

        /*初始坐标*/
        int startX = rect.left;
        int startY = rect.top;
        int startWidth = rect.width();
        int startHeight = rect.height();
        /*结束动画*/
        if (targetView == null) return;
        targetView.getLocationInWindow(location);
        rect.left = location[0];
        rect.top = location[1];
        rect.right = location[0] + targetView.getWidth();
        rect.bottom = location[1] + targetView.getHeight();
        if (rect.left == 0) return;

        /*目标坐标*/
        int endWidth = rect.width();
        int endHeight = rect.height();
        int offsetX = Math.abs((startWidth - endWidth) / 2);
        int offsetY = Math.abs((startHeight - endHeight) / 2);
        float widthScale = endWidth * 1.f / startWidth;
        float heightScale = endHeight * 1.f / startHeight;
        int endX = rect.left - offsetX;
        int endY = rect.top - offsetY;

        final float cx1 = (startX + endX) / 4;
        final float cy1 = startY;
        final float cx2 = (startX + endX) / 2;
        final float cy2 = startY;

        PathPoint startPoint = PathPoint.curveTo(cx1, cy1, cx2, cy2, startX, startY);
        PathPoint endPoint = PathPoint.curveTo(cx1, cy1, cx2, cy2, endX, endY);

        AnimateView wrapView = new AnimateView(animateView);
        ObjectAnimator anim = ObjectAnimator.ofObject(wrapView, "Position", new TypeEvaluator<PathPoint>() {
            @Override
            public PathPoint evaluate(float t, PathPoint startValue, PathPoint endValue) {
                float x, y;//B(t) = PO(1-t)^3 + 3*P1*t(1-t)^2 + 3*P2*t^2(1-t) + P3*t^3
                float oneMinusT = 1 - t;
                x = oneMinusT * oneMinusT * oneMinusT * startValue.mX +
                        3 * oneMinusT * oneMinusT * t * endValue.mControl0X +
                        3 * oneMinusT * t * t * endValue.mControl1X +
                        t * t * t * endValue.mX;
                y = oneMinusT * oneMinusT * oneMinusT * startValue.mY +
                        3 * oneMinusT * oneMinusT * t * endValue.mControl0Y +
                        3 * oneMinusT * t * t * endValue.mControl1Y +
                        t * t * t * endValue.mY;

                return PathPoint.moveTo(x, y);
            }
        }, startPoint, endPoint);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(animateView, "scaleX", widthScale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(animateView, "scaleY", heightScale);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.playTogether(anim, scaleX, scaleY);
        animatorSet.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        try { //View 动画之后remove
                                            container.removeView(animateView);
                                            windowManager.removeView(container);
                                        } catch (Exception e) {
                                        }
                                    }
                                }

        );
        animatorSet.start();
    }
}
