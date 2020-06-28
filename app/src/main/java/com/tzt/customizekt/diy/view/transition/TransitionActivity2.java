package com.tzt.customizekt.diy.view.transition;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tzt.customizekt.R;


public class TransitionActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TransitionActivity2", "onCreate: ");
        setContentView(R.layout.activity_transition2);

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TransitionActivity2", "onResume: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("TransitionActivity2", "onStart: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("TransitionActivity2", "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("TransitionActivity2", "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("TransitionActivity2", "onDestroy: ");
    }
}