package com.tzt.customizekt.diy.shader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.tzt.customizekt.R;

/**
 * 放大镜
 * 使用Canvas 缩放变换实现
 */
public class MagnifyingView2 extends View {
    Bitmap mBitmap;
    Paint mPaint;

    // 坐标
    float cx;
    float cy;
    int cr = 80;
    float scale=2;
    private Path mPath;


    public MagnifyingView2(Context context) {
        super(context);
        init();
    }

    public MagnifyingView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);

        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画放大镜
        canvas.save();
        canvas.scale(scale,scale,cx,cy);
        mPath.reset();
        mPath.addCircle(cx,cy,cr, Path.Direction.CW);
        canvas.clipPath(mPath);
//        canvas.drawCircle();
        canvas.drawBitmap(mBitmap,null,new Rect(0,0,getWidth(),getHeight()),mPaint);
        canvas.restore();
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