package com.tzt.customizekt.study;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class FancyFlipView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Camera camera = new Camera();
    float left = 100;
    float top = 100;
    int width = 600;

    float topFlip = 0;
    float bottomFlip = 0;
    float flipRotation = 0;

    public FancyFlipView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
//        camera.rotateX(45);
        camera.setLocation(0, 0, Utils.getZForCamera()); // -8 = -8 * 72
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
////////单纯的翻页 改变flipRotation
//        // 绘制并 斜切上半部分
//        canvas.save();
//        canvas.translate(left + width / 2, top + width / 2);
//        canvas.rotate(-flipRotation);
//        canvas.clipRect(-600, -600, 600, 0);
//        canvas.rotate(flipRotation);
//        canvas.translate(-(left + width / 2), -(top + width / 2));
//        canvas.drawBitmap(Utils.getAvatar(getResources(), width), left, top, paint);
//        canvas.restore();
//
//        //效果3 绘制下半部分 用camera翻转45度
//        canvas.save();
//        //做完操作再移动回来
//        canvas.translate(left + width / 2, top + width / 2);
//        canvas.rotate(-flipRotation);
//        camera.applyToCanvas(canvas);//要不要用camera翻转45度
//        canvas.clipRect(-600, 0, 600, 600);
//        canvas.rotate(flipRotation);
//        //1.先中心点移动到中心坐标中再操作其他的(主要用view坐标系操作,不是canvas坐标系)
//        canvas.translate(-(left + width / 2), -(top + width / 2));
//        canvas.drawBitmap(Utils.getAvatar(getResources(), width), left, top, paint);
//        canvas.restore();
//

//        val flipRotationAnim = ObjectAnimator.ofFloat(viewId, "flipRotation", 180f)
//        flipRotationAnim.duration = 3000
//        flipRotationAnim.startDelay = 2000
//        flipRotationAnim.start()
        ///////////////////////////////////////////

        // 绘制并 斜切上半部分
//        canvas.save();
//        canvas.translate(left + width / 2, top + width / 2);
//        canvas.rotate(-flipRotation);
//
//        canvas.clipRect(-width, -width, width, 0);
//        canvas.rotate(flipRotation);
//        canvas.translate(-(left + width / 2), -(top + width / 2));
//        canvas.drawBitmap(Utils.getAvatar(getResources(), width), left, top, paint);
//        canvas.restore();
//
//        //效果3 绘制下半部分 用camera翻转45度
//        canvas.save();
//        canvas.translate(left + width / 2, top + width / 2);
//        canvas.rotate(-flipRotation);
//
//        //底部翻页
//        camera.save();
//        camera.rotateX(bottomFlip);
//        camera.applyToCanvas(canvas);//要不要用camera翻转45度
//        camera.restore();
//
//        canvas.clipRect(-width, 0, width, width);
//        canvas.rotate(flipRotation);
//        //1.先中心点移动到中心坐标中再操作其他的(主要用view坐标系操作,不是canvas坐标系)
//        canvas.translate(-(left + width / 2), -(top + width / 2));
//        canvas.drawBitmap(Utils.getAvatar(getResources(), width), left, top, paint);
//        canvas.restore();
        ///////////////////////////////////////////

        // 绘制并 斜切上半部分
        canvas.save();
        canvas.translate(left + width / 2, top + width / 2);
        canvas.rotate(-flipRotation);
        camera.save();
        camera.rotateX(topFlip);
        camera.applyToCanvas(canvas);//要不要用camera翻转45度
        camera.restore();
        canvas.clipRect(-width, -width, width, 0);
        canvas.rotate(flipRotation);
        canvas.translate(-(left + width / 2), -(top + width / 2));
        canvas.drawBitmap(Utils.getAvatar(getResources(), width), left, top, paint);
        canvas.restore();

        //效果3 绘制下半部分 用camera翻转45度
        canvas.save();
        canvas.translate(left + width / 2, top + width / 2);
        canvas.rotate(-flipRotation);

        //底部翻页
        camera.save();
        camera.rotateX(bottomFlip);
        camera.applyToCanvas(canvas);//要不要用camera翻转45度
        camera.restore();

        canvas.clipRect(-width, 0, width, width);
        canvas.rotate(flipRotation);
        //1.先中心点移动到中心坐标中再操作其他的(主要用view坐标系操作,不是canvas坐标系)
        canvas.translate(-(left + width / 2), -(top + width / 2));
        canvas.drawBitmap(Utils.getAvatar(getResources(), width), left, top, paint);
        canvas.restore();

    }


    ////////////////////////////////////
    public float getTopFlip() {
        return topFlip;
    }

    public void setTopFlip(float topFlip) {
        this.topFlip = topFlip;
        invalidate();
    }

    public float getBottomFlip() {
        return bottomFlip;
    }

    public void setBottomFlip(float bottomFlip) {
        this.bottomFlip = bottomFlip;
        invalidate();
    }

    public float getFlipRotation() {
        return flipRotation;
    }

    public void setFlipRotation(float flipRotation) {
        this.flipRotation = flipRotation;
        invalidate();
    }
}
