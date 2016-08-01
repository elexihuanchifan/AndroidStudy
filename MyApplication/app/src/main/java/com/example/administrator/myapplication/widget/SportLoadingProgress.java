package com.example.administrator.myapplication.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.myapplication.utils.DisplayUtils;


/**
 * Created by Administrator on 2016/7/27.
 */
public class SportLoadingProgress extends View {

    private Paint fontProgressPaint;

    private int width;
    private int height;

    private int fontHandPercent = 50;
    private int progressHeight = DisplayUtils.dip2px(getContext(), 2);

    public SportLoadingProgress(Context context) {
        super(context);
        init();
    }

    public SportLoadingProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SportLoadingProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SportLoadingProgress(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    private void init() {

        fontProgressPaint = new Paint();
        fontProgressPaint.setColor(Color.RED);
        fontProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        fontProgressPaint.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        float progressY = height / 2 - progressHeight / 2;

        fontProgressPaint.setColor(Color.parseColor("#eaeaea"));
        canvas.drawRect(0, progressY, width, progressY + progressHeight, fontProgressPaint);

        fontProgressPaint.setColor(Color.RED);
        canvas.drawRect(0, progressY, width * fontHandPercent / 100f, progressY + progressHeight, fontProgressPaint);

    }


    public void setProgress(int progress) {
        fontHandPercent = progress;
        invalidate();
    }

}
