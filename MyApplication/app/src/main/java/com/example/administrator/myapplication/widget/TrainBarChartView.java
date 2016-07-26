package com.example.administrator.myapplication.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.myapplication.utils.DisplayUtils;

/**
 * Created by Administrator on 2016/7/21.
 * 训练柱形图
 */
public class TrainBarChartView extends View {

    private final int HORIZATAL_PADDING = 10;

    private int width;
    private int height;

    private String[] belonges = new String[]{"<100", "100-110", "110-120", "120-130", "130-140", "≥140"};
    private int[] data = new int[]{20, 10, 18, 30, 8, 27};

    private Paint textPaint;
    private Paint barPaint;

    public TrainBarChartView(Context context) {
        super(context);
        init(context);
    }

    public TrainBarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TrainBarChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TrainBarChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(DisplayUtils.dip2px(context, 10));

        barPaint = new Paint();
        barPaint.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int bottomY = height - 10;
        int max_value = getMaxValue();
        float dis = (width - HORIZATAL_PADDING * 2) / (float) belonges.length;
        int padding_top = DisplayUtils.dip2px(getContext(), 10);
        int barWidth = DisplayUtils.dip2px(getContext(), 30);
        int perPadding = 5;

        if (barWidth >= dis) {
            barWidth = (int) dis - 10;
            perPadding = 5;
        } else {
            perPadding = (int) (dis - barWidth) / 2;
        }

        int barMaxHeight = bottomY - (int) textPaint.getTextSize() * 2 - padding_top - 10;

        for (int i = 0; i < data.length; i++) {
            textPaint.setColor(Color.GRAY);
            canvas.drawText(belonges[i], HORIZATAL_PADDING + dis / 2 * ((i + 1) * 2 - 1) - textPaint.measureText(belonges[i]) / 2, bottomY, textPaint);

            Shader shader = new LinearGradient(
                    HORIZATAL_PADDING + dis * i + perPadding,
                    barMaxHeight - data[i] * barMaxHeight / max_value + padding_top + textPaint.getTextSize(),
                    HORIZATAL_PADDING + dis * i + perPadding + barWidth,
                    bottomY - textPaint.getTextSize() - 10,
                    Color.parseColor("#83f09f"),
                    Color.parseColor("#25c9ad"),
                    Shader.TileMode.CLAMP);

            barPaint.setShader(shader);

            canvas.drawRect(HORIZATAL_PADDING + dis * i + perPadding,
                    barMaxHeight - data[i] * barMaxHeight / max_value + padding_top + textPaint.getTextSize(),
                    HORIZATAL_PADDING + dis * i + perPadding + barWidth,
                    bottomY - textPaint.getTextSize() - 10, barPaint);

            textPaint.setColor(Color.BLUE);
            canvas.drawText(String.valueOf(data[i]),
                    HORIZATAL_PADDING + dis * i + perPadding + barWidth / 2 - textPaint.measureText(String.valueOf(data[i])) / 2,
                    barMaxHeight - data[i] * barMaxHeight / max_value + padding_top + textPaint.getTextSize() - 5, textPaint);
        }

    }

    private int getMaxValue() {
        int max = 0;
        for (int i = 0; i < data.length; i++) {
            if (max < data[i]) {
                max = data[i];
            }
        }
        return max == 0 ? 1 : max;
    }
}
