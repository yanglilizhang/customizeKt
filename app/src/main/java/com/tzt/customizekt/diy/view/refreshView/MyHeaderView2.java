package com.tzt.customizekt.diy.view.refreshView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * author: DragonForest
 * time: 2019/12/13
 */
public class MyHeaderView2 extends View implements IHeaderView {
    Paint mPaint;

    public static final int STATE_START = 1;
    public static final int STATE_EFFECT = 2;
    public static final int STATE_LOADING = 3;
    public static final int STATE_END = 4;

    public int state = 0;

    private String textStart = "下拉刷新";
    private String textEffect = "松开刷新";
    private String textLoading = "加载中...";
    private String textEnd = "结束";

    public MyHeaderView2(Context context) {
        super(context);
        init();
    }

    public MyHeaderView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setTextSize(25);
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (state) {
            case STATE_START:
                drawStart(canvas);
                break;
            case STATE_EFFECT:
                drawEffect(canvas);
                break;
            case STATE_LOADING:
                drawLoading(canvas);
                break;
            case STATE_END:
                break;
        }
    }

    private void drawStart(Canvas canvas) {
        float textWidth = mPaint.measureText(textStart);
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        canvas.drawColor(Color.GRAY);
        float dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        canvas.drawText(textStart, //下拉刷新
                getWidth() / 2 - textWidth / 2,
                getHeight() - 50 - dy,
                mPaint);

    }

    private void drawEffect(Canvas canvas) {
        float textWidth = mPaint.measureText(textEffect);
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        canvas.drawColor(Color.RED);
        float dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        canvas.drawText(textEffect,//松开刷新
                getWidth() / 2 - textWidth / 2,
                getHeight() - 50 - dy,
                mPaint);
    }

    private void drawLoading(Canvas canvas) {
        float textWidth = mPaint.measureText(textLoading);
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        canvas.drawColor(Color.GREEN);
        float dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        canvas.drawText(textLoading,//加载中...
                getWidth() / 2 - textWidth / 2,
                getHeight() - 50 - dy,
                mPaint);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onStart(float fac) {
        this.state = STATE_START;
        postInvalidate();
    }

    @Override
    public void onEffect(float fac) {
        this.state = STATE_EFFECT;
        postInvalidate();
    }

    @Override
    public void onLoading() {
        this.state = STATE_LOADING;
        postInvalidate();
    }

    @Override
    public void onFinish() {
        this.state = STATE_END;
        postInvalidate();
    }
}