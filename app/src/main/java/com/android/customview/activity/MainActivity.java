package com.android.customview.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.customview.R;
import com.android.customview.tools.Utils;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("lth","code:"+ Utils.getVersionCode()+"  channel:"+Utils.getChannel());
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
    public void onUninstallClick(View view){
        startActivity(new Intent(this,UninstallActivity.class));
    }
    public void onDialogClick(View view){
        startActivity(new Intent(this,DialogActivity.class));
    }
    public void onLockClick(View view){
        startActivity(new Intent(this,LockActivity.class));}
    public void onDropClick(View view){
        startActivity(new Intent(this,BackDropActivity.class));
    }
    public void onSetClick(View view){
        startActivity(new Intent(this,SettingActivity.class));
    }

    public void onSlantedClick(View view){
        startActivity(new Intent(this,SlantedActivity.class));
    }
    public void onShopClick(View view){
        startActivity(new Intent(this,ShopActivity.class));
    }
    public void onRcViewClick(View view){
        startActivity(new Intent(this,RCViewActivity.class));
    }
    public void onGiftClick(View view){
        startActivity(new Intent(this,GiftCardActivity.class));
    }
    public void onExpandableClick(View view){
        startActivity(new Intent(this,ExpandableActivity.class));
    }

    public void onMovieRecorderClick(View view){
        startActivity(new Intent(this,MovieRecorderActivity.class));
    }

    public void onRSBlurClick(View view){
        startActivity(new Intent(this,RSBlurActivity.class));
    }

    public void onGradientClick(View view) {
        startActivity(new Intent(this, GradientActivity.class));
    }

    public void onCircularClick(View view) {
        startActivity(new Intent(this, CircularFillableActivity.class));
    }





}
