package com.tzt.customizekt.study.page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tzt.common.basedepency.base.BaseActivity;
import com.tzt.customizekt.R;
import com.tzt.customizekt.diy.text.DxTextViewTest;

public class EnterDiyActivity extends BaseActivity {

    @Override
    protected int layoutResID() {
        return R.layout.activity_enter_diy;
    }

    @Override
    public void initData() {
        super.initData();
        findViewById(R.id.btEnter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterDiyActivity.this, DxTextViewTest.class);
                startActivity(intent);
            }
        });
    }
}
