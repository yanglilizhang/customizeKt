package com.tzt.customizekt.diy.view.DRefreshLayout;


/**
 * create by DragonForest at 2020/4/24
 */
public interface DHeaderAction {

    /**
     * 下拉中 未生效
     *
     * @param fac
     */
    void onDropDown(float fac);

    /**
     * 下拉生效
     *
     */
    void onDropDownEffect();

    /**
     * 下拉松手 刷新中
     */
    void onRefreshing();

    /**
     * 刷新完成
     */
    void onFinishRefresh();

    /**
     * 新页面生效
     */
    void onNewPageEffect();

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

    /**
     *
     * 显示新页面生效距离
     * @return
     */
    int getNewPageEffectDistance();
}