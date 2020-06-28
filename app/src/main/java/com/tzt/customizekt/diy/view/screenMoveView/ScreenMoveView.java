package com.tzt.customizekt.diy.view.screenMoveView;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.tzt.customizekt.R;


/**
 * 屏幕内移动的View
 */
public class ScreenMoveView extends View {
    Paint mPaint;
    Bitmap mBitmap;

    private int screenWidth;
    private int screenHeight;
    Rect rect = new Rect();

    public ScreenMoveView(Context context) {
        super(context);
        init();
    }

    public ScreenMoveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
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

    float downX, downY;
    float moveX, moveY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getRawX();
                downY = event.getRawY();
                return true;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getRawX();
                moveY = event.getRawY();
                //getX()手指点击处距离View的边界距离
                //getLeft()View的边界和父View的距离
                //getRaw点击处和屏幕的距离
                //实际移动的距离x = moveX-downX
                this.setX(getX() + moveX - downX);
                this.setY(getY() + moveY - downY);
                downX = moveX;
                downY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                //移动后View的位置=点击处据View边界的距离+实际移动的距离
                float currentX = getX() + event.getRawX() - downX;
                float currentY = getY() + event.getRawY() - downY;
                Log.e("ScreenMoveView", currentX + "," + currentY + "," + getX() + "," + getY());
                if (currentX < 0) {
                    currentX = -getWidth() / 2;
                }
                if (currentX > (screenWidth - getWidth())) {
                    currentX = screenWidth - getWidth() / 2;
                }
                if (currentY < 0) {
                    currentY = -getHeight() / 2;
                }
                if (currentY > (screenHeight - getHeight())) {
                    currentY = screenHeight - getHeight() / 2;
                }
                Log.e("ScreenMoveView", "result" + currentX + "," + currentY);
                animateTo(getX(), getY(), currentX, currentY);
                break;
        }
        return super.onTouchEvent(event);
    }


    /**
     * 动画方式移动到指定位置
     *
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    private void animateTo(float startX, float startY, float endX, float endY) {
        ValueAnimator animatorX = ValueAnimator.ofFloat(startX, endX);
        animatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (float) animation.getAnimatedValue();
                setX(x);
            }
        });
        ValueAnimator animatorY = ValueAnimator.ofFloat(startY, endY);
        animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float y = (float) animation.getAnimatedValue();
                setY(y);
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorX, animatorY);
        animatorSet.setDuration(200);
        animatorSet.start();
    }
}