package com.huadin.assetstatistics.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by admin on 2017/7/19.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter {
    public  ArrayList<T> list;
    public Context context;
    public BaseAdapter(Context context,ArrayList<T> list) {
        this.list = list;
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(),getItemLayoutId(),null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final MyHolder holder1 = (MyHolder) holder;

        setItemView(holder1.itemView,position);

        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder1.getLayoutPosition());
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }



    public OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    class MyHolder extends RecyclerView.ViewHolder{

        public MyHolder(View itemView) {
            super(itemView);
        }
    }

    public abstract int getItemLayoutId();
    public abstract void setItemView(View itemView,int postion);
}
