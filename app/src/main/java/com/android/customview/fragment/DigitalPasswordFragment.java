package com.android.customview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.customview.R;
import com.android.customview.adapter.GridRecycleAdapter;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.digital_password_fragment,null,false);
        mMessageTv = (TextView) view.findViewById(R.id.message_textview);
        mSetNumberIvi = (ImageView) view.findViewById(R.id.number_1_set_imageView);
        mSetNumberIvii = (ImageView) view.findViewById(R.id.number_1_set_imageView);
        mSetNumberIviii = (ImageView) view.findViewById(R.id.number_1_set_imageView);
        mSetNumberIviv = (ImageView) view.findViewById(R.id.number_1_set_imageView);
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
    }
    
}
