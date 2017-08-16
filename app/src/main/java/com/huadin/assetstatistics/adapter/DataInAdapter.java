package com.huadin.assetstatistics.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.activity.DataInActivity;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by 华电 on 2017/8/14.
 */

public class DataInAdapter extends BaseAdapter<String> {
  public DataInAdapter(Context context, ArrayList<String> list) {
    super(context, list);
  }

  @Override
  public int getItemLayoutId() {
    return R.layout.item_text;
  }

  @Override
  public void setItemView(View itemView, final int postion) {
    final TextView tv = (TextView) itemView.findViewById(R.id.tv);
    tv.setText(list.get(postion));
    setItemBackGround(tv,postion);

    //长按监听
    itemView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {

        if(longClikcListener != null){
          longClikcListener.onItemLongClick(postion);
        }
        return true;
      }
    });
  }

  public void setItemBackGround(TextView tv,int position) {

    DataInActivity context = (DataInActivity) this.context;
    Set<Integer> positionSet = context.positionSet;
    if (positionSet.contains(position)) {
      tv.setBackgroundColor(Color.BLUE);
    } else {
      tv.setBackgroundColor(Color.WHITE);
    }
  }

  public OnItemLongClickListener longClikcListener;
  public interface OnItemLongClickListener{
    void onItemLongClick(int position);
  }

  public void setOnItemLongClickListener(OnItemLongClickListener longClikcListener){
    this.longClikcListener = longClikcListener;
  }
}
