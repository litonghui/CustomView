package com.android.customview.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.customview.R;
import com.android.customview.tools.LockUtils;
import com.android.customview.tools.Preferences;
import com.android.customview.view.SeleterView;

/**
 * Created by litonghui on 2016/5/16.
 */
public class LockActivity extends Activity{

    private Context mContext;

    private SeleterView mSelectView_i,mSelectView_ii,mSelectView_iii;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        mContext = this ;
        initView();
        initData();
    }
    private void initView(){
        mSelectView_i = (SeleterView) findViewById(R.id.select_i);
        mSelectView_ii = (SeleterView) findViewById(R.id.select_ii);
        mSelectView_iii = (SeleterView) findViewById(R.id.select_iii);
    }
    private void initData(){
        mSelectView_i.setTitle("密码为空");
        mSelectView_ii.setTitle("数字密码");
        mSelectView_iii.setTitle("图形密码");
        mSelectView_i.setDescrip("忽视");
        mSelectView_ii.setDescrip("由四个数字组成");
        mSelectView_iii.setDescrip("由九宫格图形组成");

    }
    public void onSelectClick(View view) {
        switch (view.getId()) {
            case R.id.select_i:
                Preferences.setLockPassword(mContext, "");
                LockUtils.setCurrentPwdType(mContext, LockUtils.PwdType.SLIDE);
                break;
            case R.id.select_ii:
                startActivity(new Intent(this,VerifyPDActivity.class));
               // Toast.makeText(mContext,"数字密码",Toast.LENGTH_SHORT).show();
                break;
            case R.id.select_iii:
                startActivity(new Intent(this,VerifyPTActivity.class));
               // Toast.makeText(mContext,"图形密码",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

}
