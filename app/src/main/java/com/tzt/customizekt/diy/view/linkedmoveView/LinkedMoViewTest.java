package com.tzt.customizekt.diy.view.linkedmoveView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.tzt.customizekt.R;
import com.tzt.customizekt.diy.view.linkedmoveView.adapter.SampleAdapter;
import com.tzt.customizekt.diy.view.linkedmoveView.view.ScrollItemContentLayout;
import com.tzt.customizekt.diy.view.linkedmoveView.view.ScrollItemLayout;

import java.util.ArrayList;
import java.util.List;

public class LinkedMoViewTest extends AppCompatActivity {

    private TabLayout tabLayout_hide;
    //    private ScrollItemLayout scrollItemLayout1;
//    private TabLayout tabLayout1;
//    private RecyclerView linkRecyclerView1;
//    private ScrollItemLayout scrollItemLayout2;
//    private TabLayout tabLayout2;
//    private RecyclerView linkRecyclerView2;
    private LinkNestedScrollView linkNestedScrollView;
    private ImageView iv_head;
    private ScrollItemContentLayout ll_content;

    private String TAG = "LinkedMoViewTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linked_mo_view_test);

        initView();

    }

    private void initView() {
        ll_content = findViewById(R.id.ll_content);
        tabLayout_hide = findViewById(R.id.tablayout_hide);
        linkNestedScrollView = findViewById(R.id.linknestscrollview);
        iv_head = findViewById(R.id.iv_head);

        List<ScrollItemLayout> scrollItemLayouts = createScrollItemLayouts();
        for (ScrollItemLayout layout : scrollItemLayouts) {
            layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ll_content.addView(layout);
        }

        tabLayout_hide.setVisibility(View.INVISIBLE);
        linkNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.e(TAG, "onScrollChange: srollX->" + scrollX + ",scrollY->" + scrollY + ",oldScrollX->" + oldScrollX + ",oldScrollY->" + oldScrollY);
                dealHoverViewInScroll(v, scrollX, scrollY, oldScrollX, oldScrollY);
            }
        });
        linkNestedScrollView.setOnStopListener(new LinkNestedScrollView.OnStopListener() {
            @Override
            public void onStop(MotionEvent ev) {
                dealHoverViewOnStop(ev);
            }
        });
    }

    /**
     * 处理停止滑动
     *
     * @param ev
     */
    private void dealHoverViewOnStop(MotionEvent ev) {
        Log.e(TAG, "dealHoverViewOnStop: 停止滑动");
//        int height = iv_head.getHeight();
//        int scrollY = linkNestedScrollView.getScrollY();
//        if (scrollY != 0 && scrollY != height) {
//            if (scrollY > height / 2) {
////                linkNestedScrollView.smoothScrollTo(0,height);
//                linkNestedScrollView.scrollTo(0, height);
//            } else {
////                linkNestedScrollView.smoothScrollTo(0,0);
//                linkNestedScrollView.scrollTo(0, 0);
//            }
//        }
    }


    /**
     * 处理悬停view
     *
     * @param v
     * @param scrollX
     * @param scrollY
     * @param oldScrollX
     * @param oldScrollY
     */
    private void dealHoverViewInScroll(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        int height = iv_head.getHeight();//头高度
        if (scrollY > oldScrollY) {
            // 向上滑动
            if (scrollY < height) {
                //滑动的高度小于头部布局高度-隐藏中...
                tabLayout_hide.setVisibility(View.INVISIBLE);
                return;
            }

            //extralScrollY = 滑动高度-头部布局高度
            int extralScrollY = scrollY - height;
            for (int i = 0; i < ll_content.getList().size() - 1; i++) {
                int overlayHeight1 = ll_content.getOverlayHeight(i);//tab的高度
                int overlayHeight2 = ll_content.getOverlayHeight(i + 1);
                Log.e(TAG, "dealHoverViewInScroll: overlayHeight1->" + overlayHeight1 + ",overlayHeight2->" + overlayHeight2);
                if (extralScrollY > overlayHeight1 && extralScrollY < overlayHeight2) {
                    //大于Tab高度并且小于RecyclerView高度 就显示
                    tabLayout_hide.setVisibility(View.VISIBLE);
                    setHideTabs(ll_content.getList().get(i).getTabLayout());
                    Log.e(TAG, "向上滑动: ");

                    int tabHeight = ll_content.getList().get(i).getTabLayout().getMeasuredHeight();
                    if (extralScrollY > (overlayHeight2 - tabHeight)) {
                        //大于RecyclerView高度-Tab高度 时渐变显示
                        float diff = extralScrollY - (overlayHeight2 - tabHeight);
                        // diff： tabHeight~0
                        // alpha: 1~0
                        tabLayout_hide.setAlpha(1 - diff / tabHeight);
                        Log.e(TAG, "dealHoverViewInScroll: tabHeight->" + tabHeight + ",alpha->" + (1 - diff / tabHeight));
                    } else {
                        tabLayout_hide.setAlpha(1);
                    }

                    break;
                }
            }
        } else {
            // 向下滑动
            if (scrollY < height) {
                tabLayout_hide.setVisibility(View.INVISIBLE);
                return;
            }

            int extralScrollY = scrollY - height;
            for (int i = 0; i < ll_content.getList().size() - 1; i++) {
                int overlayHeight1 = ll_content.getOverlayHeight(i);
                int overlayHeight2 = ll_content.getOverlayHeight(i + 1);
                if (extralScrollY > overlayHeight1 && extralScrollY < overlayHeight2) {
                    //大于Tab高度并且小于RecyclerView高度 就显示
                    tabLayout_hide.setVisibility(View.VISIBLE);
                    setHideTabs(ll_content.getList().get(i).getTabLayout());

                    int tabHeight = ll_content.getList().get(i).getTabLayout().getMeasuredHeight();
                    if (extralScrollY > (overlayHeight2 - tabHeight)) {
                        float diff = extralScrollY - (overlayHeight2 - tabHeight);
                        // diff： tabHeight~0
                        // alpha: 1~0
                        tabLayout_hide.setAlpha(1 - diff / tabHeight);
                        Log.e(TAG, "dealHoverViewInScroll: tabHeight->" + tabHeight + ",alpha->" + (1 - diff / tabHeight));
                    } else {
                        tabLayout_hide.setAlpha(1);
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public List<ScrollItemLayout> createScrollItemLayouts() {
        List<ScrollItemLayout> list = new ArrayList<ScrollItemLayout>();

        ScrollItemLayout layout1 = new ScrollItemLayout(this);
        setTabs1(layout1.getTabLayout());
        setAdapter1(layout1.getRecyclerView());
        list.add(layout1);

        ScrollItemLayout layout2 = new ScrollItemLayout(this);
        setTabs2(layout2.getTabLayout());
        setAdapter2(layout2.getRecyclerView());
        list.add(layout2);

        ScrollItemLayout layout3 = new ScrollItemLayout(this);
        setTabs1(layout3.getTabLayout());
        setAdapter1(layout3.getRecyclerView());
        list.add(layout3);

        ScrollItemLayout layout4 = new ScrollItemLayout(this);
        setTabs2(layout4.getTabLayout());
        setAdapter2(layout4.getRecyclerView());
        list.add(layout4);

        return list;
    }

    private void setAdapter1(RecyclerView recyclerView) {
        SampleAdapter adapter = new SampleAdapter(getNameList());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setAdapter2(RecyclerView recyclerView) {
        SampleAdapter adapter = new SampleAdapter(getFruits());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);
    }

    private void setTabs1(TabLayout mTabLayout) {
        TabLayout.Tab tab1 = mTabLayout.newTab();
        tab1.setContentDescription("first");
        tab1.setText("首页");
        TabLayout.Tab tab2 = mTabLayout.newTab();
        tab2.setContentDescription("advice");
        tab2.setText("推荐");
        TabLayout.Tab tab3 = mTabLayout.newTab();
        tab3.setContentDescription("more");
        tab3.setText("更多");
        TabLayout.Tab tab4 = mTabLayout.newTab();
        tab4.setContentDescription("mine");
        tab4.setText("我的");
        mTabLayout.addTab(tab1);
        mTabLayout.addTab(tab2);
        mTabLayout.addTab(tab3);
        mTabLayout.addTab(tab4);
        mTabLayout.setBackgroundColor(Color.RED);
    }

    private void setTabs2(TabLayout mTabLayout) {
        TabLayout.Tab tab1 = mTabLayout.newTab();
        tab1.setContentDescription("first");
        tab1.setText("蔬菜");
        TabLayout.Tab tab2 = mTabLayout.newTab();
        tab2.setContentDescription("advice");
        tab2.setText("沙拉");
        TabLayout.Tab tab3 = mTabLayout.newTab();
        tab3.setContentDescription("more");
        tab3.setText("豆浆");
        TabLayout.Tab tab4 = mTabLayout.newTab();
        tab4.setContentDescription("mine");
        tab4.setText("油条");
        mTabLayout.addTab(tab1);
        mTabLayout.addTab(tab2);
        mTabLayout.addTab(tab3);
        mTabLayout.addTab(tab4);
        mTabLayout.setBackgroundColor(Color.BLUE);

    }

    private void setHideTabs(TabLayout tablayout) {
        tabLayout_hide.removeAllTabs();
        int tabCount = tablayout.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            TabLayout.Tab tab = tabLayout_hide.newTab();
            tab.setText(tablayout.getTabAt(i).getText());
            tabLayout_hide.addTab(tab);
        }
        if (Build.VERSION.SDK_INT >= 16)
            tabLayout_hide.setBackground(tablayout.getBackground());
    }

    private List<String> getNameList() {
        List<String> nameList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            nameList.add("韩龙林" + i);
        }
        return nameList;
    }

    private List<String> getFruits() {
        List<String> nameList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            nameList.add("苹果" + i);
        }
        return nameList;
    }
}