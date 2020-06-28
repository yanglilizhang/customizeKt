package com.tzt.customizekt.diy.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * 渐变
 * 利用 clipRect 的 API 可以裁剪，左边用一个画笔去画，右边用另一个画笔去画，不断的改变中间值，已达到变色的效果。
 */
public class ColorTrackTextView extends AppCompatTextView {
    private int textColor = Color.BLACK;
    private int leftColor = Color.RED;
    private int rightColor = Color.RED;

    // 初始状态为0 颜色为rightColor
    // 0~1 表示从左向右渐变， 1 时leftColor充满
    // 0~-1 表示从右向左渐变， -1 时leftColor充满
    private float progress = 0; // 0~1
    private TextPaint paint;

    String TAG = getClass().getSimpleName();

    public ColorTrackTextView(Context context) {
        super(context);
        paint = getPaint();
        textColor = getCurrentTextColor();
    }

    public ColorTrackTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = getPaint();
    }

    public void setProgress(float progress) {
        progress = progress % 2;
        if (progress > 1) {
            progress -= 2;
        } else if (progress < -1) {
            progress += 2;
        }
        this.progress = progress;
        Log.e(TAG, "setProgress: " + progress);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (progress >= 0 && progress <= 1) {
            //从左向右渐变
            canvas.save();
            //首页
            //绘制左半部分
            canvas.clipRect(0, 0, getWidth() * progress, getHeight());
            setTextColor(leftColor);
            super.onDraw(canvas);
            canvas.restore();

            canvas.save();
            //绘制右半部分
            canvas.clipRect(getWidth() * progress, 0, getRight(), getHeight());
            setTextColor(textColor);
            super.onDraw(canvas);
            canvas.restore();
        } else if (progress <= 0 && progress >= -1) {
            //从右向左渐变  -1   0
            //首页
            canvas.save();
            canvas.clipRect(0, 0, getWidth() * (1 + progress), getHeight());
            setTextColor(textColor);//黑
            super.onDraw(canvas);
            canvas.restore();

            canvas.save();
            canvas.clipRect(getWidth() * (1 + progress), 0, getRight(), getHeight());
            setTextColor(rightColor);
            super.onDraw(canvas);
            canvas.restore();
        }

    }
}