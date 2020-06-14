package com.tzt.customizekt.study.draw.camera;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;


import androidx.annotation.Nullable;

import com.tzt.customizekt.study.Utils;
import com.tzt.customizekt.study.base.BaseView;
import com.tzt.customizekt.study.base.BaseView2;
//重点:Canvas 的几何变换方法参照的是 ==View 的坐标系==，
// ⽽绘制⽅法(drawXxx())参照的是 ==Canvas ⾃⼰的坐标系==。
public class CameraView2 extends BaseView2 {
    private Bitmap bitmap;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Camera camera;

    public CameraView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

//    使⽤ Camera 做三维旋转 CameraView
//    rotate() / rotateX() / rotateY() / rotateZ()
//    translate()
//    setLocation() 其中，⼀般只⽤ ==rotateX()== 和 ==rorateY()== 来做沿 x 轴或 y 轴的旋转，
//    以及使⽤ ==setLocation()== 来调整放缩的视觉幅度。
//    对 Camera 变换之后，要⽤ ==Camera.applyToCanvas(Canvas)== 来应⽤到 Canvas。
//    ==多次变换也需要反着写，如果两个draw，先进行第一个draw的倒叙，再进行第二个draw的倒叙。==
//    切割在三维变换之前做更加方便
    {
        bitmap = Utils.getAvatar(getResources(), 600);
        camera = new Camera();
        camera.rotateX(45);
        camera.setLocation(0, 0, Utils.getCameraZLocation());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制上半部分
        canvas.save();
        canvas.translate((200 + (float) 600 / 2), (200 + (float) 600 / 2));
        canvas.rotate(-15);
        canvas.clipRect(-600, -600, 600, 0);
        canvas.rotate(15);
        canvas.translate(-(200 + (float) 600 / 2), -(200 + (float) 600 / 2));
        canvas.drawBitmap(bitmap, 200, 200, paint);
        canvas.restore();

        //绘制下半部分 绘制流程解析
        canvas.save();
        // 7 平移回来
        canvas.translate((200 + (float) 600 / 2), (200 + (float) 600 / 2));
        // 6 旋转回来
        canvas.rotate(-15);
        // 5 三维投影生成新图
        camera.applyToCanvas(canvas);
        // 4 裁切半个图
        canvas.clipRect(-600, 0, 600, 600);
//        canvas.drawRect(-600, 0, 600, 600, paint);
        //第三部 旋转15度
        canvas.rotate(15);
        //第二部 平移到坐标系中心
        //1.先中心点移动到中心坐标中再操作其他的(主要用view坐标系操作,不是canvas坐标系)
        canvas.translate(-(200 + (float) 600 / 2), -(200 + (float) 600 / 2));
        //第一步 画图
        canvas.drawBitmap(bitmap, 200, 200, paint);
        canvas.restore();
    }

//    Matrix 的⼏何变换
//    preTranslate(x, y) / postTranslate(x, y)
//    preRotate(degree) / postRotate(degree)
//    preScale(x, y) / postScale(x, y)
//    preSkew(x, y) / postSkew(x, y)
//    其中 ==preXxx() 效果和 Canvas 的准同名⽅法相同，
//    postXxx() 效果和 Canvas 的准同名⽅法顺序相反。==
//
//    注意
//    如果多次重复使⽤ Matrix，在使⽤之前需要⽤ Matrix.reset() 来把 ==Matrix 重置==。
}
