package com.tzt.customizekt.study;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathMeasure;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Dashboard extends View {
    private static final int angle = 120;//角度
    private static final float radius = Utils.dp2px(150);//radius
    private static final float length = Utils.dp2px(100);
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    PathDashPathEffect effect;
    Path dash = new Path();

    public Dashboard(Context context) {
        super(context);
    }

    public Dashboard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Dashboard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(Utils.dp2px(2));
        //刻度
        dash.addRect(0, 0, Utils.dp2px(2), Utils.dp2px(10), Path.Direction.CW);
        Path arc = new Path();
        arc.addArc(getWidth() / 2 - radius, getHeight() / 2 - radius,
                getWidth() / 2 + radius, getHeight() / 2 + radius, 90 + angle / 2, 360 - angle);
        PathMeasure pathMeasure = new PathMeasure(arc, false);//用于测量弧线的长度
        float advance = (pathMeasure.getLength() - Utils.dp2px(2)) / 20;
        effect = new PathDashPathEffect(dash, advance, 0, PathDashPathEffect.Style.ROTATE);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 画线
        canvas.drawArc(getWidth() / 2 - radius, getHeight() / 2 - radius,
                getWidth() / 2 + radius, getHeight() / 2 + radius,
                90 + angle / 2, 360 - angle, false, paint);
        //画刻度
        paint.setPathEffect(effect);
        canvas.drawArc(getWidth() / 2 - radius, getHeight() / 2 - radius, getWidth() / 2 + radius, getHeight() / 2 + radius,
                90 + angle / 2, 360 - angle, false, paint);
        paint.setPathEffect(null);

        //画指针 Math.toRadians()将角度转换为弧度
        //x->cos值
        //y->sin值
        canvas.drawLine(getWidth() / 2, getHeight() / 2,
                (float) Math.cos(Math.toRadians(getAngleFromMark(5))) * length + getWidth() / 2,
                (float) Math.sin(Math.toRadians(getAngleFromMark(5))) * length + getHeight() / 2,
                paint);
    }

    //计算角度
    int getAngleFromMark(int mark) {
        return (int) (90 + (float) angle / 2 + (360 - (float) angle) / 20 * mark);
    }

}
