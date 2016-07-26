package com.example.administrator.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.widget.IndicatorDotSeekBar;
import com.example.administrator.myapplication.widget.IndicatorSeekBar;

/**
 * Created by Administrator on 2016/6/30.
 */
public class CircleAcivity extends Activity {

    private IndicatorSeekBar imgIndicatorSeekBar;
    private IndicatorDotSeekBar circleIndicatorSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);

        imgIndicatorSeekBar = (IndicatorSeekBar) findViewById(R.id.img_indicator_seek_bar);
        circleIndicatorSeekBar = (IndicatorDotSeekBar) findViewById(R.id.circle_indicator_seek_bar);

//        imgIndicatorSeekBar.setMax(20);

//        imgIndicatorSeekBar.setOnSeekbarChangedListener(new IndicatorSeekBar.OnSeekbarChangedListener() {
//            @Override
//            public void onChangedProgress(int progress) {
//                Log.i("Seekbar", "img_id == " + progress);
//            }
//        });

        circleIndicatorSeekBar.setMax(100);
        circleIndicatorSeekBar.setOnSeekbarChangedListener(new IndicatorDotSeekBar.OnSeekbarChangedListener() {
            @Override
            public void onChangedProgress(int progress) {
                Log.i("seekBar", "dot_id = " + progress);
            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
//            imgIndicatorSeekBar.setProgress(1000);
            circleIndicatorSeekBar.setProgress(50);
        }
    }
}
