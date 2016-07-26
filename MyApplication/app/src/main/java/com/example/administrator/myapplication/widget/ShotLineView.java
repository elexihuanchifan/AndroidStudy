package com.example.administrator.myapplication.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.administrator.myapplication.bean.ShotInfo;
import com.example.administrator.myapplication.listener.OnShotDotClickListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/7/6.
 * 实时击球线显示
 *
 * @notice 所有long类型的timeStemp都是精确到毫秒的时间
 */
public class ShotLineView extends View {

    /**
     * 记录模式
     */
    public static final int RECORDING = 1001;
    /**
     * 历史显示模式
     */
    public static final int SHOWING = 1002;

    private final String RED = "#e60012";
    private final String BLUE = "#4296fe";
    private final String GRAY = "#7d7d7d";

    private int lineHeight = 6;
    /**
     * 整个视图宽度表示的最长时间
     */
    private int totalLineTime = 20 * 1000;
    private int dotWidth = 20;

    private int curType;

    private int width;
    private int height;

    private Paint linePaint;
    private Paint dotPaint;
    private RectF rectLine;

    private boolean isShowing = false;
    private long startTimeStemp;
    private long finishTimeStemp;
    private long curTime;
    /**
     * 当前左偏移量
     */
    private float curPosition;

    private float actionX;

    private float totalWidth;

    private List<ShotInfo> shotPoints;

    private OnShotDotClickListener listener;
    private boolean isClick = false;

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
            handler.postDelayed(this, 20);
        }
    };

    public ShotLineView(Context context) {
        super(context);
        init();
    }

    public ShotLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShotLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ShotLineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#7f7c7b80"));

        dotPaint = new Paint();
        dotPaint.setAntiAlias(true);

        listener = new OnShotDotClickListener() {
            @Override
            public void onDotClick(ShotInfo bean) {

            }
        };
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

    /**
     * 记录模式
     */
    public void setRecordingType() {
        curType = RECORDING;
    }

    public void startRecord() {
        startTimeStemp = System.currentTimeMillis();
        finishTimeStemp = 0;
        shotPoints = new ArrayList<>();
        isShowing = true;

        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 100);
        invalidate();
    }

    public long getStartTime() {
        return startTimeStemp;
    }

    public void setShotData(ShotInfo bean) {
        if (isShowing && shotPoints != null) {

            Iterator<ShotInfo> iter = shotPoints.iterator();
            while (iter.hasNext()) {
                ShotInfo info = iter.next();
                if (System.currentTimeMillis() - info.timeStemp > totalLineTime) {
                    iter.remove();
                }
            }
            shotPoints.add(bean);
        }
    }

    /**
     * 结束记录
     */
    public void finishRecording(long finishTimeStemp) {
        this.finishTimeStemp = finishTimeStemp;
    }

    /**
     * 历史显示模式
     */
    public void setShowType(List<ShotInfo> shotPoints, int windowWidth, long startTimestemp) {
        curType = SHOWING;
        this.shotPoints = shotPoints;
        this.startTimeStemp = startTimestemp;
        curPosition = 0;

        resetWidth(windowWidth);
        invalidate();
    }

    private void resetWidth(int windowWidth) {
        if (shotPoints != null && !shotPoints.isEmpty()) {
            totalWidth = (shotPoints.get(shotPoints.size() - 1).timeStemp - startTimeStemp) * windowWidth / (float) totalLineTime + windowWidth;
//            setMeasuredDimension((int) totalWidth, height);
//            measure((int)totalWidth , height);
        } else {
            totalWidth = windowWidth;
        }
    }

    public void setOnDotClickListener(OnShotDotClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 记录模式
        float lineY = height / 2 - lineHeight / 2;

        if (curType == RECORDING) {
            if (isShowing) {
                curTime = System.currentTimeMillis();

                if (finishTimeStemp == 0) {
                    if (curTime - startTimeStemp < totalLineTime) {
                        float lineX = (totalLineTime - curTime + startTimeStemp) * width / (float) totalLineTime;
                        RectF rectBg = new RectF(lineX, lineY, width, lineY + lineHeight);
                        canvas.drawRect(rectBg, linePaint);
                    } else {
                        RectF rectLine = new RectF(0, lineY, width, lineY + lineHeight);
                        canvas.drawRect(rectLine, linePaint);
                    }
                } else {
                    if (curTime - startTimeStemp < totalLineTime && finishTimeStemp < curTime) {
                        float lineStartX = (totalLineTime - curTime + startTimeStemp) * width / (float) totalLineTime;
                        float lineFinishX = width - ((curTime - finishTimeStemp) * width / (float) totalLineTime);
                        RectF rectBg = new RectF(lineStartX, lineY, lineFinishX, lineY + lineHeight);
                        canvas.drawRect(rectBg, linePaint);
                    } else {
                        float lineFinishX = width - ((curTime - finishTimeStemp) * width / (float) totalLineTime);
                        RectF rectLine = new RectF(0, lineY, lineFinishX, lineY + lineHeight);
                        canvas.drawRect(rectLine, linePaint);
                    }

                    if (curTime - finishTimeStemp >= totalLineTime) {
                        isShowing = false;
                        handler.removeCallbacks(runnable);
                    }
                }

                for (ShotInfo bean : shotPoints) {
                    if (curTime - bean.timeStemp <= totalLineTime) {
                        if (bean.handType == 0) {
                            dotPaint.setColor(Color.parseColor(RED));
                        } else if (bean.handType == 1) {
                            dotPaint.setColor(Color.parseColor(BLUE));
                        } else if (bean.handType == 2) {
                            dotPaint.setColor(Color.parseColor(GRAY));
                        }

                        float dotX = (totalLineTime - curTime + bean.timeStemp) * width / (float) totalLineTime;
                        canvas.drawCircle(dotX, height / 2, dotWidth, dotPaint);
                    }
                }
            }
            // 历史显示模式
        } else if (curType == SHOWING) {
            if (curPosition < width / 2) {
                float lineX = width / 2 - curPosition;
                RectF rectBg = new RectF(lineX, lineY, width, lineY + lineHeight);
                canvas.drawRect(rectBg, linePaint);
            } else {
                RectF rectLine = new RectF(0, lineY, width, lineY + lineHeight);
                canvas.drawRect(rectLine, linePaint);
            }

            for (ShotInfo bean : shotPoints) {
                if (bean.handType == 0) {
                    dotPaint.setColor(Color.parseColor(RED));
                } else if (bean.handType == 1) {
                    dotPaint.setColor(Color.parseColor(BLUE));
                } else if (bean.handType == 2) {
                    dotPaint.setColor(Color.parseColor(GRAY));
                }

                float dotX = (bean.timeStemp - startTimeStemp) * width / (float) totalLineTime + width / 2 - curPosition;
                canvas.drawCircle(dotX, height / 2, dotWidth, dotPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (curType == SHOWING) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    actionX = event.getX();
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (Math.abs(actionX - event.getX()) > 50) {
                        isClick = false;

                        if (actionX - event.getX() > 0 && curPosition >= totalWidth - width) {
                            curPosition = totalWidth - width;
                        } else if (actionX - event.getX() < 0 && curPosition <= 0) {
                            curPosition = 0;
                        } else {
                            curPosition += actionX - event.getX();
                            actionX = event.getX();

                            if (curPosition < 0) {
                                curPosition = 0;
                            }
                        }

//                        Log.i("curPosition", "curPosition = " + curPosition);
//                        Log.i("width", "windowWidth = " + width / 2);
                        invalidate();
                    } else {
                        isClick = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
//                    Log.i("clickTime" , "isClick = " + isClick);
                    if (isClick) {
                        float clickTime = (curPosition + actionX - width / 2) * totalLineTime / (float) width;
                        if (clickTime < 0) {
                            return true;
                        }
//                        Log.i("clickTime", "clickTime = " + clickTime);
//                        Log.i("clickTime", "clickTime2 = " + (clickTime + startTimeStemp));
                        checkClickPoint(clickTime);
                    }
                    break;

                default:
                    break;
            }
            return true;
        }

        return super.onTouchEvent(event);
    }


    private void checkClickPoint(float clickTime) {
        if (shotPoints != null && !shotPoints.isEmpty()) {
//            for (int i = 0; i < shotPoints.size(); i++) {
//                if (clickDot(i, clickTime)) {
//                    return;
//                }
//            }
            findDot(0, shotPoints.size(), clickTime);
        }
    }


    private boolean clickDot(int position, float clickTime) {
        if (shotPoints.get(position).timeStemp - startTimeStemp >= clickTime - 500 &&
                shotPoints.get(position).timeStemp - startTimeStemp <= clickTime + 500) {
            listener.onDotClick(shotPoints.get(position));
            return true;
        }
        return false;
    }

    private void findDot(int min, int max, float clickTime) {
        int middle = (max + min) / 2;

        if (middle > min) {
            if (Math.abs(shotPoints.get(middle).timeStemp - startTimeStemp - clickTime) < 500) {
                listener.onDotClick(shotPoints.get(middle));
                return;
            } else {
                if (shotPoints.get(middle).timeStemp - startTimeStemp > clickTime) {
                    findDot(min, middle, clickTime);
                } else {
                    findDot(middle, max, clickTime);
                }
            }
        } else {
            for (int i = min; i < max; i++) {
                if (Math.abs(shotPoints.get(middle).timeStemp - startTimeStemp - clickTime) < 500) {
                    listener.onDotClick(shotPoints.get(i));
                    return;
                }
            }
        }
    }
}
