package com.tzt.customizekt.diy.recycler;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 作者： ch
 * 时间： 2019/1/15 0015-下午 1:37
 * 描述： 半透明的间隔线
 * 来源：
 */

public class TransDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private Xfermode xfermode;
    private LinearGradient linearGradient;
    private int layerId;

    public TransDecoration() {
        mPaint = new Paint();
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        linearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, 100.0f, new int[]{0, Color.BLACK}, null, Shader.TileMode.CLAMP);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        // 此处 Paint的参数这里传的null， 在传入 mPaint 时会出现第一次打开黑屏闪现的问题
        // 注意 saveLayer 不能省也不能移动到onDrawOver方法里
        layerId = c.saveLayer(0.0f, 0.0f, (float) parent.getWidth(), (float) parent.getHeight(), null, Canvas.ALL_SAVE_FLAG);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }

    @Override
    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);
        mPaint.setXfermode(xfermode);
        mPaint.setShader(linearGradient);
        canvas.drawRect(0.0f, 0.0f, parent.getRight(), 200.0f, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }
}