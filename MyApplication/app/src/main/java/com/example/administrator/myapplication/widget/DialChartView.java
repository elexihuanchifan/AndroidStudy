package com.example.administrator.myapplication.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.utils.DisplayUtils;

/**
 * Created by Administrator on 2016/7/18.
 * 仪表盘
 */
public class DialChartView extends View {

    private final int DO_PROGRESS = 1101;

    private Context mContext;

    private OnSeekChangeListener mListener;

    private Paint circleColor;
    private Paint circleRing;

    private float angle = 0;

    private int startAngle = 150;
    private int barWidth = 5;

    private int width;
    private int height;

    private int maxProgress = 100;
    private int progress;
    private int targetProcess;

    private int progressPercent;

    private float innerRadius;
    private float outerRadius;

    private float cx;
    private float cy;

    private float left;
    private float right;
    private float top;
    private float bottom;

    private float startPointX;
    private float startPointY;

    private int distance;

    private int[] mulitBarColors = new int[]{Color.parseColor("#ffd246"), Color.parseColor("#fa5d4c"), Color.parseColor("#f8296b"),
            Color.parseColor("#fa5d4c"), Color.parseColor("#ffd246")};

    private String[] speedRate = new String[]{"0", "20", "40", "60", "80", "100", "120", "140", "160", "180", "200"};

    private float adjustmentFactor = 100;

    private int lineWidth;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DO_PROGRESS:
                    setCurProgress();

                    int newPercent = (progress * 100) / maxProgress;
                    int newAngle = (newPercent * 240) / 100;
                    setAngle(newAngle);
                    setProgressPercent(newPercent);

                    invalidate();
                    if (progress != targetProcess) {
                        handler.sendEmptyMessageDelayed(DO_PROGRESS, 15);
                    }

                    break;
                default:
                    break;
            }
        }
    };

    private RectF rect = new RectF();
    private RectF rectBcg = new RectF();

    private Bitmap indicatorBitmap;
    private int indicatorSize;

    private Paint textPaint;
    private Paint linePaint;


    {
        mListener = new OnSeekChangeListener() {

            @Override
            public void onProgressChange(DialChartView view, int newProgress) {

            }
        };

        circleColor = new Paint();
        circleRing = new Paint();

        circleRing.setColor(Color.parseColor("#f3f3f3"));// Set default background color to Gray

        circleColor.setAntiAlias(true);
        circleRing.setAntiAlias(true);

        // 设置为空心
        circleColor.setStyle(Paint.Style.STROKE);
        circleRing.setStyle(Paint.Style.STROKE);

        textPaint = new Paint();
        textPaint.setColor(Color.parseColor("#cc8d8c90"));
        textPaint.setAntiAlias(true);
    }

    /**
     * Instantiates a new circular seek bar.
     *
     * @param context  the context
     * @param attrs    the attrs
     * @param defStyle the def style
     */
    public DialChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initDrawable();
    }

    /**
     * Instantiates a new circular seek bar.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public DialChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initDrawable();
    }

    /**
     * Instantiates a new circular seek bar.
     *
     * @param context the context
     */
    public DialChartView(Context context) {
        super(context);
        mContext = context;
        initDrawable();
    }

    /**
     * Inits the drawable.
     */
    public void initDrawable() {
        textPaint.setTextSize(DisplayUtils.dip2px(mContext, 10));

        indicatorBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.indicator_dial_chart);
        indicatorSize = indicatorBitmap.getHeight() - DisplayUtils.dip2px(mContext, 20);

        lineWidth = DisplayUtils.dip2px(mContext, 6);
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(1);
        linePaint.setColor(Color.RED);
    }


    /*
     * (non-Javadoc)
     *
     * @see android.view.View#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = width;

        int size = (width > height) ? height : width; // Choose the smaller

        cx = width / 2; // Center X for circle
        cy = height / 2; // Center Y for circle
        outerRadius = size / 2 - 10; // Radius of the outer circle

        innerRadius = outerRadius + barWidth / 2; // Radius of the inner circle

        left = cx - outerRadius; // Calculate left bound of our rect
        right = cx + outerRadius;// Calculate right bound of our rect
        top = cy - outerRadius;// Calculate top bound of our rect
        bottom = cy + outerRadius;// Calculate bottom bound of our rect

        startPointX = cx; // 12 O'clock X coordinate
        startPointY = cy - outerRadius;// 12 O'clock Y coordinate

        distance = DisplayUtils.dip2px(mContext, 30) + 10;

        rectBcg.set(left + distance, top + distance, right - distance, bottom - distance); // assign
        // size
        rect.set(left + distance, top + distance, right - distance, bottom - distance); // assign size
        setMeasuredDimension(width, height);
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        angle = 180;
        circleColor.setStrokeWidth(DisplayUtils.dip2px(getContext(), 10));
        circleRing.setStrokeWidth(DisplayUtils.dip2px(getContext(), 10));

        Shader shader = new SweepGradient(rectBcg.centerX(), rectBcg.centerY(), mulitBarColors, null);
        circleColor.setShader(shader);
        Matrix matrix = new Matrix();
        circleColor.getShader().getLocalMatrix(matrix);
        matrix.postTranslate(-rectBcg.centerX(), -rectBcg.centerY());
        matrix.postRotate(150);
        matrix.postTranslate(rectBcg.centerX(), rectBcg.centerY());
        circleColor.getShader().setLocalMatrix(matrix);

        circleRing.setStrokeCap(Paint.Cap.BUTT);
        circleColor.setStrokeCap(Paint.Cap.BUTT);
        canvas.drawArc(rectBcg, startAngle, 240, false, circleRing); // 画圆环背景
        //angle = 0 会被当做360处理，画出一个圆
        if (angle == 0) {
            angle = 0.1f;
        }
        canvas.drawArc(rect, startAngle, angle, false, circleColor);// 画扇形进度

        int textHeight = DisplayUtils.dip2px(mContext, 10);

        int radian;
        double disX, disY;
        float innerR = outerRadius - distance - DisplayUtils.dip2px(getContext(), 10) + 10;
        for (int i = 0; i < speedRate.length; i++) {

            radian = i * 24;
            if (radian < 30) {
                disX = (outerRadius - distance + 10) * Math.cos(Math.toRadians(Math.abs(30 - radian)));
                disY = (outerRadius - distance + 10) * Math.sin(Math.toRadians(Math.abs(30 - radian)));
                canvas.drawText(speedRate[i], (float) (cx - disX - textPaint.measureText(speedRate[i]) - 10), (float) (cy + disY + textHeight / 2), textPaint);

                canvas.drawLine(cx - (float) (innerR * Math.cos(Math.toRadians(Math.abs(30 - radian)))),
                        (float) (cy + innerR * Math.sin(Math.toRadians(Math.abs(30 - radian)))),
                        (float) (cx - (innerR - lineWidth) * Math.cos(Math.toRadians(Math.abs(30 - radian)))),
                        (float) (cy + (innerR - lineWidth) * Math.sin(Math.toRadians(Math.abs(30 - radian)))), linePaint);
            } else if (radian <= 120) {
                disX = (outerRadius - distance + 10) * Math.cos(Math.toRadians(Math.abs(30 - radian)));
                disY = (outerRadius - distance + 10) * Math.sin(Math.toRadians(Math.abs(30 - radian)));
                if (speedRate[i].equals("100")) {
                    disY = (outerRadius - distance + 10) * Math.sin(Math.toRadians(90));
                    canvas.drawText(speedRate[i], (float) (cx - textPaint.measureText(speedRate[i]) / 2.0f), (float) (cy - disY - textHeight / 2), textPaint);
                } else {
                    canvas.drawText(speedRate[i],
                            (float) (cx - disX - textPaint.measureText(speedRate[i]) - (speedRate[i].equals("80") ? 0 : 10)),
                            (float) (cy - disY - (speedRate[i].equals("80") ? textHeight / 2 : 0) - (speedRate[i].equals("60") ? textHeight / 3.0f : 0)),
                            textPaint);
                }

                canvas.drawLine(cx - (float) (innerR * Math.cos(Math.toRadians(Math.abs(30 - radian)))),
                        (float) (cy - innerR * Math.sin(Math.toRadians(Math.abs(30 - radian)))),
                        (float) (cx - (innerR - lineWidth) * Math.cos(Math.toRadians(Math.abs(30 - radian)))),
                        (float) (cy - (innerR - lineWidth) * Math.sin(Math.toRadians(Math.abs(30 - radian)))), linePaint);
            } else if (radian <= 210) {
                disX = (outerRadius - distance + 10) * Math.sin(Math.toRadians(radian - 120));
                disY = (outerRadius - distance + 10) * Math.cos(Math.toRadians(radian - 120));
                canvas.drawText(speedRate[i],
                        (float) (cx + disX) + (speedRate[i].equals("160") ? 10 : 0),
                        (float) (cy - disY - (speedRate[i].equals("160") ? 0 : textHeight / 2.0f)), textPaint);

                canvas.drawLine(cx + (float) (innerR * Math.sin(Math.toRadians(Math.abs(radian - 120)))),
                        (float) (cy - innerR * Math.cos(Math.toRadians(Math.abs(radian - 120)))),
                        (float) (cx + (innerR - lineWidth) * Math.sin(Math.toRadians(Math.abs(radian - 120)))),
                        (float) (cy - (innerR - lineWidth) * Math.cos(Math.toRadians(Math.abs(radian - 120)))), linePaint);
            } else {
                disX = (outerRadius - distance + 10) * Math.cos(Math.toRadians(radian - 210));
                disY = (outerRadius - distance + 10) * Math.sin(Math.toRadians(radian - 210));
                canvas.drawText(speedRate[i], (float) (cx + disX + 10), (float) (cy + disY + textHeight / 2), textPaint);

                canvas.drawLine(cx + (float) (innerR * Math.cos(Math.toRadians(Math.abs(radian - 210)))),
                        (float) (cy + innerR * Math.sin(Math.toRadians(Math.abs(radian - 210)))),
                        (float) (cx + (innerR - lineWidth) * Math.cos(Math.toRadians(Math.abs(radian - 210)))),
                        (float) (cy + (innerR - lineWidth) * Math.sin(Math.toRadians(Math.abs(radian - 210)))), linePaint);
            }
        }


//        Matrix bitmapMatrix = new Matrix();
//        Bitmap dstbmp;
//        double bitmapDegree = Math.toDegrees(Math.atan2(18, 204));
//        double bitmapDegree2 = Math.toDegrees(Math.atan2(20, 18));
        double bitmapLength01 = Math.sqrt((indicatorBitmap.getHeight() * 204 / 224) * (indicatorBitmap.getHeight() * 204 / 224) +
                indicatorBitmap.getWidth() * indicatorBitmap.getWidth() / 4);
//        double bitmapLength02 = Math.sqrt(indicatorBitmap.getWidth() * indicatorBitmap.getWidth() / 4 +
//                (indicatorBitmap.getHeight() * 20 / 224) * (indicatorBitmap.getHeight() * 20 / 224));
//        if (angle <= 30) {
//            disX = cx - bitmapLength01 * Math.cos(Math.toRadians(Math.abs(30 - angle - bitmapDegree)));
//            disY = cy - bitmapLength02 * Math.sin(Math.toRadians(Math.abs(bitmapDegree2 - angle + 30)));
//            bitmapMatrix.postRotate(-120 + angle);
//        } else if (angle <= 120) {
//            disX = cx - bitmapLength01 * Math.cos(Math.toRadians(angle - 30 - bitmapDegree));
//            if (bitmapDegree + angle - 30 < 90) {
//                disY = cy - bitmapLength01 * Math.sin(Math.toRadians(bitmapDegree + angle - 30));
//            } else {
//                disY = cy - bitmapLength01 * Math.cos(Math.toRadians(bitmapDegree + angle - 120));
//            }
//            bitmapMatrix.postRotate(-120 + angle);
//        } else if (angle <= 210) {
//            if (angle - 120 + bitmapDegree2 > 90) {
//                disX = cx - bitmapLength02 * Math.cos(Math.toRadians(angle + bitmapDegree2 - 210));
//            } else {
//                disX = cx - bitmapLength02 * Math.sin(Math.toRadians(angle - 120 + bitmapDegree2));
//            }
//            if (angle - 120 > bitmapDegree) {
//                disY = cy - bitmapLength01 * Math.cos(Math.toRadians(angle - 120 - bitmapDegree));
//            } else {
//                disY = cy - bitmapLength01 * Math.cos(Math.toRadians(bitmapDegree + 120 - angle));
//            }
//
//            bitmapMatrix.postRotate(angle - 120);
//
//        } else {
//            disX = cx - bitmapLength02 * Math.cos(Math.toRadians(bitmapDegree2 + 210 - angle));
//            disY = cy - bitmapLength02 * Math.sin(Math.toRadians(bitmapDegree2 + angle - 210));
//            bitmapMatrix.postRotate(angle - 120);
//        }
//        dstbmp = Bitmap.createBitmap(indicatorBitmap,
//                0,
//                0,
//                indicatorBitmap.getWidth(), indicatorBitmap.getHeight(), bitmapMatrix, true);
//        canvas.drawBitmap(dstbmp, (int) disX, (int) disY, null);

        canvas.save();
        canvas.rotate(angle - 120 , cx , cy);
        canvas.drawBitmap(indicatorBitmap, (int) cx - indicatorBitmap.getWidth() / 2, (int) (cy - bitmapLength01), null);
        canvas.restore();

        mListener.onProgressChange(this, getProgress());
    }


    /**
     * Get the angle.
     *
     * @return the angle
     */

    public float getAngle() {
        return angle;
    }

    /**
     * Set the angle.
     *
     * @param angle the new angle
     */
    public void setAngle(int angle) {
        this.angle = angle;
        float donePercent = (((float) this.angle) / 240) * 100;
        float progress = (donePercent / 100) * getMaxProgress();
        setProgressPercent(Math.round(donePercent));
//        setProgress(Math.round(progress));
    }

    /**
     * Sets the seek bar change listener.
     *
     * @param listener the new seek bar change listener
     */
    public void setSeekBarChangeListener(OnSeekChangeListener listener) {
        mListener = listener;
    }

    /**
     * Gets the seek bar change listener.
     *
     * @return the seek bar change listener
     */
    public OnSeekChangeListener getSeekBarChangeListener() {
        return mListener;
    }

    /**
     * Gets the bar width.
     *
     * @return the bar width
     */
    public int getBarWidth() {
        return barWidth;
    }

    /**
     * Sets the bar width.
     *
     * @param barWidth the new bar width
     */
    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
    }

    /**
     * The listener interface for receiving onSeekChange events. The class that
     * is interested in processing a onSeekChange event implements this
     * interface, and the object created with that class is registered with a
     * component using the component's
     * <code>setSeekBarChangeListener(OnSeekChangeListener)<code> method. When
     * the onSeekChange event occurs, that object's appropriate
     * method is invoked.
     */
    public interface OnSeekChangeListener {

        /**
         * On progress change.
         *
         * @param view        the view
         * @param newProgress the new progress
         */
        public void onProgressChange(DialChartView view, int newProgress);
    }

    /**
     * Gets the max progress.
     *
     * @return the max progress
     */
    public int getMaxProgress() {
        return maxProgress;
    }

    /**
     * Sets the max progress.
     *
     * @param maxProgress the new max progress
     */
    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    /**
     * Gets the progress.
     *
     * @return the progress
     */
    public int getProgress() {
        return progress;
    }

    /**
     * Sets the progress.
     *
     * @param progress the new progress
     */
    public void setProgress(int progress) {
        if (this.targetProcess != progress) {
            if (targetProcess == maxProgress && progress > maxProgress) {
                return;
            }
            this.targetProcess = progress;
            handler.sendEmptyMessage(DO_PROGRESS);
        }
    }

    /**
     * Gets the progress percent.
     *
     * @return the progress percent
     */
    public int getProgressPercent() {
        return progressPercent;
    }

    /**
     * Sets the progress percent.
     *
     * @param progressPercent the new progress percent
     */
    public void setProgressPercent(int progressPercent) {
        this.progressPercent = progressPercent;
    }

    /**
     * Sets the ring background color.
     *
     * @param color the new ring background color
     */
    public void setRingBackgroundColor(int color) {
        circleRing.setColor(color);
    }

    /**
     * Sets the progress color.
     *
     * @param color the new progress color
     */
    public void setProgressColor(int color) {
        circleColor.setColor(color);
    }


    private void setCurProgress() {
        int dis = 0;
        if (Math.abs(progress - targetProcess) > 5) {
            dis = 5;
        } else {
            dis = 1;
        }

        if (progress > targetProcess) {
            progress -= dis;
        } else {
            progress += dis;
        }
    }

}
