package com.android.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.android.customview.R;


/**
 * Created by wangzhengyi on 2017/8/18.
 */

public class GradientView extends View{

    private int mWidth;
    private int mHeight;

    private Drawable mPic;
    private Bitmap mBitmap;
    private boolean isPalette;
    private int mBgColor;

    private boolean isPreDrawInit = false;
    private Rect mRcBmp = new Rect();
    private Paint mPaint = new Paint();
    private PaintFlagsDrawFilter mPaintFilter;
    private LinearGradient mLinearGradient;
    private BitmapShader mBitmapShader;
    private ComposeShader mComposeShader;

    public GradientView(Context context) {
        super(context);
    }

    public GradientView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GradientView);
        mPic = a.getDrawable(R.styleable.GradientView_drawable);
        mBgColor = a.getColor(R.styleable.GradientView_bgcolor, 0xFFFFB401);
        isPalette = a.getBoolean(R.styleable.GradientView_palette, true);
        a.recycle();
        init();
    }

    private void init(){
        mPaint.setDither(false);
        mPaint.setAntiAlias(true);
        mPaintFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
        ViewTreeObserver vto = this.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if(!isPreDrawInit){
                    mWidth = GradientView.this.getMeasuredWidth();
                    mHeight = GradientView.this.getMeasuredHeight();
                    getPaint(mPic);
                    isPreDrawInit = true;
                }
                return true;
            }
        });
    }

    public void setNewPic(int drawableID, int color){
        mBgColor = color;
        getPaint(ContextCompat.getDrawable(getContext(), drawableID));
    }

    private void getPaint(Drawable drawable){
        if(drawable == null)
            return;
        mBitmap = drawableToBitmap(drawable, mWidth);
        if(isPalette){
            //generatePaletteColor(mBitmap);
        }else{
            generatePaint();
        }
    }

    private void  generatePaint(){
        setBackgroundColor(mBgColor);
        mRcBmp.set(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mLinearGradient = new LinearGradient(0, mBitmap.getHeight()/4, 0, mBitmap.getHeight()*3/4, new int[] {
                Color.TRANSPARENT, mBgColor}, null,
                Shader.TileMode.CLAMP);
        mComposeShader = new ComposeShader(mBitmapShader, mLinearGradient,  PorterDuff.Mode.SRC_ATOP);
        mPaint.setShader(mComposeShader);
        invalidate();
    }

    /*private void generatePaletteColor(Bitmap bitmap){
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

            @Override
            public void onGenerated(Palette arg0) {
                int color = arg0.getVibrantColor(0xffffffff);
                mBgColor = color;
                generatePaint();
            }
        });
    }*/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(mPaintFilter);
        canvas.drawRect(mRcBmp, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int width;
        final int specMode = MeasureSpec.getMode(measureSpec);
        final int specSize = MeasureSpec.getSize(measureSpec);

        if (MeasureSpec.EXACTLY == specMode) {
            width = specSize;
        } else {
            width = getPaddingLeft() + getPaddingRight() + 1;
            if (MeasureSpec.AT_MOST == specMode) {
                width = Math.min(width, specSize);
            }
        }

        return width;
    }

    private int measureHeight(int measureSpec) {
        int height;
        final int specMode = MeasureSpec.getMode(measureSpec);
        final int specSize = MeasureSpec.getSize(measureSpec);

        if (MeasureSpec.EXACTLY == specMode) {
            height = specSize;
        } else {
            height = getPaddingTop() + getPaddingBottom() + 1;
            if (MeasureSpec.AT_MOST == specMode) {
                height = Math.min(height, specSize);
            }
        }

        return height;
    }

    public Bitmap drawableToBitmap(Drawable drawable, int width) {
        int height = drawable.getIntrinsicHeight() * mWidth / drawable.getIntrinsicWidth();
        Bitmap bitmap = Bitmap.createBitmap(
                width,
                height,
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    public void destroy() {
        if ( mBitmap != null ) {
            mBitmap.recycle();
        }
    }

}
