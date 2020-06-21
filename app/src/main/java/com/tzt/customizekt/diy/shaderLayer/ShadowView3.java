package com.tzt.customizekt.diy.shaderLayer;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 图形阴影效果 发光效果
 */
public class ShadowView3 extends View {

    Paint mPaint;

    // 是否显示阴影
    boolean isShowShadowLayer = true;

    public ShadowView3(Context context) {
        super(context);
        init();
    }

    public ShadowView3(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // 禁止硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        if (isShowShadowLayer) {
            // 发光效果
            mPaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.SOLID));
        } else {
            // 清除阴影
            mPaint.clearShadowLayer();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.BLUE);
        // 画正方形
//        canvas.drawRect(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom(), mPaint);
        // 画圆形
        float r = Math.min(getWidth() - getPaddingLeft() - getPaddingRight(), getHeight() - getPaddingTop() - getPaddingBottom()) / 2;
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, r, mPaint);
        // 画文字
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setTextSize(30);
        mPaint.setColor(Color.WHITE);
        String text = "点我试试";
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float textWidth = mPaint.measureText(text);
        float dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        canvas.drawText(text, getWidth() / 2 - textWidth / 2, getHeight() / 2 + dy, mPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isShowShadowLayer = false;
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                isShowShadowLayer = true;
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }
}