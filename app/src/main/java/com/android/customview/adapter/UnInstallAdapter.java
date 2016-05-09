package com.android.customview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.customview.R;
import com.android.customview.model.LocalPackageInfo;
import com.android.customview.tools.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by litonghui on 2016/5/9.
 */
public class UnInstallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<LocalPackageInfo> mLoaclInfos = new ArrayList<>();

    public UnInstallAdapter(Context context){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<LocalPackageInfo> data){
        if(null != data && null != mLoaclInfos){
            mLoaclInfos.clear();
            mLoaclInfos = data;
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppInfoHolder(mLayoutInflater.inflate(R.layout.item_uninstall,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(null != mLoaclInfos && mLoaclInfos.size() >0) {
            final LocalPackageInfo info = mLoaclInfos.get(position);
            if(null != info){
                AppInfoHolder appInfoHolder = (AppInfoHolder)holder;
                appInfoHolder.nameTv.setText(info.name);
                appInfoHolder.iconIv.setImageDrawable(info.getIcon(mContext));
                appInfoHolder.statueBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.unInstallApk(mContext,info.packagename);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mLoaclInfos != null ? mLoaclInfos.size() : 0;
    }
    public static class AppInfoHolder extends RecyclerView.ViewHolder{
        public ImageView iconIv;
        public TextView nameTv;
        public TextView channelTv;
        public Button statueBtn;
        public AppInfoHolder(View itemView) {
            super(itemView);
            iconIv = (ImageView) itemView.findViewById(R.id.ic_app);
            nameTv = (TextView) itemView.findViewById(R.id.title);
            statueBtn = (Button) itemView.findViewById(R.id.app_status_button);
            channelTv = (TextView)itemView.findViewById(R.id.channel);
        }
    }
}
