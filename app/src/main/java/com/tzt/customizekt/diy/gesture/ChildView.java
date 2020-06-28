package com.tzt.customizekt.diy.gesture;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

/**
 * author: DragonForest
 * time: 2020/3/9
 */
public class ChildView extends View {
    public ChildView(Context context) {
        super(context);
    }

    public ChildView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams lp = getLayoutParams();
        int width = resolveSize(lp.width, widthMeasureSpec);
        int height = resolveSize(lp.height, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.RED);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(this.getClass().getSimpleName(), "onTouchEvent: event-->" + getActionName(event.getAction()) + ":(" + event.getX() + "," + event.getY() + ")");
//        return super.onTouchEvent(event);
        if (event.getY() > 100) { //自己处理事件否则父类处理事件
            //阻止父层的View截获touch事件
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return true;
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