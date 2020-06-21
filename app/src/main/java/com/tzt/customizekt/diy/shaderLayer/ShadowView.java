package com.tzt.customizekt.diy.shaderLayer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 阴影绘制需要的知识点
 * https://www.jianshu.com/p/7a832e074b69
 * 1.硬件加速(硬件加速开启时，CPU的工作是把绘制工作转换为GPU的操作)
 * 2.Paint对象的setShadowLayer方法
 * Application级别开启硬件加速 <application android:hardwareAccelerated="true"/>
 * Activity级别开启硬件加速 <activity android:hardwareAccelerated="true">
 * Window级别开启硬件加速
 * View级别开启硬件加速 myView.setLayerType(View.LAYER_TYPE_*, null);？？
 * 图形阴影效果
 * https://github.com/yingLanNull/ShadowImageView 图片阴影
 */
public class ShadowView extends View {
//    Window级别开启硬件加速
//    getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
//            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

//    调用了Paint.setShadowLayer()方法，然而添加这个方法之后View没有任何反应，
//    Google说需要给View设置软渲染，具体做法为调用View的setLayerType
//    (LAYER_TYPE_SOFTWARE, null)方法，在开启它时，之后进行的绘制都会
//    绘制到一张Bitmap(software layer)上，绘制完成后再渲染到hardware layer上。
//注：一个View，即使它的Canvas没有开启硬件加速，但是它仍然可以被绘制到开启了硬件加速的Window上。
//    获取到是否支持硬件加速的方法：
//    1. View.isHardwareAccelerated();
//    2. Canvas.isHardwareAccelerated();

    //  注：在View中可以关闭硬件加速setLayerType(LAYER_TYPE_SOFTWARE, null)，
    //  但是不能开启View级别的硬件加速， 因为它会受到别的设置影响。

    //受到 GPU 绘制方式的限制，Canvas 的有些方法在硬件加速开启式会失效或无法正常工作

    Paint mPaint;

    // 是否显示阴影
    boolean isShowShadowLayer = true;

    public ShadowView(Context context) {
        super(context);
        init();
    }

    public ShadowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    //view.setLayerType(LAYER_TYPE_SOFTWARE, null)这个方法的作用并不是关闭硬件加速
// 只是当它的参数为LAYER_TYPE_SOFTWARE的时候，可以顺便把硬件加速关掉而已；
//参数为 LAYER_TYPE_SOFTWARE 时，使用软件来绘制 View Layer，绘制到一个 Bitmap，并顺便关闭硬件加速；
    private void init() {
        //因为setShadowLayer函数 在关闭硬件加速的情况下对文字阴影效果起作用，
        // 其他图形和图片的阴影都需要开启硬件加速
        // 禁止硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        if (isShowShadowLayer) {
            //阴影
            mPaint.setShadowLayer(2, 8, 8, Color.GREEN);
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
        mPaint.setColor(Color.WHITE);
        // 画正方形
        canvas.drawRect(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom(), mPaint);
        // 画圆形
//        float r=Math.min(getWidth()-getPaddingLeft()-getPaddingRight(),getHeight()-getPaddingTop()-getPaddingBottom())/2;
//        canvas.drawCircle(getWidth()/2,getHeight()/2,r,mPaint);
        // 画文字
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setTextSize(80);
        mPaint.setColor(Color.RED);
        String text = "点我试试";
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float textWidth = mPaint.measureText(text);
        float dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        canvas.drawText(text,
                getWidth() / 2 - textWidth / 2,
                getHeight() / 2 + dy, mPaint);
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