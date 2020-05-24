package com.tzt.customizekt.study.layout;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

/**
 * 改已有view的尺寸
 */
public class SquareImageView extends androidx.appcompat.widget.AppCompatImageView {
    public SquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);测量
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量以后拿到测量的尺寸和父亲给定的规则
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int size = Math.max(measuredWidth, measuredHeight);

        setMeasuredDimension(size, size); // 保存测得的尺寸

        //getMeasuredWidth()是测量尺寸可能不准，在layout调用前(onMeasure中)只能拿到这个尺寸
        //getWidth()
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);//保存最后的尺寸---->super.layout(l, t, r, b);
    }
}