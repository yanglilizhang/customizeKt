package com.tzt.customizekt.study.draw.camera;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.tzt.customizekt.study.Utils;

public class CameraAnimationView extends View {

    private Bitmap bitmap;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Camera camera;
    private float rotateBottom = 0;
    private float rotateTop = 0;
    private float rotateCamera = 0;

    public CameraAnimationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        bitmap = Utils.getAvatar(getResources(),600);
        camera = new Camera();
        //这个⽅法⼀般前两个参数都填 0，第三个参数为负值。由于这个值的单位是硬编码写死的，因此像素 密度越⾼的⼿机，
        // 相当于 Camera 距离 View 越近，所以最好把这个值写成与机器的 density 成正⽐ 的⼀个负值，例如 -6 * density
        //==三维图形的适配==：setLocation(x:0,y:0,z:-8) ,-8的单位是英寸，是openGL中sky的东西，1英寸=72像素
        // ，使用 ==-8*getResources().getDisplayMetrics().density== 来动态适配。
        camera.setLocation(0,0,Utils.getCameraZLocation());
    }

    public float getRotateBottom() {
        return rotateBottom;
    }

    public void setRotateBottom(float rotateBottom) {
        this.rotateBottom = rotateBottom;
        invalidate();
    }

    public float getRotateTop() {
        return rotateTop;
    }

    public void setRotateTop(float rotateTop) {
        this.rotateTop = rotateTop;
        invalidate();
    }

    public float getRotateCamera() {
        return rotateCamera;
    }

    public void setRotateCamera(float rotateCamera) {
        this.rotateCamera = rotateCamera;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制上半部分
        canvas.save();
        canvas.translate((200 + (float)600 / 2),(200 + (float)600 / 2));
        canvas.rotate(-rotateCamera);
        camera.save();
        camera.rotateX(rotateTop);
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.clipRect(- 600, - 600, 600, 0);
        canvas.rotate(rotateCamera);
        canvas.translate(-(200 + (float)600 / 2),-(200 + (float)600 / 2));
        canvas.drawBitmap(bitmap,200,200,paint);
        canvas.restore();

        //绘制下半部分 绘制流程解析
        canvas.save();
        // 7 平移回来
        canvas.translate((200 + (float)600 / 2),(200 + (float)600 / 2));
        // 6 旋转回来
        canvas.rotate(-rotateCamera);
        // 5 三维投影生成新图
        camera.save();
        camera.rotateX(rotateBottom);
        camera.applyToCanvas(canvas);
        camera.restore();
        // 4 裁切半个图
        canvas.clipRect(- 600, 0, 600, 600);
        //第三部 旋转15度
        canvas.rotate(rotateCamera);
        //第二部 平移到坐标系中心
        canvas.translate(-(200 + (float)600 / 2),-(200 + (float)600 / 2));
        //第一步 画图
        canvas.drawBitmap(bitmap,200,200,paint);
        canvas.restore();
    }
}