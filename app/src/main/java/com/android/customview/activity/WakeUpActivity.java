package com.android.customview.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.android.customview.R;

/**
 * Created by litonghui on 2017/8/9.
 */

public class WakeUpActivity extends Activity {

    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wakeup);
        context = this;
        findViewById(R.id.iv_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName("com.sogou.androidtool","com.sogou.androidtool.service.CoreService");
                context.startService(intent);
                Toast.makeText(context,"正在启动...",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
