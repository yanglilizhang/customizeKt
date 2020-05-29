package com.tzt.customizekt.study.path;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.tzt.customizekt.study.base.BaseView2;

//波浪-
public class WaveView2 extends BaseView2 {

    private int mWaveLength; //波长
    private int mScreenHeight; //屏幕高
    private int mScreenWidth; //屏幕宽
    private int mCenterY; //Y轴上的重点
    private int mWaveCount; //屏幕上能显示完整波形的个数
    private int mOffset; //波形绘制的偏移量

    private ValueAnimator mValueAnimator; //改变 mOffSet的插值器
    private Paint mPaintBezier; //绘制波纹的画笔1
    private Path mPath; //绘制波纹的路径1


    public WaveView2(Context context) {
        this(context, null);
    }

    public WaveView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaintBezier = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBezier.setColor(Color.LTGRAY);
        mPaintBezier.setStrokeWidth(8);
        mPaintBezier.setStyle(Paint.Style.FILL_AND_STROKE);

        mWaveLength = 400;

        //点击View,开始动画
        mValueAnimator = ValueAnimator.ofInt(0, mWaveLength);
        mValueAnimator.setDuration(1000);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        //更新位移量
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mOffset = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mValueAnimator.start();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPath = new Path();

        mScreenHeight = h;
        mScreenWidth = w;
//        mCenterY = h / 2;
        mCenterY = 400;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }

}