package com.tzt.customizekt.diy.shaderLayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import com.tzt.customizekt.R;

/**
 * 图片阴影效果 纯色阴影
 * <p>
 * 思路：
 * 1.通过Bitmap.extractAlpha() 新建出一张与原图alpha值相同的空白图片
 * 2.通过canvas.drawBitmap给空白图片上色
 * 3.在原图的基础上绘制底部的空白图片，则形成了阴影的效果
 */
public class ShadowView4 extends View {

    Paint mPaint;
    Paint shadowPaint;
    Bitmap mBitmap;
    Bitmap mAlphaBitmap;

    // 是否显示阴影
    boolean isShowShadowLayer = true;

    public ShadowView4(Context context) {
        super(context);
        init();
    }

    public ShadowView4(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // 禁止硬件加速
        /*
            因为setShadowLayer函数 在开启硬件加速的情况下 只对文字阴影效果起作用，其他图形和图片的阴影都需要关闭硬件加速
         */
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        /*
            extractAlpha()函数会新建一张具有与原图相同alpha值的空白图形，这幅图形的颜色是在使用canvas.drawBitmap()
            绘制时所使用的画笔颜色指定的
         */
        mAlphaBitmap = mBitmap.extractAlpha();
        setPaint();
    }

    private void setPaint() {

        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        shadowPaint = new Paint();
        shadowPaint.setStrokeWidth(15);
        shadowPaint.setColor(Color.RED);
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setAntiAlias(true);
        shadowPaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.SOLID));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画阴影
        if (isShowShadowLayer) {
            drawShadow(canvas);
        }
        setPaint();
        // 画图片
        /*
            对图片而言，绘制阴影的颜色无效，图片产生的阴影是直接产生一张相同的图片，仅对阴影图片的边缘进行模糊。
         */
        canvas.drawBitmap(mBitmap, null, new RectF(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom()), mPaint);


    }

    private void drawShadow(Canvas canvas) {
        // 画阴影
        canvas.save();
        canvas.translate(5, 5);
        canvas.drawBitmap(mAlphaBitmap, null, new RectF(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom()), shadowPaint);
        canvas.restore();
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