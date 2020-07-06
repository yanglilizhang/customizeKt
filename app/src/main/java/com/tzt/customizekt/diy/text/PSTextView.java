package com.tzt.customizekt.diy.text;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.tzt.customizekt.R;

/**
 * 作者： ch
 * 时间： 2018/8/13 0013-上午 10:32
 * 描述：
 * 来源：
 */
public class PSTextView extends AppCompatTextView {
    /**
     * 边框 宽度
     */
    private int psBorderWidth;
    /**
     * 边框颜色
     */
    private int psBorderColor = Color.TRANSPARENT;
    /**
     * 左上圆角
     */
    private int psTopLeftRadius;
    /**
     * 右上圆角
     */
    private int psTopRightRadius;
    /**
     * 左下圆角
     */
    private int psBottomLeftRadius;
    /**
     * 右下圆角
     */
    private int psBottomRightRadius;
    /**
     * 圆角
     */
    private int psRadius;
    /**
     * 填充颜色
     */
    private int psBtnBackgroundColor = Color.TRANSPARENT;
    /**
     * 焦点时颜色
     */
    private int psFocusBackgroundColor;

    /**
     * 不可用时 颜色
     */
    private int psDisableBackgroundColor;

    public PSTextView(Context context) {
        super(context);
    }

    public PSTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttrs(context, attrs);
    }

    public PSTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttrs(context, attrs);
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PSTextView);
        psBorderWidth = typedArray.getDimensionPixelSize(R.styleable.PSTextView_ps_border_width, 0);
        psBorderColor = typedArray.getColor(R.styleable.PSTextView_ps_border_color, psBorderColor);
        psTopLeftRadius = typedArray.getDimensionPixelSize(R.styleable.PSTextView_ps_top_left_radius, 0);
        psTopRightRadius = typedArray.getDimensionPixelSize(R.styleable.PSTextView_ps_top_right_radius, 0);
        psBottomLeftRadius = typedArray.getDimensionPixelSize(R.styleable.PSTextView_ps_bottom_left_radius, 0);
        psBottomRightRadius = typedArray.getDimensionPixelSize(R.styleable.PSTextView_ps_bottom_right_radius, 0);
        psRadius = typedArray.getDimensionPixelSize(R.styleable.PSTextView_ps_radius, 0);
        psBtnBackgroundColor = typedArray.getColor(R.styleable.PSTextView_ps_background_color, psBtnBackgroundColor);
        psFocusBackgroundColor = typedArray.getColor(R.styleable.PSTextView_ps_focus_background_color, psBtnBackgroundColor);
        psDisableBackgroundColor = typedArray.getColor(R.styleable.PSTextView_ps_disable_background_color, psBtnBackgroundColor);
        typedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        refresh();
    }

    private void refresh() {
        updateBackground(createDrawable());
    }

    private Drawable createDrawable() {
        //不可用时
        if (!isEnabled()) {
            GradientDrawable disabledDrawable = new GradientDrawable();
            initRadius(disabledDrawable);
            disabledDrawable.setColor(psDisableBackgroundColor);
            return disabledDrawable;
        }

        //默认
        GradientDrawable defaultDrawable = new GradientDrawable();
        initRadius(defaultDrawable);

        defaultDrawable.setColor(psBtnBackgroundColor);

        if (psBorderWidth != 0) {
            defaultDrawable.setStroke(psBorderWidth, psBorderColor);
        }
        //有按下效果时
        if (psFocusBackgroundColor != 0) {
            //按下时
            GradientDrawable focusDrawable = new GradientDrawable();
            initRadius(focusDrawable);
            focusDrawable.setColor(psFocusBackgroundColor);
            return getRippleDrawable(defaultDrawable, focusDrawable);
        }


        return defaultDrawable;
    }

    /**
     * 设置圆角
     */
    private void initRadius(GradientDrawable defaultDrawable) {
        if (psRadius > 0) {
            defaultDrawable.setCornerRadius(psRadius);
        } else {
            defaultDrawable.setCornerRadii(new float[]{psTopLeftRadius, psTopLeftRadius, psTopRightRadius, psTopRightRadius,
                    psBottomRightRadius, psBottomRightRadius, psBottomLeftRadius, psBottomLeftRadius});
        }
    }

    @Override
    public void refreshDrawableState() {
        super.refreshDrawableState();
        refresh();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Drawable getRippleDrawable(Drawable defaultDrawable, Drawable focusDrawable) {
        return new RippleDrawable(ColorStateList.valueOf(psFocusBackgroundColor), defaultDrawable, focusDrawable);
    }


    private void updateBackground(Drawable background) {
        if (background == null) {
            return;
        }
        this.setBackground(background);
    }


}