package com.android.customview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.customview.R;

/**
 * Created by litonghui on 2016/5/24.
 */
public class SettingActivity extends Activity implements  View.OnClickListener {

    /**
     * 设置新消息通知布局
     */
    private RelativeLayout mSwitchNotify;
    /**
     * 设置扬声器布局
     */
    private RelativeLayout rl_switch_speaker;

    /**
     * 打开新消息通知imageView
     */
    private ImageView iv_switch_open_notification;
    /**
     * 关闭新消息通知imageview
     */
    private ImageView iv_switch_close_notification;
    /**
     * 打开声音提示imageview
     */
    private ImageView iv_switch_open_sound;
    /**
     * 关闭声音提示imageview
     */
    private ImageView iv_switch_close_sound;

    private  boolean isNotifyVisiable = false;

    private  boolean isOpenisiable = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }
    private void init(){
        mSwitchNotify = (RelativeLayout) findViewById(R.id.rl_switch_notification);
        mSwitchNotify.setOnClickListener(this);
        iv_switch_open_notification = (ImageView) findViewById(R.id.iv_switch_open_notification);
        iv_switch_close_notification = (ImageView) findViewById(R.id.iv_switch_close_notification);

        rl_switch_speaker = (RelativeLayout) findViewById(R.id.rl_switch_speaker);
        rl_switch_speaker.setOnClickListener(this);
        iv_switch_open_sound = (ImageView) findViewById(R.id.iv_switch_open_sound);
        iv_switch_close_sound = (ImageView) findViewById(R.id.iv_switch_close_sound);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_switch_notification:
                isNotifyVisiable = iv_switch_open_notification.getVisibility() == View.VISIBLE ? true : false;
                iv_switch_open_notification.setVisibility(isNotifyVisiable?View.INVISIBLE:View.VISIBLE);
                iv_switch_close_notification.setVisibility(isNotifyVisiable?View.VISIBLE:View.INVISIBLE);
                break;
            case R.id.rl_switch_speaker:
                isOpenisiable = iv_switch_open_sound.getVisibility() == View.VISIBLE ? true : false;
                iv_switch_open_sound.setVisibility(isOpenisiable?View.INVISIBLE:View.VISIBLE);
                iv_switch_close_sound.setVisibility(isOpenisiable?View.VISIBLE:View.INVISIBLE);
                break;

        }
    }
}
