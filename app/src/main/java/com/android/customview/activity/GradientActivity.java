package com.android.customview.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.android.customview.R;

/**
 * Created by litonghui on 2017/8/21.
 */

public class GradientActivity extends Activity {
    private ImageView mImgCode;
    private ImageView mImgCode2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradient);
        mImgCode = (ImageView) findViewById(R.id.img_gradient_code);
        mImgCode2 = (ImageView) findViewById(R.id.img_gradient_code2);
        mImgCode.setImageBitmap(
                addGradient
                        (drawableToBitmap
                                (ContextCompat.getDrawable
                                        (getApplicationContext(), R.mipmap.icon_temp))
                                ,0xFF670032)
        );

        mImgCode2.setImageBitmap(
                addGradient2
                        (drawableToBitmap
                                (ContextCompat.getDrawable
                                        (getApplicationContext(), R.mipmap.icon_temp))
                                ,0xFF670032)
        );
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return (((BitmapDrawable) drawable).getBitmap());
        }
        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    public static Bitmap addGradient(Bitmap originalBitmap,int templateColor) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        Bitmap updatedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(updatedBitmap);
        canvas.drawBitmap(originalBitmap, 0, 0, null);

        Paint paint = new Paint();
        LinearGradient linearGradient = new LinearGradient(0, height / 4, 0, height * 3 / 4,
                Color.TRANSPARENT, templateColor, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawRect(0, 0, width, height, paint);
        return updatedBitmap;
    }

    public static Bitmap addGradient2(Bitmap originalBitmap,int templateColor) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        Bitmap updatedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(updatedBitmap);
        canvas.drawBitmap(originalBitmap, 0, 0, null);

        Paint paint = new Paint();
        BitmapShader bitmapShader = new BitmapShader(originalBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        LinearGradient linearGradient = new LinearGradient(0, height / 4, 0, height * 3 / 4,
                Color.TRANSPARENT, templateColor,
                Shader.TileMode.CLAMP);
        ComposeShader composeShader = new ComposeShader(bitmapShader, linearGradient, PorterDuff.Mode.SRC_ATOP);
        paint.setShader(composeShader);
        canvas.drawRect(0, 0, width, height, paint);
        return updatedBitmap;
    }
}
