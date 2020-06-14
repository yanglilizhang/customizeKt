package com.tzt.customizekt.study.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.tzt.customizekt.R;
import com.tzt.customizekt.study.Utils;

/***
 * 绘制圆形头像
 */
public class CircleImageView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    private static final float PADDING = Utils.dp2px(50);//padding
    private static final float WIDTH = Utils.dp2px(300);//width
    Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    RectF savedArea = new RectF();
    private static final float EDGE_WIDTH = Utils.dp2px(10);
    //SRC--显示src图像
    //SRC_OUT---按src形状 镂空目标
    //SRC_IN ---按目标形状 显示src图像
    //SRC_OVER ---src压在目标上都显示，压住的不显示


    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        bitmap = getAvatar((int) WIDTH);
//        setLayerType(LAYER_TYPE_SOFTWARE,null);//对某个view关闭硬件加速 全局使用的
    }

    //软件绘制就是绘制到bitmap上面，硬件绘制就是用GPU绘制(也就是硬件加速)
//        setLayerType(LAYER_TYPE_SOFTWARE,null);//对某个view关闭硬件加速 全局使用的
//        setLayerType(LAYER_TYPE_HARDWARE,null);
//        setLayerType(LAYER_TYPE_NONE,null);//开启硬件加速
    //离屏缓冲就是单独拿出来一块区域绘制
    //使用离屏缓冲
    // 1.setLayerType()
    // 2.saveLayer()

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        savedArea.set(PADDING, PADDING, PADDING + WIDTH, PADDING + WIDTH);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //绘制椭圆 当成圆
        paint.setColor(Color.GREEN);
        //一层
        canvas.drawOval(PADDING, PADDING, PADDING + WIDTH, PADDING + WIDTH, paint);

        int saved = canvas.saveLayer(savedArea, paint);
        //二层
        canvas.drawOval(PADDING + EDGE_WIDTH, PADDING + EDGE_WIDTH, PADDING + WIDTH - EDGE_WIDTH, PADDING + WIDTH - EDGE_WIDTH, paint);
        paint.setXfermode(xfermode);
        //三层
        canvas.drawBitmap(bitmap, PADDING, PADDING, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(saved);

    }

    Bitmap getAvatar(int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.icon, options);
        options.inJustDecodeBounds = false;
        options.inDensity = options.outWidth;
        options.inTargetDensity = width;
        return BitmapFactory.decodeResource(getResources(), R.drawable.icon, options);
    }

}
