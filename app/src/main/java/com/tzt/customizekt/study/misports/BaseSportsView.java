package com.tzt.customizekt.study.misports;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.tzt.customizekt.R;

public class BaseSportsView extends View {
    private int width;
    private int height;
    private float circleX;
    private float circleY;
    private Bitmap backgroundBitmap;
    /**
     * 步数离圆心的 Y 轴偏移 px
     **/
    private float mainTitleOffsetY;
    private float subTitleOffsetX;
    private float subTitleOffsetY;
    private Paint mainTitlePaint;
    private int circleColor = 0;
    private Paint subTitlePaint;
    private Paint solidCirclePaint;
    /**
     * 点画笔
     **/
    private Paint dotPaint;
    /**
     * 圆环画笔
     **/
    private Paint bigCirclePaint;
    /**
     * 光晕画笔
     **/
    private Paint blurPaint;

    public BaseSportsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        dottedCirclePaint = new Paint();
        dottedCirclePaint.setStrokeWidth(DensityUtils.dp2px(context, 2f));
        dottedCirclePaint.setColor(ContextCompat.getColor(context, R.color.whiteTransparent));
        dottedCirclePaint.setStyle(Paint.Style.STROKE);
//        float gagPx = DensityUtils.dp2px(context, DOTTED_CIRCLE_GAG);
//        dottedCirclePaint.setPathEffect(new DashPathEffect(new float[]{gagPx, gagPx}, 0));
        dottedCirclePaint.setAntiAlias(true);

        //背景图
        backgroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bg_step_law);

        //main字体
        mainTitlePaint = new Paint();
        mainTitlePaint.setColor(ContextCompat.getColor(context, R.color.white));
        mainTitlePaint.setTextAlign(Paint.Align.CENTER);
        mainTitlePaint.setTextSize(DensityUtils.sp2px(context, 64));
        //main字体偏移量计算
        mainTitleOffsetY = -(mainTitlePaint.getFontMetrics().ascent +
                mainTitlePaint.getFontMetrics().descent) / 2;
        mainTitlePaint.setAntiAlias(true);


        circleColor = ContextCompat.getColor(context, R.color.whiteTransparent);
        subTitlePaint = new Paint();
        subTitlePaint.setTextAlign(Paint.Align.CENTER);
        subTitlePaint.setColor(circleColor);
        subTitlePaint.setTextSize(DensityUtils.sp2px(context, 16));
        subTitleOffsetY = DensityUtils.sp2px(context, 50);
//        subTitleSeparator = getResources().getString(R.string.sub_title_separator);
        subTitlePaint.setAntiAlias(true);

        solidCirclePaint = new Paint();
        solidCirclePaint.setStrokeWidth(DensityUtils.dp2px(context, 2f));
        solidCirclePaint.setColor(ContextCompat.getColor(context, R.color.white));
        solidCirclePaint.setStyle(Paint.Style.STROKE);
        float gagPx = DensityUtils.dp2px(context, 1.2f);//虚线间隔大小
        dottedCirclePaint.setPathEffect(new DashPathEffect(new float[]{gagPx, gagPx}, 0));
        solidCirclePaint.setAntiAlias(true);
        //点
        dotPaint = new Paint();
        dotPaint.setStrokeWidth(DensityUtils.dp2px(context, 12f));
        dotPaint.setStrokeCap(Paint.Cap.ROUND);
        dotPaint.setColor(ContextCompat.getColor(context, R.color.white));
        dotPaint.setAntiAlias(true);

        //大圆环
        bigCirclePaint = new Paint();
        bigCirclePaint.setStrokeWidth(DensityUtils.dp2px(context, 16));
        bigCirclePaint.setStyle(Paint.Style.STROKE);
        bigCirclePaint.setAntiAlias(true);

        //https://blog.csdn.net/qq_30889373/article/details/78802882
        //CornerPathEffect 拐角变圆点
        //DiscretePathEffect 线条随机偏离-把线条进行随机的偏离，让轮廓变得乱七八糟
        //DashPathEffect 虚线效果
        //PathDashPathEffect 利用 Path 绘制 线条
        //SumPathEffect 组合效果
        PathEffect pathEffect1 = new CornerPathEffect(DensityUtils.dp2px(getContext(), 20f));
        PathEffect pathEffect2 = new DiscretePathEffect(DensityUtils.dp2px(getContext(), 20f),
                DensityUtils.dp2px(getContext(), 0.5f));
        PathEffect pathEffect = new ComposePathEffect(pathEffect1, pathEffect2);
        bigCirclePaint.setPathEffect(pathEffect);
        bigCirclePaint.setColor(Color.GREEN);

        //圆半径
        float bigCircleRadius = width * 0.38f;
        Shader bigCircleLinearGradient = new LinearGradient(
                circleX - bigCircleRadius, circleY,
                circleX + bigCircleRadius, circleY,
                ContextCompat.getColor(getContext(), R.color.whiteTransparent),
                ContextCompat.getColor(getContext(), R.color.white),
                Shader.TileMode.CLAMP);
        bigCirclePaint.setShader(bigCircleLinearGradient);

        blurPaint = new Paint(bigCirclePaint);
        Shader blurLinearGradient = new LinearGradient(
                circleX, circleY,
                circleX + bigCircleRadius, circleY,
                ContextCompat.getColor(getContext(), R.color.transparent),
                ContextCompat.getColor(getContext(), R.color.white),
                Shader.TileMode.CLAMP);
        blurPaint.setShader(blurLinearGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        resetDataIfNeeded(canvas);

        drawBackground(canvas);
        drawProgressCircle(canvas);
        drawCenterText(canvas);
        drawBigCircle(canvas);
    }


    private void resetDataIfNeeded(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        if (width == this.width && height == this.height) {
            return;
        }

        this.width = width;
        this.height = height;
        circleX = width * 0.5f;
        circleY = height * 0.5f;
        // 背景：设置背景大小，使其可覆盖整个 View
        float scaleX = (float) width / backgroundBitmap.getWidth();
        float scaleY = (float) height / backgroundBitmap.getHeight();
        float scale = Math.max(scaleX, scaleY);
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        backgroundBitmap = Bitmap.createBitmap(backgroundBitmap,
                0, 0, backgroundBitmap.getWidth(), backgroundBitmap.getHeight(),
                matrix, true);
    }

    private void drawBackground(Canvas canvas) {
        // 背景，左下角对齐
        canvas.drawBitmap(backgroundBitmap, 0, 0, null);
    }

    // 进度圆环
    /**
     * 虚线画笔
     **/
    private Paint dottedCirclePaint;
    private RectF solidCircleRectF = new RectF();

    private void drawProgressCircle(Canvas canvas) {
        //半径
        float dottedCircleRadius = width * 0.32f;
        solidCircleRectF.set(circleX - dottedCircleRadius, circleY - dottedCircleRadius,
                circleX + dottedCircleRadius, circleY + dottedCircleRadius);
        canvas.drawCircle(circleX, circleY, dottedCircleRadius, dottedCirclePaint);
        //圆中圆--60度
        canvas.drawArc(solidCircleRectF, -90, 3.6f * 60, false, solidCirclePaint);

        //************方法一: 计算进度点位置 60 -->0-100 ************
        //x=中心点坐标+半径*(float) Math.cos((3.6f * 60 - 90))* Math.PI / 180
        //y=中心点坐标+半径*(float) Math.sin((3.6f * 60 - 90))* Math.PI / 180
//        canvas.drawPoint(circleX + dottedCircleRadius * (float) Math.cos((3.6f * 60 - 90) * Math.PI / 180),
//                circleY + dottedCircleRadius * (float) Math.sin((3.6f * 60 - 90) * Math.PI / 180),
//                dotPaint);
        //************方法二：(float) Math.cos(Math.toRadians(currentAngle + angles[i] / 2))************
        //x=中心点坐标x+(float) Math.cos(Math.toRadians(3.6f * 60 - 90))*半径
        //y=中心点坐标x+(float) Math.sin(Math.toRadians(3.6f * 60 - 90))*半径
        canvas.drawPoint(circleX + (float) Math.cos(Math.toRadians(3.6f * 60 - 90)) * dottedCircleRadius, circleY + (float) Math.sin(Math.toRadians(3.6f * 60 - 90)) * dottedCircleRadius, dotPaint);
    }

    //绘制中间文字
    private void drawCenterText(Canvas canvas) {
        canvas.drawText("25000", 0, "2500".length(), circleX, circleY + mainTitleOffsetY, mainTitlePaint);
        String subText = "240km | 88kcal";
        // 副标题文字居中
        float indexBefore = subTitlePaint.measureText(subText, 0, subText.indexOf(""));
        float indexAfter = subTitlePaint.measureText(subText, 0, subText.indexOf("") + 1);
        subTitleOffsetX = -(indexBefore + indexAfter) / 2;
        canvas.drawText(subText, 0, subText.length(), circleX + subTitleOffsetX, circleY + subTitleOffsetY, subTitlePaint);
    }

    // 大圆环

    private void drawBigCircle(Canvas canvas) {
        float bigCircleRadius = width * 0.38f;

        canvas.drawCircle(circleX, circleY, bigCircleRadius, bigCirclePaint);
    }


}
