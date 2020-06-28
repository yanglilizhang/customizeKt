package com.tzt.customizekt.study.path.path_measure;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;

import com.tzt.customizekt.study.base.BaseView;


/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/4
 * @description 加载圈
 */

public class LoadingView extends BaseView {
    private Path mPath;
    private Paint mPaint;
    private PathMeasure mPathMeasure;
    private float mAnimatorValue;
    private Path mDst;
    private float mLength;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPath = new Path();
        mPath.addCircle(0, 0, 100, Path.Direction.CW);

        mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(mPath, true);
        mLength = mPathMeasure.getLength();

        mDst = new Path();

        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinate(canvas);
        //translate(mWidth / 2, mHeight / 2)执行以后画布中心点（0，0）就移动到屏幕中心
        canvas.translate(mWidth / 2, mHeight / 2);

        // 需要重置，否则受上次影响，因为getSegment方法是添加而非替换
        mDst.reset();
        // 4.4版本以及之前的版本，需要使用这行代码，否则getSegment无效果
        // 导致这个原因是 硬件加速问题导致
        mDst.lineTo(0, 0);

        float stop = mLength * mAnimatorValue;
        float start = (float) (stop - ((0.5 - Math.abs(mAnimatorValue - 0.5)) * mLength));
        //第一个参数 startD： 截取的路径的起始点距离path起始点的长度，取值范围：0<=startD<stopD<=Path.getLength()；
        //第二个参数 stopD： 截取的路径的终止点距离path起始点的长度，取值范围：0<=startD<stopD<=Path.getLength()；
        // 第三个参数 dst： 截取的路径保存的地方，此处特别注意截取的路径是添加到dst中，而非替换；
        //第四个参数 startWithMoveTo： 截取的片段的第一个点是否保持不变； 设置为true：保持截取的片段不变，添加至dst路径中；
        // 设置为false：会将截取的片段的起始点移至dst路径中的最后一个点，让dst路径保持连续
        mPathMeasure.getSegment(start, stop, mDst, true);

        canvas.drawPath(mDst, mPaint);


//        值得一提 如果你在4.4或更早的版本使用在使用这个函数时，需要先调用一下 mDst.lineTo(0, 0);
//        这句代码，这是因为硬件加速导致的问题；如不调用，会导致没有任何效果。
    }
}