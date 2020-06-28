package com.tzt.customizekt.diy.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.tzt.customizekt.R;

public class ColorTrackTextView2 extends AppCompatTextView {

    private Paint mOriginPaint;
    private Paint mChangePaint;

    //当前进度
    private float mCurrentProgress;

    //默认朝向
    private Directory mCurrentDirectory = Directory.LEFT_TO_RIGHT;

    //朝向
    public enum Directory {
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT
    }

    public ColorTrackTextView2(Context context) {
        this(context, null);
    }

    public ColorTrackTextView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTrackTextView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context, attrs);
    }

    private void initPaint(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView2);
        int mOriginTextColor = typedArray.getColor(R.styleable.ColorTrackTextView2_ctOriginTextColor,
                getTextColors().getDefaultColor());
        int mChangeTextColor = typedArray.getColor(R.styleable.ColorTrackTextView2_ctChangeTextColor,
                getTextColors().getDefaultColor());
        typedArray.recycle();

        //根据自定义的颜色来创建画笔
        mOriginPaint = getPaintByColor(mOriginTextColor);
        mChangePaint = getPaintByColor(mChangeTextColor);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //根据当前进度，获取当前中间值
        int middle = (int) (getWidth() * mCurrentProgress);

        //根据朝向，绘制TextView
        if (mCurrentDirectory == Directory.LEFT_TO_RIGHT) {
            //当前朝向为  从左到右
            drawText(canvas, mOriginPaint, middle, getWidth());
            drawText(canvas, mChangePaint, 0, middle);
        } else {
            //当前朝向  从右到左
            drawText(canvas, mOriginPaint, 0, getWidth() - middle);
            drawText(canvas, mChangePaint, getWidth() - middle, getWidth());
        }

    }

    /**
     * 绘制TextView
     */
    private void drawText(Canvas canvas, Paint textPaint, int start, int end) {
        //保存画布状态
        canvas.save();

        Rect rect = new Rect(start, 0, end, getHeight());
        canvas.clipRect(rect);

        //获取文字
        String text = getText().toString();

        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);

        //获取字体的宽度
        int x = getWidth() / 2 - bounds.width() / 2;
        //获取基线
        Paint.FontMetricsInt fontMetricsInt = textPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int baseLine = getHeight() / 2 + dy;

        canvas.drawText(text, x, baseLine, textPaint);

        //释放画布状态，既恢复Canvas旋转，缩放等之后的状态。
        canvas.restore();
    }


    private Paint getPaintByColor(int textColor) {
        Paint paint = new Paint();
        paint.setColor(textColor);
        paint.setAntiAlias(true);
        //设置防抖动
        paint.setDither(true);
        paint.setTextSize(getTextSize());
        return paint;
    }

    /**
     * 设置方向
     *
     * @param directory
     */
    public synchronized void setDirectory(Directory directory) {
        this.mCurrentDirectory = directory;
    }

    /**
     * 设置进度
     *
     * @param currentProgress
     */
    public synchronized void setCurrentProgress(float currentProgress) {
        this.mCurrentProgress = currentProgress;
        invalidate();
    }

    /**
     * 设置画笔颜色
     *
     * @param changeColor
     */
    public synchronized void setChangeColor(int changeColor) {
        this.mChangePaint.setColor(changeColor);
    }

    public synchronized void setOriginColor(int originColor) {
        this.mOriginPaint.setColor(originColor);
    }


//    ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 1);
//                valueAnimator.setDuration(2000);
//                valueAnimator.setInterpolator(new DecelerateInterpolator());
//
//                trackTextView.setDirectory(ColorTrackTextView.Directory.LEFT_TO_RIGHT);
//
//                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//        @Override
//        public void onAnimationUpdate(ValueAnimator animation) {
//            float animatedValue = (float) animation.getAnimatedValue();
//            trackTextView.setCurrentProgress(animatedValue);
//        }
//    });
//
//    valueAnimator.start();

}