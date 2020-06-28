package com.tzt.customizekt.diy.view.refreshView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.TextView;

import com.tzt.customizekt.R;

/**
 * https://github.com/hanlonglinandroidstudys/DiyViewStudy
 * author: DragonForest
 * time: 2019/12/13
 */
public class PullToRefreshView extends ViewGroup {
    IHeaderView headerView;
    View footerView;
    View contentView;

    TextView tv_header;
    Scroller mScroller;

    private float lastY;
    private float mContentHeight = 0;

    boolean isLoading = false;

    /**
     * 有效滑动距离
     */
    private float effectiveScrollY = 300;

    /**
     * 最大滑动距离
     */
    private float maxScrollY = 500;

    public PullToRefreshView(Context context) {
        super(context);
        init();
    }

    public PullToRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.view_footer, null);

        mScroller = new Scroller(getContext());
    }

    public void setHeaderView(IHeaderView headerView) {
        this.headerView = headerView;
        addView(headerView.getView());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, (int) maxScrollY - 20);
        footerView.setLayoutParams(params);
        addView(footerView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int totalContentHieght = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, heightMeasureSpec);
            totalContentHieght += child.getMeasuredHeight();
        }
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.min(totalContentHieght, heightSize));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top;
        int left;
        int right;
        int bottom;
        int childCount = getChildCount();
        mContentHeight = 0;
        Log.e("PullToRefresh", "onLayout: childCount:" + childCount);
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt == headerView) {
                // 头布局
                left = 0;
                top = -childAt.getMeasuredHeight();
                bottom = top + childAt.getMeasuredHeight();
                right = left + childAt.getMeasuredWidth();
                childAt.layout(left, top, right, bottom);

                Log.e("PullToRefresh", "头布局: (" + top + "," + left + "," + right + "," + bottom + "):");
            } else if (childAt == footerView) {
                // 底布局
                left = 0;
                top = (int) mContentHeight;
                bottom = top + childAt.getMeasuredHeight();
                right = left + childAt.getMeasuredWidth();
                childAt.layout(left, top, right, bottom);
                Log.e("PullToRefresh", "底部距: (" + top + "," + left + "," + right + "," + bottom + "):");
            } else {
                // 内容布局
                left = 0;
                top = (int) mContentHeight;
                bottom = top + childAt.getMeasuredHeight();
                right = left + childAt.getMeasuredWidth();
                childAt.layout(left, top, right, bottom);
                mContentHeight += childAt.getMeasuredHeight();
                Log.e("PullToRefresh", "内容布局: (" + top + "," + left + "," + right + "," + bottom + "):");
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();//相对于父
//        event.getRawY()//相对于屏幕
//        getScrollY()
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = lastY - y;
                if (Math.abs(getScrollY()) < maxScrollY) {
                    if (Math.abs(getScrollY()) > effectiveScrollY) {
                        float fac = (float) (Math.abs(getScrollY()) - effectiveScrollY) / (maxScrollY - effectiveScrollY);
                        headerView.onEffect(fac);
                    } else {
                        float fac = (float) Math.abs(getScrollY()) / effectiveScrollY;
                        headerView.onStart(fac);
                    }
//                    ScrollTo是到那个位置，ScrollBy是经过这段位置
                    scrollBy(0, (int) dy);
//                    scrollTo(0, (int) dy);
                }

                Log.e("PullToReferesh", "getScrollY():" + getScrollY());
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(getScrollY()) > effectiveScrollY) {
                    mScroller.startScroll(0, getScrollY(), 0, -getScrollY() - (int) effectiveScrollY, 200);
                    invalidate();
//                    scrollTo(0,-(int)effectiveScrollY);
                    headerView.onLoading();
                    isLoading = true;
                } else {
                    mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), 200);
                    invalidate();
                    headerView.onFinish();
                    isLoading = false;
                }
                break;
        }
        lastY = y;
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    public void startRefresh() {
        scrollTo(0, -(int) effectiveScrollY);
        headerView.onLoading();
        isLoading = true;
    }

    public void endRefresh() {
        scrollTo(0, 0);
        headerView.onFinish();
        isLoading = false;
    }

    public boolean isLoading() {
        return isLoading;
    }
}