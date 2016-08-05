package com.example.administrator.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.utils.PreferenceCookieManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/7/26.
 */
public class HttpActivity extends Activity implements View.OnClickListener {

    private EditText edtPhone;
    private EditText edtVarCode;
    private Button btnUpload;
    private Button btnUserInfo;
    private TextView tvResult;
    private TextView tvCookies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);

        edtPhone = (EditText) findViewById(R.id.edt_phone);
        edtVarCode = (EditText) findViewById(R.id.edt_var_code);
        btnUpload = (Button) findViewById(R.id.btn_upload);
        btnUserInfo = (Button) findViewById(R.id.btn_user_info);
        tvResult = (TextView) findViewById(R.id.tv_back_content);
        tvCookies = (TextView) findViewById(R.id.tv_back_cookie);

        btnUpload.setOnClickListener(this);
        btnUserInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_upload:
                doUpload();
                break;

            case R.id.btn_user_info:
                getUserInfo();
                break;
        }
    }

    private void getUserInfo() {
//        OkHttpUtils.getInstance().getOkHttpClient().
//        OkHttpUtils.post()
//                .url("http://192.168.199.130/MemberController/getTennisUserInfo")
//                .build()
//                .connTimeOut(5 * 1000)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        tvCookies.setText(e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        tvCookies.setText(response);
//                    }
//                });

        new Thread(new Runnable() {
            @Override
            public void run() {
                CookieJar cookieJar = new CookieJar() {
                    private List<Cookie> cookies = new ArrayList<Cookie>();


                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        this.cookies.clear();
                        this.cookies.addAll(cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        return cookies;
                    }
                };
                List<Cookie> cookieList = new PreferenceCookieManager(HttpActivity.this).getCookies();
                Request.Builder builder = new Request.Builder().url("http://192.168.199.130/MemberController/getTennisUserInfo");
                for (Cookie cookie : cookieList) {
                    builder.addHeader("cookie", cookie.toString());
                }
                Request request = builder.build();
                try {
                    cookieJar.saveFromResponse(request.url(), cookieList);
                    OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();
                    Response response = okHttpClient.newCall(request).execute();
                    Log.i("userinfo", response.body().string());

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

//    private void doUpload() {
//        String phone = edtPhone.getText().toString().trim();
//        String varCode = edtVarCode.getText().toString().trim();
//
//        OkHttpUtils.post()
//                .url("http://192.168.199.130/TLoginController/phoneVerify")
//                .addParams("zone", "86")
//                .addParams("phone", phone)
//                .addParams("code", varCode)
//                .addParams("oemType", "T0")
//                .addParams("systemVersion", "Android")
//                .addParams("appVersion", "1.0.0")
//                .build()
//                .connTimeOut(5 * 1000)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        tvResult.setText(e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        tvResult.setText(response);
//
//                        CookieJar cookieJar = OkHttpUtils.getInstance().getOkHttpClient().cookieJar();
//                        if (cookieJar instanceof CookieJarImpl) {
//                            CookieStore store = ((CookieJarImpl) cookieJar).getCookieStore();
//                            List<Cookie> cookies = store.getCookies();
//
//                            StringBuffer buffer = new StringBuffer();
//                            for (Cookie cookie : cookies) {
//                                buffer.append(cookie.name()).append(" = ").append(cookie.value()).append("\n");
//                            }
//
//                            tvCookies.setText(buffer.toString());
//                        }
//                    }
//                });
//
//    }

    private void doUpload() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient okHttpClient = new OkHttpClient();

                RequestBody formBody = new FormBody.Builder()
                        .add("zone", "86")
                        .add("phone", "13800138000")
                        .add("code", "0000")
                        .add("oemType", "T0")
                        .add("systemVersion", "Android")
                        .add("appVersion", "1.0.0").build();
                Request request = new Request.Builder().url("http://192.168.199.130/TLoginController/phoneVerify").post(formBody).build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Log.i("message", response.message());
//                        tvResult.setText(response.message());
                        Headers headers = response.headers();
                        Map<String, List<String>> data = headers.toMultimap();
                        List<String> cookies = data.get("set-cookie");
                        if (cookies != null) {
                            for (int i = 0; i < cookies.size(); i++) {
                                Log.i("cookies", cookies.get(i));
                            }
                        }

//                        tvCookies.setText(headers.get("cookies"));
//                        Log.i("cookies" ,headers.get("Set-Cookie"));

                        Log.i("body", response.body().string());

//                        List<Cookie> cookieList = okHttpClient.cookieJar().loadForRequest(request.url());
                        List<Cookie> cookieList = Cookie.parseAll(request.url(), headers);
                        if (cookieList != null) {
                            PreferenceCookieManager cookieManager = new PreferenceCookieManager(HttpActivity.this);
                            for (Cookie cookie : cookieList) {
                                cookieManager.addCookie(cookie);
                                Log.i("cookieList", cookie.name() + "  == " + cookie.value());
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
