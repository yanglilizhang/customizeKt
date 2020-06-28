package com.tzt.customizekt.diy.view.dropdownView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.tzt.customizekt.R;

public class DropDownTest extends AppCompatActivity {

    private DropDownView dropDownView;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_down_test);

        findViewById(R.id.ed_choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "onClick: ");
                if (dropDownView == null) {
                    dropDownView = DropDownView.make(v)
                            .setCustomeView(R.layout.diy_dropdown_view_inner1);
                }
                if (!dropDownView.isShow()) {
                    dropDownView.show();
                } else {
                    dropDownView.hide();
                }
//                if (popupWindow == null) {
//                    showPop(v);
//                } else {
//                    hidePop();
//                }
            }
        });

        findViewById(R.id.btn_hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dropDownView != null) {
                    dropDownView.hide();
                }
            }
        });
    }

    private void showPop(View anthor) {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(this);
            popupWindow.setHeight(-2);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            popupWindow.setWidth(metrics.widthPixels);
            View view = LayoutInflater.from(this).inflate(R.layout.diy_dropdown_view_inner1, null, false);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            popupWindow.setContentView(view);
            popupWindow.setAnimationStyle(R.style.dropdownStyle);
            popupWindow.setOutsideTouchable(true);
            popupWindow.update();
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    // 蒙版效果恢复
                    WindowManager.LayoutParams attributes = getWindow().getAttributes();
                    attributes.alpha=1f;
                    getWindow().setAttributes(attributes);
                }
            });
        }
        popupWindow.showAsDropDown(anthor, anthor.getWidth() / 2, 0);
        // 蒙版效果
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.alpha=0.4f;
        getWindow().setAttributes(attributes);
    }

    private void hidePop() {
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;

        }
    }
}