package com.tzt.customizekt.diy.edittext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * 可清除的输入框
 * 使用绘制Path实现
 * <p>
 * author: DragonForest
 * time: 2019/12/9
 */
public class ClearableEditText extends AppCompatEditText implements View.OnFocusChangeListener {

    // 右侧偏移
    int paddingRight = 20;

    // 删除图标的长度
    int clearWidth = 50;
    // 删除图标的坐标 组成一个正方形
    int clearStartX;
    int clearStartY;
    int clearEndX;
    int clearEndY;

    Paint mPaint;
    Path mPath;

    // 是否有焦点
    boolean hasFocus = false;


    public ClearableEditText(Context context) {
        super(context);
        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight() + paddingRight + clearWidth, getPaddingBottom());
        setLines(1);
        setMaxLines(1);

        setOnFocusChangeListener(this);

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);

        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (hasFocus && getText().toString().length() > 0) {
            // 画删除图标
            drawClearIcon(canvas);
        }
    }

    //也可绘制bitmap
    private void drawClearIcon(Canvas canvas) {
        // 删除图标的中点
        //x中点= 宽度-paddingRight-clearWidth / 2
        int centerX = getWidth() - paddingRight - clearWidth / 2;
        int centerY = getHeight() / 2;
        clearStartX = centerX - clearWidth / 2;
        clearStartY = centerY - clearWidth / 2;
        clearEndX = centerX + clearWidth / 2;
        clearEndY = centerY + clearWidth / 2;

        mPath.moveTo(centerX - clearWidth / 2, centerY - clearWidth / 2);
        mPath.lineTo(centerX + clearWidth / 2, centerY + clearWidth / 2);
        mPath.moveTo(centerX + clearWidth / 2, centerY - clearWidth / 2);
        mPath.lineTo(centerX - clearWidth / 2, centerY + clearWidth / 2);

        canvas.drawPath(mPath, mPaint);

        Log.e("ClearableEditText", "(csx,csy,cex,cey):" + "(" + clearStartX + "," + clearStartY + "," + clearEndX + "," + clearEndY + ")");
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (this.hasFocus != hasFocus) {
            this.hasFocus = hasFocus;
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                handleClick(event);
                break;
        }
        return super.onTouchEvent(event);
    }

    private void handleClick(MotionEvent event) {
        int centerX = getWidth() - paddingRight - clearWidth / 2;
        int centerY = getHeight() / 2;
        clearStartX = centerX - clearWidth / 2;
        clearStartY = centerY - clearWidth / 2;
        clearEndX = centerX + clearWidth / 2;
        clearEndY = centerY + clearWidth / 2;

        if (event.getX() > clearStartX
                && event.getX() < clearEndX
                && event.getY() > clearStartY
                && event.getY() < clearEndY) {
            // 点击命中
            setText("");
        }
    }
}