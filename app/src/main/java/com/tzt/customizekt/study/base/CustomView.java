package com.tzt.customizekt.study.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomView extends View {
    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        //ViewGroup绘制children
    }

//    当我们自定义ViewGroup的时候默认是不会执行OnDraw方法的（ViewGroup默认调用了setWillNotDraw（true），
//    因为系统默认认为我们不会在ViewGroup中绘制内容），我们如果需要进行绘制可以在dispatchDraw中去进行或
//    者调用setWillNotDraw(false)方法。


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //在视图及其所有子级都已从 XML 扩充之后调用。
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //调用以确定此视图及其所有子级的大小要求。
//        setMeasuredDimension();期望宽高
//        通常情况下期望宽高是和最终的宽高相同的，但是也有特殊情况(比如在layout方法最终赋值View宽高的时候
//        手动的修改值而不用测量得到的值)
//        getMeasureWidth()方法中的值是通过setMeasuredDimension()方法来进行设置的。
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //在此视图应为其所有子级分配大小和位置时调用。
//        getWidth()方法要在layout()过程结束后才能获取到，当在layout方法中调用setFrame()后就可以获取此值了
//        这个值是View的真实宽高。
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //invalidate()用于主线程
        //postInvalidate()用于子线程
        //requestLayout()会调用measure和layout方法，当View的大小位置需要改变的时候调用。
        //如果view的大小发生了变化那么requestlayout也会调用draw()方法。
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //在此视图的大小发生变化时调用。
        //如果我们的一些用到的属性是跟View的大小变化相关的话，那么我们可以通过OnSizeChanged去进行监听
        //OnSizeChanged在layout方法中的setFrame执行时会被调用，
        //也就是说当我们调用requestLayout时可以通过OnSizeChanged去获取新的控件宽高等值
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
        //在发生新的按键事件时调用。。
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
        //	在发生松开按键事件时调用。
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
        //在发生触屏动作事件时调用。
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        return super.onTrackballEvent(event);
        //在发生轨迹球动作事件时调用。
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        //在视图获得或失去焦点时调用。
    }


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        //在包含视图的窗口获得或失去焦点时调用。
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }


    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        //在包含视图的窗口的可见性发生变化时调用。
    }
}
