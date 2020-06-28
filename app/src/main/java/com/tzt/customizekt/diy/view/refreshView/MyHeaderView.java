package com.tzt.customizekt.diy.view.refreshView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tzt.customizekt.R;

/**
 * author: DragonForest
 * time: 2019/12/13
 */
public class MyHeaderView extends LinearLayout implements IHeaderView{

    private TextView tv_header;
    private View view;

    public MyHeaderView(Context context) {
        super(context);
        init(context);
    }

    public MyHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.view_header, this);
        tv_header = findViewById(R.id.tv_header);
    }

    @Override
    public void onStart(float fac) {
        tv_header.setText("下拉刷新"+fac);
    }

    @Override
    public void onEffect(float fac) {
        tv_header.setText("松开刷新"+fac);
    }

    @Override
    public void onLoading() {
        tv_header.setText("正在加载");
    }

    @Override
    public void onFinish() {
        tv_header.setText("结束");
    }

    @Override
    public View getView() {
        return view;
    }
}