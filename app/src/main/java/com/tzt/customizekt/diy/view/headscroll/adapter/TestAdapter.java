package com.tzt.customizekt.diy.view.headscroll.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * author: DragonForest
 * time: 2020/1/9
 */
public class TestAdapter extends RecyclerView.Adapter {
    List<String> list;

    public TestAdapter(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView tv=new TextView(parent.getContext());
        tv.setTextSize(30);
        tv.setTextColor(Color.BLACK);
        ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        return new TestViewHolder(tv);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String title = list.get(position);
        ((TextView)holder.itemView).setText(title);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TestViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv=(TextView) itemView;
        }
    }
}