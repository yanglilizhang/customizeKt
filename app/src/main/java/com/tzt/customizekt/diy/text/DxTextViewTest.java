package com.tzt.customizekt.diy.text;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.tzt.customizekt.R;

public class DxTextViewTest extends AppCompatActivity {

    private DXTextView tv1;
    private DXTextView tv2;
    private DXTextView tv3;
    private DXTextView tv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dx_text_view_test);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);

        initSetting();
    }

    private void initSetting() {
        tv1.setUnderline(true);
        tv2.setStrikeThruText(true);
        tv3.setGradient(Color.WHITE,Color.RED);
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv4.startYMove();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv3.startXMove();
            }
        });
    }
}