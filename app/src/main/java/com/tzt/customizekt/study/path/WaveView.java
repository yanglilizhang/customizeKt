package com.tzt.customizekt.study.path;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.tzt.customizekt.study.base.BaseView2;

//波浪-https://blog.csdn.net/qq_30379689/article/details/53098481
public class WaveView extends BaseView2 {

    private int mWaveLength; //波长
    private int mScreenHeight; //屏幕高
    private int mScreenWidth; //屏幕宽
    private int mCenterY; //Y轴上的重点
    private int mWaveCount; //屏幕上能显示完整波形的个数
    private int mOffset; //波形绘制的偏移量

    private ValueAnimator mValueAnimator; //改变 mOffSet的插值器
    private Paint mPaintBezier; //绘制波纹的画笔1
    private Path mPath; //绘制波纹的路径1


    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

        //计算需要绘制几个完整的波形，注意需要多绘制一个完整的波形用来位移，多出来0.5是防止被四舍五入
        mWaveCount = (int) Math.round(mScreenWidth / mWaveLength + 1.5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        //位移到屏幕外左侧一个波长的地方，开始绘制水波 mCenterY是Y轴方向上的中线。
        mPath.moveTo(-mWaveLength + mOffset, mCenterY);
        //利用两个Bezier曲线绘制出水波
//        //上方弧
//        mPath.quadTo(mWaveLength / 4, mCenterY - 80, mWaveLength / 2, mCenterY);
//        //下方弧
//        mPath.quadTo(mWaveLength * 3 / 4, mCenterY + 80, mWaveLength, mCenterY);
        for (int i = 0; i < mWaveCount; i++) {
            int totalOffSet = i * mWaveLength + mOffset;
            //控制点在波峰处上方
            mPath.quadTo(-mWaveLength * 3 / 4 + totalOffSet, mCenterY - 80, -mWaveLength / 2 + totalOffSet, mCenterY);
            //控制点在波谷处下方
            mPath.quadTo(-mWaveLength / 4 + totalOffSet, mCenterY + 80, totalOffSet, mCenterY);
        }

        //闭合图象，并填充
//        mPath.lineTo(mScreenWidth, mScreenHeight);
//        mPath.lineTo(0, mScreenHeight);
//        mPath.close();
        canvas.drawPath(mPath, mPaintBezier);
    }

    // android.graphics.Path
// quadTo为二次贝塞尔曲线，x1,y1点为控制点，x2,y2为结束点
//    public void quadTo(float x1, float y1, float x2, float y2) ;

    // cubicTo为三次贝塞尔曲线，x1,y1点和x2,y2点为控制点,x3,y3为结束点
//    public void cubicTo(float x1, float y1, float x2, float y2,float x3, float y3);

}