package com.tzt.customizekt.study.draw.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


import androidx.annotation.Nullable;

import com.tzt.customizekt.study.Utils;
import com.tzt.customizekt.study.base.BaseView;

public class SportsView extends BaseView {

    private RectF rectF = new RectF();
    private static float RADIUS = Utils.dp2px(100);
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static String TEXT = "测试Text";
    private Rect rectFOffset = new Rect();
    private Paint.FontMetrics fontMetrics = new Paint.FontMetrics();

    public SportsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(Utils.dp2px(8));
        paint.setColor(Color.LTGRAY);
        paint.setTextSize(Utils.dp2px(30));
        paint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float widthHalf = getWidth() / 2f;
        float heightHalf = getHeight() / 2f;
        rectF.set(widthHalf - RADIUS, heightHalf - RADIUS,
                widthHalf + RADIUS, heightHalf + RADIUS);
    }

    @Override
    protected void init(Context context) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCoordinate(canvas);
        canvas.drawRect(rectF, paint);

        //绘制环
        canvas.drawOval(rectF, paint);

        //画弧形进度条
        paint.setColor(Color.MAGENTA);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(rectF, 60, 200, false, paint);

        //写文字
        paint.setStyle(Paint.Style.FILL);
        //文字上下居中-测量基准 - 4条baseLine基线
        //方式一:Paint.getTextBounds() 之后，使⽤用 (bounds.top + bounds.bottom) / 2（会造成文字变动上下跳\\\
        //方式二:Paint.getFontMetrics() 之后，使⽤用 (fontMetrics.ascend + fontMetrics.descend) / 2 （最多文字的中点）
        // 文字上下绝对偏移
//        paint.getTextBounds(TEXT,0,TEXT.length(),rectFOffset);
//        int offset = (rectFOffset.top + rectFOffset.bottom) / 2;
        //不上下跳动的偏移
        paint.getFontMetrics(this.fontMetrics);
        float offset = (fontMetrics.ascent + fontMetrics.descent) / 2;

        //文字左对齐--->多个文字字体不同时会产生左边空隙情况。
        // 解决方法：用 getTextBounds() 之后的 left 来计算
        paint.getTextBounds(TEXT, 0, TEXT.length(), rectFOffset);
//        canvas.drawText(TEXT, getWidth() / 2f, getHeight() / 2f, paint);
        canvas.drawText(TEXT, getWidth() / 2f + rectFOffset.left,
                getHeight() / 2f - offset, paint);

    }
}