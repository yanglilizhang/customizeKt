package com.tzt.customizekt.diy.view.transition;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tzt.customizekt.R;

import java.lang.reflect.Field;

public class SetFactory2Test extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_factory2_test);

        initView();
    }

    private void initView() {
        findViewById(R.id.btn_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(getClass().getSimpleName(), "换肤");
                Toast.makeText(SetFactory2Test.this, "换肤", Toast.LENGTH_SHORT).show();
                changeTo();
            }
        });
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(getClass().getSimpleName(), "切换回来");
                Toast.makeText(SetFactory2Test.this, "切换回来", Toast.LENGTH_SHORT).show();
                changeBack();
            }
        });
    }

    /**
     * 换肤
     */
    private void changeTo() {
        setFactory2();
        setContentView(R.layout.activity_set_factory2_test);
        initView();
    }

    /**
     * 切换回来
     */
    private void changeBack() {
        setFactory2Null();
        setContentView(R.layout.activity_set_factory2_test);
        initView();
    }

    private void setFactory2() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        // 反射设置mFactorySet 为false
        try {
            Field mFactorySetField = LayoutInflater.class.getDeclaredField("mFactorySet");
            mFactorySetField.setAccessible(true);
            mFactorySetField.set(layoutInflater, false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        layoutInflater.setFactory2(new LayoutInflater.Factory2() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                if (name.equals("TextView")) {
                    TextView textView = new TextView(context, attrs);
                    textView.setTextColor(Color.RED);
                    return textView;
                }
                long start = System.currentTimeMillis();
                View view = getDelegate().createView(parent, name, context, attrs);
                long end = System.currentTimeMillis();
                Log.e("SetFactory2", "加载" + name + "耗时：" + (end - start) + "ms");
                return view;
            }

            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                return null;
            }
        });
    }

    private void setFactory2Null() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        // 反射设置mFactory2 为null
        try {
            Field mFactory2Field = LayoutInflater.class.getDeclaredField("mFactory2");
            mFactory2Field.setAccessible(true);
            mFactory2Field.set(layoutInflater, null);
            Field mFactoryField = LayoutInflater.class.getDeclaredField("mFactory");
            mFactoryField.setAccessible(true);
            mFactoryField.set(layoutInflater, null);
            Field mPrivateFactoryField = LayoutInflater.class.getDeclaredField("mPrivateFactory");
            mPrivateFactoryField.setAccessible(true);
            mPrivateFactoryField.set(layoutInflater, null);

            LayoutInflater.Factory2 factory2 = layoutInflater.getFactory2();
            LayoutInflater.Factory factory = layoutInflater.getFactory();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}