package com.android.customview.listener;

/**
 * Created by litonghui on 2016/7/4.
 */
public interface Response<T>{
    public void onResponse(T response);
    public void onErrorResponse();
}