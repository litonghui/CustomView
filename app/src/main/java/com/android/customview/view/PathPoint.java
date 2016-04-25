package com.android.customview.view;

/**
 * Created by admin on 2015/8/21.
 */
public class PathPoint {
    public float mX, mY;

    public float mControl0X, mControl0Y;

    public float mControl1X, mControl1Y;

    private PathPoint(float x, float y) {
        mX = x;
        mY = y;
    }

    private PathPoint(float c0X, float c0Y, float c1X, float c1Y, float x, float y) {
        mControl0X = c0X;
        mControl0Y = c0Y;
        mControl1X = c1X;
        mControl1Y = c1Y;
        mX = x;
        mY = y;
    }

    public static PathPoint curveTo(float c0X, float c0Y, float c1X, float c1Y, float x, float y) {
        return new PathPoint(c0X, c0Y, c1X, c1Y, x, y);
    }

    public static PathPoint moveTo(float x, float y) {
        return new PathPoint(x, y);
    }
}
