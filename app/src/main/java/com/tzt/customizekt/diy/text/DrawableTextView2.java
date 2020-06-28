package com.tzt.customizekt.diy.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;

import com.tzt.customizekt.R;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ description: 可以自由控制 drawable 大小的 TextView(TextView的drawable无法控制大小)
 * TextView 已经有实现drawableLeft,drawablePadding等，我们只要继承并扩展TextView即可。
 * android:drawablePadding="20dp"
 * @ author: vchao  blog: https://vchao.blog.csdn.net
 */
public class DrawableTextView2 extends AppCompatTextView {
    public static final int LEFT = 1, TOP = 2, RIGHT = 3, BOTTOM = 4;

    private int mHeight, mWidth;

    private Drawable mDrawable;

    private int mLocation;

    public DrawableTextView2(Context context) {
        this(context, null);
    }

    public DrawableTextView2(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView);
        mWidth = a.getDimensionPixelSize(R.styleable.DrawableTextView2_drawable_width, 0);
        mHeight = a.getDimensionPixelSize(R.styleable.DrawableTextView2_drawable_height, 0);
        mDrawable = a.getDrawable(R.styleable.DrawableTextView2_drawable_src);
        mLocation = a.getInt(R.styleable.DrawableTextView2_drawable_location, LEFT);
        a.recycle();
        //绘制Drawable宽高,位置
        drawDrawable();

    }

    /**
     * 绘制Drawable宽高,位置
     */
    public void drawDrawable() {
        if (mDrawable != null) {
            Drawable drawable;
            if (!(mDrawable instanceof BitmapDrawable)) {
                drawable = mDrawable;
            } else {
                Bitmap bitmap = ((BitmapDrawable) mDrawable).getBitmap();
                if (mWidth != 0 && mHeight != 0) {
                    drawable = new BitmapDrawable(getResources(), getBitmap(bitmap, mWidth, mHeight));
                } else {
                    drawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true));
                }
            }

            switch (mLocation) {
                case LEFT:
                    this.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    break;
                case TOP:
                    this.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                    break;
                case RIGHT:
                    this.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                    break;
                case BOTTOM:
                    this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
                    break;
            }
        }
    }

    /**
     * 缩放图片
     *
     * @param bm
     * @param newWidth
     * @param newHeight
     * @return
     */
    public Bitmap getBitmap(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = (float) newWidth / width;
        float scaleHeight = (float) newHeight / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    /**
     * 设置图片
     *
     * @param res
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setImageResource(int res) {
        mDrawable = getResources().getDrawable(res, null);
        drawDrawable();
    }

    /**
     * 设置图片
     * @param res
     */
//    public void setLocationDrawable(int res) {
//        mDrawable = getResources().getDrawable(res, null);
//        drawDrawable();
//    }

    /**
     * 设置图片
     *
     * @param res
     * @param location
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setLocationDrawable(int res, int location) {
        mDrawable = getResources().getDrawable(res, null);
        mLocation = location;
        drawDrawable();
    }

    /**
     * 设置图片
     *
     * @param res
     * @param location
     * @param width
     * @param height
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setLocationDrawable(int res, int location, int width, int height) {
        mDrawable = getResources().getDrawable(res, null);
        mLocation = location;
        mWidth = width;
        mHeight = height;
        drawDrawable();
    }

}