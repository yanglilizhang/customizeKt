package com.tzt.customizekt.diy.text;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * 渐变文字 通过继承TextView实现
 */
public class GradientTextView2 extends AppCompatTextView {
    Paint mPaint;

    // 渐变长度
    float dx;
    // 移动长度
    float translateX;
    // 文字长度
    private float length;
    private LinearGradient linearGradient;
    private Matrix matrix;
    private ValueAnimator animator;
    boolean order = true; // 0 正序 1反序

    public GradientTextView2(Context context) {
        super(context);
        init();
    }

    public GradientTextView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //此处必须通过getPaint() 获取TextView的画笔，后面才能将shader设置到此画笔上，
        // 自己新建的画笔对TextView的文字是无效的
        mPaint = getPaint();
        setText("自己新建的画笔对TextView的文字是无效的");
        length = mPaint.measureText(getText().toString());
        dx = length / getText().toString().length() * 4;
        Log.e("GradientTextView2", "dx:" + dx);
        linearGradient = new LinearGradient(0, 0, dx, 0,
                new int[]{Color.BLUE, Color.RED, Color.BLUE},
                new float[]{0, 0.5f, 1f}, Shader.TileMode.CLAMP);
        matrix = new Matrix();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float textWidth = mPaint.measureText(getText().toString());
        if (order) {
            translateX += dx;
            if (translateX > (textWidth + dx)) {
                order = !order;
            }
        } else {
            translateX -= dx;
            if (translateX < -dx) {
                order = !order;
            }
        }
        Log.e("GradientTextView2", "transalteX:" + translateX);
        matrix.setTranslate(translateX, 0);
        linearGradient.setLocalMatrix(matrix);
        mPaint.setShader(linearGradient);

        postInvalidateDelayed(500);
    }
}