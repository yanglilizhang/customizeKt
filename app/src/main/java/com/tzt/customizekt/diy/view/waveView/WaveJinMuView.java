package com.tzt.customizekt.diy.view.waveView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.tzt.customizekt.study.base.BaseView;
import com.tzt.customizekt.study.base.BaseView2;

/**
 * 金姆太极波浪自定义view
 * <p>
 * author: DragonForest
 * time: 2020/1/13
 */
public class WaveJinMuView extends BaseView {
    int cr = 300;

    Path leftPath;
    Path rightPath;
    Path wavePath;
    int leftColor = Color.RED;
    int rightColor = Color.BLUE;
    int textColor = Color.WHITE;
    int textSize = 25;
    Paint mPaint;
    Paint contentPaint;
    private int cx;
    private int cy;

    // 半个波长
    int halfWaveWidth = 150;
    // 波峰的高度
    int waveHeight = 30;
    float startX = -halfWaveWidth;
    float leftProgress = 0.3f;  // 0~1
    float rightProgress = 0.5f; // 0~1

    private RectF rectF = new RectF();


    public WaveJinMuView(Context context) {
        super(context);
        init();
    }

    public WaveJinMuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void init(Context context) {
        init();
    }


    private void init() {
        leftPath = new Path();
        rightPath = new Path();
        wavePath = new Path();

        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);

        contentPaint = new Paint();
        contentPaint.setColor(Color.BLUE);
        contentPaint.setAntiAlias(true);
        contentPaint.setTextSize(textSize);
        contentPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCoordinate(canvas);
        cx = getWidth() / 2;
        cy = getHeight() / 2;
        drawLeft(canvas);
        drawRight(canvas);
    }

    private void drawLeft(Canvas canvas) {
        canvas.save();
        leftPath.reset();
        leftPath.moveTo(cx, cy);
        rectF.set(cx - cr, cy - cr, cx + cr, cy + cr);
        leftPath.arcTo(rectF, 90, 180, true);
        //贝塞尔曲线quadTo
        //mPath.quadTo(x1, y1, x2, y2) (x1,y1) 为控制点，(x2,y2)为结束点。
        //上
        leftPath.quadTo((cx - cr + cx) / 2, (cy - cr + cy) / 2, cx, cy);
//        leftPath.quadTo(( cx) / 2, ( cy) / 2, cx, cy);
        //下
        leftPath.quadTo((cx + cr + cx) / 2, (cy + cr + cy) / 2, cx, cy + cr);
        canvas.drawPath(leftPath, mPaint);
        canvas.clipPath(leftPath);
        canvas.drawColor(Color.GRAY);
        // 使用相对贝塞尔曲线画出横向波形
        wavePath.reset();
        wavePath.moveTo(startX, getHeight() * (1 - leftProgress));
        for (int i = -halfWaveWidth * 2; i < getWidth() + halfWaveWidth * 2; i += halfWaveWidth * 2) {
            //每循环一次画一次完整的波浪
            wavePath.rQuadTo(halfWaveWidth / 2, waveHeight, halfWaveWidth, 0);
            wavePath.rQuadTo(halfWaveWidth / 2, -waveHeight, halfWaveWidth, 0);
        }
        //闭合
        wavePath.lineTo(getWidth(), getHeight());
        wavePath.lineTo(0, getHeight());
        wavePath.close();
        contentPaint.setColor(leftColor);
        canvas.drawPath(wavePath, contentPaint);
        // 画文字
        contentPaint.setColor(textColor);
        String text = Math.round(leftProgress * 100) + "%";
        float textWidth = mPaint.measureText(text);
        canvas.drawText(text, (cx + cx - cr) / 2 - textWidth / 2, cy, contentPaint);
        canvas.restore();
    }

    private void drawRight(Canvas canvas) {
        canvas.save();
        rightPath.reset();
        rightPath.moveTo(cx, cy);
        rectF.set(cx - cr, cy - cr, cx + cr, cy + cr);
        rightPath.arcTo(rectF, -90, 180, true);
        rightPath.quadTo((cx + cr + cx) / 2, (cy + cr + cy) / 2, cx, cy);
        rightPath.quadTo((cx - cr + cx) / 2, (cy - cr + cy) / 2, cx, cy - cr);
        canvas.drawPath(rightPath, mPaint);
        canvas.clipPath(rightPath);
        canvas.drawColor(Color.GRAY);
        rightPath.close();
        // 使用相对贝塞尔曲线画出横向波形
        wavePath.reset();
        wavePath.moveTo(startX, getHeight() * (1 - rightProgress));
        for (int i = -halfWaveWidth * 2; i < getWidth() + halfWaveWidth * 2; i += halfWaveWidth * 2) {
            wavePath.rQuadTo(halfWaveWidth / 2, waveHeight, halfWaveWidth, 0);
            wavePath.rQuadTo(halfWaveWidth / 2, -waveHeight, halfWaveWidth, 0);
        }
        wavePath.lineTo(getWidth(), getHeight());
        wavePath.lineTo(0, getHeight());
        wavePath.close();
        contentPaint.setColor(rightColor);
        canvas.drawPath(wavePath, contentPaint);
        // 画文字
        contentPaint.setColor(textColor);
        String text = Math.round(rightProgress * 100) + "%";
        float textWidth = mPaint.measureText(text);
        canvas.drawText(text, (cx + cx + cr) / 2 - textWidth / 2, cy, contentPaint);
        canvas.restore();
    }

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
        valueAnimator.setDuration(3000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }

    private void animLeftToProgress(float newProgress) {
        // 垂直方向的动画 上升
        ValueAnimator valueAnimator1 = ValueAnimator.ofFloat(leftProgress, newProgress);
        valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                leftProgress = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator1.setDuration(500);
        valueAnimator1.start();
    }

    private void animRightToProgress(float newProgress) {
        // 垂直方向的动画 上升
        ValueAnimator valueAnimator1 = ValueAnimator.ofFloat(rightProgress, newProgress);
        valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rightProgress = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator1.setDuration(500);
        valueAnimator1.start();
    }

    public void setLeftProgress(float newLeftProgress) {
        this.leftProgress = newLeftProgress;
        animLeftToProgress(newLeftProgress);
    }

    public void setRightProgress(float newRightProgress) {
        this.rightProgress = newRightProgress;
        animRightToProgress(newRightProgress);
    }
}