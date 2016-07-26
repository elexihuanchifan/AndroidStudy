package com.example.administrator.myapplication.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.myapplication.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/19.
 * 散点图
 */
public class ScatterChartView extends View {

    private final int TEXT_SIZE = 10;
    private final int DOT_WIDTH = 5;

    private final int LEFT_LEVEL = 10;
    private final int LEFT_MAX = 200;

    private final int LEFT_PADDING = 10;
    private final int RIGHT_PADDING = 20;
    private final int BOTTOM_PADDING = 20;

    private int width;
    private int height;

    private Paint linePaint;
    private Paint textPaint;
    private Paint limitLinePaint;
    private Paint dotPaint;

    private Context mContext;
    private List<Integer> leftYData;
    private float leftTextHeight;
    private float rightLimitWidht;

    private List<Integer> speedList;

    private int limitData = 70;

    public ScatterChartView(Context context) {
        super(context);
        init(context);
    }

    public ScatterChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScatterChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScatterChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    private void init(Context context) {
        this.mContext = context;

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(1);
        linePaint.setColor(Color.GRAY);

        limitLinePaint = new Paint();
        limitLinePaint.setStyle(Paint.Style.STROKE);
        limitLinePaint.setColor(Color.RED);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(DisplayUtils.dip2px(mContext, TEXT_SIZE));

        dotPaint = new Paint();
        dotPaint.setAntiAlias(true);

        leftYData = new ArrayList<>();
        int leftDis = LEFT_MAX / LEFT_LEVEL;
        for (int i = 0; i <= LEFT_LEVEL; i++) {
            leftYData.add(i * leftDis);
        }

        speedList = new ArrayList<>();

        rightLimitWidht = Math.max(textPaint.measureText("目标球速"), textPaint.measureText("200km/h"));

        leftTextHeight = DisplayUtils.dip2px(mContext, TEXT_SIZE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float perHeight = (height - BOTTOM_PADDING) / (float) leftYData.size();
        for (int i = leftYData.size() - 1; i >= 0; i--) {
            canvas.drawText(String.valueOf(leftYData.get(i)),
                    textPaint.measureText(String.valueOf(LEFT_MAX)) + LEFT_PADDING - textPaint.measureText(String.valueOf(leftYData.get(i))),
                    leftTextHeight + perHeight * (leftYData.size() - 1 - i), textPaint);
        }

        float leftX = textPaint.measureText(String.valueOf(LEFT_MAX)) + LEFT_PADDING + 5;
        float leftTopY = leftTextHeight / 2;
        float leftBottomY = height - BOTTOM_PADDING - perHeight + leftTextHeight / 2;
        float rightBottomX = width - RIGHT_PADDING - rightLimitWidht;

        canvas.drawLine(leftX, leftTopY, leftX, leftBottomY, linePaint);
        canvas.drawLine(leftX, leftBottomY, rightBottomX, leftBottomY, linePaint);

        float limitY = leftBottomY - (leftBottomY - leftTopY) * limitData / LEFT_MAX;
        Path path = new Path();
        path.moveTo(leftX, limitY);
        path.lineTo(rightBottomX, limitY);
        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        limitLinePaint.setPathEffect(effects);
        canvas.drawPath(path, limitLinePaint);

        String content = String.format("%skm/h", String.valueOf(limitData));
        canvas.drawText("目标球速", rightBottomX + 4, limitY - 4, textPaint);
        canvas.drawText(content, rightBottomX + 4, limitY + leftTextHeight, textPaint);

        if (speedList.size() > 0) {
            float perDotX = (rightBottomX - leftX - DOT_WIDTH) / (speedList.size() + 1);
            for (int i = 0; i < speedList.size(); i++) {
                float dotY = leftBottomY - (leftBottomY - leftTopY) * speedList.get(i) / LEFT_MAX;
                if (speedList.get(i) < limitData) {
                    dotPaint.setColor(Color.GRAY);
                } else {
                    dotPaint.setColor(Color.RED);
                }
                canvas.drawCircle(perDotX * (i + 1), dotY, DOT_WIDTH, dotPaint);
            }
        }
    }


    public void setLimitData(int limitData) {
        this.limitData = limitData;
        invalidate();
    }

    public void setSpeedList(List<Integer> list) {
        this.speedList = list;
        invalidate();
    }

    public void addSpeedData(int speed) {
        if (null == speedList) {
            speedList = new ArrayList<>();
        }

        speedList.add(speed);
    }

}
