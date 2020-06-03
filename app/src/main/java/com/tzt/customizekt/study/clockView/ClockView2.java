package com.tzt.customizekt.study.clockView;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.tzt.customizekt.R;
import com.tzt.customizekt.study.base.BaseView;

public class ClockView2 extends BaseView {

    private Paint mPaint;
    private int mPadding;
    private int mBorderWidth;
    private int mMainLineLength;
    private int mMainLineWidth;
    private int mSubLineLength;
    private int mSubLineWidth;
    private int mClockColor;
    private Path mPointerPath;
    private RectF mPointerRectF;
    private int mPointerRadius;

    public ClockView2(Context context) {
        super(context);
    }

    public ClockView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ClockView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mClockColor = ContextCompat.getColor(context, R.color.colorPrimary);
        mPointerPath = new Path();

        mPadding = dpToPx(5);
        mBorderWidth = dpToPx(3);

        mPointerRadius = dpToPx(8);
        mPointerRectF = new RectF(-mPointerRadius,
                -mPointerRadius,
                mPointerRadius,
                mPointerRadius);
        mMainLineLength = dpToPx(10) + mBorderWidth / 2;
        mMainLineWidth = dpToPx(5);

        mSubLineLength = dpToPx(5) + mBorderWidth / 2;
        mSubLineWidth = dpToPx(3);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCoordinate(canvas);
        int w = getWidth();
        int h = getHeight();

        int width = Math.min(w, h);
        width = width - mPadding - mBorderWidth / 2;
        canvas.translate(w / 2, h / 2);


        canvas.save();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(0, 0, width / 2, mPaint);
        canvas.drawPoint(0f,0f,mPaint);

        int ange = 360 / 12;
        for (int i = 0; i < 12; i++) {
            if (i != 0) {
                canvas.rotate(ange);
            }
            if (i % 3 == 0) {
                mPaint.setStrokeWidth(mMainLineWidth);
                canvas.drawLine(0f, -width / 2, 0f, -(width / 2 - mMainLineLength), mPaint);
            } else {
                mPaint.setStrokeWidth(mSubLineWidth);
                canvas.drawLine(0f, -width / 2, 0f, -(width / 2 - mSubLineLength), mPaint);
            }
        }
        canvas.restore();


        drawPointer(canvas, width);
    }

    private void drawPointer(Canvas canvas, int width) {
        canvas.save();
        canvas.rotate(100f);
        if (mPointerPath.isEmpty()) {
            mPointerPath.moveTo(mPointerRadius, 0);
            mPointerPath.addArc(mPointerRectF, 0, 180);
            mPointerPath.lineTo(0, -width / 4);
            mPointerPath.lineTo(mPointerRadius, 0);
            mPointerPath.close();
        }

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.GREEN);
        canvas.drawPath(mPointerPath, mPaint);
        canvas.restore();
    }


    /**
     * 转换 dp 至 px
     *
     * @param dpValue dp值
     * @return px值
     */
    protected int dpToPx(float dpValue) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dpValue * metrics.density + 0.5f);
    }

}
