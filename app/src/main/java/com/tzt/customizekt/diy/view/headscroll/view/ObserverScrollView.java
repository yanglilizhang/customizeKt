package com.tzt.customizekt.diy.view.headscroll.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.widget.NestedScrollView;

/**
 * NestedScrollView默认支持嵌套滚动，在包含recyclerView的时候滑动不冲突，
 * 如果继承自ScrollView，那么就要处理滑动冲突
 *
 * author: DragonForest
 * time: 2020/1/9
 */
public class ObserverScrollView extends NestedScrollView {
    private OnScrollChangedListener onScrollChangedListener;

    public ObserverScrollView(Context context) {
        super(context);
    }

    public ObserverScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObserverScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        scrollTo(l, t);//移动到left/top
        if (this.onScrollChangedListener != null) {
            onScrollChangedListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener;
    }

    public interface OnScrollChangedListener {
        /**
         * 当前的这个ScrollView距离原点
         * @param l    水平方向滑动的距离 （离原点）
         * @param t    垂直方向滑动的距离 （离原点）
         * @param oldl 水平方向的上一次滑动的距离 （离原点）
         * @param oldt 垂直方向的上一次滑动的距离 （离原点）
         */
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }
}