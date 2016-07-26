package com.example.administrator.myapplication.http;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/7/26.
 */
public class OkHttpUtils {

    private static final MediaType MEDIA_TYPE_STRING = MediaType.parse("text/plain;charset=utf-8");
    public static final String URL_SERVER = "http://192.168.199.130/";

    private static OkHttpClient okhttpClient = new OkHttpClient();

    private static Handler mDelivery;

    public static Response execute(Request request) throws IOException {
        return okhttpClient.newCall(request).execute();
    }

    public static void enqueue(Request request, Callback callback) {
        okhttpClient.newCall(request).enqueue(callback);
    }

    public static OkHttpUtils getInstance() {
        if (null == okhttpClient) {
            synchronized (OkHttpUtils.class) {
                if (okhttpClient == null) {
                    okhttpClient = new OkHttpClient();
                }
            }
        }
        return okhttpClient;
    }

    /**
     * 同步Get请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public Response get(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = okhttpClient.newCall(request).execute();
        return response;
    }

    /**
     * 同步的Get请求
     *
     * @param url
     * @return 字符串
     */
    public String getContent(String url) throws IOException {
        Response response = get(url);
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * 异步的get请求
     *
     * @param url
     * @param callback
     */
    public static void getAsyn(String url, final ResultCallback callback) {
        final Request request = new Request.Builder().url(url).build();
        deliveryResult(callback, request);
    }

    /**
     * 异步的post请求
     *
     * @param r
     */
    public static void postAsynRequest(String r, final ResultCallback callbackListener, HashMap<String, String> reqMap) {
        Request request = buildStringParams(r, reqMap);
        deliveryResult(callbackListener, request);
    }

    /**
     * 仅限字符串使用，构建参数
     *
     * @param map
     * @return
     */
    public static Request buildStringParams(String r, final HashMap<String, String> map) {
        FormBody builder = new FormBody();

        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("r", r);
        Set<String> keys = map.keySet();
        for (String key : keys) {
            if (map.get(key) != null) {
                builder.add(key, map.get(key));
            }
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder().url(URL_SERVER).post(requestBody).tag(r).build();
    }

    private static void deliveryResult(ResultCallback callback, Request request) {
        if (callback == null)
            callback = DEFAULT_RESULT_CALLBACK;
        final ResultCallback resCallBack = callback;
        // UI thread
        callback.onStart(request);
        okhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(request, e, resCallBack);
            }

            @Override
            public void onResponse(final Response response) {
                try {
                    final String string = response.body().string();
                    sendSuccessResultCallback(string, resCallBack);

                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, resCallBack);
                }

            }
        });
    }

    private static void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(request, e);
                callback.onFinish();
            }
        });
    }

    private static void sendSuccessResultCallback(final String content, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(content);
                callback.onFinish();
            }
        });
    }

    public static void shutDown(String tag) {
        okhttpClient.cancel(tag);
    }

    private static final ResultCallback DEFAULT_RESULT_CALLBACK = new ResultCallback() {

        @Override
        public void onFailure(Request request, Exception e) {

        }

        @Override
        public void onSuccess(String response) {

        }

    };
}
