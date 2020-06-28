package com.tzt.customizekt.diy.view.headscroll;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;


import com.tzt.customizekt.R;
import com.tzt.customizekt.diy.view.headscroll.adapter.TestAdapter;
import com.tzt.customizekt.diy.view.headscroll.view.ObserverScrollView;
import com.tzt.customizekt.study.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 仿微信朋友圈头部
 */
public class WechatHeaderTest2 extends AppCompatActivity implements ObserverScrollView.OnScrollChangedListener {

    private ImageView img_head;
    private ObserverScrollView observerableScrollView;
//    private RecyclerView recyclerView;
    private Toolbar toolbar;

    private int imgHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_header_test2);
        initView();
        StatusBarUtil.getInstance().setTransparent(this);

    }

    private void initView() {
        img_head = findViewById(R.id.img_head);
        observerableScrollView = findViewById(R.id.observerableScrollView);
        initScrollObserver();
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        setAdapter(recyclerView);
        toolbar = findViewById(R.id.toolbar_title);
    }

    private void initScrollObserver() {
        observerableScrollView.scrollTo(0, -imgHeight);
        img_head.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                StatusBarUtil.getInstance().setTopPaddingView(img_head);
//                StatusBarUtil.getInstance().setTopPaddingView(toolbar);
                imgHeight = img_head.getHeight();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    toolbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                observerableScrollView.setOnScrollChangedListener(WechatHeaderTest2.this);
            }
        });
    }

    private void setAdapter(RecyclerView recyclerView) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("条目" + i);
        }
        TestAdapter testAdapter = new TestAdapter(list);
        recyclerView.setAdapter(testAdapter);
    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        Log.e("observerableScrollView", "onScrollChanged: (" + l + "," + t + "," + oldl + "," + oldt + ")，fac:" + ((float) t / imgHeight));
        if (t > 0 && t < imgHeight) {
            float fac = (float) t / imgHeight;
            if (fac > 0.8) {
                toolbar.setAlpha(fac * 5f - 4f);
            } else {
                toolbar.setAlpha(0);
            }
        } else if (t > imgHeight) {
            toolbar.setAlpha(1);
            return;
        } else {
            toolbar.setAlpha(0f);
        }

        Log.e("observerableScrollView", "alpha-->: " + toolbar.getAlpha());
    }
}