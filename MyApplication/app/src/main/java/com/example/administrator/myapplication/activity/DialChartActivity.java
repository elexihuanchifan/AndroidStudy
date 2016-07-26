package com.example.administrator.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.widget.DialChartView;

/**
 * Created by Administrator on 2016/7/18.
 */
public class DialChartActivity extends Activity {

    private DialChartView dialChart;
    private EditText edtAngle;
    private Button btnAngle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dial_chart);

        findViewByIds();

        btnAngle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialChart.setProgress(Integer.parseInt(edtAngle.getText().toString()));
            }
        });
    }

    private void findViewByIds() {
        dialChart = (DialChartView) findViewById(R.id.dial_chart);
        edtAngle = (EditText) findViewById(R.id.edt_angle);
        btnAngle = (Button) findViewById(R.id.btn_sure_angle);
    }


}
