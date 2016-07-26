package com.example.administrator.myapplication.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;


/**
 *
 * 作者：陈冬冬
 *
 * 说明：主界面的进度条,带渐变的 绿：#51ff7e 蓝：#5dffea
 *
 * 时间：2015-11-12 上午10:03:34
 *
 */
public class CircleView extends View {
    private static final String TAG = "CircularSeekBar";

    /** The context */
    private Context mContext;

    /** The listener to listen for changes */
    private OnSeekChangeListener mListener;

    /** The color of the progress ring */
    private Paint circleColor;

    /** the color of the inside circle. Acts as background color */
    // private Paint innerColor;

    /** The progress circle ring background */
    private Paint circleRing;

    /** The angle of progress */
    private float angle = 0;

    /** The start angle (12 O'clock */
    private int startAngle = 140;

    /** The width of the progress ring */
    private int barWidth = 5;

    /** The width of the view */
    private int width;

    /** The height of the view */
    private int height;

    /** The maximum progress amount */
    private int maxProgress = 100;

    /** The current progress */
    private int progress;

    /** The progress percent */
    private int progressPercent;

    /** The radius of the inner circle */
    private float innerRadius;

    /** The radius of the outer circle */
    private float outerRadius;

    /** The circle's center X coordinate */
    private float cx;

    /** The circle's center Y coordinate */
    private float cy;

    /** The left bound for the circle RectF */
    private float left;

    /** The right bound for the circle RectF */
    private float right;

    /** The top bound for the circle RectF */
    private float top;

    /** The bottom bound for the circle RectF */
    private float bottom;

    /** The X coordinate for the top left corner of the marking drawable */
    private float dx;

    /** The Y coordinate for the top left corner of the marking drawable */
    private float dy;

    /** The X coordinate for 12 O'Clock */
    private float startPointX;

    /** The Y coordinate for 12 O'Clock */
    private float startPointY;

    /**
     * The X coordinate for the current position of the marker, pre adjustment
     * to center
     */
    private float markPointX;

    /**
     * The Y coordinate for the current position of the marker, pre adjustment
     * to center
     */
    private float markPointY;

    /**
     * The adjustment factor. This adds an adjustment of the specified size to
     * both sides of the progress bar, allowing touch events to be processed
     * more user friendly (yes, I know that's not a word)
     */
    private float adjustmentFactor = 100;

    /** The progress mark when the view isn't being progress modified */
//	private Bitmap[] progressMark;

    /** The progress mark when the view is being progress modified. */
//	private Bitmap progressMarkPressed;

    /** The flag to see if view is pressed */
    private boolean IS_PRESSED = false;

    private int curType = 0;

    private int index = 0; // 进度动画播放图片的下标
    private double x = 0;
    private double y = 0;
    private Handler handler = new Handler();

    /** The rectangle containing our circles and arcs. */
    private RectF rect = new RectF();
    private RectF rectBcg = new RectF();

    {
        mListener = new OnSeekChangeListener() {

            @Override
            public void onProgressChange(CircleView view, int newProgress) {

            }
        };

        circleColor = new Paint();
        // innerColor = new Paint();
        circleRing = new Paint();

        circleColor.setColor(Color.parseColor("#51ff7e")); // Set default
//		circleColor.setColor(getStartColor());
        circleRing.setColor(Color.parseColor("#22ffffff"));// Set default background color to Gray

        circleColor.setAntiAlias(true);
        // innerColor.setAntiAlias(true);
        circleRing.setAntiAlias(true);

        circleColor.setStrokeWidth(15);
        // innerColor.setStrokeWidth(5);
        circleRing.setStrokeWidth(15);
        // 设置为空心
        circleColor.setStyle(Paint.Style.STROKE);
        circleRing.setStyle(Paint.Style.STROKE);
    }

    /**
     * Instantiates a new circular seek bar.
     *
     * @param context
     *            the context
     * @param attrs
     *            the attrs
     * @param defStyle
     *            the def style
     */
    public CircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initDrawable();
    }

    /**
     * Instantiates a new circular seek bar.
     *
     * @param context
     *            the context
     * @param attrs
     *            the attrs
     */
    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initDrawable();
    }

    /**
     * Instantiates a new circular seek bar.
     *
     * @param context
     *            the context
     */
    public CircleView(Context context) {
        super(context);
        mContext = context;
        initDrawable();
    }

    /**
     * Inits the drawable.
     */
    public void initDrawable() {
//		progressMark = new Bitmap[] { BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_seekbar_holo_1),
//				BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_seekbar_holo_2), BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_seekbar_holo_3),
//				BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_seekbar_holo_4), BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_seekbar_holo_5),
//				BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_seekbar_holo_6), BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_seekbar_holo_5),
//				BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_seekbar_holo_4), BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_seekbar_holo_3),
//				BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_seekbar_holo_2), };
//		progressMarkPressed = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_seekbar_holo_1);
    }

    public void setRingBlue(){
        curType = 1;
        circleColor.setColor(getCurColor());
    }

    public void setRingRed(){
        curType = 2;
        circleColor.setColor(getCurColor());
    }

    public void setRingYello(){
        curType = 3;
        circleColor.setColor(getCurColor());
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
        markPointX = startPointX;// Initial locatino of the marker X coordinate
        markPointY = startPointY;// Initial locatino of the marker Y coordinate

        rectBcg.set(left + 10, top + 10, right - 10, bottom - 10); // assign
        // size
        rect.set(left + 10, top + 10, right - 10, bottom - 10); // assign size
        setMeasuredDimension(width, height);
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Shader shader = new SweepGradient(rectBcg.centerX(), rectBcg.centerY(), getStartColor(), getCurColor());
        circleColor.setShader(shader);
        Matrix matrix = new Matrix();
        circleColor.getShader().getLocalMatrix(matrix);
        matrix.postTranslate(-rectBcg.centerX(), -rectBcg.centerY());
        matrix.postRotate(90);
        matrix.postTranslate(rectBcg.centerX(), rectBcg.centerY());
        circleColor.getShader().setLocalMatrix(matrix);

        circleRing.setStrokeCap(Cap.BUTT);
        circleColor.setStrokeCap(Cap.BUTT);
        canvas.drawArc(rectBcg, 0, 270, false, circleRing); // 画圆环背景
        //angle = 0 会被当做360处理，画出一个圆
        if(angle==0){
            angle = 0.1f;
        }
        canvas.drawArc(rect, startAngle, angle, false, circleColor);// 画扇形进度
//		LogUtils.e(TAG, "angle="+angle+"  startAngle="+startAngle);
//		int a = (int) ((startAngle + angle) % 360);
//		double r = outerRadius - 10;
//		int padding = 0;
//		if (a >= 0 && a <= 90) {
//			x = cx + r * Math.cos(Math.toRadians(a)) - padding;
//			y = cy + r * Math.sin(Math.toRadians(a)) - padding;
//
//		} else if (a > 90 && a <= 180) {
//			x = cx - r * Math.sin(Math.toRadians(a - 90)) + padding;
//			y = cy + r * Math.cos(Math.toRadians(a - 90)) - padding;
//		} else if (a > 180 && a <= 270) {
//			x = cx - r * Math.sin(Math.toRadians(270 - a)) + padding;
//			y = cy - r * Math.cos(Math.toRadians(270 - a)) + padding;
//		} else if (a > 270 && a <= 360) {
//			x = cx + r * Math.cos(Math.toRadians(360 - a)) - padding;
//			y = cy - r * Math.sin(Math.toRadians(360 - a)) + padding;
//		}
//		dx = (float) x;
//		dy = (float) y;

//		canvas.drawBitmap(progressMark[index], dx - progressMark[index].getWidth() / 2, dy - progressMark[index].getWidth() / 2, null);
//		index++;
//		if (index >= progressMark.length) {
//			index = 0;
//		}
//		handler.postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//				invalidate();
//			}
//		}, 150);

    }

    private int getCurColor(){
        int color = Color.parseColor("#51ff7e");
        switch (curType){
            case 1:
                color = Color.parseColor("#f06fa5");
                break;
            case 2:
                color = Color.parseColor("#7abeeb");
                break;
            case 3:
                color = Color.parseColor("#f8c353");
                break;
            default:
                break;
        }

        return color;
    }

    private int getStartColor(){
        int color = Color.parseColor("#51ff7e");
        switch (curType){
            case 1:
                color = Color.parseColor("#44f06fa5");
                break;
            case 2:
                color = Color.parseColor("#447abeeb");
                break;
            case 3:
                color = Color.parseColor("#44f8c353");
                break;
            default:
                break;
        }

        return color;
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
     * @param angle
     *            the new angle
     */
    public void setAngle(int angle) {
        this.angle = angle;
        markPointX = (float) (cx + innerRadius * Math.cos(Math.atan2(innerRadius * Math.sin(angle), innerRadius * Math.cos(angle)) - (Math.PI / 2)));
        markPointY = (float) (cy + innerRadius * Math.sin(Math.atan2(innerRadius * Math.sin(angle), innerRadius * Math.cos(angle)) - (Math.PI / 2)));
        float donePercent = (((float) this.angle) / 260) * 100;
        float progress = (donePercent / 100) * getMaxProgress();
        setProgressPercent(Math.round(donePercent));
        setProgress(Math.round(progress));
    }

    /**
     * Sets the seek bar change listener.
     *
     * @param listener
     *            the new seek bar change listener
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
     * @param barWidth
     *            the new bar width
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
     *
     */
    public interface OnSeekChangeListener {

        /**
         * On progress change.
         *
         * @param view
         *            the view
         * @param newProgress
         *            the new progress
         */
        public void onProgressChange(CircleView view, int newProgress);
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
     * @param maxProgress
     *            the new max progress
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
     * @param progress
     *            the new progress
     */
    public void setProgress(int progress) {
        if (this.progress != progress) {
            this.progress = progress;
            // if (!CALLED_FROM_ANGLE) {
            int newPercent = (this.progress * 100) / this.maxProgress;
            int newAngle = (newPercent * 260) / 100;
            this.setAngle(newAngle);
            this.setProgressPercent(newPercent);
            // }
            mListener.onProgressChange(this, this.getProgress());
            // CALLED_FROM_ANGLE = false;

            invalidate();
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
     * @param progressPercent
     *            the new progress percent
     */
    public void setProgressPercent(int progressPercent) {
        this.progressPercent = progressPercent;
    }

    /**
     * Sets the ring background color.
     *
     * @param color
     *            the new ring background color
     */
    public void setRingBackgroundColor(int color) {
        circleRing.setColor(color);
    }

    /**
     * Sets the progress color.
     *
     * @param color
     *            the new progress color
     */
    public void setProgressColor(int color) {
        circleColor.setColor(color);
    }

    /**
     * Moved.
     *
     * @param x
     *            the x
     * @param y
     *            the y
     * @param up
     *            the up
     */
    private void moved(float x, float y, boolean up) {
        float distance = (float) Math.sqrt(Math.pow((x - cx), 2) + Math.pow((y - cy), 2));
        if (distance < outerRadius + adjustmentFactor && distance > innerRadius - adjustmentFactor && !up) {
            IS_PRESSED = true;

            markPointX = (float) (cx + outerRadius * Math.cos(Math.atan2(x - cx, cy - y) - (Math.PI / 2)));
            markPointY = (float) (cy + outerRadius * Math.sin(Math.atan2(x - cx, cy - y) - (Math.PI / 2)));

            float degrees = (float) ((float) ((Math.toDegrees(Math.atan2(x - cx, cy - y)) + 300.0)) % 300.0);
            // and to make it count 0-360
            if (degrees < 0) {
                degrees += 2 * Math.PI;
            }

            setAngle(Math.round(degrees));
            invalidate();

        } else {
            IS_PRESSED = false;
            invalidate();
        }

    }

    /**
     * Gets the adjustment factor.
     *
     * @return the adjustment factor
     */
    public float getAdjustmentFactor() {
        return adjustmentFactor;
    }

    /**
     * Sets the adjustment factor.
     *
     * @param adjustmentFactor
     *            the new adjustment factor
     */
    public void setAdjustmentFactor(float adjustmentFactor) {
        this.adjustmentFactor = adjustmentFactor;
    }

}
