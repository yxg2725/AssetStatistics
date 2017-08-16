package com.huadin.assetstatistics.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.bean.AssetsStyle;

import java.util.ArrayList;

/**
 * Created by 华电 on 2017/7/19.
 */

public class MyAdapter extends BaseAdapter<AssetsStyle>  {
  public MyAdapter(Context context, ArrayList<AssetsStyle> list) {
    super(context,list);
  }

  @Override
  public int getItemLayoutId() {
    return R.layout.item_inventory;
  }



  @Override
  public void setItemView(View itemView, int postion) {

    TextView tableName = (TextView) itemView.findViewById(R.id.table_name);//名称
    TextView tableNum = (TextView) itemView.findViewById(R.id.table_num);//数量
    TextView existNum = (TextView) itemView.findViewById(R.id.tv_exist_num);//库存
    TextView outNum = (TextView) itemView.findViewById(R.id.tv_out_num);//出库
    TextView notGood = (TextView) itemView.findViewById(R.id.tv_not_good);//不合格

    AssetsStyle asset = list.get(postion);

    tableName.setText(asset.getAsssetStyle());
    tableNum.setText(asset.getCount() + "");
    existNum.setText(asset.getExistNum()+"");
    outNum.setText(asset.getOutNum()+"");
    notGood.setText(asset.getNotGood()+"");

  }


}
