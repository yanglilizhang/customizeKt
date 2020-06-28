package com.tzt.customizekt.diy.view.linkedmoveView.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * author: DragonForest
 * time: 2020/4/17
 */
public class ScrollItemContentLayout extends LinearLayout {
    List<ScrollItemLayout> list = new ArrayList<>();

    public ScrollItemContentLayout(Context context) {
        super(context);
    }

    public ScrollItemContentLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof ScrollItemLayout) {
                list.add((ScrollItemLayout) getChildAt(i));
            }
        }
    }

    public List<ScrollItemLayout> getList() {
        return list;
    }

    public int getOverlayHeight(int position) {
        int height = 0;
        for (int i = 0; i < position; i++) {
            height += list.get(i).getMeasuredHeight();
        }
        return height;
    }
}