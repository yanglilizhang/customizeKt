package com.tzt.customizekt.study.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.graphics.ColorUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 状态栏工具类
 *
 * @author 韩龙林
 * @date 2019/9/23 14:36
 */

/**
 * * 各个版本的沉浸式状态栏的实现方法：
 * Android4.4(api19)--Android5.0(api21):
 * 这个阶段的实现方式为： 通过FLAG_TRANSLUCENT_STATUS设置状态栏为透明并且为全屏模式，然后通过添加一个与StatusBar一样大小的View,将View的背景设置为要设置的颜色
 * <p>
 * Android5.0-(api21)--Android6.0(api23)
 * 从Android5.0开始，加入了一个重要的属性 android:statusBarColor 和方法 setStatusBarColor(), 通过这个方法我们就可以轻松实现沉浸式状态栏。但是在Andrid6.0一下版本官方还不支持设置状态栏的文字和图标颜色，目前只有小米和魅族的RO提供了支持。
 * <p>
 * Android6.0（api23）以上版本:
 * 其实Android 6.0以上版本的实现方式和Android5.0+是一样的，区别是从Android6.0开始，官方支持改变状态栏的文字和图标的颜色。Android6.0一下设置状态栏的文字和图标颜色现在只有小米和魅族支持。
 */


public class StatusBarUtil {
    private static StatusBarUtil instance = null;

    private StatusBarUtil() {
    }

    public static StatusBarUtil getInstance() {
        if (instance == null) {
            instance = new StatusBarUtil();
        }
        return instance;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        try {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 设置状态栏颜色
     *
     * @param window
     * @param color
     */
    public void setColor(Window window, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(color);
            setTextDark(window, !isDarkColor(color));
        }
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity
     * @param color
     */
    public void setColor(Activity activity, int color) {
        if (activity != null) {
            setColor(activity.getWindow(), color);
        }
    }

    /**
     * 设置状态栏文字和图标颜色为深色
     *
     * @param window
     * @param isDark
     */
    public void setTextDark(Window window, boolean isDark) {
        Log.e("StatusBarUtil", "setTextDark:设置状态栏文字颜色");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = window.getDecorView();
            int systemUiVisibility = decorView.getSystemUiVisibility();
            if (isDark) {
                decorView.setSystemUiVisibility(systemUiVisibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decorView.setSystemUiVisibility(systemUiVisibility | ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            OSUtil.ROM romType = OSUtil.getRomType();
            if (romType == OSUtil.ROM.MIUI) {
//                setMIUIDark(window, isDark);
                MIUISetStatusBarLightMode(window, isDark);
            } else if (romType == OSUtil.ROM.Flyme) {
                setFlymeDark(window, isDark);
            }
        }
    }

    /**
     * 设置状态栏文字和图标颜色为深色
     *
     * @param activity
     * @param isDark
     */
    public void setTextDark(Activity activity, boolean isDark) {
        if (activity != null) {
            setTextDark(activity.getWindow(), isDark);
        }
    }

    /**
     * 判断颜色是否为深色
     *
     * @param color
     * @return
     */
    public boolean isDarkColor(int color) {
        return ColorUtils.calculateLuminance(color) < 0.5;
    }


    private static void setMIUIDark(Window window, boolean isDark) {
        try {
            Class<? extends Window> clazz = window.getClass();
            int darkModeFlag;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(window, isDark ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    }
                }
            } catch (Exception e) {

            }
        }
        return result;
    }

    private static void setFlymeDark(Window window, boolean isDark) {
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (isDark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setTransparent(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }
    }

    public void setTransparent(Activity activity) {
        if (activity != null) {
            setTransparent(activity.getWindow());
        }
    }

    public void setTopPaddingView(View topView) {
        boolean fitsSystemWindows = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            fitsSystemWindows = topView.getFitsSystemWindows();
        }
        if(!fitsSystemWindows) {
            int statusBarHeight = getStatusBarHeight(topView.getContext());
            // 设置头部padding
            int paddingTop = statusBarHeight + topView.getPaddingTop();
            int paddingRight = topView.getPaddingRight();
            int paddingLeft = topView.getPaddingLeft();
            int paddingBottom = topView.getPaddingBottom();
            topView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            ViewGroup.LayoutParams lp = topView.getLayoutParams();
            lp.height += statusBarHeight;
            topView.setLayoutParams(lp);
        }else{
        }
    }
}