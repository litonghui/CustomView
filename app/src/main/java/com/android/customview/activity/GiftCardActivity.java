package com.android.customview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.customview.R;
import com.android.customview.view.GiftCardView;

/**
 * Created by litonghui on 2016/7/29.
 */
public class GiftCardActivity extends AppCompatActivity {

    private GiftCardView mGiftCardView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giftcard);
        mGiftCardView = (GiftCardView) findViewById(R.id.gift_view);
        mGiftCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGiftCardView.restore();
            }
        });
    }
}
