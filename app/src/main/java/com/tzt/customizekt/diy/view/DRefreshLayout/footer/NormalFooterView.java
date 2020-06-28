package com.tzt.customizekt.diy.view.DRefreshLayout.footer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tzt.customizekt.R;
import com.tzt.customizekt.diy.view.DRefreshLayout.DFooterAction;

/**
 * create by DragonForest at 2020/4/24
 */
public class NormalFooterView extends FrameLayout implements DFooterAction {
    TextView tv_footer;

    public NormalFooterView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public NormalFooterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_footer, this);
        tv_footer = findViewById(R.id.tv_footer);
    }

    @Override
    public void onLoadmore(float fac) {
        tv_footer.setText("加载更多" + fac);
    }

    @Override
    public void onLoadmoreEffect() {
        tv_footer.setText("松开加载更多");
    }

    @Override
    public void onLoading() {
        tv_footer.setText("加载中...");
    }

    @Override
    public void onFinishLoad() {
        tv_footer.setText("加载完成");
    }

    @Override
    public int getEffectDistance() {
        return tv_footer.getBottom()+10;//生效距离
    }

    @Override
    public int getHoldingDistance() {
        return tv_footer.getBottom();// 加载中距离
    }
}