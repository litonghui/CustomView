package com.android.customview.view;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.customview.R;
import com.android.customview.tools.Utils;

/**
 * Created by litonghui on 2016/8/19.
 */
public class GiftExpandableView extends LinearLayout implements View.OnClickListener {

    private TextView mAppDescrip;
    private RelativeLayout mAppKeyLayout;
    private ImageView mArrowDown;

    private boolean mAnimating;
    private boolean mRelayout;
    private boolean mCollapsed = true;

    private int mCollapsedHeight;
    private int mExpandableHeight;

    private Paint mTextPaint;
    private int textHeight;

    private OnViewExpandedListener mListener;

    private static final int ANIM_DURATION = 300;
    private static final float TEXT_SIZE_SP = 13.0f;



    public interface OnViewExpandedListener{
        public void onViewExpanded(boolean isExpanded);
    }

    private Context mContext;

    public GiftExpandableView(Context context) {
        this(context,null);
    }

    public GiftExpandableView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GiftExpandableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    public void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.layout_gift_getway_header, this);
        mAppDescrip = (TextView) findViewById(R.id.app_descrip);
        mAppKeyLayout = (RelativeLayout) findViewById(R.id.ry_key);
        mArrowDown = (ImageView) findViewById(R.id.arrow_down);

        mTextPaint = new Paint();
        mTextPaint.setTextSize(Utils.dp2px(getContext(), TEXT_SIZE_SP));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        textHeight = (int) Math.abs(mTextPaint.descent()) + (int)Math.abs(mTextPaint.ascent());
        getCollapsedHeight();
        setOnClickListener(this);
    }


    private void getCollapsedHeight(){
        mCollapsedHeight = (int) (2 * textHeight +
                Utils.dp2px(getContext(), 12 + 13));
    }

    public void setOnViewExpandedListener(OnViewExpandedListener viewExpandedListener) {
        this.mListener = viewExpandedListener;
    }

    public void setData(String descrip){
        if(!TextUtils.isEmpty(descrip)){
            mAppDescrip.setText(descrip);
            mRelayout = true;
        }
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

    @Override
    public void onClick(View v) {
        mRelayout = false;
        setAinmation();

    }

    protected void setAinmation(){
        mAnimating = true;

        Animation animation;
        if (mCollapsed) {
            animation = new ExpandCollapseAnimation(this, getHeight(), mExpandableHeight);
            mArrowDown.setVisibility(GONE);
        } else {
            animation = new ExpandCollapseAnimation(this, getHeight(), mCollapsedHeight);
            mArrowDown.setVisibility(VISIBLE);
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
}
