package com.example.administrator.myapplication.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.utils.DisplayUtils;


public class IndicatorSeekBar extends View {

    private static final String TAG = "MultiColorSeekBar";

    private static final String PROGRESS_COLOR = "#4296fe";
    private static final String PROGRESS_BG_COLOR = "#dededf";
    // dp
    private static final int TEXT_SIZE = 12;
    private static final int PROGRESS_WIDTH = 5;

    private int width; // 控件的宽度
    private int height; // 控件的高度
    private int seekbarHeight;

    private int baseY = 20;

    private int max = 2000;
    private int progress = 1000;
    private float move2X = 0;

    private Paint progressPaint;
    private Paint textPaint;

    private Bitmap progressMark; // seekbar的thumb图片
    private Bitmap numBg;

    private OnSeekbarChangedListener listener;

    public interface OnSeekbarChangedListener {
        public void onChangedProgress(int progress);
    }

    public void setOnSeekbarChangedListener(OnSeekbarChangedListener listener) {
        this.listener = listener;
    }

    public IndicatorSeekBar(Context context) {
        super(context);
        init();
    }

    public IndicatorSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IndicatorSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        seekbarHeight = DisplayUtils.dip2px(getContext(), PROGRESS_WIDTH);
        progressMark = BitmapFactory.decodeResource(getResources(), R.drawable.ic_target_sb_thumb);
        numBg = BitmapFactory.decodeResource(getResources(), R.drawable.real_qipao);

        progressPaint = new Paint();
        progressPaint.setStyle(Style.FILL);
        progressPaint.setAntiAlias(true);
        progressPaint.setStrokeWidth(seekbarHeight);
        progressPaint.setStrokeCap(Cap.ROUND); // 设置画笔为圆角

        textPaint = new Paint();
        textPaint.setTextSize(DisplayUtils.dip2px(getContext(), TEXT_SIZE));
        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        if (height < progressMark.getHeight() + numBg.getHeight() + baseY) {
            height = progressMark.getHeight() + numBg.getHeight() + baseY;
        }
        setMeasuredDimension(width, height);
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        move2X = (float) (progress * (width - numBg.getWidth() * 2) * 1.0 / max) + numBg.getWidth();
        invalidate();
    }

    public int getProgress(){
        return progress;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float progressY = baseY + (float) (progressMark.getHeight() / 2.0) + numBg.getHeight();
        float prgStartX = numBg.getWidth();
        float progressX = move2X;

        if (progressX < prgStartX) {
            progressX = prgStartX;
        }

        if (progressX > width - numBg.getWidth()) {
            progressX = width - numBg.getWidth();
        }

        progress = (int) ((progressX - prgStartX) * max / (width - numBg.getWidth() - prgStartX));
        if (listener != null) {
            listener.onChangedProgress(progress);
        }
        String value = String.valueOf(progress);

        progressPaint.setColor(Color.parseColor(PROGRESS_BG_COLOR));
        canvas.drawLine(prgStartX, progressY, width - numBg.getWidth(), progressY, progressPaint);
        progressPaint.setColor(Color.parseColor(PROGRESS_COLOR));
        canvas.drawLine(prgStartX, progressY, progressX, progressY, progressPaint);

        canvas.drawBitmap(progressMark, progressX - (float) (progressMark.getWidth() / 2.0), baseY + numBg.getHeight(), null);

        canvas.drawBitmap(numBg, progressX - (float) (numBg.getWidth() / 2.0), baseY, null);
        canvas.drawText(value, progressX - (textPaint.measureText(value) / 2), baseY + (float) (numBg.getHeight() * 2 / 3.0), textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        move2X = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 父控件不可以去拦截事件
                getParent().requestDisallowInterceptTouchEvent(true);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                break;
        }
        return true;
    }
}