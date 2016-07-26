package com.example.administrator.myapplication.bean;

/**
 * Created by Administrator on 2016/7/6.
 */
public class ShotInfo {

    public ShotInfo() {
    }

    public ShotInfo(long timeStemp, int handType) {
        this.timeStemp = timeStemp;
        this.handType = handType;
    }

    public long timeStemp;

    public int handType;

}
