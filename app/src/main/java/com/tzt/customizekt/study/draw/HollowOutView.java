package com.tzt.customizekt.study.draw;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.tzt.customizekt.study.base.BaseView;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 1101313414@qq.com
 *     time   : 2020-05-01 11:03
 *     desc   : 高级UI绘制 - 镂空(相交的地方镂空)
 *     version: 1.0
 * </pre>
 */
public class HollowOutView extends BaseView {

    private Paint paint;
    private Path path;
    private PathMeasure pathMeasure;

    public HollowOutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    //    Path.Direction.CCW 逆时针 Path.Direction.CW 顺时针
    {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        path = new Path();
        //从最内部图形发射一条射线穿过所有嵌套图形。镂空两种方式：
        //2.1  默认path.fileType = WINDING  （需要看方向）
        //        2.1.1 如果方向相同，全部内部，带有涂色
        //        2.1.2 如果方向相反的穿插次数相等则为内部，不等则为外部
//        path.setFillType(Path.FillType.WINDING);
        //2.2 path.fileType = EVEN_ODD （常用）
        //        2.2.1 不考虑方向。穿插奇数次则为内部，偶数次则为外部：
        path.setFillType(Path.FillType.EVEN_ODD);

        pathMeasure = new PathMeasure();

        //forceClosed 是否自动填充未补全图形
        pathMeasure.setPath(path, false);
//        pathMeasure.getLength();
//        pathMeasure.getPosTan();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float width = (float) getWidth() / 2;
        float height = (float) getHeight() / 2;
        path.addRect(width - px2dp(100),
                height - px2dp(100),
                width + px2dp(100),
                height + px2dp(100), Path.Direction.CW);

        path.addCircle(
                width,
                height - px2dp(100),
                px2dp(100),
                Path.Direction.CW);

    }

    @Override
    protected void init(Context context) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCoordinate(canvas);

        canvas.drawPath(path, paint);


    }

    private float px2dp(float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, Resources.getSystem().getDisplayMetrics());
    }
}