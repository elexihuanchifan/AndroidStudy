package com.example.administrator.myapplication.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.myapplication.utils.DisplayUtils;

/**
 * Created by Administrator on 2016/7/28.
 * 运动界面层叠卡片
 * 高度 80 + 6 + 6 + 上下padding各2
 */
public class SportShadowView extends View {

    private int width;
    private int height;

    private Context mContext;

    private Paint bgPaint;
    private Paint shadowPaint;

    private int topPadding;
    private int baseHight;
    private int bottomHeight;
    private int bottomPadding;
    private int hMargin;
    private int leftWidth;
    private int shadowWidth;

    private int radians;

    private RectF bt2Rect;
    private RectF bt2RightRect;
    private RectF bt1Rect;
    private RectF bt1RightRect;
    private RectF rectF;
    private RectF rightRect;
    private RectF topShadowRect;

    public SportShadowView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public SportShadowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public SportShadowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SportShadowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        if (height < DisplayUtils.dip2px(mContext, 96)) {
            height = DisplayUtils.dip2px(mContext, 96);
        }

        setMeasuredDimension(width, height);
    }

    private void init() {

        topPadding = DisplayUtils.dip2px(mContext, 2);
        baseHight = DisplayUtils.dip2px(mContext, 80);
        bottomHeight = DisplayUtils.dip2px(mContext, 6);
        bottomPadding = DisplayUtils.dip2px(mContext, 2);
        hMargin = DisplayUtils.dip2px(mContext, 10);
        leftWidth = DisplayUtils.dip2px(mContext, 83);
        shadowWidth = DisplayUtils.dip2px(mContext, 1);

        radians = DisplayUtils.dip2px(mContext, 4);


        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);

        shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(Color.parseColor("#33000000"));
        shadowPaint.setStyle(Paint.Style.STROKE);
        shadowPaint.setStrokeWidth(shadowWidth);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        bt2Rect = new RectF(hMargin + radians * 2, topPadding + baseHight - radians + bottomHeight,
                leftWidth + hMargin + radians * 3, topPadding + baseHight + bottomHeight * 2);
        bgPaint.setColor(Color.parseColor("#be5938"));
        canvas.drawRoundRect(bt2Rect, radians, radians, bgPaint);

        bgPaint.setColor(Color.parseColor("#e0e0e0"));
        bt2RightRect = new RectF(leftWidth + hMargin + radians * 2, topPadding + baseHight - radians + bottomHeight,
                width - hMargin - radians * 2, topPadding + baseHight + bottomHeight * 2);

        canvas.drawRoundRect(bt2RightRect, radians, radians, bgPaint);
        canvas.drawRect(leftWidth + hMargin + radians * 2, topPadding + baseHight + bottomHeight,
                leftWidth + hMargin + radians * 3, topPadding + baseHight + bottomHeight * 2, bgPaint);

        bgPaint.setColor(Color.parseColor("#ce6543"));
        bt1Rect = new RectF(hMargin + radians, topPadding + baseHight - radians,
                leftWidth + hMargin + radians * 2, topPadding + baseHight + bottomHeight);
        canvas.drawRoundRect(bt1Rect, radians, radians, bgPaint);

        bgPaint.setColor(Color.parseColor("#efefef"));
        bt1RightRect = new RectF(leftWidth + hMargin + radians, topPadding + baseHight - radians
                , width - hMargin - radians, topPadding + baseHight + bottomHeight);
        canvas.drawRoundRect(bt1RightRect, radians, radians, bgPaint);
        canvas.drawRect(leftWidth + hMargin + radians, topPadding + baseHight, leftWidth + hMargin + radians * 2, topPadding + baseHight + bottomHeight, bgPaint);

        bgPaint.setColor(Color.parseColor("#ff8560"));
        rectF = new RectF(hMargin, topPadding, leftWidth + hMargin + radians, topPadding + baseHight);
        canvas.drawRoundRect(rectF, radians, radians, bgPaint);

        bgPaint.setColor(Color.WHITE);
        rightRect = new RectF(leftWidth + hMargin, topPadding, width - hMargin, topPadding + baseHight);
        canvas.drawRoundRect(rightRect, radians, radians, bgPaint);
        canvas.drawRect(leftWidth + hMargin, topPadding, leftWidth + hMargin + radians, topPadding + radians, bgPaint);
        canvas.drawRect(leftWidth + hMargin, topPadding + baseHight - radians, leftWidth + hMargin + radians, topPadding + baseHight, bgPaint);

//        topShadowRect = new RectF(hMargin - shadowWidth, topPadding - shadowWidth, width - hMargin + shadowWidth, topPadding + shadowWidth + baseHight);
//        canvas.drawRoundRect(topShadowRect, radians, radians, shadowPaint);
    }
}
