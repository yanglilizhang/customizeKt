package com.tzt.customizekt.diy.view.dropdownView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.tzt.customizekt.R;
import com.tzt.customizekt.study.utils.StatusBarUtil;


/**
 * author: DragonForest
 * time: 2020/1/20
 */
public class DropDownView {

    private ViewGroup mTargetParent;
    private FrameLayout mContentView;
    private View mAnchor;
    private final Context mContext;

    private boolean isShow = false;

    private DropDownView(View anchor, ViewGroup parent) {
        this.mAnchor = anchor;
        this.mTargetParent = parent;
        this.mContext = parent.getContext();
        this.mContentView = (FrameLayout) LayoutInflater.from(mContext).inflate(R.layout.diy_dropdown_view_content, parent, false);
        int height = mAnchor.getHeight();
        int width = mAnchor.getWidth();
        int[] point = new int[2];
        mAnchor.getLocationOnScreen(point);
        //1080,69,====,0,946
        Log.e("TAG", "DropDownView: " + width + "," + height + ",====," + point[0] + "," + point[1]);
    }

    /**
     * create a dropdownView with a parent view
     *
     * @param anchor
     * @return
     */
    public static DropDownView make(View anchor) {
        DropDownView dropDownView = new DropDownView(anchor, findSuitableParent(anchor));
        return dropDownView;
    }

    public DropDownView setCustomeView(int layoutId) {
        mContentView.removeAllViews();
        View view = LayoutInflater.from(mContext).inflate(layoutId, mContentView, false);
        mContentView.addView(view);
        return this;
    }

    public void show() {
        if (!isShow) {
            int anchorHeight = mAnchor.getHeight();
            int[] anchorPoint = new int[2];
            mAnchor.getLocationInWindow(anchorPoint);
            int y = anchorPoint[1] + anchorHeight - StatusBarUtil.getInstance().getStatusBarHeight(mContext);
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mContentView.getLayoutParams();
            lp.topMargin = y;
            mTargetParent.addView(mContentView, lp);
            isShow = true;
        }
    }

    public void hide() {
        if (isShow) {
            mTargetParent.removeView(mContentView);
            isShow = false;
        }
    }

    public boolean isShow() {
        return isShow;
    }

    private void startEnterAnim() {
        mContentView.animate().translationY(mContentView.getHeight()).setDuration(500);
    }

    /**
     * find a parent, and later we will add mContent to it
     *
     * @param view
     * @return
     */
    private static ViewGroup findSuitableParent(View view) {
        ViewGroup fallback = null;
        do {
            if (view instanceof CoordinatorLayout) {
                // We've found a CoordinatorLayout, use it
                return (ViewGroup) view;
            } else if (view instanceof FrameLayout) {
                if (view.getId() == android.R.id.content) {
                    // If we've hit the decor content view, then we didn't find a CoL in the
                    // hierarchy, so use it.
                    return (ViewGroup) view;
                } else {
                    // It's not the content view but we'll use it as our fallback
                    fallback = (ViewGroup) view;
                }
            }

            if (view != null) {
                // Else, we will loop and crawl up the view hierarchy and try to find a parent
                final ViewParent parent = view.getParent();
                view = parent instanceof View ? (View) parent : null;
            }
        } while (view != null);

        // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
        return fallback;
    }
}