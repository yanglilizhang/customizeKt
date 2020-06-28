package com.tzt.customizekt.diy.view.screenMoveView;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.tzt.customizekt.R;

/**
 * WindowManager-在屏幕上显示一个浮动控件。（8.0需要悬浮窗权限）
 * 需要能接收点击事件，还要能显示在statusBar(状态栏)之上，不能被状态栏遮住。
 */
public class ScreenMoveView2 extends View {
    Paint mPaint;
    Bitmap mBitmap;
    Rect rect = new Rect();
    float lastX;
    float lastY;
    private int screenWidth;
    private int screenHeight;

    WindowManager windowManager;
    WindowManager.LayoutParams windowLayoutParams;

    public ScreenMoveView2(Context context) {
        super(context);
        init(context);
    }

    public ScreenMoveView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void attachToWindow() {
        //window起始位置
        windowLayoutParams.x = 100;
        windowLayoutParams.y = 100;
        //window大小
        windowLayoutParams.width = 200;
        windowLayoutParams.height = 200;
        windowLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        windowLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        //TYPE_SYSTEM_OVERLAY的可以覆盖在状态栏之上，而TYPE_SYSTEM_ALERT不行。
        // 但是TYPE_SYSTEM_OVERLAY添加的view是不能获取焦点的----需要权限
//        windowLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        // 此flag表示此window下层的其他window接收事件不受影响， 不然其他window事件会被此window覆盖
        windowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

        windowManager.addView(this, windowLayoutParams);
    }

    private void init(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowLayoutParams = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0, 0, PixelFormat.TRANSLUCENT);

//        windowLayoutParams = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                        |WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
//                PixelFormat.TRANSLUCENT);

        initScreenParams();
        Log.e("ScreenMoveView", "screenHeight&Width," + screenHeight + "," + screenWidth);
        mPaint = new Paint();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car);
    }

    private void initScreenParams() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();

        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            int statusBarHeight = getResources().getDimensionPixelSize(height);
            screenHeight -= statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rect.set(0, 0, getWidth(), getHeight());
        canvas.drawBitmap(mBitmap, null, rect, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                windowLayoutParams.x = (int) event.getRawX() - windowLayoutParams.width / 2;
                windowLayoutParams.y = (int) event.getRawY() - windowLayoutParams.height / 2;
                windowManager.updateViewLayout(this, windowLayoutParams);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

}