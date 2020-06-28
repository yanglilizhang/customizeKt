package com.tzt.customizekt.diy.view.headscroll;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.tzt.customizekt.R;
import com.tzt.customizekt.diy.view.headscroll.adapter.TestAdapter;
import com.tzt.customizekt.diy.view.headscroll.view.ObserverScrollView;
import com.tzt.customizekt.study.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 仿微信朋友圈头部
 */
public class WechatHeaderTest extends AppCompatActivity implements ObserverScrollView.OnScrollChangedListener {

    private ImageView img_head;
    private ObserverScrollView observerableScrollView;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    private int imgHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_header_test);
        initView();
        StatusBarUtil.getInstance().setTransparent(this);
    }

    private void initView() {
        img_head = findViewById(R.id.img_head);
        observerableScrollView = findViewById(R.id.observerableScrollView);
        initScrollObserver();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setAdapter(recyclerView);
        toolbar = findViewById(R.id.toolbar_title);
    }

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
//            observerableScrollView.scrollTo(0, 1000);// 改变滚动条的位置
            observerableScrollView.smoothScrollTo(0, 1000);// 改变滚动条的位置
        }
    };


    private void initScrollObserver() {
        Handler handler = new Handler();
        handler.postDelayed(runnable, 200);
        observerableScrollView.scrollTo(0, -imgHeight);
        //在oncreate中View.getWidth和View.getHeight无法获得一个view的高度和宽度，这是因为View组件布局要在onResume回调后完成。
        // 所以现在需要使用getViewTreeObserver().addOnGlobalLayoutListener()来获得宽度或者高度
        img_head.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //当一个视图树的布局发生改变时，可以被ViewTreeObserver监听到
                StatusBarUtil.getInstance().setTopPaddingView(img_head);
//                StatusBarUtil.getInstance().setTopPaddingView(toolbar);
                imgHeight = img_head.getHeight();
                Log.e("shitou", "imgHeight---->" + imgHeight);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    toolbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                observerableScrollView.setOnScrollChangedListener(WechatHeaderTest.this);
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
            float fac = (float) t / imgHeight;//高度比例
            if (fac > 0.8) {
                toolbar.setAlpha(fac * 5f - 4f);
            } else {
                toolbar.setAlpha(0);
            }
        } else if (t >= imgHeight) {
            //头部完全消失
            toolbar.setAlpha(1);
            return;
        } else {
            toolbar.setAlpha(0f);
        }

        Log.e("observerableScrollView", "alpha-->: " + toolbar.getAlpha());
    }
}