package com.tzt.customizekt.diy.shader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.tzt.customizekt.R;

/**
 * 放大镜
 */
public class MagnifyingView extends View {
    Bitmap mBitmap;
    Paint mPaint;

    BitmapShader mBitmapShader;
    Matrix matrix = new Matrix();
    // 坐标
    float cx;
    float cy;
    int cr = 160;

    float scale = 2;

    public MagnifyingView(Context context) {
        super(context);
        init();
    }

    public MagnifyingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.GRAY);
        mPaint.setAntiAlias(true);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //做相应的放大操作
        matrix.setScale(scale, scale, cx, cy);
        mBitmapShader.setLocalMatrix(matrix);

        mPaint.setShader(mBitmapShader);
        // 画放大镜
        canvas.drawCircle(cx, cy, cr, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                cx = event.getX();
                cy = event.getY();
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                cx = event.getX();
                cy = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                cx = -getWidth();
                cy = -getHeight();
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }
}