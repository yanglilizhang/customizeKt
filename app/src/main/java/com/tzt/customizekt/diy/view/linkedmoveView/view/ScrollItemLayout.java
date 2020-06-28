package com.tzt.customizekt.diy.view.linkedmoveView.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.tzt.customizekt.R;

/**
 * author: DragonForest
 * time: 2020/4/17
 */
public class ScrollItemLayout extends LinearLayout {

    // 记录第一个子view和第二个子view的高度

    private TabLayout tabLayout;
    private RecyclerView recyclerView;

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public ScrollItemLayout(Context context) {
        super(context);
        initView(context);
    }

    public ScrollItemLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.layout_scrollitem, this);
        tabLayout = findViewById(R.id.tablayout);
        recyclerView = findViewById(R.id.linkrecyclerView);
    }
}