package com.tzt.customizekt.diy.view.linkedmoveView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author: DragonForest
 * time: 2020/4/17
 */
public class LinkRecyclerView extends RecyclerView {
    private String TAG = "LinkRecyclerView";

    public LinkRecyclerView(@NonNull Context context) {
        super(context);
    }

    public LinkRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        boolean isIntercept = super.onInterceptTouchEvent(e);
        Log.e(TAG, "onInterceptTouchEvent: ev->"+e.getAction()+",isIntercept->" + isIntercept);
        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        Log.e(TAG, "onTouchEvent: ev->"+e.getAction() );
        return true;
    }
}