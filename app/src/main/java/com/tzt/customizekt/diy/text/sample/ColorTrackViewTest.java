package com.tzt.customizekt.diy.text.sample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tzt.customizekt.R;
import com.tzt.customizekt.diy.text.ColorTrackTextView3;

import java.util.ArrayList;
import java.util.List;

public class ColorTrackViewTest extends AppCompatActivity {

    private TextView tv_progress1;
    private SeekBar seekBar1;
    private TextView tv_progress2;
    private SeekBar seekBar2;
    private ColorTrackTextView3 colorTrackTextView;

    private LinearLayout ll_tablayout;
    private ColorTrackTextView3 tab_home;
    private ColorTrackTextView3 tab_mine;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_track_view_test);
        initView();
    }

    private void initView() {
        colorTrackTextView = findViewById(R.id.tv_name);
        tv_progress1 = findViewById(R.id.tv_progress1);
        seekBar1 = findViewById(R.id.seekBar1);

        seekBar1.setMax(200);
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_progress1.setText(progress + "%");
                float fac = ((float) progress) / 100;
                colorTrackTextView.setProgress(fac);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tv_progress2 = findViewById(R.id.tv_progress2);
        seekBar2 = findViewById(R.id.seekBar2);

        seekBar2.setMax(200);
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_progress2.setText(progress + "%");
                float fac = -((float) progress) / 100;
                colorTrackTextView.setProgress(fac);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ll_tablayout = findViewById(R.id.ll_tablayout);
        tab_home = findViewById(R.id.tab_home);
        tab_mine = findViewById(R.id.tab_mine);
        viewPager = findViewById(R.id.viewPager);

        List<Fragment> list = new ArrayList<>();
        list.add(new MyFragment(R.layout.fg_my1));
        list.add(new MyFragment(R.layout.fg_my2));
        list.add(new MyFragment(R.layout.fg_my3));
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), list));

        setUpWithViewPager(viewPager, ll_tablayout);
    }

    private void setUpWithViewPager(ViewPager viewPager, final LinearLayout ll_tablayout) {
        if (viewPager.getAdapter().getCount() != ll_tablayout.getChildCount()) {
            return;
        }
        ((ColorTrackTextView3) ll_tablayout.getChildAt(viewPager.getCurrentItem())).setProgress(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("setUpWithViewPager", "onPageScrolled: " + position + "," + positionOffset + "," + positionOffsetPixels);

                ColorTrackTextView3 currentTab = (ColorTrackTextView3) ll_tablayout.getChildAt(position);
                currentTab.setProgress(positionOffset - 1);
                if ((position + 1) < ll_tablayout.getChildCount()) {
                    ColorTrackTextView3 nextTab = (ColorTrackTextView3) ll_tablayout.getChildAt(position + 1);
                    nextTab.setProgress(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}