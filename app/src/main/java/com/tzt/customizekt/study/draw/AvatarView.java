package com.tzt.customizekt.study.draw;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.tzt.customizekt.study.Utils;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * 蒙版图形 AvatarView
 */
public class AvatarView extends View {

    private static float WIDTH = Utils.dp2px(100);
    private static float BITMAP_WIDTH = Utils.dp2px(250);
    private float centerX;
    private float centerY;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap avatar;
    private Xfermode xfermode;//特别是绘制圆形时，图形周围存在毛边，利用Xfermode做蒙版完美解决
    private RectF rectF;


    public AvatarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        avatar = Utils.getAvatar(getResources(), (int) BITMAP_WIDTH);
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        rectF = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = (float) getWidth() / 2;
        centerY = (float) getHeight() / 2;
        rectF = new RectF();
        rectF.left = (float) getWidth() / 2 - WIDTH;
        rectF.right = (float) getWidth() / 2 + WIDTH;
        rectF.top = (float) getHeight() / 2 - WIDTH;
        rectF.bottom = (float) getHeight() / 2 + WIDTH;
//        rectF.set(getWidth() / 2f - WIDTH, getHeight() / 2f - WIDTH,
//                getWidth() / 2f + WIDTH, getHeight() / 2f + WIDTH);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画一个圆
        //离屏缓冲
        int id = canvas.saveLayer(rectF, paint);
        canvas.drawOval(rectF, paint);
//        canvas.drawRect(rectF, paint);
        //设置成为蒙版
        paint.setXfermode(xfermode);
        if (!avatar.isRecycled()) {
            //画真是图形
            canvas.drawBitmap(avatar, getWidth() / 2f - BITMAP_WIDTH / 2,
                    getHeight() / 2f - BITMAP_WIDTH / 2, paint);
            avatar.recycle();
            paint.setXfermode(null);
            canvas.restoreToCount(id);
        }

        //需要设置离屏缓冲将蒙版区域抠出来，
        // ==最新API用setLayerType(View.LAYER_TYPE_HARDWARE,null)更加轻量级，
        // 不过这个设置针对整个View起作用==
    }
}