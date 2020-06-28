package com.tzt.customizekt.diy.view.headscroll.adapter;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 为了处理和父布局ScrollView的滑动冲突
 * <p>
 * author: DragonForest
 * time: 2020/1/9
 */
public class NoScrollRecyclerView extends RecyclerView {
    public NoScrollRecyclerView(@NonNull Context context) {
        super(context);
    }

    public NoScrollRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, expandSpec);
    }
}