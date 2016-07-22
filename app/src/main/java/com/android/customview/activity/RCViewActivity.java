package com.android.customview.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.android.customview.R;
import com.android.customview.fragment.RCViewFragment;

/**
 * Created by litonghui on 2016/7/22.
 */
public class RCViewActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rcview);
        if(savedInstanceState == null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RCViewFragment rcviewFragment = new RCViewFragment();
            transaction.replace(R.id.sample_content_fragment,rcviewFragment);
            transaction.commit();
        }
    }
}
