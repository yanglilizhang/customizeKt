package com.tzt.customizekt.study.page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tzt.common.basedepency.base.BaseActivity;
import com.tzt.customizekt.R;
import com.tzt.customizekt.diy.gesture.InterceptEventTest;
import com.tzt.customizekt.diy.text.DxTextViewTest;
import com.tzt.customizekt.diy.text.sample.ColorTrackViewTest;
import com.tzt.customizekt.diy.view.DRefreshLayout.DRefreshViewTest;
import com.tzt.customizekt.diy.view.dropdownView.DropDownTest;
import com.tzt.customizekt.diy.view.headscroll.WechatHeaderTest;
import com.tzt.customizekt.diy.view.headscroll.WechatHeaderTest2;
import com.tzt.customizekt.diy.view.letterSideBar.LetterSideBarActivity;
import com.tzt.customizekt.diy.view.linkedmoveView.LinkedMoViewTest;
import com.tzt.customizekt.diy.view.refreshView.PullToRefreshTest;
import com.tzt.customizekt.diy.view.screenMoveView.ScreenMoveView2;
import com.tzt.customizekt.diy.view.searchView.SearchView;
import com.tzt.customizekt.diy.view.transition.SetFactory2Test;
import com.tzt.customizekt.diy.view.transition.TransitionActivity1;
import com.tzt.customizekt.diy.view.waveView.WaveTest;

public class EnterDiyActivity extends BaseActivity {
    private SearchView searchView1;

    @Override
    protected int layoutResID() {
        return R.layout.activity_enter_diy;
    }

    @Override
    public void initData() {
        super.initData();
        ScreenMoveView2 screenMoveView2 = new ScreenMoveView2(this);
//        screenMoveView2.attachToWindow();
        findViewById(R.id.btEnter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(EnterDiyActivity.this, DxTextViewTest.class);
//                Intent intent = new Intent(EnterDiyActivity.this, ColorTrackViewTest.class);
//                Intent intent = new Intent(EnterDiyActivity.this, WaveTest.class);
//                Intent intent = new Intent(EnterDiyActivity.this, DropDownTest.class);
//                Intent intent = new Intent(EnterDiyActivity.this, PullToRefreshTest.class);
//                Intent intent = new Intent(EnterDiyActivity.this, DRefreshViewTest.class);
//                Intent intent = new Intent(EnterDiyActivity.this, InterceptEventTest.class);
//                Intent intent = new Intent(EnterDiyActivity.this, LinkedMoViewTest.class);
//                Intent intent = new Intent(EnterDiyActivity.this, TransitionActivity1.class);
//                Intent intent = new Intent(EnterDiyActivity.this, SetFactory2Test.class);
//                Intent intent = new Intent(EnterDiyActivity.this, WechatHeaderTest.class);
//                Intent intent = new Intent(EnterDiyActivity.this, WechatHeaderTest2.class);
                Intent intent = new Intent(EnterDiyActivity.this, LetterSideBarActivity.class);
                startActivity(intent);
            }
        });
        searchView1 = findViewById(R.id.searchView);
        searchView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchView1.getState() == SearchView.STATE_NONE) {
                    searchView1.start();
                } else if (searchView1.getState() == SearchView.STATE_LOADING) {
                    searchView1.stop();
                }
            }
        });
    }
}
