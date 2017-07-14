package com.android.customview.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.customview.R;
import com.android.customview.view.MovieRecorderView;

/**
 * Created by litonghui on 2017/7/13.
 */

public class MovieRecorderActivity extends Activity{

    MovieRecorderView mMovieRecorderView;
    ImageView mImageView;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_recorder);
        mContext = this;
        mMovieRecorderView = (MovieRecorderView) findViewById(R.id.movie_recorder);
        mImageView = (ImageView) findViewById(R.id.start);
        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mMovieRecorderView.record(new MovieRecorderView.OnRecordFinishListener() {
                            @Override
                            public void onRecordFinish() {
                                Toast.makeText(mContext, "成功保存", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mMovieRecorderView.getTimeCount() > 3) {
                            Toast.makeText(mContext, "成功保存：" + mMovieRecorderView.getmVecordFile().toString(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            mMovieRecorderView.getmVecordFile().delete();
                            mMovieRecorderView.stop();
                            Toast.makeText(mContext, "太短咧", Toast.LENGTH_SHORT).show();
                        }

                        break;
                }
                return false;
            }
        });
    }
}
