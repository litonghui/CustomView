package com.android.customview.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.customview.R;
import com.android.customview.activity.VerifyPasswordActivity;
import com.android.customview.adapter.GridRecycleAdapter;
import com.android.customview.listener.OnClickListener;
import com.android.customview.tools.LockUtils;
import com.android.customview.tools.Preferences;
import com.android.customview.tools.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by litonghui on 2016/5/16.
 */
public class DigitalPasswordFragment extends Fragment {

    private TextView mMessageTv;

    private ImageView mSetNumberIvi,mSetNumberIvii, mSetNumberIviii,mSetNumberIviv;

    private RecyclerView mNumberRecyclerView;

    private GridRecycleAdapter mDigitalAdapter;

    private List<String> mStrDigitals = new ArrayList<>();

    private StringBuilder mStrPassword = new StringBuilder();

    private Context mContext;

    private String mFirstPassword = null;

    private int mType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        if (LockUtils.PwdType.DIGITAL == LockUtils.getCurrentPwdType(getActivity())) {
            mType = VerifyPasswordActivity.TYPE_VERIFICATION;
        } else if (LockUtils.PwdType.SLIDE == LockUtils.getCurrentPwdType(getActivity())) {
            mType = VerifyPasswordActivity.TYPE_PD_SETTING;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.digital_password_fragment,null,false);
        mMessageTv = (TextView) view.findViewById(R.id.message_textview);
        mSetNumberIvi = (ImageView) view.findViewById(R.id.number_1_set_imageView);
        mSetNumberIvii = (ImageView) view.findViewById(R.id.number_2_set_imageView);
        mSetNumberIviii = (ImageView) view.findViewById(R.id.number_3_set_imageView);
        mSetNumberIviv = (ImageView) view.findViewById(R.id.number_4_set_imageView);
        mNumberRecyclerView = (RecyclerView) view.findViewById(R.id.number_recyclerview);
        init();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void init() {
        String[] mStrTemp = new String[]{
                "1", "2", "3",
                "4", "5", "6",
                "7", "8", "9",
                "null", "0", "del"};

        mStrDigitals = new ArrayList<String>();
        for (int i = 0; i < mStrTemp.length; ++i) {
            mStrDigitals.add(mStrTemp[i]);
        }
        mDigitalAdapter = new GridRecycleAdapter(getActivity(),mStrDigitals);
        GridLayoutManager ly = new GridLayoutManager(getActivity(),3);
        mNumberRecyclerView.setLayoutManager(ly);
        mNumberRecyclerView.setAdapter(mDigitalAdapter);
        mDigitalAdapter.setListener(new OnClickListener() {
            @Override
            public void onItemClick(int position) {
                if (null != mStrDigitals && mStrDigitals.size() > 0) {
                    String strDigit = mStrDigitals.get(position);
                    if (!TextUtils.isEmpty(strDigit) &&
                            null != mStrPassword) {
                        if ("del".equals(strDigit)) {
                            if (mStrPassword.length() > 0) {
                                mStrPassword.deleteCharAt(mStrPassword.length() - 1);
                                updateNumSetStatus();
                            }
                        } else {
                            if (mStrPassword.length() < 4) {
                                mStrPassword.append(strDigit);
                                updateNumSetStatus();
                            }
                            if (mStrPassword.length() == 4) {
                                String password = mStrPassword.toString();
                                mStrPassword.setLength(0);
                                onDigitalDetected(password);
                            }
                        }
                    }
                }
            }
        });
    }

    private void updateNumSetStatus() {
        int num = mStrPassword.length();
        mSetNumberIvi.setImageResource(num > 0 ? R.mipmap.number_set_gray : R.mipmap.number_normal_gray);
        mSetNumberIvii.setImageResource(num > 1 ? R.mipmap.number_set_gray : R.mipmap.number_normal_gray);
        mSetNumberIviii.setImageResource(num > 2 ? R.mipmap.number_set_gray : R.mipmap.number_normal_gray);
        mSetNumberIviv.setImageResource(num > 3 ? R.mipmap.number_set_gray : R.mipmap.number_normal_gray);
    }

    private void onDigitalDetected(String strPwd) {
        if (!TextUtils.isEmpty(strPwd)) {
            if (mType == VerifyPasswordActivity.TYPE_VERIFICATION) {
                String savepwd = Preferences.getLockPassword(getContext());
                String currentpwd = Utils.md5(strPwd);
                if (!TextUtils.isEmpty(savepwd) &&
                        !TextUtils.isEmpty(currentpwd) &&
                        savepwd.equals(currentpwd)) {
                    getActivity().finish();
                }else{
                    ShowMessage("密码错误");
                }
            } else {
                if (TextUtils.isEmpty(mFirstPassword)) {
                    mFirstPassword = strPwd;
                    ShowMessage("请再次输入密码");
                } else {
                    if (strPwd.equals(mFirstPassword)) {
                        Preferences.setLockPassword(getActivity(), Utils.md5(strPwd));
                        LockUtils.setCurrentPwdType(getActivity(), LockUtils.PwdType.DIGITAL);
                        getActivity().finish();
                    } else {
                        mFirstPassword = null;
                        ShowMessage("密码不匹配，请重新输入密码");
                    }
                }
            }
        }
    }
    private void ShowMessage(String strMsg) {
        mMessageTv.setText(strMsg);
    }
    
}
