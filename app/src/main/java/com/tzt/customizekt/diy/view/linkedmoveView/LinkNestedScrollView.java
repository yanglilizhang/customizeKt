package com.tzt.customizekt.diy.view.linkedmoveView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

/**
 * author: DragonForest
 * time: 2020/4/17
 */
public class LinkNestedScrollView extends NestedScrollView {
    private String TAG = "LinkNestedScrollView";
    private OnStopListener onStopListener;

    public void setOnStopListener(OnStopListener onStopListener) {
        this.onStopListener = onStopListener;
    }

    public LinkNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public LinkNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept= super.onInterceptTouchEvent(ev);
        Log.e(TAG, "onInterceptTouchEvent: ev->"+ev.getAction()+",isIntercept->"+isIntercept );
        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onTouchEvent: ev->"+ev.getAction());
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (onStopListener != null) {
                onStopListener.onStop(ev);
            }
        }
        return super.onTouchEvent(ev);
    }


    public interface OnStopListener {
        void onStop(MotionEvent ev);
    }
}