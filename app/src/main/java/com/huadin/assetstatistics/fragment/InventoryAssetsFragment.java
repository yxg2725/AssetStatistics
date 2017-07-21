package com.huadin.assetstatistics.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.adapter.MyAdapter;
import com.huadin.assetstatistics.bean.Asset;

import junit.framework.Assert;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 华电 on 2017/7/19.
 */

public class InventoryAssetsFragment extends BaseFragment {


  @BindView(R.id.recyclerview)
  RecyclerView mRecyclerview;
  @BindView(R.id.ll_table_title)
  LinearLayout mLlTableTitle;
  Unbinder unbinder;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_inventory, null);
    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    initView();
  }


  private void initView() {
    mRecyclerview.setLayoutManager(new LinearLayoutManager(mActivity));

    ArrayList<Asset> assets = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      Asset asset = new Asset();
      asset.setName("资产" + i);
      asset.setCount(i);
      asset.setUnit("个");
      assets.add(asset);
    }
    MyAdapter adapter = new MyAdapter(assets);
    mRecyclerview.setAdapter(adapter);
  }


  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }
}
