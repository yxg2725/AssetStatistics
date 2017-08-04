package com.huadin.assetstatistics.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.utils.DbUtils;
import com.huadin.assetstatistics.utils.DialogUtils;
import com.huadin.assetstatistics.widget.dragItem.ItemTouchHelperAdapter;
import com.huadin.assetstatistics.widget.dragItem.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.Collections;

import static jxl.format.PaperSize.D;

/**
 * Created by admin on 2017/7/19.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter implements ItemTouchHelperAdapter {
    public  ArrayList<T> list;
    public Context context;
    private MyHolder holder1;

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
        holder1 = (MyHolder) holder;

        setItemView(holder1.itemView,position);

        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
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

    public class MyHolder extends RecyclerView.ViewHolder{

        public  TextView tv;
        public  ImageView iv;

        public MyHolder(View itemView) {
            super(itemView);
            //删除的textView
            tv = (TextView) itemView.findViewById(R.id.tv_text);
            iv = (ImageView) itemView.findViewById(R.id.iv_img);
        }
    }

    public abstract int getItemLayoutId();
    public abstract void setItemView(View itemView,int postion);

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        //交换位置
        Collections.swap(list,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
    }

    @Override
    public void onItemDissmiss(final int position) {
        //移除数据
        DialogUtils.showMDDialog(context, "提示", "确定删除此数据？", new DialogUtils.OnResponseCallBack() {
            @Override
            public void onPositiveClick() {
                //删除数据库
                DbUtils.delete(list.get(position));
                list.remove(position);
                notifyItemRemoved(position);
            }

            @Override
            public void onNegativeClick() {
                notifyDataSetChanged();
            }
        });
    }
}
