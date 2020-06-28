package com.tzt.customizekt.diy.view.DRefreshLayout.header;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tzt.customizekt.R;
import com.tzt.customizekt.diy.view.DRefreshLayout.DHeaderAction;
import com.tzt.customizekt.study.utils.ScreenUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * create by DragonForest at 2020/4/24
 */
public class NormalHeaderView extends FrameLayout implements DHeaderAction {

    TextView tv_header;
    View contentView;

    public NormalHeaderView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public NormalHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }


    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_header, this);
        tv_header = findViewById(R.id.tv_header);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //拉中 未生效
    @Override
    public void onDropDown(float fac) {
        tv_header.setText("正在下拉"+fac);
    }

    //下拉生效
    @Override
    public void onDropDownEffect() {
        tv_header.setText("松开刷新");
    }

    //下拉松手 刷新中
    @Override
    public void onRefreshing() {
        tv_header.setText("刷新中...");
    }

    @Override
    public void onFinishRefresh() {
        tv_header.setText("刷新完成");
    }

    //新页面生效
    @Override
    public void onNewPageEffect() {
        tv_header.setText("下拉显示更多内容");
    }

    //生效距离
    @Override
    public int getEffectDistance() {
        return getMeasuredHeight() - tv_header.getTop() + 10;
    }

    //加载中距离
    @Override
    public int getHoldingDistance() {
        return getMeasuredHeight() - tv_header.getTop();
    }

    //显示新页面生效距离
    @Override
    public int getNewPageEffectDistance() {
        return ScreenUtil.getScreenHeight() / 2;
    }

    public void setCustomContentView(int layoutid){
        contentView = LayoutInflater.from(getContext()).inflate(layoutid, this, false);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(contentView,lp);
        contentView.setVisibility(GONE);
    }

    public void showContent(boolean show){
        if(contentView==null) return;
        if(show){
            contentView.setVisibility(VISIBLE);
            tv_header.setVisibility(GONE);
        }else{
            contentView.setVisibility(GONE);
            tv_header.setVisibility(VISIBLE);
        }
    }
}