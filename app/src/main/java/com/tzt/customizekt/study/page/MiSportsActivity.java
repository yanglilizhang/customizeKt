package com.tzt.customizekt.study.page;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.tzt.customizekt.R;
import com.tzt.customizekt.study.misports.LogUtils;
import com.tzt.customizekt.study.misports.MISportsConnectView;
import com.tzt.customizekt.study.misports.SportsConnectView;
import com.tzt.customizekt.study.misports.SportsData;

public class MiSportsActivity extends AppCompatActivity {
    private Handler handler;
    boolean connect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_sports);
        LogUtils.setLogLevel(LogUtils.LOG_LEVEL_DEBUG);

        final SportsConnectView miSportsConnectView = findViewById(R.id.mi_sports_loading_view);

//        SportsData sportsData = new SportsData();
//        sportsData.step = 2714;
//        sportsData.distance = 1700;
//        sportsData.calories = 34;
//        sportsData.progress = 75;
//        miSportsConnectView.setSportsData(sportsData);
//
//        handler = new Handler();
//        final Button connectButton = (Button) findViewById(R.id.connect_button);
//        connectButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        connect = !connect;
//                        miSportsConnectView.setConnected(connect);
//                        connectButton.setText(connect ? getString(R.string.disconnect) : getString(R.string.connect));
//                    }
//                }, 500);
//            }
//        });
    }
}

