package com.example.administrator.myapplication;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2016/5/10.
 */
public class UserBean {

    public UserBean() {

    }

    public UserBean(String userid, String username, String sex) {
        this.userid = userid;
        this.username = username;
        this.sex = sex;
    }


    @Expose
    public String userid;
    @Expose
    public String username;
    @Expose
    public String sex;
}
