package com.tzt.customizekt.diy.view.waveView;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.tzt.customizekt.R;


/**
 * 小船水上漂
 */
public class WaveMoveShipView extends View {
    Paint mPaint;
    Path mPath;

    // 半个波长
    int halfWaveWidth = 150;
    // 波峰的高度
    int waveHeight = 50;
    // 初始点坐标
    float startX = -halfWaveWidth;
    float startY = 100;

    // 小船移动的进度 0~1
    float progress = 0;
    // 小船坐标
    float[] pos = new float[2];
    float[] tan = new float[2];
    // 小船图标
    Bitmap mShipBitmap;
    // 缩放后的小船图标
    Bitmap mScaleShipBitmap;
    //
    private Matrix mBitmapMatrix;

    public WaveMoveShipView(Context context) {
        super(context);
        init();
    }

    public WaveMoveShipView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPath = new Path();
        mBitmapMatrix = new Matrix();
        mShipBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ship);
//        setProgress(50);
//        startAnim();
    }

    private void initWaveStyle() {
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GREEN);
        // 画波浪
        drawWave(canvas);
        // 画小船
        drawShip(canvas);

    }

    private void drawWave(Canvas canvas) {
        mPath.reset();
        initWaveStyle();
        // 起始点
        mPath.moveTo(startX, startY);
        // 使用相对贝塞尔曲线画出横向波形
        for (int i = -halfWaveWidth * 2; i < getWidth() + halfWaveWidth * 2; i += halfWaveWidth * 2) {
            mPath.rQuadTo(halfWaveWidth / 2, waveHeight, halfWaveWidth, 0);
            mPath.rQuadTo(halfWaveWidth / 2, -waveHeight, halfWaveWidth, 0);
        }

        canvas.drawPath(mPath, mPaint);
    }

    private void drawShip(Canvas canvas) {
        PathMeasure measure = new PathMeasure(mPath, false);
        float length = measure.getLength();
        //小船进度 = length * progress
        measure.getPosTan(length * progress, pos, tan);


        if (mScaleShipBitmap == null) {
            mScaleShipBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            Canvas canvas1 = new Canvas(mScaleShipBitmap);
            canvas1.drawBitmap(mShipBitmap, null, new Rect(0, 0, mScaleShipBitmap.getWidth(), mScaleShipBitmap.getHeight()), mPaint);
        }

        // 通过tan计算角度
        double tann = Math.atan2(this.tan[1], this.tan[0]);
        double angle = 180 * tann / Math.PI;

        // 通过变换设置坐标和角度
        // 方法① 自己计算matrix
//        mBitmapMatrix.reset();
//        mBitmapMatrix.postRotate((float)angle,mScaleShipBitmap.getWidth()/2, mScaleShipBitmap.getHeight()/2);
//        mBitmapMatrix.postTranslate(pos[0] - mScaleShipBitmap.getWidth(),pos[1] - mScaleShipBitmap.getHeight());

        // 方法② 通过PathMeasure获取matrix， 需要增加偏移量
        measure.getMatrix(length * progress, mBitmapMatrix, PathMeasure.POSITION_MATRIX_FLAG | PathMeasure.TANGENT_MATRIX_FLAG);
        mBitmapMatrix.preTranslate(-mScaleShipBitmap.getWidth(), -mScaleShipBitmap.getHeight());
        canvas.drawBitmap(mScaleShipBitmap, mBitmapMatrix, mPaint);
    }

    /**
     * 开启动画
     */
    @SuppressLint("WrongConstant")
    public void startAnim() {
        // 水平方向的动画 波动
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(-halfWaveWidth * 2, 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                startX = value;
                postInvalidate();
            }
        });
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }

    private void animToProgress(float newProgress) {
        // 小船滑动
        ValueAnimator valueAnimator1 = ValueAnimator.ofFloat(progress, newProgress);
        valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        long duration = (long) (Math.abs(newProgress - progress) * 20 * 1000);
        Log.e("WaveShipView", "duration: " + duration);
        valueAnimator1.setDuration(duration);
        valueAnimator1.start();
    }

    public void setProgress(float progress) {
        animToProgress(progress);
    }
}