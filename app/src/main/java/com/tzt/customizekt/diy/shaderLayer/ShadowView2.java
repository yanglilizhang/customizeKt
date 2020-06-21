package com.tzt.customizekt.diy.shaderLayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.tzt.customizekt.R;

/**
 * 图片阴影效果 图片叠加（默认）
 */
public class ShadowView2 extends View {

    Paint mPaint;
    Bitmap mBitmap;

    // 是否显示阴影
    boolean isShowShadowLayer = true;

    public ShadowView2(Context context) {
        super(context);
        init();
    }

    public ShadowView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // 禁止硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        setPaint();
    }

    private void setPaint() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        if (isShowShadowLayer) {
            mPaint.setShadowLayer(2, 10, 10, Color.RED);
        } else {
            // 清除阴影
            mPaint.clearShadowLayer();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setPaint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.BLUE);
        /*
            对图片而言，绘制阴影的颜色无效，图片产生的阴影是直接产生一张相同的图片，
            仅对阴影图片的边缘进行模糊。
         */
        canvas.drawBitmap(mBitmap, null,
                new RectF(getPaddingLeft(), getPaddingTop(),
                        getWidth() - getPaddingRight(),
                        getHeight() - getPaddingBottom()),
                mPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isShowShadowLayer = false;
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                isShowShadowLayer = true;
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }
}