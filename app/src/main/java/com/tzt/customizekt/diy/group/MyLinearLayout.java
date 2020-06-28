package com.tzt.customizekt.diy.group;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 自定义线性布局
 */
public class MyLinearLayout extends ViewGroup {
    // 方向 0垂直 1水平
    int orientation = 0;
    // 内容的高度
    int totalHeight = 0;
    // 内容的宽度
    int totalWidth = 0;

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (orientation == 0) {
            measureVertical(widthMeasureSpec, heightMeasureSpec);
        } else {
            measureHorizontal(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        totalHeight = 0;
        totalWidth = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            measureChild(childAt, widthMeasureSpec, heightMeasureSpec);
            totalHeight += childAt.getMeasuredHeight();
            if (childAt.getMeasuredWidth() > totalWidth) {
                totalWidth = childAt.getMeasuredWidth();
            }
        }

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                totalWidth = widthSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                totalWidth = Math.min(totalWidth, widthSize);
                break;
        }
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                totalHeight = heightSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                totalHeight = Math.min(totalHeight, heightSize);
                break;
        }
        setMeasuredDimension(totalWidth, totalHeight);
    }

    private void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        totalHeight = 0;
        totalWidth = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            measureChild(childAt, widthMeasureSpec, heightMeasureSpec);
            totalWidth += childAt.getMeasuredWidth();
            if (childAt.getMeasuredHeight() > totalHeight) {
                totalHeight = childAt.getMeasuredHeight();
            }
        }

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                totalWidth = widthSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                totalWidth = Math.min(totalWidth, widthSize);
                break;
        }
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                totalHeight = heightSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                totalHeight = Math.min(totalHeight, heightSize);
                break;
        }
        setMeasuredDimension(totalWidth, totalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (orientation == 0) {
            layoutVertical(changed, l, t, r, b);
        } else {
            layoutHorizontal(changed, l, t, r, b);
        }
    }

    private void layoutVertical(boolean changed, int l, int t, int r, int b) {
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            right = left + childAt.getMeasuredWidth();
            bottom = top + childAt.getMeasuredHeight();
            childAt.layout(left, top, right, bottom);
            top += childAt.getMeasuredHeight();
        }
    }

    private void layoutHorizontal(boolean changed, int l, int t, int r, int b) {
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            right = left + childAt.getMeasuredWidth();
            bottom = top + childAt.getMeasuredHeight();
            childAt.layout(left, top, right, bottom);
            left = right;
        }
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
        requestLayout();
    }
}