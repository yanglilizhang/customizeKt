package com.tzt.customizekt.diy.view.refreshView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.tzt.customizekt.R;

public class PullToRefreshTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_to_refresh_test);

        final PullToRefreshView refreshView = findViewById(R.id.pullToRefreshView);
        refreshView.setHeaderView(new MyHeaderView2(this));
        refreshView.startRefresh();
        refreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!refreshView.isLoading()) {
                    refreshView.startRefresh();
                }else {
                    refreshView.endRefresh();
                }
            }
        });
    }
}