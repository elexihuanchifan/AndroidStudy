package com.example.administrator.myapplication.http;


import okhttp3.Request;

/**
 * Created by Administrator on 2016/7/26.
 */
public abstract class ResultCallback {

    public void onStart(Request request) {
    }

    public void onFinish() {
    }

    public abstract void onFailure(Request request, Exception e);

    public abstract void onSuccess(String response);
}
