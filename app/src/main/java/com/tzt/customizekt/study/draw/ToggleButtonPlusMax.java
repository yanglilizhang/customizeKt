package com.tzt.customizekt.study.draw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

import com.tzt.customizekt.R;

public class ToggleButtonPlusMax extends View {

    /**
     * 开关的默认宽度
     */
    private static final int DEFAULT_WIDTH = 400;
    /**
     * 开关的默认高度为宽度的1半
     */
    private static final int DEFAULT_HEIGHT = (int) (DEFAULT_WIDTH / 2.5f);

    private boolean toggle;
    private GestureDetectorCompat gestureDetectorCompat;

    public boolean isToggle() {
        return toggle;
    }

    public void setToggle(boolean toggle) {
        this.toggle = toggle;
        buttonLeft = toggle ? maxDistance : 0;
        invalidate();
    }

    private Bitmap background;

    private Bitmap button;

    public ToggleButtonPlusMax(Context context) {
        super(context);
    }

    public ToggleButtonPlusMax(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        gestureDetectorCompat = new GestureDetectorCompat(context, simpleOnGestureListener);
        gestureDetectorCompat.setIsLongpressEnabled(false);
        init(attrs);
        initBitmap();
    }

    private Rect backgroundRect;
    private Rect buttonRect;

    private void initBitmap() {
        background = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
        button = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button);

        backgroundRect = new Rect(0, 0, background.getWidth(), background.getHeight());
        buttonRect = new Rect(0, 0, button.getWidth(), button.getHeight());

    }


    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ToggleButtonPlusMax);
        toggle = typedArray.getBoolean(R.styleable.ToggleButtonPlusMax_toggle, false);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specModeWidth = MeasureSpec.getMode(widthMeasureSpec);//得到宽度的布局模式
        int specSizeWidth = MeasureSpec.getSize(widthMeasureSpec);//得到布局参数的宽度
        int resultWidth = specSizeWidth;
        switch (specModeWidth) {
            case MeasureSpec.EXACTLY:// 宽高的值是固定的时候,如:match_parent 或 "22dp"
                resultWidth = specSizeWidth;
                break;
            case MeasureSpec.AT_MOST:// 你的宽高的值 为 wrap_content 的时候
                resultWidth = DEFAULT_WIDTH;
                break;
        }
        int specModeHeight = MeasureSpec.getMode(heightMeasureSpec);//得到高度的布局模式
        int specSizeHeight = MeasureSpec.getSize(heightMeasureSpec);//得到布局参数的高度
        int resultHeight = specSizeHeight;
        switch (specModeHeight) {
            case MeasureSpec.EXACTLY:// 宽高的值是固定的时候,如:match_parent 或 "22dp"
                resultHeight = specSizeHeight;
                break;
            case MeasureSpec.AT_MOST:// 你的宽高的值 为 wrap_content 的时候
                resultHeight = DEFAULT_HEIGHT;
                break;
        }
        //调用该方法,最终确定该View的宽高
        setMeasuredDimension(resultWidth, resultHeight);
        buttonWidth = getMeasuredWidth() / 1.8f;
        maxDistance = getMeasuredWidth() - buttonWidth;
        buttonLeft = toggle ? maxDistance : 0;
    }

    private float maxDistance;//计算 按钮最大滑动的距离

    private float buttonWidth;

    private float buttonLeft = 0;//改变 button 的Left位置.就可以实现按钮的滑动

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        RectF drawBackRect = new RectF(0, 0, width, height);
        canvas.drawBitmap(background, backgroundRect, drawBackRect, null);

        RectF drawButtonRect = new RectF(buttonLeft, 0, buttonLeft + buttonWidth, height);
        canvas.drawBitmap(button, buttonRect, drawButtonRect, null);
    }

    private boolean isClick;

    /**
     * 触摸
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetectorCompat.onTouchEvent(event);
        if (!isClick) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (buttonLeft > maxDistance / 2) {
                        buttonLeft = maxDistance;
                    } else {
                        buttonLeft = 0;
                    }
                    invalidate();
                    toggle = buttonLeft != 0;
                    if (listener != null) {
                        listener.onToggle(toggle);
                    }
                    return true;
            }
        }
        return true;
    }

    private GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            buttonLeft = buttonLeft == 0 ? maxDistance : 0;
            invalidate();
            isClick = true;

            toggle = buttonLeft != 0;
            if (listener != null) {
                listener.onToggle(toggle);
            }
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            isClick = false;
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            buttonLeft -= distanceX;
            buttonLeft = buttonLeft <= 0 ? 0 : buttonLeft;
            buttonLeft = buttonLeft >= maxDistance ? maxDistance : buttonLeft;
            invalidate();
            return true;
        }
    };

    private OnToggleListener listener;

    public void setOnToggleListener(OnToggleListener listener) {
        this.listener = listener;
    }

    public interface OnToggleListener {
        void onToggle(boolean toggle);
    }
}