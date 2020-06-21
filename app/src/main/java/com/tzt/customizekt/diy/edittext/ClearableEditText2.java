package com.tzt.customizekt.diy.edittext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import com.tzt.customizekt.R;

/**
 * 可清除的输入框
 * 设置DrawableRight实现
 *
 * author: DragonForest
 * time: 2019/12/9
 */
public class ClearableEditText2 extends AppCompatEditText implements View.OnFocusChangeListener, TextWatcher {

    // 右侧偏移
    int paddingRight = 20;

    // 删除图标的长度
    int clearWidth = 50;
    // 删除图标的坐标 组成一个正方形
    int clearStartX;
    int clearStartY;
    int clearEndX;
    int clearEndY;


    // 是否有焦点
    boolean hasFocus = false;
    private Drawable clearDrawable;


    public ClearableEditText2(Context context) {
        super(context);
        init();
    }

    public ClearableEditText2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight() + paddingRight, getPaddingBottom());
        setOnFocusChangeListener(this);

        clearDrawable = getResources().getDrawable(R.drawable.arrow);
        clearDrawable.setBounds(0, 0, clearWidth, clearWidth);
        //使用drawable默认大小
        //1.setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0],drawable,getCompoundDrawables()[2],getCompoundDrawables()[0]);
        //2.setCompoundDrawables();如果不设置bounds是不会显示的
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (this.hasFocus != hasFocus) {
            this.hasFocus = hasFocus;
            if (hasFocus && getText().toString().length() > 0) {
                setClearIcon(true);
            } else {
                setClearIcon(false);
            }
        }
    }

    private void setClearIcon(boolean isVisible) {
        Log.e("ClearableEditText2", "setClearIcon(): " + isVisible);
        if (isVisible) {
            //需要设置图片的大小才能显示!!!!!--->setBounds(0, 0, clearWidth, clearWidth);
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], clearDrawable, getCompoundDrawables()[3]);
        } else {
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], null, getCompoundDrawables()[3]);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                handleClick(event);
                break;
        }
        return super.onTouchEvent(event);
    }

    private void handleClick(MotionEvent event) {
        int clearStartX = getWidth() - getTotalPaddingRight();
        int clearStartY = getPaddingTop();
        int clearEndX = getWidth() - getPaddingRight();
        int clearEndY = getHeight() - getPaddingBottom();
        if (hasFocus
                && event.getX() > clearStartX
                && event.getX() < clearEndX
                && event.getY() > clearStartY
                && event.getY() < clearEndY) {
            // 点击命中
            setText("");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (hasFocus) {
            setClearIcon(getText().toString().length() > 0);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}