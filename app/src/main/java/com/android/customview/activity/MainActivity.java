package com.android.customview.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.customview.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onRectClick(View view){
       startActivity(new Intent(this, RectActivity.class));
    }
    public void onBezierClick(View view){
        startActivity(new Intent(this,BezierActivity.class));
    }

    public void onRxClick(View view){
        startActivity(new Intent(this,ReactivexActivity.class));
    }

    public void onScrollClick(View view) {
        startActivity(new Intent(this, ScrollActivity.class));
    }

}
