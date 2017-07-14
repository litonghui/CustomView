package com.android.customview.activity;

import android.app.Activity;
import android.os.Bundle;

import com.android.customview.R;
import com.android.customview.view.GiftExpandableView;

/**
 * Created by litonghui on 2016/8/18.
 */
public class ExpandableActivity extends Activity implements GiftExpandableView.OnViewExpandedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand);
        GiftExpandableView view = (GiftExpandableView) findViewById(R.id.expand_view);
        view.setData("解释一下原因：\n" +
               "状态的更新是来自浏览器数据库传过来状态，当我们点击暂停，状态强制状态变化，同时传给浏览器，但是与此同时也在接收浏览器\n" +
               "传过来的数据为继续下载，会将状态修改，然后下一刻又会收到浏览器回传过来暂停状态，一瞬间出出现两次。这是以前测试也问题，\n" +
               "不过当时定义为不是bug。主要原因是现在下载在浏览器，暂停和开始都会相互传递，会有时间误差");
        view.setOnViewExpandedListener(this);
    }

    @Override
    public void onViewExpanded(boolean isExpanded) {

    }
}
