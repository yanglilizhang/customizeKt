package com.tzt.customizekt.diy.gesture;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * author: DragonForest
 * time: 2020/3/9
 */
public class ParentViewGroup extends ViewGroup {
    float lastX;
    float lastY;

    public ParentViewGroup(Context context) {
        super(context);
    }

    public ParentViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int contentHeight = 0; // 取子view之和
        int contentWidth = 0;  // 取子view最大值

        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            measureChild(childAt, widthMeasureSpec, heightMeasureSpec);
            if (childAt.getMeasuredWidth() > contentWidth) {
                contentWidth = childAt.getMeasuredWidth();
            }
            contentHeight += childAt.getMeasuredHeight();

            Log.e(getClass().getSimpleName(), "onMeasure: child->" + childAt.getMeasuredHeight());
        }
        int resultHeight = resolveSize(contentHeight, heightMeasureSpec);
        int resultWidth = resolveSize(contentWidth, widthMeasureSpec);
        setMeasuredDimension(resultWidth, resultHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        t = 0;
        l = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            childAt.layout(l, t, l + childAt.getMeasuredWidth(), t + childAt.getMeasuredHeight());
            t += childAt.getMeasuredHeight();
            Log.e(getClass().getSimpleName(), "onLayout: child->" + childAt.getLeft() + "," + childAt.getBottom());
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(this.getClass().getSimpleName(), "onTouchEvent: event-->" + getActionName(event.getAction()) + ":(" + event.getX() + "," + event.getY() + ")");
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (lastY != 0)
                    scrollTo((int)x,(int)y);
                break;
        }
        Log.e(this.getClass().getSimpleName(), "onTouchEvent: event--> last("+lastX+","+lastY+")"+",now("+x+","+y+")");
        lastX = x;
        lastY = y;
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.e(this.getClass().getSimpleName(), "onInterceptTouchEvent: event-->" + getActionName(event.getAction()) + ":(" + event.getX() + "," + event.getY() + ")");
//        return super.onInterceptTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // 在move事件的时候接管事件流
            return true;
        }
        return false;
    }

    private String getActionName(int action) {
        String actionName;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                actionName = "down事件";
                break;
            case MotionEvent.ACTION_MOVE:
                actionName = "move事件";
                break;
            case MotionEvent.ACTION_UP:
                actionName = "up事件";
                break;
            case MotionEvent.ACTION_CANCEL:
                actionName = "cancel事件";
                break;
            default:
                actionName = "未知事件" + action;
                break;
        }
        return actionName;
    }
}