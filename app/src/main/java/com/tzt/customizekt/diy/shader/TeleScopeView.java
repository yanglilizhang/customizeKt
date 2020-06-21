package com.tzt.customizekt.diy.shader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.tzt.customizekt.R;


/**
 * 望远镜
 */
public class TeleScopeView extends View {
    Bitmap mBitmap;
    Bitmap mCacheBitmap;
    Paint mPaint;
    //着色器
    BitmapShader mBitmapShader;

    // 圆心
    int cx;
    int cy;
    // 半径
    int cr = 200;

    public TeleScopeView(Context context) {
        super(context);
        init();
    }

    public TeleScopeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mCacheBitmap == null) {
            // y + height must be <= bitmap.height()
//            mCacheBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight());
//            canvas.drawBitmap(mCacheBitmap, null, new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight()), mPaint);
            mCacheBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas cacheCanvas = new Canvas(mCacheBitmap);
            cacheCanvas.drawBitmap(mBitmap, null, new Rect(0, 0, getWidth(), getHeight()), mPaint);
        }
        if (mBitmapShader == null) {
            mBitmapShader = new BitmapShader(mCacheBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            Matrix matrix = new Matrix();
            matrix.setScale(2,2);//对mCacheBitmap放大操作
            mBitmapShader.setLocalMatrix(matrix);
        }
        mPaint.setShader(mBitmapShader);
        canvas.drawCircle(cx, cy, cr, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                cx = (int) event.getX();
                cy = (int) event.getY();
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                cx = (int) event.getX();
                cy = (int) event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }
}