package com.huadin.assetstatistics.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.bean.Asset;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 华电 on 2017/7/19.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
  private final ArrayList<Asset> assets;


  public MyAdapter(ArrayList<Asset> assets) {
    this.assets = assets;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = View.inflate(parent.getContext(), R.layout.item_inventory, null);
    return new MyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {

    Asset asset = assets.get(position);

      holder.tableName.setText(asset.getName());
      holder.tableNum.setText(asset.getCount() + "");
      holder.tableUnit.setText(asset.getUnit());


  }

  @Override
  public int getItemCount() {
    return assets == null ? 0 : assets.size();
  }

  class MyViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.table_name)
    TextView tableName;
    @BindView(R.id.table_num)
    TextView tableNum;
    @BindView(R.id.table_unit)
    TextView tableUnit;
    public MyViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this,itemView);
    }
  }
}
