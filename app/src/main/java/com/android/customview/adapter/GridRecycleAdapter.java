package com.android.customview.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.customview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by litonghui on 2016/5/17.
 */
public class GridRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;

    private LayoutInflater mLayoutInflater;

    private List<String> mStrArray = new ArrayList<>();

    public GridRecycleAdapter(Context context,List<String> strArray){
        mContext = context;
        mStrArray = strArray;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GridViewHolder(mLayoutInflater.inflate(R.layout.digital_item_gray,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder != null &&  holder instanceof  GridViewHolder
                && mStrArray !=null && mStrArray.size()>0) {
            String strDigit = mStrArray.get(position);
            if (!TextUtils.isEmpty(strDigit)) {
                if ("null".equals(strDigit)) {
                    holder.itemView.setVisibility(View.INVISIBLE);
                } else if ("del".equals(strDigit)) {
                    ((GridViewHolder) holder).tvNumber.setVisibility(View.INVISIBLE);
                    ((GridViewHolder) holder).ivDelete.setVisibility(View.VISIBLE);
                } else {
                    ((GridViewHolder) holder).tvNumber.setText(strDigit);
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        return mStrArray!= null ? mStrArray.size():0;
    }

    public static class GridViewHolder  extends RecyclerView.ViewHolder{

        public TextView tvNumber;
        public ImageView ivDelete;

        public GridViewHolder(View itemView) {
            super(itemView);
            tvNumber = (TextView) itemView.findViewById(R.id.number_textView);
            ivDelete = (ImageView) itemView.findViewById(R.id.delete_imageView);
            Typeface tf = Typeface.create("sans-serif-light", 0);
            tvNumber.setTypeface(tf);
        }
    }

}
