package com.tzt.customizekt.study;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CameraView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Camera camera = new Camera();
    float left = 100;
    float top = 100;
    int width = 600;


    public CameraView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        camera.rotateX(45);
        camera.setLocation(0, 0, Utils.getZForCamera()); // -8 = -8 * 72
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //三维变换写的时候从下往上写

        //*******************************************
        //效果1-x轴翻转效果,不正常版
//        canvas.save();
//        //1.应用到camera
//        camera.applyToCanvas(canvas);
//        canvas.drawBitmap(Utils.getAvatar(getResources(), width), left, top, paint);
//        canvas.restore();

        //*******************************************

        //效果1-x轴翻转效果,正常版
//        canvas.save();
//        //3.做完操作再移动回来
//        canvas.translate(left + width / 2, top + width / 2);
//        //2.应用到camera
//        camera.applyToCanvas(canvas);
//        //1.先中心点移动到中心坐标中再操作其他的(主要用view坐标系操作,不是canvas坐标系)
//        canvas.translate(-(left + width / 2), -(top + width / 2));
//        canvas.drawBitmap(Utils.getAvatar(getResources(), width), left, top, paint);
//        canvas.restore();

        //*******************************************
        // 只绘制上半部分
//        canvas.save();
//        canvas.translate(100 + 600 / 2, 100 + 600 / 2);
//        canvas.clipRect(-600, -600, 600, 0);
//        canvas.translate(-(100 + 600 / 2), -(100 + 600 / 2));
//        canvas.drawBitmap(Utils.getAvatar(getResources(), 600), 100, 100, paint);
//        canvas.restore();

        //只绘制下半部分
//        canvas.save();
//        canvas.translate(100 + 600 / 2, 100 + 600 / 2);
//        canvas.clipRect(-600, 0, 600, 600);
//        canvas.translate(-(100 + 600 / 2), -(100 + 600 / 2));
//        canvas.drawBitmap(Utils.getAvatar(getResources(), 600), 100, 100, paint);
//        canvas.restore();

        //只绘制右边部分
//        canvas.save();
//        canvas.translate(100 + 600 / 2, 100 + 600 / 2);
//        canvas.clipRect(0, -600, 600, 600);
//        canvas.translate(-(100 + 600 / 2), -(100 + 600 / 2));
//        canvas.drawBitmap(Utils.getAvatar(getResources(), 600), 100, 100, paint);
//        canvas.restore();
        //*******************************************

//        //效果3 绘制下半部分 并斜切图片
//        canvas.save();
//        //做完操作再移动回来
//        canvas.translate(left + width / 2, top + width / 2);
//        canvas.rotate(-20);
//        canvas.clipRect(-600, 0, 600, 600);
//        canvas.rotate(20);
//        //1.先中心点移动到中心坐标中再操作其他的(主要用view坐标系操作,不是canvas坐标系)
//        canvas.translate(-(left + width / 2), -(top + width / 2));
//        canvas.drawBitmap(Utils.getAvatar(getResources(), width), left, top, paint);
//        canvas.restore();

        //*******************************************

//        //效果3 绘制下半部分 用camera翻转45度
//        canvas.save();
//        //做完操作再移动回来
//        canvas.translate(left + width / 2, top + width / 2);
//        canvas.rotate(-20);
//        camera.applyToCanvas(canvas);//要不要用camera翻转45度
//        canvas.clipRect(-600, 0, 600, 600);
//        canvas.rotate(20);
//        //1.先中心点移动到中心坐标中再操作其他的(主要用view坐标系操作,不是canvas坐标系)
//        canvas.translate(-(left + width / 2), -(top + width / 2));
//        canvas.drawBitmap(Utils.getAvatar(getResources(), width), left, top, paint);
//        canvas.restore();


        //*******************************************
        // 绘制并 斜切上半部分
//        canvas.save();
//        canvas.translate(left + width / 2, top + width / 2);
//        canvas.rotate(-20);
//        canvas.clipRect(-600, -600, 600, 0);
//        canvas.rotate(20);
//        canvas.translate(-(left + width / 2), -(top + width / 2));
//        canvas.drawBitmap(Utils.getAvatar(getResources(), width), left, top, paint);
//        canvas.restore();
//
//        //效果3 绘制下半部分 用camera翻转45度
//        canvas.save();
//        //做完操作再移动回来
//        canvas.translate(left + width / 2, top + width / 2);
//        canvas.rotate(-20);
//        camera.applyToCanvas(canvas);//要不要用camera翻转45度
//        canvas.clipRect(-600, 0, 600, 600);
//        canvas.rotate(20);
//        //1.先中心点移动到中心坐标中再操作其他的(主要用view坐标系操作,不是canvas坐标系)
//        canvas.translate(-(left + width / 2), -(top + width / 2));
//        canvas.drawBitmap(Utils.getAvatar(getResources(), width), left, top, paint);
//        canvas.restore();


        //*******************************************

        // 绘制并 斜切上半部分
        canvas.save();
        canvas.translate(left + width / 2, top + width / 2);
        canvas.rotate(-90);
        canvas.clipRect(-600, -600, 600, 0);
        canvas.rotate(90);
        canvas.translate(-(left + width / 2), -(top + width / 2));
        canvas.drawBitmap(Utils.getAvatar(getResources(), width), left, top, paint);
        canvas.restore();

        //效果3 绘制下半部分 用camera翻转45度
        canvas.save();
        //做完操作再移动回来
        canvas.translate(left + width / 2, top + width / 2);
        canvas.rotate(-90);
        camera.applyToCanvas(canvas);//要不要用camera翻转45度
        canvas.clipRect(-600, 0, 600, 600);
        canvas.rotate(90);
        //1.先中心点移动到中心坐标中再操作其他的(主要用view坐标系操作,不是canvas坐标系)
        canvas.translate(-(left + width / 2), -(top + width / 2));
        canvas.drawBitmap(Utils.getAvatar(getResources(), width), left, top, paint);
        canvas.restore();


    }
}