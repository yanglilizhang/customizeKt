package com.tzt.customizekt.diy.view.DRefreshLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import com.tzt.customizekt.R;
import com.tzt.customizekt.diy.view.DRefreshLayout.footer.NormalFooterView;
import com.tzt.customizekt.diy.view.DRefreshLayout.header.NormalHeaderView;
import com.tzt.customizekt.study.utils.ScreenUtil;

/**
 * https://github.com/hanlonglinandroidstudys/DiyViewStudy
 * create by DragonForest at 2020/4/24
 */
public class DRefreshLayout extends LinearLayout {
    private String TAG = "DRefreshLayout";

    NormalHeaderView headerView;
    View contentView;
    NormalFooterView footerView;


    float lastY;
    Scroller scroller;
    State state = State.NORMAL;
    DRefreshListener dRefreshListener;

    public void setdRefreshListener(DRefreshListener dRefreshListener) {
        this.dRefreshListener = dRefreshListener;
    }

    public DRefreshLayout(Context context) {
        super(context);
        setOrientation(VERTICAL);
    }

    enum State {
        /**
         * 初始状态
         */
        NORMAL,
        /**
         * 正在刷新
         */
        REFRESHING,
        /**
         * 新页面显示
         */
        NEWPAGE,
        /**
         * 加载更多
         */
        LOADING
    }

    interface DRefreshListener {
        void onRefresh();

        void onNewPage();

        void onLoadmore();
    }

    public DRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.e(TAG, "onFinishInflate: ");
        int childCount = getChildCount();
        if (childCount == 2) {
            headerView = (NormalHeaderView) getChildAt(0);
            contentView = getChildAt(1);
        } else if (childCount == 3) {
            headerView = (NormalHeaderView) getChildAt(0);
            contentView = getChildAt(1);
            footerView = (NormalFooterView) getChildAt(2);
        } else {
            throw new IllegalArgumentException("子布局必须为3个或2个");
        }

        headerView.setCustomContentView(R.layout.activity_search_view_test1);
        scroller = new Scroller(getContext());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(TAG, "onSizeChanged: (" + w + "," + h + "," + oldw + "," + oldh + ")");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure: ");
        int totalHeight = 0;
        int maxWidth = 0;
        if (headerView != null) {
            measureChild(headerView, widthMeasureSpec, heightMeasureSpec);
            totalHeight += headerView.getMeasuredHeight();
            maxWidth = Math.max(headerView.getMeasuredWidth(), maxWidth);
        }
        if (contentView != null) {
            int contentHeight = MeasureSpec.makeMeasureSpec(ScreenUtil.getScreenHeight() - ScreenUtil.getStatusBarHeight(), MeasureSpec.EXACTLY);
            int contentWidth = MeasureSpec.makeMeasureSpec(ScreenUtil.getScreenWidth(), MeasureSpec.EXACTLY);
            contentView.measure(contentWidth, contentHeight);
            totalHeight += contentView.getMeasuredHeight();
            maxWidth = Math.max(contentView.getMeasuredWidth(), maxWidth);
        }
        if (footerView != null) {
            measureChild(footerView, widthMeasureSpec, heightMeasureSpec);
            totalHeight += footerView.getMeasuredHeight();
            maxWidth = Math.max(footerView.getMeasuredWidth(), maxWidth);
        }

        setMeasuredDimension(maxWidth, totalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.e(TAG, "onLayout: ");

        if (headerView != null) {
            headerView.layout(0, -headerView.getMeasuredHeight(), headerView.getMeasuredWidth(), 0);
        }
        if (contentView != null) {
            contentView.layout(0, 0, contentView.getMeasuredWidth(), contentView.getMeasuredHeight());
        }
        if (footerView != null) {
            footerView.layout(0, contentView.getMeasuredHeight(), footerView.getMeasuredWidth(), ScreenUtil.getScreenHeight() + footerView.getMeasuredHeight());
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //action->2,x,y->469.48425,619.9424
        Log.e(TAG, "onTouchEvent: action->" + event.getAction() + ",x,y->" + event.getX() + "," + event.getY());

        float y = event.getY();
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                return true;
            case MotionEvent.ACTION_MOVE: {
                float dy = lastY - y;//滑动距离
                scrollBy(0, (int) dy);
                if (getScrollY() < 0 && getScrollY() > -headerView.getMeasuredHeight()) {
                    // 下拉getScrollY() < 0
                    if (getScrollY() > -headerView.getEffectDistance()) {
                        float fac = getScrollY() / (float) headerView.getEffectDistance();
                        headerView.onDropDown(fac);
                    } else if (getScrollY() > -headerView.getNewPageEffectDistance()) {
                        headerView.onDropDownEffect();
                    } else {
                        headerView.onNewPageEffect();
                    }
                } else if (getScrollY() > 0 && getScrollY() < footerView.getMeasuredHeight()) {
                    // 上拉getScrollY() > 0
                    if (getScrollY() < footerView.getEffectDistance()) {
                        float fac = getScrollY() / (float) footerView.getEffectDistance();
                        footerView.onLoadmore(fac);
                    } else {
                        footerView.onLoadmoreEffect();
                    }
                }

                Log.e(TAG, "onTouchEvent: scrollY->" + getScrollY());
                lastY = y;
                break;
            }
            case MotionEvent.ACTION_UP: {
                Log.e(TAG, "onTouchEvent: headerEffectDistance->" + headerView.getEffectDistance() + ",hreaderHeight->" + headerView.getMeasuredHeight());
                if (getScrollY() < 0) {
                    // 下拉
                    if (getScrollY() > -headerView.getEffectDistance()) {
                        smoothScrollTo(0, 0);
                        state = State.NORMAL;
                    } else if (getScrollY() > -headerView.getNewPageEffectDistance()) {
                        if (state != State.NEWPAGE) {
                            headerView.onRefreshing();
                            smoothScrollTo(0, -headerView.getHoldingDistance());
                            state = State.REFRESHING;
                            dRefreshListener.onRefresh();
                        } else {
                            smoothScrollTo(0, 0);
                            state = State.NORMAL;
                        }
                    } else {
                        smoothScrollTo(0, -headerView.getMeasuredHeight());
                        state = State.NEWPAGE;
                        dRefreshListener.onNewPage();
                    }
                } else if (getScrollY() > 0) {
                    // 上拉
                    if (getScrollY() < footerView.getEffectDistance()) {
                        smoothScrollTo(0, 0);
                        state = State.NORMAL;
                    } else {
                        footerView.onLoading();
                        smoothScrollTo(0, footerView.getHoldingDistance());
                        state = State.LOADING;
                        dRefreshListener.onLoadmore();
                    }
                }

                break;
            }
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onTouchEvent(event);
    }


    private void smoothScrollTo(int x, int y) {
        int dy = y - getScrollY();
        int duration = Math.abs(dy) * 2;
        if (duration > 500) {
            duration = 500;
        }
        scroller.startScroll(0, getScrollY(), 0, dy, duration);
        postInvalidate();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.e(TAG, "onScrollChanged: t->" + t + ",oldt->" + oldt);
        if (t == -headerView.getMeasuredHeight()) {
            headerView.showContent(true);
        } else {
            headerView.showContent(false);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            int currX = scroller.getCurrX();
            int currY = scroller.getCurrY();
            scrollTo(currX, currY);
            postInvalidate();
        }
    }


    public void startRefreshing() {
        smoothScrollTo(0, -headerView.getHoldingDistance() - 1);
        headerView.onRefreshing();
        state = State.REFRESHING;
        dRefreshListener.onRefresh();
    }

    public void stopRefreshing() {
        headerView.onFinishRefresh();
        smoothScrollTo(0, 0);
        state = State.NORMAL;
    }

    public void startNewPage() {
        smoothScrollTo(0, -headerView.getMeasuredHeight());
        state = State.NEWPAGE;
        dRefreshListener.onNewPage();
    }

    public void startLoadingmore() {
        smoothScrollTo(0, footerView.getHoldingDistance() + 1);
        footerView.onLoading();
        state = State.LOADING;
        dRefreshListener.onLoadmore();
    }

    public void stopLoadingmore() {
        footerView.onFinishLoad();
        smoothScrollTo(0, 0);
        state = State.NORMAL;
    }
}