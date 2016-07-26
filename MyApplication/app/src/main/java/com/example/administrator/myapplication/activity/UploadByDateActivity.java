package com.example.administrator.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/4.
 * 按天上传数据
 */
public class UploadByDateActivity extends Activity {

    private static final String TAG = "upload";

    private TextView tvContent;

    private long[] timeStemp = new long[]{1467331200, 1467244800, 1467158400, 1467092000, 1466985600, 1466467200};
    private List<Long> timeList;
    private List<String> updateFailDateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        tvContent = (TextView) findViewById(R.id.time);

        timeList = new ArrayList<>();
        for (int i = 0; i < timeStemp.length; i++) {
            timeList.add(timeStemp[i]);
        }

        updateFailDateList = new ArrayList<>();
        updateNoSignToServer();

    }


    private void updateNoSignToServer() {
        updateFailDateList.clear();
        doUpload(timeStemp[0]);
    }

    private void doUpload(long l) {
        Log.i(TAG , "doUpload=== " + l);

        if (l % 600 != 0) {
            onError(l);
        } else {
            onSuccess(l);
        }
    }

    private void onSuccess(long l) {

        Log.i(TAG , "onSuccess=== " + l);
        timeList.remove(l);
        onUpdateByDateFinish(l);
    }

    private void onError(long l) {
        Log.i(TAG , "onError=== " + l);
        if (!updateFailDateList.contains(String.valueOf(l))) {
            updateFailDateList.add(String.valueOf(l));
        }
        onUpdateByDateFinish(l);
    }


    private void onUpdateByDateFinish(long l) {
        boolean notBreak = false;
        if (!timeList.isEmpty()) {
            for (int i = 0; i < timeList.size(); i++) {
                if (!updateFailDateList.contains(String.valueOf(timeList.get(i)))){
                    doUpload(timeList.get(i));
                    notBreak = true;
                    break;
                }
            }
            if(!notBreak){
                Log.i(TAG , "BREAK");
            }
        } else {
            Log.i(TAG , "FINISH===");
        }
    }


}
