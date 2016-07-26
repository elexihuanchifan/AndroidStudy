package com.example.administrator.myapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.bean.ShotInfo;
import com.example.administrator.myapplication.listener.OnShotDotClickListener;
import com.example.administrator.myapplication.widget.ShotLineView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/6.
 */
public class ShowLineActivity extends Activity implements View.OnClickListener {

    private ShotLineView shotline;
    private ShotLineView showLine;

    private List<ShotInfo> points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_line);

        shotline = (ShotLineView) findViewById(R.id.shotline);
        showLine = (ShotLineView) findViewById(R.id.show_line);
        findViewById(R.id.btn_start_line).setOnClickListener(this);
        findViewById(R.id.btn_font_hand).setOnClickListener(this);
        findViewById(R.id.btn_back_hand).setOnClickListener(this);
        findViewById(R.id.btn_finish_hand).setOnClickListener(this);

        shotline.setRecordingType();

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        setShotPoints();
        showLine.setShowType(points , width,  1467265130 * 1000L);
        showLine.setOnDotClickListener(new OnShotDotClickListener() {
            @Override
            public void onDotClick(ShotInfo bean) {
                Toast.makeText(ShowLineActivity.this, String.valueOf(bean.timeStemp) ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setShotPoints() {
        points = new ArrayList<>();

        ShotInfo info1 = new ShotInfo(1467265134000L,1);
        ShotInfo info2 = new ShotInfo(1467265136000L,2);
        ShotInfo info3 = new ShotInfo(1467265138000L,2);
        ShotInfo info4 = new ShotInfo(1467265141000L,1);
        ShotInfo info5 = new ShotInfo(1467265143000L,1);
        ShotInfo info6 = new ShotInfo(1467265146000L,1);
//        ShotInfo info7 = new ShotInfo(1467265271000L,2);
//        ShotInfo info8 = new ShotInfo(1467265274000L,1);
//        ShotInfo info9 = new ShotInfo(1467265755000L,3);

        points.add(info1);
        points.add(info2);
        points.add(info3);
        points.add(info4);
        points.add(info5);
        points.add(info6);
//        points.add(info7);
//        points.add(info8);
//        points.add(info9);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_line:
                shotline.startRecord();
                break;
            case R.id.btn_font_hand:
                ShotInfo bean = new ShotInfo(System.currentTimeMillis(), 0);
                shotline.setShotData(bean);
                break;
            case R.id.btn_back_hand:
                ShotInfo backBean = new ShotInfo(System.currentTimeMillis(), 1);
                shotline.setShotData(backBean);
                break;
            case R.id.btn_finish_hand:
                shotline.finishRecording(System.currentTimeMillis());
                break;
        }
    }
}
