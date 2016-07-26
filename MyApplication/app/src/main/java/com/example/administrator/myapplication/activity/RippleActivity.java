package com.example.administrator.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.widget.ControlRippleView;

/**
 * Created by Administrator on 2016/7/14.
 */
public class RippleActivity extends Activity implements View.OnClickListener {

    private ControlRippleView rippleView;
    private Button btnStart;
    private Button btnStop;
    private Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripple);

        findViewByIds();
        initListener();
    }

    private void findViewByIds() {
        rippleView = (ControlRippleView) findViewById(R.id.control_ripple);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStop = (Button) findViewById(R.id.btn_stop);
        btnFinish = (Button) findViewById(R.id.btn_finish);
    }

    private void initListener() {
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnFinish.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                rippleView.startRipping();
                break;
            case R.id.btn_stop:
                rippleView.stopRipping();
                break;
            case R.id.btn_finish:
                rippleView.finishRipping();
                break;
            default:
                break;
        }
    }
}
