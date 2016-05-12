package com.android.customview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.customview.R;
import com.android.customview.view.AppBubbleDialog;

/**
 * Created by litonghui on 2016/5/10.
 */
public class DialogActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        initView();
    }

    private void initView() {
        final AppBubbleDialog dialog = new AppBubbleDialog(this);
        dialog.initView();
        TextView textView = (TextView) findViewById(R.id.tv_show);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }
}
