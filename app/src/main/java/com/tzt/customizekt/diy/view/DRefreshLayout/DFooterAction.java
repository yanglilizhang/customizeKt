package com.tzt.customizekt.diy.view.DRefreshLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * create by DragonForest at 2020/4/24
 */
public interface DFooterAction {
    /**
     * 上拉中 未生效
     *
     * @param fac
     */
  void onLoadmore(float fac);

    /**
     * 上拉生效
     *
     */
    void onLoadmoreEffect();

    /**
     * 上拉松手 加载中
     */
    void onLoading();

    /**
     * 加载完成
     */
   void onFinishLoad();

    /**
     * 生效距离
     * @return
     */
   int getEffectDistance();

    /**
     * 加载中距离
     * @return
     */
   int getHoldingDistance();
}