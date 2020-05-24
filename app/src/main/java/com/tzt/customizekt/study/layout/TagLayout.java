package com.tzt.customizekt.study.layout;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class TagLayout extends ViewGroup {
    List<Rect> childrenBounds = new ArrayList<>();

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
//            child.measure();测量出每个子view的尺寸:1.对子view的要求2.开发者的要求
            ////1.开发者的要求
            LayoutParams layoutParams = child.getLayoutParams();
            int specWidthMode = MeasureSpec.getMode(widthMeasureSpec);
            int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
            int childMode;
            int chileWidthSize;
            switch (layoutParams.width){
                case LayoutParams.MATCH_PARENT:
                    switch (specWidthMode){
                        case MeasureSpec.EXACTLY:
                        case MeasureSpec.AT_MOST:
                            childMode = MeasureSpec.EXACTLY;
                            chileWidthSize = specWidthSize-useWidth;
                            int childWidthSpec = MeasureSpec.makeMeasureSpec(chileWidthSize,childMode);
                            break;
                        case MeasureSpec.UNSPECIFIED:
                            childMode = MeasureSpec.UNSPECIFIED;
                            chileWidthSize = 0;
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
//            child.measure(int widthMeasureSpec, int heightMeasureSpec);
//            childrenBounds.set() 测量结果存起来Rect!!!
        //测量所有子view后再计算出自己的尺寸
        int width = widthUsed;
        int height = heightUsed + lineMaxHeight;
        setMeasuredDimension(width, height);
    }
     */

/**
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthUsed = 0;
        int heightUsed = 0;
        int lineWidthUsed = 0;
        int lineMaxHeight = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            //测量子view 传已用的宽高  widthUsed 0随便用 不能知道是不是超屏幕了只能自己判断
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed);

            Rect childBound;
            if (childrenBounds.size() <= i) {
                childBound = new Rect();
                childrenBounds.add(childBound);
            } else {
                childBound = childrenBounds.get(i);
            }
            //保存
            childBound.set(lineWidthUsed, heightUsed, widthUsed + child.getMeasuredWidth(), heightUsed + child.getMeasuredHeight());
            //更新已使用的值
            widthUsed += child.getMeasuredWidth();
            lineMaxHeight = Math.max(lineMaxHeight, child.getMeasuredHeight());
        }
        //测量所有子view后再计算出自己的尺寸
        int width = widthUsed;
        int height = lineMaxHeight;
        setMeasuredDimension(width, height);
    }
*/

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthUsed = 0;
        int heightUsed = 0;
        int lineWidthUsed = 0;
        int lineMaxHeight = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
//            child.measure();测量出每个子view的尺寸:1.对子view的要求2.开发者的要求
//            child.measure(int widthMeasureSpec, int heightMeasureSpec);
//            childrenBounds.set() 测量结果存起来Rect!!!
            //measureChildWithMargins 测量子view
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed);
            if (specMode != MeasureSpec.UNSPECIFIED &&
                    lineWidthUsed + child.getMeasuredWidth() > specWidth) {
                //已用宽度+测量的宽度>父的宽度 则换行 换行就是更新heightUsed再调用一次measureChildWithMargins()
                lineWidthUsed = 0;//回车
                heightUsed += lineMaxHeight;//换行
                lineMaxHeight = 0;
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed);
            }
            Rect childBound;
            if (childrenBounds.size() <= i) {
                childBound = new Rect();
                childrenBounds.add(childBound);
            } else {
                childBound = childrenBounds.get(i);
            }
            //!!!
            childBound.set(lineWidthUsed, heightUsed, lineWidthUsed + child.getMeasuredWidth(), heightUsed + child.getMeasuredHeight());
            lineWidthUsed += child.getMeasuredWidth();
            widthUsed = Math.max(widthUsed, lineWidthUsed);
            lineMaxHeight = Math.max(lineMaxHeight, child.getMeasuredHeight());
        }
        //测量所有子view后再计算出自己的尺寸
        int width = widthUsed;
        int height = heightUsed + lineMaxHeight;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            Rect childBounds = childrenBounds.get(i);
//            child.layout(l,t,(l+r)/2,(t+b)/2);
            child.layout(childBounds.left, childBounds.top, childBounds.right, childBounds.bottom);
        }
    }



    ///需要重写generateLayoutParams()这个measureChildWithMargins才能获取到
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}