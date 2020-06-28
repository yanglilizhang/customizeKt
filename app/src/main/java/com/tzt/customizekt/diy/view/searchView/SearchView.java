package com.tzt.customizekt.diy.view.searchView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

/**
 * 动态加载的搜索按钮
 */
public class SearchView extends View {
    // 状态枚举
    /**
     * 初始状态
     */
    public static final int STATE_NONE = 0;
    /**
     * 正在开始状态
     */
    public static final int STATE_STARTING = 1;
    /**
     * 加载中状态
     */
    public static final int STATE_LOADING = 2;
    /**
     * 正在结束状态
     */
    public static final int STATE_ENDING = 3;

    /**
     * 当前状态
     */
    int state = STATE_NONE;

    /**
     * 正在开始状态比例 0~1
     */
    float startingFraction = 0;
    /**
     * 加载中状态比例 0~1
     */
    float loadingFraction = 0;
    /**
     * 正在结束状态 0~1
     */
    float endingFraction = 0;

    /**
     * 内半径
     */
    int in_cr = 40;
    /**
     * 外半径
     */
    int out_cr = in_cr * 3;
    /**
     * 圆心
     */
    private int cx;
    private int cy;

    Paint mPaint;
    Paint mTextPaint;
    Path mPath = null;
    PathMeasure measure = new PathMeasure();
    Path dstPath = new Path();
    String loadingText = "搜索中...";
    private ValueAnimator startAnimator;
    private ValueAnimator loadAnimator;
    private ValueAnimator endAnimator;

    private RectF rectF = new RectF();

    public SearchView(Context context) {
        super(context);
        init();
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(12);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(25);

        mPath = new Path();

    }

    private void prepareOuterPath() {
        mPath.reset();
        mPath.addArc(new RectF(cx - out_cr, cy - out_cr, cx + out_cr, cy + out_cr),
                0,
                360);
        measure.setPath(mPath, false);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        drawEnding(canvas);
        switch (state) {
            case STATE_NONE:
                drawDefault(canvas);
                break;
            case STATE_STARTING:
                drawStarting(canvas);
                break;
            case STATE_LOADING:
                drawLoading(canvas);
                break;
            case STATE_ENDING:
                drawEnding(canvas);
                break;
        }
    }

    private void drawDefault(Canvas canvas) {
        // 画正常的搜索图标
        canvas.save();
        cx = getWidth() / 2;
        cy = getHeight() / 2;
        mPath.moveTo(cx + in_cr, cy);
        rectF.set(cx - in_cr, cy - in_cr, cx + in_cr, cy + in_cr);
        mPath.addArc(rectF, 0, 360);
        mPath.lineTo(cx + in_cr * 2, cy);
        canvas.rotate(45, cx, cy);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
    }

    //准备搜索
    private void drawStarting(Canvas canvas) {
//        measure.setPath(mPath, false);
//        float length = measure.getLength();
//        Log.e("SearchView", "Faction:" + startingFraction);
//        dstPath.reset();
//        measure.getSegment(length * startingFraction, length, dstPath, true);
//        canvas.drawPath(dstPath, mPaint);

        Log.e("SearchView", "startingFraction:" + startingFraction + ",startAngle:" + 360 * startingFraction);
        // 画正常的搜索图标
        canvas.save();
        cx = getWidth() / 2;
        cy = getHeight() / 2;
        mPath.reset();
        mPath.moveTo(cx + in_cr, cy);
        rectF.set(cx - in_cr, cy - in_cr, cx + in_cr, cy + in_cr);
        //顺时针画弧
        mPath.addArc(rectF, 360 * startingFraction, 360 - 360 * startingFraction);
        mPath.lineTo(cx + in_cr * 2, cy);
        canvas.rotate(45, cx, cy);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
    }

    private void drawLoading(Canvas canvas) {
        prepareOuterPath();
        float length = measure.getLength();
        float startD = length * loadingFraction;
        float stopD = startD + length / 12;
        dstPath.reset();
        measure.getSegment(startD, stopD, dstPath, true);
        canvas.drawPath(dstPath, mPaint);

        // 画文字
        float textWidth = mTextPaint.measureText(loadingText);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float baseY = cy + ((fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);
        canvas.drawText(loadingText, cx - textWidth / 2, baseY, mTextPaint);
    }

    private void drawEnding(Canvas canvas) {
        Log.e("SearchView", "endingFraction:" + endingFraction + ",endAngle:" + 360 * (1 - endingFraction));
        // 画正常的搜索图标
        canvas.save();
        cx = getWidth() / 2;
        cy = getHeight() / 2;
        mPath.reset();
        mPath.moveTo(cx + in_cr, cy);
        rectF.set(cx - in_cr, cy - in_cr, cx + in_cr, cy + in_cr);
        // 逆时针画弧
        mPath.addArc(rectF, 360 * (1 - endingFraction), 360 * endingFraction);
        mPath.lineTo(cx + in_cr * 2, cy);
        canvas.rotate(45, cx, cy);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
    }

    /**
     * 开始动画
     */
    private void startAnimation() {
        if (startAnimator == null) {
            startAnimator = ValueAnimator.ofFloat(0, 1);
            startAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    startingFraction = (float) animation.getAnimatedValue();
                    postInvalidate();
                    if (startingFraction == 1) {
                        // 切换状态到 正在加载状态
                        SearchView.this.state = STATE_LOADING;
                        startLoadingAnimation();
                        // 重置
                        resetParams();
                    }
                }
            });
            startAnimator.setDuration(1000);
        }
        startAnimator.start();
    }

    /**
     * 正在加载状态的动画
     */
    private void startLoadingAnimation() {
        if (loadAnimator == null) {
            loadAnimator = ValueAnimator.ofFloat(0, 1);
            loadAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    loadingFraction = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            loadAnimator.setDuration(2000);
            loadAnimator.setRepeatCount(ValueAnimator.INFINITE);
        }
        loadAnimator.start();
    }

    /**
     * 结束动画
     */
    private void endingAnimation() {
        if (loadAnimator != null && loadAnimator.isRunning()) {
            loadAnimator.cancel();
        }
        if (endAnimator == null) {
            endAnimator = ValueAnimator.ofFloat(0, 1);
            endAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    endingFraction = (float) animation.getAnimatedValue();
                    postInvalidate();
                    if (endingFraction == 1) {
                        // 切换状态到 正在加载状态
                        SearchView.this.state = STATE_NONE;
                        postInvalidate();
                        // 重置
                        resetParams();
                    }
                }
            });
            endAnimator.setDuration(1000);
        }
        endAnimator.start();
    }

    private void resetParams() {
        startingFraction = 0;
        loadingFraction = 0;
        endingFraction = 0;
    }


    public void start() {
        this.state = STATE_STARTING;
        startAnimation();
    }

    public void stop() {
        this.state = STATE_ENDING;
        endingAnimation();
    }

    public int getState() {
        return state;
    }
}