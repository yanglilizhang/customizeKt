package com.tzt.customizekt.diy.view.refreshView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.Nullable;

import com.tzt.customizekt.R;

public class DefaultHeaderView extends RelativeLayout implements IHeaderView {

    private View view;
    private TextView tv_header;

    public DefaultHeaderView(Context context) {
        super(context);
        init(context);
    }

    public DefaultHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.view_header_default, this);
        tv_header = view.findViewById(R.id.tv_header);
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void onStart(float fac) {
        tv_header.setText("下拉刷新..."+fac);
    }

    @Override
    public void onEffect(float fac) {
        tv_header.setText("松开刷新..."+fac);
    }

    @Override
    public void onLoading() {
        tv_header.setText("加载中...");
    }

    @Override
    public void onFinish() {
        tv_header.setText("加载完成");
    }
}