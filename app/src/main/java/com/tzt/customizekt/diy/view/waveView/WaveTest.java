package com.tzt.customizekt.diy.view.waveView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

import com.tzt.customizekt.R;

public class WaveTest extends AppCompatActivity {

    private WaveView3 waveView;
    private WaveView4 waveView2;
    private WaveMoveShipView waveView3;
    private WaveJinMuView waveView4;
    private Button btn_change;
    private EditText ed_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave_test);
        waveView = findViewById(R.id.waveView);
        waveView2 = findViewById(R.id.waveView2);
        waveView3 = findViewById(R.id.waveView3);
        waveView4 = findViewById(R.id.waveJinmuView);
        waveView.startAnim();
        waveView2.startAnim();
        waveView4.startAnim();

        btn_change = findViewById(R.id.btn_change);
        ed_progress = findViewById(R.id.ed_progress);

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String progressStr = ed_progress.getText().toString().trim();
                if ("".equals(progressStr))
                    progressStr = "0";
                float progress = Float.parseFloat(progressStr);
                waveView.setProgress(progress);
                waveView2.setProgress(progress);
                waveView3.setProgress(progress / 100);
                waveView4.setLeftProgress(progress/100);
                waveView4.setRightProgress(1-progress/100);

            }
        });
    }
}