package com.android.customview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.android.customview.R;
import com.android.customview.model.LocalPackageInfo;
import com.android.customview.model.LocalPackageManager;
import com.android.customview.view.AppHorizontalLayout;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by litonghui on 2016/4/28.
 */
public class ScrollActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        init();
    }
    private  void init(){
        LinearLayout layout = (LinearLayout) findViewById(R.id.ly_scroll);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        AppHorizontalLayout horizontalLayout = new AppHorizontalLayout(this);
        horizontalLayout.setAppArray(getArray(10));
        layout.addView(horizontalLayout,lp);
    }
    private String[] getArray(int num) {
        int index = 0;
        String[]  array = new String[num];
        List<LocalPackageInfo> infos = LocalPackageManager.getInstance().mLocalInfos;
        if (infos != null && infos.size() > 0) {
            for (LocalPackageInfo info : infos) {
                if (info != null && info.packagename != null) {
                    array[index] = info.packagename;
                    index++;
                    if (index == num)
                        break;
                }
            }
        }
        return array;
    }
}
