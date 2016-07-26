package com.example.administrator.myapplication.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2016/7/14.
 * 可控水波纹效果
 */
public class ControlRippleView extends View {

    // 初始状态
    private final int ORIGINAL = 1002;
    // 绘制状态
    private final int DRAWING = 1001;
    // 成功状态
    private final int FINISH = 2001;

    private int width;
    private int height;

    private String startColor = "#000000";
    private String finishColor = "#aaaaaa";

    private int duration = 3 * 1000;

    private Paint ripplePaint;

    private boolean needRipple = false;
    private int curStatus;

    private float centerX;
    private float centerY;

    private RectF curRecf;
    private int count = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DRAWING:
                    if (needRipple) {
                        handler.sendEmptyMessageDelayed(DRAWING, 16);
                    }
                    count++;
                    invalidate();
                    break;
                case FINISH:
                    needRipple = false;
                    handler.removeMessages(DRAWING);
                    curStatus = FINISH;
                    invalidate();
                    break;
                default:
                    break;
            }
        }
    };

    public ControlRippleView(Context context) {
        super(context);
        init();
    }

    public ControlRippleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ControlRippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ControlRippleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        if (height < 200) {
            height = 200;
        }

        setMeasuredDimension(width, height);
    }

    private void init() {
        ripplePaint = new Paint();
        ripplePaint.setColor(Color.parseColor(finishColor));
        ripplePaint.setAntiAlias(true);

        curStatus = ORIGINAL;
        count = 0;

        curRecf = new RectF(0, 0, width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        centerX = width / 2.0f;
        centerY = height / 2.0f;

        Log.i("Ripple", "count = " + count);
        switch (curStatus) {
            case ORIGINAL:
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                break;
            case DRAWING:
                canvas.drawCircle(centerX, centerY, width * count / 150f, ripplePaint);
                break;
            case FINISH:
//                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                setBackgroundColor(Color.parseColor(finishColor));
                break;
            default:
                break;
        }

    }

    public void startRipping() {
        Log.i("Ripple", "start");
        count = 0;
        needRipple = true;
        curStatus = DRAWING;
        setBackgroundColor(Color.parseColor(startColor));
        handler.sendEmptyMessage(DRAWING);
    }

    public void stopRipping() {
        Log.i("Ripple", "stopRipping");
        needRipple = false;
        handler.removeMessages(DRAWING);
        curStatus = ORIGINAL;
        invalidate();
    }

    public void finishRipping() {
        Log.i("Ripple", "finishRipping");
        needRipple = false;
        handler.removeMessages(DRAWING);
        curStatus = FINISH;
        invalidate();
    }

}
