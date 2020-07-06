package com.tzt.customizekt.diy.view.degreeView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.tzt.customizekt.R;


/**
 * @author ch
 * @date 2020/3/23-16:59
 * @desc 刻度view
 */
public class ScaleView extends View {
    /**
     * 左右边距
     */
    private int margin;

    /**
     * 刻度颜色
     */
    private int lineColor;
    /**
     * 背景色
     */
    private int backgroundColor;
    /**
     * 数据颜色
     */
    private int dataColor;

    /**
     * 刻度线宽度
     */
    private int lineWidth;

    /**
     * 刻度线高度
     */
    private int lineHeight;

    /**
     * 分隔
     */
    private int lineSpace;

    /**
     * 是否需要刻度
     */
    private boolean needLine;

    /**
     * 是否需要文字
     */
    private boolean needText;

    /**
     * 文字大小
     */
    private int textSize;

    /**
     * 刻度线
     */
    private Paint paintLine;

    /**
     * 刻度文字
     */
    private Paint paintText;

    /**
     * 数据
     */
    private Paint paintData;

    /**
     * 背景
     */
    private Paint paintBackground;

    /**
     * 宽度
     */
    private int currentWidth;

    /**
     * 高度
     */
    private int currentHeight;

    /**
     * 数据开始
     */
    private int currentStart;

    /**
     * 数据截止
     */
    private int currentEnd;


    public ScaleView(Context context) {
        super(context);
        init(null);
    }

    public ScaleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        initAttrs(attrs);

        paintLine = new Paint();
        paintLine.setColor(lineColor);
        paintLine.setStrokeWidth(lineWidth);
        paintLine.setStyle(Paint.Style.FILL);

        paintText = new Paint();
        paintText.setColor(lineColor);
        paintText.setStrokeWidth(lineWidth);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setTextSize(textSize);

        paintData = new Paint();
        paintData.setColor(dataColor);
        paintData.setStrokeWidth(lineWidth);
        paintData.setStyle(Paint.Style.FILL);

        paintBackground = new Paint();
        paintBackground.setColor(backgroundColor);
        paintBackground.setStrokeWidth(lineWidth);
        paintBackground.setStyle(Paint.Style.FILL);
    }

    /**
     * 初始化自定义属性
     *
     * @param attrs attrs
     */
    private void initAttrs(AttributeSet attrs) {
        TypedArray attributes = getContext().obtainStyledAttributes(
                attrs, R.styleable.ScaleView);

        lineColor = attributes.getColor(R.styleable.ScaleView_h_lineColor, Color.BLACK);
        dataColor = attributes.getColor(R.styleable.ScaleView_h_dataColor, Color.RED);
        backgroundColor = attributes.getColor(R.styleable.ScaleView_h_backgroundColor, Color.GRAY);

        lineWidth = (int) attributes.getDimension(R.styleable.ScaleView_h_lineWidth, 1);
        lineHeight = (int) attributes.getDimension(R.styleable.ScaleView_h_lineHeight, 15);
        lineSpace = attributes.getInt(R.styleable.ScaleView_h_lineSpace, 16) * 2 + 1;
        textSize = (int) attributes.getDimension(R.styleable.ScaleView_h_textSize, 14);
        margin = (int) attributes.getDimension(R.styleable.ScaleView_h_margin, 0);
        needLine = attributes.getBoolean(R.styleable.ScaleView_h_needLine, true);
        needText = attributes.getBoolean(R.styleable.ScaleView_h_needText, true);
        attributes.recycle();
    }

    /**
     * 设置数据
     *
     * @param start 开始下标
     * @param end   截止下标
     */
    public void setData(int start, int end) {
        if (start >= 0 && end > 0) {
            currentStart = start * 2;
            currentEnd = end * 2;
            invalidate();
        } else {
            currentStart = 0;
            currentEnd = 0;
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        currentWidth = getMeasuredWidth() - margin * 2;
        currentHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        drawData(canvas, 0, lineSpace - 3, paintBackground);

        if (currentStart >= 0 && currentEnd > 0) {
            drawData(canvas, currentStart, currentEnd, paintData);
        }

        if (needLine) {
            for (int i = 0; i < lineSpace - 1; i++) {
                float temX = getTemX(i);
                float tmpEndY = i % 2 == 0 ? 0 : lineHeight >> 1;
                //绘制刻度线
                canvas.drawLine(temX, lineHeight, temX, tmpEndY, paintLine);
                //绘制文字
                if (needText && i % 2 == 0) {
                    canvas.drawText(String.valueOf(i / 2 + 8), temX, currentHeight - lineHeight / 2, paintText);
                }
            }
        }
    }

    /**
     * 绘制数据
     *
     * @param canvas 画布
     * @param start  开始位置
     * @param end    结束位置
     */
    private void drawData(Canvas canvas, int start, int end, Paint paint) {
        float left = getTemX(start);
        float right = getTemX(end);
        float top = 0;
        float bottom = lineHeight;
        canvas.drawRect(left, top, right, bottom, paint);
    }

    /**
     * 获取x 坐标位置
     *
     * @param temX 下标
     * @return float
     */
    private float getTemX(int temX) {
        return currentWidth / lineSpace * temX + lineWidth * temX + margin;
    }
}