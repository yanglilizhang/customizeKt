package com.tzt.customizekt.study.draw;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.tzt.customizekt.R;
import com.tzt.customizekt.study.base.BaseView;

public class ArcProgressView extends BaseView {

    private int arcBackgroundColor;    // 圆弧背景颜色
    private int arcProgressColor;      // 圆弧进度颜色
    private int arcSubTitleColor;      // 副标题颜色
    private float arcStrokeWidth;      // 圆弧线的厚度
    private float arcTitleTextSize;    // 标题文字大小
    private float arcSubTitleTextSize; // 副标题文字大小
    private float arcProgress;    //   进度
    private int arcTitleNumber;   // 值
    private Paint paint;
    private float centerX;
    private float centerY;
    private float radius;   // 半径
    private RectF rectF;

    private int startAngle = 135;
    private int sweepAngle = 270;
    private String subTitle = "1月份";
    private SpannableString spannableString;

    private TextPaint textPaint;
    private RelativeSizeSpan relativeSizeSpan;
    private DynamicLayout dynamicLayout;
    private String text = "11分";
    private StyleSpan styleSpan;


    private float curProgress;    // 当前进度
    private int curNumber;

    public ArcProgressView(Context context) {
        this(context, null);
    }

    public ArcProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttrs(context, attrs);
        init(context);
    }

    private void readAttrs(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ArcProgressView);
        arcBackgroundColor = typedArray.getColor(R.styleable.ArcProgressView_arcBackgroundColor, 0x1c979797);
        arcProgressColor = typedArray.getColor(R.styleable.ArcProgressView_arcProgressColor, 0xff3372FF);
        arcSubTitleColor = typedArray.getColor(R.styleable.ArcProgressView_arcSubTitleColor, 0x66000000);
        arcStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.ArcProgressView_arcStrokeWidth, dp2px(5));
        arcTitleTextSize = typedArray.getDimensionPixelSize(R.styleable.ArcProgressView_arcTitleTextSize, dp2px(30));
        arcSubTitleTextSize = typedArray.getDimensionPixelSize(R.styleable.ArcProgressView_arcSubTitleTextSize, dp2px(14));
        arcProgress = typedArray.getFloat(R.styleable.ArcProgressView_arcProgress, 1.0f);
        arcTitleNumber = typedArray.getInt(R.styleable.ArcProgressView_arcTitleNumber, 100);
        typedArray.recycle();
    }

    protected void init(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeCap(Paint.Cap.ROUND);

        relativeSizeSpan = new RelativeSizeSpan(0.6f);
        styleSpan = new StyleSpan(android.graphics.Typeface.BOLD);

        textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        textPaint.setColor(arcProgressColor);
//        textPaint.setTextAlign(Paint.Align.CENTER);   // 设置该属性导致文字间有间隔
        textPaint.setTextSize(sp2px(22));
    }

    //如果我们的一些用到的属性是跟View的大小变化相关的话，那么我们可以通过OnSizeChanged去进行监听
    //OnSizeChanged在layout方法中的setFrame执行时会被调用，
    //也就是说当我们调用requestLayout时可以通过OnSizeChanged去获取新的控件宽高等值
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2f;
        centerY = h / 2f;
        radius = (Math.min(w, h) - arcStrokeWidth) / 2f;//半径
        rectF = new RectF(-radius, -radius, radius, radius);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredSize(widthMeasureSpec, dp2px(100));
        int height = getMeasuredSize(heightMeasureSpec, dp2px(100));
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCoordinate(canvas);
        //画布已移动到view的中间(坐标轴中心),以后所有的操作都是在中心点(0,0)下
        canvas.translate(centerX, centerY);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        canvas.drawPoint(-100, 0, paint);//屏幕的左上角

        // 绘制圆弧和进度
        drawArc(canvas);
        // 绘制文字 title
        drawTitleText(canvas);
        // 绘制文字副标题
        drawSubTitle(canvas);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    private void startAnimation() {
        ValueAnimator progressAnimator = ValueAnimator.ofFloat(0f, arcProgress);
        ValueAnimator numberAnimator = ValueAnimator.ofInt(0, arcTitleNumber);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                curProgress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        numberAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                curNumber = (int) animation.getAnimatedValue();
                text = curNumber + "分";
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(progressAnimator, numberAnimator);
        animatorSet.setDuration(700);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();
    }

    private void drawSubTitle(Canvas canvas) {
        paint.setTextSize(arcSubTitleTextSize);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(arcSubTitleColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0);
        canvas.drawText(subTitle, 0, 60, paint);
    }

    private void drawArc(Canvas canvas) {
        paint.setColor(arcBackgroundColor);
        paint.setStrokeWidth(arcStrokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        //底部
        canvas.drawArc(rectF, startAngle, sweepAngle, true, paint);
        paint.setColor(arcProgressColor);
        canvas.drawArc(rectF, startAngle, sweepAngle * curProgress, false, paint);
    }


    private void drawTitleText(Canvas canvas) {
        canvas.save();
        textPaint.setTextSize(arcTitleTextSize);
        float textWidth = textPaint.measureText(text);   // 文字宽度
        float textHeight = -textPaint.ascent() + textPaint.descent();  // 文字高度
        // 由于 StaticLayout 绘制文字时，默认画在Canvas的(0,0)点位置，所以居中绘制居中位置，需要将画布 translate到中间位置。
//        canvas.translate(centerX - textWidth * 2 / 5f, centerY - textHeight * 2 / 3f);
        canvas.translate( 0,  0);
        spannableString = SpannableString.valueOf(text);
        spannableString.setSpan(styleSpan, 0, text.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(relativeSizeSpan, text.length() - 1, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        dynamicLayout = new DynamicLayout(spannableString, textPaint, getWidth(), Layout.Alignment.ALIGN_NORMAL, 0, 0, false);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            DynamicLayout.Builder.obtain(spannableString,textPaint,200);
//        }
        dynamicLayout.draw(canvas);
//        canvas.drawText("sssssss",0,0,textPaint);
        canvas.restore();
    }


    /**
     * 对外提供方法，设置进度
     *
     * @param percent
     */
    public void setArcProgress(float percent) {
        this.curProgress = percent;
        invalidate();
    }


    private int getMeasuredSize(int measureSpec, int defvalue) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            return size;
        }
        return Math.min(size, defvalue);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }
}


