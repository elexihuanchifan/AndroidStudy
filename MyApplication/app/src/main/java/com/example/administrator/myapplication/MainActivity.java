package com.example.administrator.myapplication;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import com.example.administrator.myapplication.activity.CalendarActivity;
import com.example.administrator.myapplication.activity.CircleAcivity;
import com.example.administrator.myapplication.activity.DialChartActivity;
import com.example.administrator.myapplication.activity.DrawActivity;
import com.example.administrator.myapplication.activity.RippleActivity;
import com.example.administrator.myapplication.activity.ScatterActivity;
import com.example.administrator.myapplication.activity.ShowLineActivity;
import com.example.administrator.myapplication.activity.TimeActivity;
import com.example.administrator.myapplication.activity.UploadByDateActivity;

public class MainActivity extends Activity {

    private Button btnCircle;
    private Button btnTime;
    private Button btnUpdate;
    private Button btnCalendar;
    private Button btnRipple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCircle = (Button) findViewById(R.id.btn_cirlce);
        btnTime = (Button) findViewById(R.id.btn_time);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnCalendar = (Button) findViewById(R.id.btn_calender_show);
        btnRipple = (Button) findViewById(R.id.btn_ripple);

        btnCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CircleAcivity.class));
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TimeActivity.class));
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UploadByDateActivity.class));
            }
        });

        findViewById(R.id.btn_showLine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShowLineActivity.class));
            }
        });

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CalendarActivity.class));
            }
        });

        btnRipple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RippleActivity.class));
            }
        });

        findViewById(R.id.btn_dial_chart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DialChartActivity.class));
            }
        });

        findViewById(R.id.btn_scatter_chart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ScatterActivity.class));
            }
        });

        findViewById(R.id.btn_value_anim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getValueAnim();
//                startActivity(new Intent(MainActivity.this , HttpActivity.class));
//                startActivity(new Intent(MainActivity.this , CardActivity.class));
//                startActivity(new Intent(MainActivity.this, ExpandleActivity.class));
                startActivity(new Intent(MainActivity.this, DrawActivity.class));
            }

        });

    }

    private void getValueAnim() {
        final ValueAnimator anim = ValueAnimator.ofInt(0, 100);
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.i("ValueAnimator", " === " + animation.getAnimatedValue());
            }
        });
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();


    }


}
