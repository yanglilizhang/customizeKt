package com.tzt.customizekt.diy.gesture;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tzt.customizekt.R;

public class InterceptEventTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intercept_event_test);
        setTitle("事件分发机制测试");
    }
}