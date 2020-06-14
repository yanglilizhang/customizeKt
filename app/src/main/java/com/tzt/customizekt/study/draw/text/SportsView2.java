package com.tzt.customizekt.study.draw.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.tzt.customizekt.study.Utils;
import com.tzt.customizekt.study.base.BaseView;

/**
 * 文字绘制
 */
public class SportsView2 extends BaseView {
    private static final float RING_WIDTH = Utils.dp2px(20);
    private static final float RADIUS = Utils.dp2px(150);
    private static final int CIRCLE_COLOR = Color.parseColor("#90A4AE");
    private static final int HIGHLIGHT_COLOR = Color.parseColor("#FF4081");

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Rect rect = new Rect();
    Paint.FontMetrics fontMetrics = new Paint.FontMetrics();

    public SportsView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {

    }

    {
        paint.setTextSize(Utils.dp2px(100));
//        paint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "Quicksand-Regular.ttf"));
        paint.getFontMetrics(fontMetrics);//测量文字方法2
        paint.setTextAlign(Paint.Align.CENTER);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCoordinate(canvas);
        // 绘制环
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(CIRCLE_COLOR);
        paint.setStrokeWidth(RING_WIDTH);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, RADIUS, paint);

        // 绘制进度条
        paint.setColor(HIGHLIGHT_COLOR);
        paint.setStrokeCap(Paint.Cap.ROUND);
        //绘制圆弧-先找个RectF
        canvas.drawArc(getWidth() / 2 - RADIUS, getHeight() / 2 - RADIUS,
                getWidth() / 2 + RADIUS, getHeight() / 2 + RADIUS,
                -90, 225, false, paint);
        paint.setStrokeWidth(dpToPx(5));
        canvas.drawRect(getWidth() / 2 - RADIUS, getHeight() / 2 - RADIUS,
                getWidth() / 2 + RADIUS, getHeight() / 2 + RADIUS, paint);
        paint.setColor(Color.GREEN);

        // 绘制文字
        paint.setTextSize(Utils.dp2px(100));
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
//        paint.getTextBounds("abab", 0, "abab".length(), rect);
        ////测量文字方法1
//        float offsets = (rect.top + rect.bottom) / 2; //相对于中心点的位移,这种计算适合字体数量不变时,否则会跳动
        float offset = (fontMetrics.ascent + fontMetrics.descent) / 2;
        canvas.drawText("abab", getWidth() / 2, getHeight() / 2 - offset, paint);


        paint.setTextSize(Utils.dp2px(150));
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.getTextBounds("aaaa", 0, "abab".length(), rect);
//        canvas.drawText("aaa", 0, 200, paint);//左边会有一点空隙
        canvas.drawText("aaaa", -rect.left, 200, paint);
    }
}