package com.tzt.customizekt.study.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class CanvasView2 extends View {
    Paint mPaint;
    Path mPath;

    // 控制点
    float mPreX;
    float mPreY;

    // 终点
    float endX;
    float endY;

    public CanvasView2(Context context) {
        super(context);
        init();
    }

    public CanvasView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);

        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(event.getX(), event.getY());
                mPreX = event.getX();
                mPreY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                endX = (mPreX + event.getX()) / 2;
                endY = (mPreY + event.getY()) / 2;
                // 用于绘制圆滑曲线
                mPath.quadTo(mPreX, mPreY, endX, endY);
                invalidate();
                mPreX = event.getX();
                mPreY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }


}