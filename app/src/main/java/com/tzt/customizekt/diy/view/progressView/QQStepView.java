package com.tzt.customizekt.diy.view.progressView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.tzt.customizekt.R;


public class QQStepView extends View {
    private int mOutColor = Color.RED;
    private int mInnerColor = Color.BLUE;
    private int mBorderWidth = 20;
    private int mStepTextSize = 15;
    private int mStepTextColor = Color.RED;
    private Paint mOutPaint;
    private Paint mInnerPaint;
    private Paint mTextPaint;

    private int mStepMax = 0;//总共的步数
    private int mCurrentStep = 0;//当前的步数

    public QQStepView(Context context) {
        this(context, null);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * 1.分析效果
         * 2.确定自定义属性，编写attr.xml
         * 3.在布局中使用
         * 4.在自定义View中获取自定义属性
         * 5.onMeasure()
         * 6.圆外圆弧。内圆弧，文字
         * 7.其他
         */
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.QQStepView);
        mOutColor = array.getColor(R.styleable.QQStepView_outerColor, mOutColor);
        mInnerColor = array.getColor(R.styleable.QQStepView_innerColor, mInnerColor);
        mBorderWidth = (int) array.getDimension(R.styleable.QQStepView_borderWidth, mBorderWidth);
        mStepTextSize = array.getDimensionPixelSize(R.styleable.QQStepView_stepTextSize, mStepTextSize);
        mStepTextColor = array.getColor(R.styleable.QQStepView_stepTextColor, mStepTextColor);
        array.recycle();

        mOutPaint = new Paint();
        mOutPaint.setAntiAlias(true);
        mOutPaint.setColor(mOutColor);
        mOutPaint.setStrokeWidth(mBorderWidth);
        mOutPaint.setStrokeCap(Paint.Cap.ROUND);
        mOutPaint.setStyle(Paint.Style.STROKE);

        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStrokeWidth(mBorderWidth);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);
        mInnerPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mStepTextColor);
        mTextPaint.setTextSize(mStepTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //宽高不一致时，取最小值，使View始终是正方形
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //保证他是正方形
        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }

    @SuppressLint("DrawAllocation")
    //6.圆外圆弧。内圆弧，文字
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //6.1 画外圆弧
        //圆弧闭合，描边有宽度
        int center = getWidth() / 2;//中心点
        int radius = center - mBorderWidth / 2;//半径
        //圆弧的矩形确认
        RectF rectF = new RectF(center - radius, center - radius,
                center + radius, center + radius);
        canvas.drawArc(rectF, 135, 270, false, mOutPaint);

        if (mStepMax == 0)
            return;
        //6.2 画内圆弧,不能写死，百分比，使用者设置，值是外面传过来的
        float sweepAngle = (float) mCurrentStep / mStepMax;
        canvas.drawArc(rectF, 135, sweepAngle * 270, false, mInnerPaint);
        //6.3 画文字
        String stepText = mCurrentStep + "";
        Rect textBounds = new Rect();
        mTextPaint.getTextBounds(stepText, 0, stepText.length(), textBounds);
        int dx = (getWidth() - textBounds.width()) / 2;
        //计算baseLine
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int baseLine = getHeight() / 2 + dy;

        canvas.drawText(stepText, dx, baseLine, mTextPaint);
    }

    //7.其他，动起来
    //设置最大步数
    public synchronized void setStepMax(int stepMax) {
        this.mStepMax = stepMax;
    }

    public int getStepMax() {
        return mStepMax;
    }

    //设置当前步数
    public synchronized void setCurrentStep(int mCurrentStep) {
        this.mCurrentStep = mCurrentStep;
        //不断绘制
        invalidate();
    }

    public int getCurrentStep() {
        return mCurrentStep;
    }
}