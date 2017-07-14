package com.android.customview.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.customview.R;
import com.android.customview.activity.VerifyPasswordActivity;
import com.android.customview.tools.LockUtils;
import com.android.customview.tools.Preferences;
import com.android.customview.tools.Utils;
import com.android.customview.view.LockPatternView;

import java.util.List;

/**
 * Created by litonghui on 2016/5/19.
 */
public class PatternPasswordFragment extends Fragment {

    private Context mContext;

    private String mFirstPassword;

    private TextView mMessageTv;

    private LockPatternView mPatternView;

    private int mType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pattern_password_fragment,container,false);
        mMessageTv = (TextView) view.findViewById(R.id.message_textview);
        mPatternView = (LockPatternView) view.findViewById(R.id.lock_view);
        mPatternView.setOnPatternListener(mOnPatternListener);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        if (LockUtils.PwdType.PATTERN == LockUtils.getCurrentPwdType(getActivity())) {
            mType = VerifyPasswordActivity.TYPE_VERIFICATION;
        } else if (LockUtils.PwdType.SLIDE == LockUtils.getCurrentPwdType(getActivity())) {
            mType = VerifyPasswordActivity.TYPE_PD_SETTING;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    LockPatternView.OnPatternListener mOnPatternListener = new LockPatternView.OnPatternListener() {
        @Override
        public void onPatternStart() {
            mPatternView.removeCallbacks(mLockPatternViewReloader);
        }

        @Override
        public void onPatternCleared() {

        }

        @Override
        public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {

        }

        @Override
        public void onPatternDetected(List<LockPatternView.Cell> pattern) {
            if(null != pattern && pattern.size() >0) {
                String pwd = LockUtils.convertToPwd(pattern);
                if (!TextUtils.isEmpty(pwd)) {
                    if (mType == VerifyPasswordActivity.TYPE_VERIFICATION) {
                        String savepwd = Preferences.getLockPassword(getContext());
                        String currentpwd = Utils.md5(pwd);
                        if (!TextUtils.isEmpty(savepwd) &&
                                !TextUtils.isEmpty(currentpwd) &&
                                savepwd.equals(currentpwd)) {
                            getActivity().finish();
                        } else {
                            mPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                            mPatternView.postDelayed(mLockPatternViewReloader,
                                    1000L);
                            ShowMessage("密码错误");
                        }
                    } else {
                        /**第一次设置 且 密码不能少于三个*/
                        if (null == mFirstPassword && pattern.size() < 3){
                            mPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                            mPatternView.postDelayed(mLockPatternViewReloader,
                                    1000L);
                            ShowMessage("密码不少于3个");
                        }else{
                            if(mFirstPassword == null){
                                mFirstPassword = pwd;
                                mPatternView.postDelayed(mLockPatternViewReloader,
                                        1000L);
                                ShowMessage("请再次绘制");
                            }else{
                                if(!TextUtils.isEmpty(mFirstPassword)&&
                                        mFirstPassword.equals(pwd)){
                                    Preferences.setLockPassword(getActivity(), Utils.md5(pwd));
                                    LockUtils.setCurrentPwdType(getActivity(), LockUtils.PwdType.PATTERN);
                                    getActivity().finish();
                                }else{
                                    mPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                                    mPatternView.postDelayed(mLockPatternViewReloader,
                                            1000L);
                                    ShowMessage("密码不一致");
                                }
                            }
                        }

                    }
                }
            }
        }
    };
    private void ShowMessage(String text){
        mMessageTv.setText(text);
    }

    private final Runnable mLockPatternViewReloader = new Runnable() {

        @Override
        public void run() {
            mPatternView.clearPattern();
        }
    };

    private final Runnable mPatternViewEnabler = new Runnable() {
        @Override
        public void run() {
            mPatternView.setEnabled(true);
            ShowMessage("请绘制解锁密码");
        }
    };

}
