package com.tzt.customizekt.diy.view.waveView;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

/**
 * 水波纹效果 圆形
 */
public class WaveView4 extends View {
    Paint mPaint;
    Path mPath;
    Path circlePath;

    // 半个波长
    int halfWaveWidth = 150;
    // 波峰的高度
    int waveHeight = 50;
    // 初始点坐标
    float startX = -halfWaveWidth;
    float startY = Integer.MAX_VALUE;

    // 剩余电量 1~100
    float progress = 0;

    public WaveView4(Context context) {
        super(context);
        init();
    }

    public WaveView4(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPath = new Path();
        circlePath = new Path();
        setProgress(80);
    }

    private void initWaveStyle() {
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);
        if (progress < 20) {
            mPaint.setColor(Color.RED);
        }
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.FILL);
    }

    private void initTextStyle() {
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        if (progress < 20) {
            mPaint.setColor(Color.RED);
        }
        mPaint.setStrokeWidth(3);
        mPaint.setTextSize(30);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 裁剪画布 圆形
        canvas.save();
        float r = Math.min(getWidth(), getHeight()) / 2;
        circlePath.addCircle(getWidth() / 2, getHeight() / 2, r, Path.Direction.CCW);
        canvas.clipPath(circlePath);
        canvas.drawColor(Color.GREEN);
        // 画波形
        drawWave(canvas);
        // 画文字
        drawText(canvas);

        // 还原画布
        canvas.restore();
    }

    private void drawWave(Canvas canvas) {
        mPath.reset();
        initWaveStyle();
        // 起始点
        mPath.moveTo(startX, startY);
        // 使用相对贝塞尔曲线画出横向波形
        for (int i = -halfWaveWidth * 2; i < getWidth() + halfWaveWidth * 2; i += halfWaveWidth * 2) {
            //每循环一次画一次完整的波浪
            mPath.rQuadTo(halfWaveWidth / 2, waveHeight, halfWaveWidth, 0);
            mPath.rQuadTo(halfWaveWidth / 2, -waveHeight, halfWaveWidth, 0);
        }

        // 画闭合空间 填充颜色
        mPath.lineTo(getWidth(), getHeight());
        mPath.lineTo(0, getHeight());
        mPath.close();

        canvas.drawPath(mPath, mPaint);
    }

    private void drawText(Canvas canvas) {
        initTextStyle();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        String text = "剩余电量" + progress + "%";
        if (progress < 20) {
            text = "剩余电量" + progress + "%,电量过低！";
        }
        float textWidth = mPaint.measureText(text);
        float textLeftX = centerX - textWidth / 2;

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float textBaseLineY = centerY + ((fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);
        canvas.drawText(text, textLeftX, textBaseLineY, mPaint);
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
        // 垂直方向的动画 上升
        ValueAnimator valueAnimator1 = ValueAnimator.ofFloat(progress, newProgress);
        valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (float) animation.getAnimatedValue();
                startY = (float) WaveView4.this.getHeight() * (100 - progress) / 100;
                postInvalidate();
            }
        });
        valueAnimator1.setDuration((long) Math.abs(newProgress - progress) * 100);
        valueAnimator1.start();
    }

    public void setProgress(float progress) {
        animToProgress(progress);
    }
}