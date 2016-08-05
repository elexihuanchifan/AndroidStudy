package com.example.administrator.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/8/4.
 */
public class RetrofitActivity extends Activity {

    private static final String URL = "http://192.168.199.130/";

    private TextView tvLogin;
    private TextView tvUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_test);

        findViewByIds();

    }

    private void findViewByIds() {
        tvLogin = (TextView) findViewById(R.id.tv_login_info);
        tvUserInfo = (TextView) findViewById(R.id.tv_user_info);
    }

    public void onButtonClick(View v) {
        switch (v.getId()) {
            // 登录
            case R.id.btn_login:
                doLogin();
                break;
            // 获取用户资料
            case R.id.btn_user_info:
                break;
        }
    }

    private void doLogin() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
}
