package com.tzt.customizekt.diy.gesture;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.renderscript.Sampler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import com.tzt.customizekt.R;


/**
 * 简单的图片手势控件 （主要介绍各个回调函数的作用）
 * 支持滑动
 * ScrollTo是到那个位置，ScrollBy是经过这段位置
 */
public class GestureView1 extends View {
    private final static String TAG = "GestureView1";
    MyGestureListener mGestureListener;
    MyDoubleTabListener mDoubleTabListener;
    MyOnScaleGestureListener mScaleGestureListener;
    GestureDetector mGestureDetector;
    ScaleGestureDetector mScaleGestureDetector;
    Scroller mScroller;
    Bitmap mBitmap;
    Paint mPaint;
    Matrix matrix;
    float mScale = 1.0f;
    float pointX = 0;
    float pointY = 0;
    private ValueAnimator valueAnimator;

    public GestureView1(Context context) {
        super(context);
        init();
    }

    public GestureView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //手势识别接口
        mGestureListener = new MyGestureListener();
        mDoubleTabListener = new MyDoubleTabListener();
        mScaleGestureListener = new MyOnScaleGestureListener();

        //手势识别类
        mGestureDetector = new GestureDetector(getContext(), mGestureListener);
        mGestureDetector.setOnDoubleTapListener(mDoubleTabListener);
        //缩放识别类
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), mScaleGestureListener);

        mScroller = new Scroller(getContext());

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        mPaint = new Paint();
        matrix = new Matrix();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        matrix.setScale(mScale, mScale, pointX, pointY);
        canvas.drawBitmap(mBitmap, matrix, mPaint);
//        canvas.drawBitmap(mBitmap, null, new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight()), mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        return mGestureDetector.onTouchEvent(event);
    }


    /**
     * 手势监听
     */
    class MyGestureListener implements GestureDetector.OnGestureListener {

        // 手指刚刚接触屏幕的一瞬间，由一个ACTION_DOWN事件触发
        @Override
        public boolean onDown(MotionEvent e) {
            Log.e(TAG, "MyGestureListener#onDown()");
            return true;
        }

        // 手指刚刚接触屏幕，尚未松开或拖动，由一个ACTION_DOWN事件触发
        // * 注意和ACTION_DOWN的区别，他强调的是没有松开或者拖动的状态
        @Override
        public void onShowPress(MotionEvent e) {
            Log.e(TAG, "MyGestureListener#onShowPress()");
        }

        // ******这是单击事件******手指轻触屏幕后松开，伴随着ACTION_UP触发
        // * 注意不会与长按事件(onLongPress)共存，但是可以与双击事件（onDoubleTap）共存
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.e(TAG, "MyGestureListener#onSingleTapUp()");
            return false;
        }

        // ******拖动事件******
        // 手指按下屏幕后拖动，由一个 ACTION_DOWN 和多个ACTION_MOVE 触发，这是拖动行为
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.e(TAG, "MyGestureListener#onScroll(): distanceX:" + distanceX + ",distanceY:" + distanceY + ",e1(" + e1.getX() + "," + e1.getY() + ")" + ",e2(" + e2.getX() + "," + e2.getY() + ")");
            //往上面拖动distanceY是正值 往下面拖动是负值
            //往左拖动distanceX是正值 往右拖动是负值
            scrollBy((int) distanceX, (int) distanceY);
            return false;
        }

//        ScrollTo 是到那个位置，ScrollBy 是经过这段位置

        // ******长按事件******
        // * 注意不会与onScroll事件共存（我测试的情况）
        @Override
        public void onLongPress(MotionEvent e) {
            Log.e(TAG, "MyGestureListener#onLongPress():");
        }

        // ******快速滑动行为事件******
        // 用户按下触摸屏，快速滑动后松开，由一个 ACTION_DOWN ,多个ACTION_MOVE 和一个ACTION_UP组成，这是快速滑动行为
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.e(TAG, "MyGestureListener#onFling(): velocityX:" + velocityX + ",velocityY:" + velocityY + ",e1(" + e1.getX() + "," + e1.getY() + ")" + ",e2(" + e2.getX() + "," + e2.getY() + ")");
            //往上面滑动velocityY是负值 往下面滑动是正值
            //往左滑动velocityX是负值 往右滑动是正值
            mScroller.fling((int) e1.getX(), (int) e1.getY(), (int) velocityX, (int) velocityY, 0, 500, 0, 500);
            return false;
        }
    }

    /**
     * 双击事件监听
     */
    class MyDoubleTabListener implements GestureDetector.OnDoubleTapListener {

        // *************严格的单击行为*************
        // * 注意它和onSingleTabUp的区别，它不能与双击（onDoubleTap）事件共存，也不能与长按（onLongPress）事件共存，即这只能试一次单击
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.e(TAG, "MyDoubleTabListener#onSingleTapConfirmed():");
            return false;
        }

        // 双击事件，由连续两次的单击事件构成
        // * 注意 他不可能和onSingleTapConfirmed共存
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.e(TAG, "MyDoubleTabListener#onDoubleTap():");
            return false;
        }

        // 表示发生了双击行为，再双击的期间，DOWN UP MOVE 都会触发此回调
        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.e(TAG, "MyDoubleTabListener#onDoubleTapEvent():");
            pointX = e.getX();
            pointY = e.getY();
            // 双击
            if (mScale != 1) {
                // 回弹
                animateScaleTo(1f);
            } else {
                // 放大
                animateScaleTo(1.5f);
            }
            return false;
        }
    }

    // 手势缩放
    class MyOnScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.e(TAG, "MyOnScaleGestureListener#onScale(): factor:" + detector.getScaleFactor());
//            matrix.preScale(detector.getScaleFactor(), detector.getScaleFactor(), detector.getFocusX(), detector.getFocusY());
            pointX = detector.getFocusX();
            pointY = detector.getFocusY();
            mScale *= detector.getScaleFactor();
            invalidate();
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            Log.e(TAG, "MyOnScaleGestureListener#onScaleBegin(): factor:" + detector.getScaleFactor());
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            Log.e(TAG, "MyOnScaleGestureListener#onScaleEnd(): factor:" + detector.getScaleFactor());
        }

    }

    private void animateScaleTo(float scale) {
        Log.e("animateScale", "mScale:" + mScale + ",endScale:" + scale);
        valueAnimator = ValueAnimator.ofFloat(mScale, scale);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mScale = (float) animation.getAnimatedValue();
                Log.e("animateScale", "mScale:" + mScale);
                postInvalidate();
            }
        });
        valueAnimator.setDuration(200);
        valueAnimator.start();
    }
}