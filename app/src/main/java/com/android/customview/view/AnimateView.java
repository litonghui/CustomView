package com.android.customview.view;

import android.widget.ImageView;

/**
 * Created by admin on 2015/8/21.
 */
public class AnimateView {
    private final ImageView mImageView;

    public AnimateView(ImageView imageView) {
        mImageView = imageView;
    }

    @SuppressWarnings("NewApi")
    public void setPosition(PathPoint point) {
        mImageView.setTranslationX(point.mX);
        mImageView.setTranslationY(point.mY);
    }
}
