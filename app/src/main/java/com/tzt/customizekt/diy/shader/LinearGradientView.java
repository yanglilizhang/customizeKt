package com.tzt.customizekt.diy.shader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 渐变形状
 */
public class LinearGradientView extends View {
    Paint mPaint;
    LinearGradient linearGradient;

    public LinearGradientView(Context context) {
        super(context);
        init();
    }

    public LinearGradientView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (linearGradient == null) {
            linearGradient = new LinearGradient(
                    0, getHeight() / 2,
                    getWidth(), getHeight() / 2,
                    Color.CYAN, Color.BLUE,
                    Shader.TileMode.CLAMP);
            mPaint.setShader(linearGradient);
        }
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
    }
}