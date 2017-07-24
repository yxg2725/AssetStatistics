package com.huadin.assetstatistics.adapter;

import android.view.View;
import android.widget.TextView;

import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.bean.AssetsStyle;

import java.util.ArrayList;

/**
 * Created by 华电 on 2017/7/19.
 */

public class MyAdapter extends BaseAdapter<AssetsStyle>  {
  public MyAdapter(ArrayList<AssetsStyle> list) {
    super(list);
  }

  @Override
  public int getItemLayoutId() {
    return R.layout.item_inventory;
  }



  @Override
  public void setItemView(View itemView, int postion) {

    TextView tableName = (TextView) itemView.findViewById(R.id.table_name);
    TextView tableNum = (TextView) itemView.findViewById(R.id.table_num);
    TextView tableUnit = (TextView) itemView.findViewById(R.id.table_unit);

    AssetsStyle asset = list.get(postion);
    tableName.setText(asset.getAsssetStyle());
    tableNum.setText(asset.getCount() + "");
    tableUnit.setText(asset.getUnit());
  }


}
