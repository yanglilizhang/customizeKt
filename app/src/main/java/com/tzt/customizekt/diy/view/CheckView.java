package com.tzt.customizekt.diy.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.tzt.customizekt.R;

/**
 * Created by yxm on 2020/3/24
 *
 * @function
 */
public class CheckView extends View implements View.OnClickListener {

    private static final int ANIM_NULL = 0; //动画状态,没有，开始，结束
    private static final int ANIM_CHECK = 1;
    private static final int ANIM_UNCHECK = 2;
    private Context mContext;
    private int mWidth, mHeight;
    private Handler mHandler;
    private Bitmap mBitmap;

    private int animCurrentPage = -1;
    private int animMaxPage = 13;
    private int animDuration = 500;
    private int animState = ANIM_NULL;

    private boolean isCheck = false;

    private Paint mPaint;

    public CheckView(Context context) {
        this(context, null);
    }

    public CheckView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOnClickListener(this);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mPaint = new Paint();
        mPaint.setColor(0xffFF5317);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.check_bg);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (animCurrentPage < animMaxPage && animCurrentPage >= 0) {
                    invalidate();
                    if (animState == ANIM_NULL) {
                        return;
                    }
                    if (animState == ANIM_CHECK) {
                        animCurrentPage++;
                    } else if (animCurrentPage == ANIM_UNCHECK) {
                        animCurrentPage--;
                    }
                    this.sendEmptyMessageDelayed(0, animDuration / animMaxPage);
                } else {
                    if (isCheck) {
                        animCurrentPage = animMaxPage - 1;
                    } else {
                        animCurrentPage = -1;
                    }
                    invalidate();
                    animState = ANIM_NULL;
                }
            }
        };
        check();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        Log.i("View", "onSizeChanged()");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //将画布坐标系移动到中心
        canvas.translate(mWidth / 2, mHeight / 2);
        //绘制背景圆形
        canvas.drawCircle(0, 0, 240, mPaint);
        //得出图像边长
        int sideLength = mBitmap.getHeight();
        //得到图像选区和实际绘制位置
        Log.e("shitou", "animCurrentPage----------------->" + animCurrentPage);
        Rect src = new Rect(sideLength * animCurrentPage, 0, sideLength * (animCurrentPage + 1), sideLength);
        Rect dst = new Rect(-200, -200, 200, 200);
        canvas.drawBitmap(mBitmap, src, dst, null);
        Log.i("View", "onDraw()" + sideLength + animCurrentPage);
    }

    /**
     * 选择
     */
    public void check() {
        if (animState != ANIM_NULL || isCheck)
            return;
        animState = ANIM_CHECK;
        animCurrentPage = 0;
        mHandler.sendEmptyMessageDelayed(0, animDuration / animMaxPage);
        isCheck = true;
    }

    /**
     * 取消选择
     */
    public void unCheck() {
        if (animState != ANIM_NULL || (!isCheck))
            return;
        animState = ANIM_UNCHECK;
        animCurrentPage = -1;
        mHandler.sendEmptyMessageDelayed(0, animDuration / animMaxPage);
        isCheck = false;
    }

    /**
     * 设置动画时长
     *
     * @param animDuration
     */
    public void setAnimDuration(int animDuration) {
        if (animDuration <= 0)
            return;
        this.animDuration = animDuration;
    }

    /**
     * 设置背景圆形颜色
     *
     * @param color
     */
    public void setBackgroundColor(int color) {
        mPaint.setColor(color);
    }

    @Override
    public void onClick(View v) {
        unCheck();
    }
}