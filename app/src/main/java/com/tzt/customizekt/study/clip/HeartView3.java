package com.tzt.customizekt.study.clip;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.tzt.customizekt.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : zinc
 * time         : 2019/4/20 上午10:05
 * desc         :
 * version      :
 */
public class HeartView3 extends View {

    private static final String SHOW_CONTENT = "猛猛的小盆友";

    private Paint mPaint;

    private Path mHeartPath;

    private RectF mHeartRect;

    private int mTextSize;

    private float mCurPos;

    private ObjectAnimator mUpAnim;

    private ValueAnimator mBezierAnim;

    private AnimatorSet mAnimSet;

    private int mTopBgColor;
    private int mBottomBgColor;

    // 当前的偏移量
    private int mCurOffset = 0;
    // 每次的偏移量
    private int mOffset;

    private Path mTopPath;
    private Path mBottomPath;

    private int mBezierHeight;

    public HeartView3(Context context) {
        this(context, null);
    }

    public HeartView3(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // 初始化变量
        mTextSize = dpToPx(16);
        mOffset = dpToPx(2.5f);
        mBezierHeight = dpToPx(10);

        mTopBgColor = ContextCompat.getColor(context, R.color.canvas_pink_color);
        mBottomBgColor = ContextCompat.getColor(context, R.color.canvas_light_blue_color);

        // 初始化对象
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mHeartRect = new RectF();
        mTopPath = new Path();
        mBottomPath = new Path();

        // 初始化心形路径
        mHeartPath = new Path();
        createHeart(mHeartPath);
        mCurPos = mHeartRect.bottom;

        // 贝塞尔曲线动画
        mBezierAnim = ValueAnimator.ofFloat(0, 1f);
        mBezierAnim.setDuration(4_000);
        mBezierAnim.setRepeatCount(ValueAnimator.INFINITE);
        mBezierAnim.setInterpolator(new LinearInterpolator());
        mBezierAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurOffset = (mCurOffset + mOffset) % (int) mWidth;
//                mCurOffset = 10;
            }
        });

        // 初始化 增加的动画
        mUpAnim = ObjectAnimator.ofFloat(this, "process", 0, 1);
        mUpAnim.setInterpolator(new LinearInterpolator());
        mUpAnim.setDuration(6_000);
    }

    /**
     * 构建心形
     */
    private void createHeart(Path path) {
        List<PointF> pointList = new ArrayList<>();
        pointList.add(new PointF(0, dpToPx(-38)));
        pointList.add(new PointF(dpToPx(50), dpToPx(-103)));
        pointList.add(new PointF(dpToPx(112), dpToPx(-61)));
        pointList.add(new PointF(dpToPx(112), dpToPx(-12)));
        pointList.add(new PointF(dpToPx(112), dpToPx(37)));
        pointList.add(new PointF(dpToPx(51), dpToPx(90)));
        pointList.add(new PointF(0, dpToPx(129)));
        pointList.add(new PointF(dpToPx(-51), dpToPx(90)));
        pointList.add(new PointF(dpToPx(-112), dpToPx(37)));
        pointList.add(new PointF(dpToPx(-112), dpToPx(-12)));
        pointList.add(new PointF(dpToPx(-112), dpToPx(-61)));
        pointList.add(new PointF(dpToPx(-50), dpToPx(-103)));

        path.reset();
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                path.moveTo(pointList.get(0).x, pointList.get(0).y);
            } else {
                path.lineTo(pointList.get(i * 3).x, pointList.get(i * 3).y);
            }

            int endPointIndex;
            if (i == 3) {
                endPointIndex = 0;
            } else {
                endPointIndex = i * 3 + 3;
            }

            path.cubicTo(pointList.get(i * 3 + 1).x, pointList.get(i * 3 + 1).y,
                    pointList.get(i * 3 + 2).x, pointList.get(i * 3 + 2).y,
                    pointList.get(endPointIndex).x, pointList.get(endPointIndex).y);
        }
        path.close();

        //计算Path的边界
        path.computeBounds(mHeartRect, false);
    }

    public void start() {
        if (mAnimSet != null) {
            mAnimSet.cancel();
        }

        mAnimSet = new AnimatorSet();
        mAnimSet.play(mUpAnim)
                .with(mBezierAnim);
        mAnimSet.start();

    }

    /**
     * 当前进度
     *
     * @param process 范围 [0-1]
     */
    public void setProcess(float process) {
        // 如果心形的rect为空，直接中止
        if (mHeartRect.isEmpty()) {
            if (mUpAnim != null) {
                mUpAnim.cancel();
            }

            return;
        }

        float curHeight = mHeartRect.height() * process;

        mCurPos = mHeartRect.bottom - curHeight;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        //波浪的长度
        int waveLength = width / 3;
        //总长！！！
        int totalLength = width * 2 + waveLength;

        // 初始化 浪的路径
        initPath(mTopPath,//路径
                waveLength,//曲线的宽度
                mBezierHeight,//贝塞尔曲线的高度
                totalLength,//总长度
                width,//屏幕宽
                height,//屏幕高
                true);//是否为上部份

        // 初始化 浪的路径
        initPath(mBottomPath, waveLength,
                mBezierHeight,
                totalLength,
                width,
                height,
                false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        canvas.translate(width / 2, height / 2);

        canvas.drawText("999999", 0, 0, mPaint);

        //裁剪心形区域
        canvas.clipPath(mHeartPath);

        //下半部分心
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        //其实是一个二阶线 裁剪
        canvas.drawPath(mBottomPath, mPaint);
//        canvas.drawPath(mTopPath, mPaint);


    }

    private float mWidth;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
    }

    /**
     * @param path         路径
     * @param length       曲线的宽度
     * @param height       贝塞尔曲线的高度
     * @param totalLength  总长度
     * @param screenWidth  屏幕宽
     * @param screenHeight 屏幕高
     * @param isTop        是否为上部份
     */
    private void initPath(Path path,
                          int length,
                          int height,
                          int totalLength,
                          int screenWidth,
                          int screenHeight,
                          boolean isTop) {

        int left;
        int right;
        int top;
        int bottom;

        if (isTop) {
            //上半部分
            left = -length - screenWidth / 2;
            right = screenWidth + screenWidth / 2;
            top = -screenHeight;
            bottom = 0;
            path.moveTo(left, bottom);
        } else {
            //下半部分
            left = -length - screenWidth / 2;
            right = screenWidth + screenWidth / 2;
            top = 0;
            bottom = screenHeight;
            path.moveTo(left, top);
        }

        for (int i = -length; i < totalLength; i += length) {
            // rQuadTo 和 quadTo 区别在于
            // rQuadTo 是相对上一个点 而 quadTo是相对于画布
            path.rQuadTo(length / 4,
                    -height,
                    length / 2,
                    0);
            path.rQuadTo(length / 4,
                    height,
                    length / 2,
                    0);
        }

        if (isTop) {
            path.lineTo(right, top);
            path.lineTo(left, top);
        } else {
            path.lineTo(right, bottom);
            path.lineTo(left, bottom);
        }

        path.close();
    }


    private void drawText(Canvas canvas, int textColor) {
        mPaint.setColor(textColor);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mTextSize);
        mPaint.setTypeface(Typeface.SANS_SERIF);

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float y = fontMetrics.bottom;

        canvas.drawText(SHOW_CONTENT, 0, y, mPaint);
    }

    /**
     * 转换 dp 至 px
     *
     * @param dpValue dp值
     * @return px值
     */
    protected int dpToPx(float dpValue) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dpValue * metrics.density + 0.5f);
    }
}