package com.tzt.customizekt.diy.view.refreshView;

import android.view.View;

/**
 * author: DragonForest
 * time: 2019/12/13
 */
public interface IHeaderView {

    View getView();

    /**
     * 开始下拉到 触发下拉事件 之间
     * @param fac 表示开始滑动距离与有效滑动距离的比值 0~1
     */
    void onStart(float fac);

    /**
     * 开始触发滑动到 滑动到最大距离之间
     * @param fac 表示开始触发滑动的距离与 最大滑动距离比值 0~1
     */
    void onEffect(float fac);

    /**
     * 正在加载状态
     */
    void onLoading();

    /**
     * 加载结束状态
     */
    void onFinish();

}