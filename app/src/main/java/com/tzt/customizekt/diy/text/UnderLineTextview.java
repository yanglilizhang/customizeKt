package com.tzt.customizekt.diy.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.appcompat.widget.AppCompatTextView;

import com.tzt.customizekt.R;

/**
 *
 * @author Andy
 * @date 2019/8/9 13:34
 * Desc: 多行有问题 目前仅支持单行
 */
public class UnderLineTextview extends AppCompatTextView {

    private final Context mContext;
    private int lineHeight = 1;
    private int lineColor;
    /**
     * 线长是否和文字长度保持一致  true去掉左右padding
     */
    private boolean isLineWeight = false;
    private int lineMarginTop = 5;
    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public UnderLineTextview(Context context) {
        this(context, null);
    }

    public UnderLineTextview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnderLineTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UnderLineTextview);
        lineHeight = (int) typedArray.getDimension(R.styleable.UnderLineTextview_line_height, 1);
        lineMarginTop = (int) typedArray.getDimension(R.styleable.UnderLineTextview_line_margin_top, 5);
        lineColor = typedArray.getColor(R.styleable.UnderLineTextview_line_color, getCurrentTextColor());
        isLineWeight = typedArray.getBoolean(R.styleable.UnderLineTextview_is_line_weight, false);
        typedArray.recycle();
        init();

    }

    private void init() {
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(lineColor);
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        setMaxLines(1);
    }

   /* @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(wSize, hSize + (lineMarginTop + lineHeight) * getLineCount());
    }*/

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, (int) (bottom + lineHeight + lineMarginTop + getLineSpacingExtra()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Layout layout = getLayout();
        int lineCount = getLineCount();
        float lineSpacingExtra = getLineSpacingExtra();
        Rect rect = null;
        for (int i = 0; i < lineCount; i++) {
            int lineBottom = layout.getLineBottom(i);
            if (isLineWeight) {
                rect = new Rect((int) layout.getPrimaryHorizontal(0) + getPaddingLeft(), (int) (lineBottom + lineSpacingExtra + lineMarginTop), (int) layout.getPrimaryHorizontal(0) + getWidth() - getPaddingRight(), (int) (lineBottom + lineSpacingExtra + lineHeight + lineMarginTop));
            } else {
                rect = new Rect((int) layout.getPrimaryHorizontal(0), (int) (lineBottom + lineSpacingExtra + lineMarginTop), (int) layout.getPrimaryHorizontal(0) + getWidth(), (int) (lineBottom + lineSpacingExtra + lineHeight + lineMarginTop));
            }
            canvas.drawRect(rect, linePaint);
        }
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        setLineColor(color);
    }

    @Override
    public int getLineHeight() {
        return lineHeight;
    }

    @Override
    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
        invalidate();
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(@ColorInt int lineColor) {
        this.lineColor = lineColor;
        linePaint.setColor(lineColor);
        invalidate();
    }

    public boolean isLineWeight() {
        return isLineWeight;
    }

    public void setLineWeight(boolean lineWeight) {
        isLineWeight = lineWeight;
        invalidate();
    }

    public int getLineMarginTop() {
        return lineMarginTop;
    }

    public void setLineMarginTop(int lineMarginTop) {
        this.lineMarginTop = lineMarginTop;
        invalidate();
    }

    public Paint getLinePaint() {
        return linePaint;
    }

    public void setLinePaint(Paint linePaint) {
        this.linePaint = linePaint;
    }
}
