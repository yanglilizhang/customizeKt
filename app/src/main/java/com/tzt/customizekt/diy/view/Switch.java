package com.tzt.customizekt.diy.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.tzt.customizekt.R;

public class Switch extends View {
    /**
     * 过渡动画默认时长，单位：ms
     */
    public static final int DEFAULT_DURATION_MS = 500;

    private int mInsideOnColor;
    private int mInsideOffColor;
    private int mOnColor;
    private int mOffColor;
    private int mBackColor;
    private int mSwitchPadding;
    private int mInsidePadding;
    private int mRadius;
    private Paint mPaint;
    private boolean switchOn;
    private int mPaddingRight;
    private int mPaddingLeft;
    private int mPaddingTop;
    private int mPaddingBottom;
    private int mTriggerX;
    private int mTriggerColor;
    private int mInsideColor;

    private OnClickListener mOnClickListener;
    private OnStateChangeListener mOnStateChangeListener;

    private ValueAnimator mTriggerXAnimator;
    private int mDuration;
    private boolean allowAnim;

    private Drawable mTriggerDrawable;

    /**
     * 按键状态监听器
     */
    public interface OnStateChangeListener{
        void onStateChanged(View view,boolean switchOn);
    }

    public Switch(Context context) {
        super(context);
        init();
    }

    public Switch(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getAttrFromXml(context,attrs);
        init();
    }

    public Switch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrFromXml(context,attrs);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Switch(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        getAttrFromXml(context,attrs);
        init();
    }

    private void getAttrFromXml(Context context,AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.Switch);

        mInsideOnColor = typedArray.getColor(R.styleable.Switch_InsideOnColor, Color.GREEN);
        mInsideOffColor = typedArray.getColor(R.styleable.Switch_InsideOffColor, Color.RED);
        mOnColor = typedArray.getColor(R.styleable.Switch_OnColor, Color.BLUE);
        mOffColor = typedArray.getColor(R.styleable.Switch_OffColor, Color.GRAY);
        mBackColor = typedArray.getColor(R.styleable.Switch_BackColor, Color.RED);
        mSwitchPadding = typedArray.getDimensionPixelSize(R.styleable.Switch_SwitchPadding,0);
        mInsidePadding = typedArray.getDimensionPixelSize(R.styleable.Switch_InsidePadding,0);
        mRadius = typedArray.getDimensionPixelSize(R.styleable.Switch_radius,0);
        switchOn = typedArray.getBoolean(R.styleable.Switch_switchOn,false);
        mTriggerDrawable = typedArray.getDrawable(R.styleable.Switch_customTriger);
        mDuration = typedArray.getInt(R.styleable.Switch_durations,DEFAULT_DURATION_MS);
        allowAnim = typedArray.getBoolean(R.styleable.Switch_allowAnim,true);

        typedArray.recycle();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setAlpha(255);
        mPaint.setAntiAlias(true);
        switchOn = false;
        setOnClickListener(null);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int height = getHeight();
                int drawRadius = (height - mPaddingTop - mPaddingBottom) / 2;

                if(switchOn){
                    mTriggerX = mPaddingLeft + drawRadius;
                    mTriggerColor = mOnColor;
                    mInsideColor = mInsideOnColor;
                }else{
                    mTriggerX = getWidth() - mPaddingRight - drawRadius;
                    mTriggerColor = mOffColor;
                    mInsideColor = mInsideOffColor;
                }
            }
        });
    }

    private void getPaddingAttr(){
        mPaddingRight = getPaddingRight();
        mPaddingLeft = getPaddingLeft();
        mPaddingTop = getPaddingTop();
        mPaddingBottom = getPaddingBottom();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int wrapWidth = mRadius * 2 * 2;
        int wrapHeight = mRadius * 2;

        if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
            width = wrapWidth;
            height = wrapHeight;
        }else if(widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.AT_MOST){
            height = wrapHeight;
        }else if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.EXACTLY){
            width = wrapWidth;
        }

        setMeasuredDimension(MeasureSpec.makeMeasureSpec(width,widthMode),MeasureSpec.makeMeasureSpec(height,heightMode));

        getPaddingAttr();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        drawBack(canvas);

        drawInside(canvas);

        drawTrigger(canvas);
    }

    protected void drawBack(Canvas canvas){
        int width = getWidth();
        int height = getHeight();
        int backRadius = (height - mPaddingTop - mPaddingBottom) / 2;

        mPaint.setColor(mBackColor);
        canvas.drawCircle(mPaddingLeft + backRadius,
                mPaddingTop + backRadius,
                backRadius, mPaint);

        canvas.drawCircle(width - mPaddingRight - backRadius,
                mPaddingTop + backRadius,
                backRadius, mPaint);

        canvas.drawRect(mPaddingLeft + backRadius,
                mPaddingTop,
                width - mPaddingRight - backRadius,
                height - mPaddingBottom,
                mPaint);
    }

    protected void drawInside(Canvas canvas){
        int width = getWidth();
        int height = getHeight();
        int circleY = mPaddingTop + (height - mPaddingTop - mPaddingBottom) / 2;
        int radius = (height - mPaddingTop - mPaddingBottom) / 2 - mInsidePadding;
        int backRadius = (height - mPaddingTop - mPaddingBottom) / 2;

        mPaint.setColor(mInsideColor);
        canvas.drawCircle(mPaddingLeft + backRadius,
                circleY,
                radius,
                mPaint);

        canvas.drawCircle(width - mPaddingRight - backRadius,
                circleY,
                radius,
                mPaint);

        canvas.drawRect(mPaddingLeft + backRadius,
                mPaddingTop + mInsidePadding,
                width - mPaddingRight - backRadius,
                height - mPaddingBottom - mInsidePadding,
                mPaint);
    }

    protected void drawTrigger(Canvas canvas){
        int height = getHeight();
        int drawY = mPaddingTop + (height - mPaddingTop - mPaddingBottom) / 2;
        int radius = (height - mPaddingTop - mPaddingBottom - mSwitchPadding * 2) / 2;

        if(mTriggerDrawable == null){
            mPaint.setColor(mTriggerColor);
            canvas.drawCircle(mTriggerX,
                    drawY,radius, mPaint);
            return;
        }

        mTriggerDrawable.setBounds(mTriggerX - radius,
                drawY - radius,
                mTriggerX + radius,
                drawY + radius);
        mTriggerDrawable.draw(canvas);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mOnClickListener = l;

        OnClickListener onSwitchClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickListener != null){
                    mOnClickListener.onClick(v);
                }
                setSwitchOn(!switchOn);
            }
        };

        super.setOnClickListener(onSwitchClickListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnim();
    }

    public boolean isSwitchOn(){
        return switchOn;
    }

    public void setSwitchOn(boolean switchOn){
        this.switchOn = switchOn;
        if(mOnStateChangeListener != null){
            mOnStateChangeListener.onStateChanged(this,switchOn);
        }
        startAnim();
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        mOnStateChangeListener = onStateChangeListener;
    }

    private void stopAnim(){
        if(mTriggerXAnimator != null && mTriggerXAnimator.isRunning()){
            mTriggerXAnimator.cancel();
            mTriggerXAnimator.removeAllUpdateListeners();
            mTriggerXAnimator = null;
        }
    }

    private void startAnim(){
        startAnim(switchOn);
    }

    private void startAnim(final boolean switchOn){
        stopAnim();

        int height = getHeight();
        int drawRadius = (height - mPaddingTop - mPaddingBottom) / 2;
        int onX = mPaddingLeft + drawRadius;
        int offX = getWidth() - mPaddingRight - drawRadius;

        if(switchOn) {
            mTriggerXAnimator = ValueAnimator.ofInt(offX,onX);
        }else{
            mTriggerXAnimator = ValueAnimator.ofInt(onX,offX);
        }
        mTriggerXAnimator.setDuration(allowAnim ? mDuration : 0);
        mTriggerXAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //mTriggerColor = switchOn ? mOnColor : mOffColor;
                mInsideColor = switchOn ? mInsideOnColor : mInsideOffColor;
                invalidate();
            }
        });
        mTriggerXAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTriggerX = (int) animation.getAnimatedValue();
                mTriggerColor = switchOn ? mOnColor : mOffColor;
                //mInsideColor = switchOn ? mInsideOnColor : mInsideOffColor;
                invalidate();
            }
        });
        mTriggerXAnimator.start();
    }

    /**
     * 设置开启时凹槽的颜色
     * @param color 开启时凹槽的颜色
     */
    public void setInsideOnColor(int color){
        mInsideOnColor = color;
        invalidate();
    }

    /**
     * 设置关闭时凹槽的颜色
     * @param color 关闭时凹槽的颜色
     */
    public void setInsideOffColor(int color){
        mInsideOffColor = color;
        invalidate();
    }

    /**
     * 设置开关开启的颜色
     * @param color 开启的颜色
     */
    public void setSwitchOnColor(int color){
        mOnColor = color;
        invalidate();
    }

    /**
     * 设置开关关闭颜色
     * @param color 关闭的颜色
     */
    public void setSwitchOffColor(int color){
        mOffColor = color;
        invalidate();
    }

    /**
     * 设置过渡动画时长，单位：ms
     * @param ms 过渡时长
     */
    public void setAnimDuration(int ms){
        if(ms < 0 || ms == mDuration){
            return;
        }

        stopAnim();
        mDuration = ms;
        startAnim();
    }

    /**
     * 设置控件底色，注意和背景色的区别
     * @param color 控件底色
     */
    public void setBackColor(int color){
        mBackColor = color;
        invalidate();
    }

    /**
     * 允许过渡动画效果
     * @param allow 是否允许
     */
    public void allowAnim(boolean allow){
        allowAnim = allow;
        startAnim();
    }

    /**
     * 设置自定义图案
     * @param drawable 自定义图案
     */
    public void setCustomDrawable(Drawable drawable){
        if(drawable == null){
            return;
        }

        stopAnim();
        mTriggerDrawable = drawable.mutate();
        startAnim();
    }
}