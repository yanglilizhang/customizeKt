package com.tzt.customizekt.study.clockView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;


import com.tzt.customizekt.R;
import com.tzt.customizekt.study.utils.DensityUtil;

import java.util.Calendar;


public class MyView extends View {

    private String TAG = MyView.class.getSimpleName();
    private String brandText;
    private float brandTextSize;
    private int brandTextColor;
    private int hourHandColor;
    private int minuteHandColor;
    private int secondHandColor;

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private Paint paint;
    private Paint textPain;

    private void initPaint() {
        //初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);


        textPain = new Paint();
        paint.setColor(Color.BLACK);
        textPain.setTextAlign(Paint.Align.CENTER);
    }

    private void init(AttributeSet attrs) {
        //获取自定义属性集
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MyView);
        //通过 TypedArray 获取每一个自定义属性值
        brandText = typedArray.getString(R.styleable.MyView_brand_text);
        brandTextSize = typedArray.getDimension(R.styleable.MyView_brand_text_size, 0);
        brandTextColor = typedArray.getColor(R.styleable.MyView_brand_text_color, Color.BLACK);
        hourHandColor = typedArray.getColor(R.styleable.MyView_hour_hand_color, Color.BLACK);
        minuteHandColor = typedArray.getColor(R.styleable.MyView_minute_hand_color, Color.BLACK);
        secondHandColor = typedArray.getColor(R.styleable.MyView_second_hand_color, Color.BLACK);
        typedArray.recycle();//回收资源,释放内存
        initPaint();
    }

    /**
     * 测量View的宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specModeWidth = MeasureSpec.getMode(widthMeasureSpec);//得到宽度的布局模式
        int specSizeWidth = MeasureSpec.getSize(widthMeasureSpec);//得到布局参数的宽度
        int resultWidth = specSizeWidth;
        switch (specModeWidth) {
            case MeasureSpec.EXACTLY:// 宽高的值是固定的时候,如:match_parent 或 "22dp"
                resultWidth = specSizeWidth;
                break;
            case MeasureSpec.AT_MOST:// 你的宽高的值 为 wrap_content 的时候
                resultWidth = DensityUtil.dip2px(getContext(), 80);
                break;
        }

        int specModeHeight = MeasureSpec.getMode(heightMeasureSpec);//得到高度的布局模式
        int specSizeHeight = MeasureSpec.getSize(heightMeasureSpec);//得到布局参数的高度
        int resultHeight = specSizeHeight;
        switch (specModeHeight) {
            case MeasureSpec.EXACTLY:// 宽高的值是固定的时候,如:match_parent 或 "22dp"
                resultHeight = specSizeHeight;
                break;
            case MeasureSpec.AT_MOST:// 你的宽高的值 为 wrap_content 的时候
                resultHeight = DensityUtil.dip2px(getContext(), 80);
                break;
        }
        //调用该方法,最终确定该View的宽高
        setMeasuredDimension(resultWidth, resultHeight);
    }

    /**
     * 绘制 自身this的内容,内容将绘制在 android:background 之上.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        calculateHourMinuteSecond();

        int measuredWidth = getWidth();//获取测量的宽度
        int measuredHeight = getHeight();//获取测量的高度

        int cx = measuredWidth / 2;
        int cy = measuredHeight / 2;

        int size = Math.min(measuredWidth, measuredHeight);//以较小的边作为绘制时针的正方形边长

        int frameWidth = 20;//时钟边框的粗细

        int r = size / 2 - frameWidth / 2;

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(frameWidth);
        canvas.drawCircle(cx, cy, r, paint);
        //画时针
        canvas.save();
        canvas.rotate(hourDegrees, cx, cy);
        paint.setColor(hourHandColor);
        paint.setStrokeWidth(10);
        canvas.drawLine(cx, cy, cx, cy - r / 2, paint);
        canvas.restore();
        //画分针
        canvas.save();
        canvas.rotate(minuteDegrees, cx, cy);
        paint.setColor(minuteHandColor);
        paint.setStrokeWidth(10);
        canvas.drawLine(cx, cy, cx, cy - r * 3 / 5, paint);
        canvas.restore();
        //画秒针
        canvas.save();
        canvas.rotate(secondDegrees, cx, cy);
        paint.setColor(secondHandColor);
        paint.setStrokeWidth(5);
        canvas.drawLine(cx, cy, cx, cy - r * 4 / 5, paint);
        canvas.restore();

        for (int i = 1; i <= 12; i++) {
            canvas.save();
            canvas.rotate(30 * i, cx, cy);
            int textSize = DensityUtil.sp2px(getContext(), 15);
            textPain.setTextSize(textSize);
            canvas.drawText(i + "", cx, cy - r + textSize, textPain);
            canvas.restore();
        }


        postInvalidateDelayed(1000);
    }


    private void calculateHourMinuteSecond() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);//7
        int minute = c.get(Calendar.MINUTE);//0
        int second = c.get(Calendar.SECOND);//18
        secondDegrees = second * 6;//108
        minuteDegrees = minute * 6 + secondDegrees / 60.0f;//142
        hourDegrees = hour * 30 + minuteDegrees / 12.f;//180
    }

    private float hourDegrees;
    private float minuteDegrees;
    private float secondDegrees;

}