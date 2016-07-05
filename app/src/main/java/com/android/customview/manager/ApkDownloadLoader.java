package com.android.customview.manager;

import com.android.customview.listener.Response;

/**
 * Created by litonghui on 2016/7/4.
 */
public class ApkDownloadLoader {
    public Response mListener;
    public String mName;
    public String mUrl;
    public static ApkDownloadLoader mInstence;
    public ApkDownloadLoader(){}
    public synchronized static ApkDownloadLoader getInstence(){
        if(mInstence == null)
            mInstence  = new ApkDownloadLoader();
        return mInstence;
    }
    public void download(){

    }

}
