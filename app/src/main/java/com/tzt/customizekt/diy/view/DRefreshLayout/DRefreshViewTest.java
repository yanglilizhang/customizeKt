package com.tzt.customizekt.diy.view.DRefreshLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tzt.customizekt.R;


public class DRefreshViewTest extends AppCompatActivity {

    private Button btn_refresh;
    private Button btn_newpage;
    private Button btn_loadmore;
    private DRefreshLayout dRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drefresh_view_test);

        btn_refresh = findViewById(R.id.btn_refresh);
        btn_newpage = findViewById(R.id.btn_newpage);
        btn_loadmore = findViewById(R.id.btn_loadmore);
        dRefreshLayout = findViewById(R.id.drefreshLayout);

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dRefreshLayout.startRefreshing();
            }
        });

        btn_newpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dRefreshLayout.startNewPage();
            }
        });

        btn_loadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dRefreshLayout.startLoadingmore();
            }
        });

        dRefreshLayout.setdRefreshListener(new DRefreshLayout.DRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("Test", "onRefresh: " );
                handler.sendEmptyMessageDelayed(0,3000);
            }

            @Override
            public void onNewPage() {
                Log.e("Test", "onNewPage: " );
//                handler.sendEmptyMessageDelayed(1,3000);
            }

            @Override
            public void onLoadmore() {
                Log.e("Test", "onLoadmnore: " );
                handler.sendEmptyMessageDelayed(1,3000);
            }
        });
    }


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    dRefreshLayout.stopRefreshing();
                    break;
                case 1:
                    dRefreshLayout.stopLoadingmore();
                    break;
            }
        }
    };
}