package com.example.administrator.myapplication.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/7/25.
 */
public class StrokeView extends View {


    public StrokeView(Context context) {
        super(context);
    }

    public StrokeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StrokeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);

        canvas.drawRect(20, 20, 100, 100, paint);

        Paint linePain = new Paint();
        linePain.setColor(Color.BLUE);
        canvas.drawLine(0, 15, 100, 15, linePain);
    }
}
