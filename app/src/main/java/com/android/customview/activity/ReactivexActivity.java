package com.android.customview.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.customview.R;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by litonghui on 2016/4/25.
 */
public class ReactivexActivity extends Activity {
    ImageView mIvView;
    int drawableRes;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx);
        mContext = this;
        mIvView = (ImageView) findViewById(R.id.iv_view);
        drawableRes = R.mipmap.ic_launcher;
        dojust();
    }

    private void dojust() {
        Subscription observable = Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getDrawable(drawableRes);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())//指定subscribe 发生在子线程
                .observeOn(AndroidSchedulers.mainThread())//指定subscribe 回调发生在主线程
                .subscribe(new Observer<Drawable>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(mContext, "加载完成", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        mIvView.setImageDrawable(drawable);
                    }
                });
    }
}
