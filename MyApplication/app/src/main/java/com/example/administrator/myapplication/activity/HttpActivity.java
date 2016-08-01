package com.example.administrator.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.CookieStore;

import java.util.List;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;

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
        OkHttpUtils.post()
                .url("http://192.168.199.130/MemberController/getTennisUserInfo")
                .build()
                .connTimeOut(5 * 1000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        tvCookies.setText(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        tvCookies.setText(response);
                    }
                });
    }

    private void doUpload() {
        String phone = edtPhone.getText().toString().trim();
        String varCode = edtVarCode.getText().toString().trim();

        OkHttpUtils.post()
                .url("http://192.168.199.130/TLoginController/phoneVerify")
                .addParams("zone", "86")
                .addParams("phone", phone)
                .addParams("code", varCode)
                .addParams("oemType", "T0")
                .addParams("systemVersion", "Android")
                .addParams("appVersion", "1.0.0")
                .build()
                .connTimeOut(5 * 1000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        tvResult.setText(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        tvResult.setText(response);

                        CookieJar cookieJar = OkHttpUtils.getInstance().getOkHttpClient().cookieJar();
                        if (cookieJar instanceof CookieJarImpl) {
                            CookieStore store = ((CookieJarImpl) cookieJar).getCookieStore();
                            List<Cookie> cookies = store.getCookies();

                            StringBuffer buffer = new StringBuffer();
                            for (Cookie cookie : cookies) {
                                buffer.append(cookie.name()).append(" = ").append(cookie.value()).append("\n");
                            }

                            tvCookies.setText(buffer.toString());
                        }
                    }
                });

    }
}
