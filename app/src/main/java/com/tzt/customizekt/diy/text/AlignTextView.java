package com.tzt.customizekt.diy.text;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * 作者： ch
 * 时间： 2019/11/1 16:41
 * 描述： 不等字数对齐
 * 来源：
 */
public class AlignTextView extends AppCompatTextView {
    public AlignTextView(Context context) {
        super(context);
    }

    public AlignTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlignTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public static final int size = 6;

    /**
     * 将给定的字符串给定的长度两端对齐
     * 如：
     * 身份证：
     * 姓  名：
     *
     * @param str 待对齐字符串
     * @Return
     */
    public SpannableStringBuilder justifyString(String str) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (TextUtils.isEmpty(str)) {
            return spannableStringBuilder;
        }
        char[] chars = str.toCharArray();
        if (chars.length >= size || chars.length == 1) {
            return spannableStringBuilder.append(str);
        }
        int l = chars.length;
        float scale = (float) (size - l) / (l - 1);
        for (int i = 0; i < l; i++) {
            spannableStringBuilder.append(chars[i]);
            if (i != l - 1) {
                SpannableString s = new SpannableString("　");//全角空格
                s.setSpan(new ScaleXSpan(scale), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.append(s);
            }
        }
        return spannableStringBuilder;
    }

    /**
     * 将给定的字符串给定的长度两端对齐
     *
     * @param str  待对齐字符串
     * @param size 汉字个数，eg:size=5，则将str在5个汉字的长度里两端对齐
     * @Return
     */
    public SpannableStringBuilder justifyString(String str, int size) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (TextUtils.isEmpty(str)) {
            return spannableStringBuilder;
        }
        char[] chars = str.toCharArray();
        if (chars.length >= size || chars.length == 1) {
            return spannableStringBuilder.append(str);
        }
        int l = chars.length;
        float scale = (float) (size - l) / (l - 1);
        for (int i = 0; i < l; i++) {
            spannableStringBuilder.append(chars[i]);
            if (i != l - 1) {
                SpannableString s = new SpannableString("　");//全角空格
                s.setSpan(new ScaleXSpan(scale), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.append(s);
            }
        }
        return spannableStringBuilder;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(justifyString(text.toString()), type);
    }


}