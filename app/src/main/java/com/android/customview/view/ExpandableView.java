package com.android.customview.view;

import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.customview.tools.Utils;


public class ExpandableView extends LinearLayout implements View.OnClickListener {

    public interface OnViewExpandedListener{
        public void onViewExpanded(boolean isExpanded);
    }

    private Context mContext;

    private TextView mDescripTv;
    private TextView mNewFeatureTv;

    private boolean mAnimating;
    private boolean mRelayout;
    private boolean mCollapsed = true;

    private int mCollapsedHeight;
    private int mExpandableHeight;
    private int mMaxHeight_DescrpiTv;
    private int mMaxHeight_VersionInfo;

    private LayoutParams layoutParams;
    private Paint mTextPaint;
    private int textHeight;

    private OnViewExpandedListener mListener;

    private static final int ANIM_DURATION = 300;
    private static final float TEXT_SIZE_SP = 13.0f;

    public void setOnViewExpandedListener(OnViewExpandedListener viewExpandedListener) {
        this.mListener = viewExpandedListener;
    }

    public int getmExpandableHeight() {
        return mExpandableHeight;
    }

    public void setmExpandableHeight(int mExpandableHeight) {
        this.mExpandableHeight = mExpandableHeight;
    }

    public ExpandableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public ExpandableView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    private void initView(){
        mTextPaint = new Paint();
        mTextPaint.setTextSize(Utils.dp2px(getContext(), TEXT_SIZE_SP));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        textHeight = (int) Math.abs(mTextPaint.descent()) + (int)Math.abs(mTextPaint.ascent());
        getCollapsedHeight();


        layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setOrientation(VERTICAL);
        setLayoutParams(layoutParams);
        mDescripTv = generateDescripTv();
        mNewFeatureTv = generateDescripTv();
        setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(!mRelayout)
            return;
        int measureWidth = measureWidth(widthMeasureSpec);

        if(mExpandableHeight == 0){
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                mExpandableHeight += getChildAt(i).getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            }
        }
        if(mMaxHeight_DescrpiTv == 0){
            mMaxHeight_DescrpiTv = mDescripTv.getMeasuredHeight();
        }
        if(mMaxHeight_VersionInfo == 0){
            mMaxHeight_VersionInfo = mNewFeatureTv.getMeasuredHeight();
        }
        mDescripTv.getLayoutParams().height = mMaxHeight_DescrpiTv;
        mNewFeatureTv.getLayoutParams().height = mMaxHeight_VersionInfo;
        setMeasuredDimension(measureWidth, mCollapsedHeight);
    }

    private int measureWidth(int pWidthMeasureSpec) {
        int result = 0;
        int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式
        int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// 得到尺寸

        switch (widthMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
        }
        return result;
    }



    public void setData(String descripStr,
                        String newFeatureStr,
                        String versionStr,
                        String updateDateStr){
        mRelayout = true;
        addView(mDescripTv);
        mDescripTv.setText(Html.fromHtml(descripStr));
        if(!TextUtils.isEmpty(newFeatureStr)){
            addView(mNewFeatureTv);
           /* mNewFeatureTv.setText(mContext.getResources().
                    getString(R.string.app_detail_new_feature,
                            Html.fromHtml(
                                    newFeatureStr.replaceAll("(\r\n|\n)", "<br/>"))));*/
            mNewFeatureTv.setText("asdad");
        }
    }

    @Override
    public void onClick(View v) {
        mRelayout = false;
        if(mAnimating)
            return;

        mAnimating = true;

        Animation animation;
        if (mCollapsed) {
            animation = new ExpandCollapseAnimation(this, getHeight(), mExpandableHeight);
        } else {
            animation = new ExpandCollapseAnimation(this, getHeight(), mCollapsedHeight);
        }
        mCollapsed = !mCollapsed;
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(null != mListener){
                    mListener.onViewExpanded(!mCollapsed);
                }
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                clearAnimation();
                mAnimating = false;
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        clearAnimation();
        startAnimation(animation);

    }

    private void getCollapsedHeight(){
            mCollapsedHeight = (int) (2 * textHeight +
                    Utils.dp2px(getContext(), 12 + 13));
    }

    protected class ExpandCollapseAnimation extends Animation {
        private final View mTargetView;
        private final int mStartHeight;
        private final int mEndHeight;

        public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
            mTargetView = view;
            mStartHeight = startHeight;
            mEndHeight = endHeight;
            setDuration(ANIM_DURATION);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final int newHeight = (int)((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
            mTargetView.getLayoutParams().height = newHeight;
            mTargetView.requestLayout();
        }

        @Override
        public void initialize( int width, int height, int parentWidth, int parentHeight ) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds( ) {
            return true;
        }
    };

    //应用介绍
    private TextView generateDescripTv(){
        return generateTextView();

    }

    private TextView generateTextView(){
        TextView tv = new TextView(mContext);
        tv.setTextColor(0xff333333);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_SP);
        tv.setLineSpacing(Utils.dp2px(mContext, 6), 1);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.topMargin = (int) Utils.dp2px(mContext, 12);
        tv.setLayoutParams(lp);
        return tv;
    }

}
