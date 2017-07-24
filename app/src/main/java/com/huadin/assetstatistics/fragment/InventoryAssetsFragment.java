package com.huadin.assetstatistics.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.activity.AssetsItemActivity;
import com.huadin.assetstatistics.activity.MainActivity;
import com.huadin.assetstatistics.adapter.MyAdapter;
import com.huadin.assetstatistics.bean.AssetDetail;
import com.huadin.assetstatistics.bean.AssetsStyle;
import com.huadin.assetstatistics.bean.dao.DaoManager;
import com.huadin.assetstatistics.gen.AssetsStyleDao;
import com.huadin.assetstatistics.gen.DaoMaster;
import com.huadin.assetstatistics.utils.Contants;
import com.huadin.assetstatistics.utils.DbUtils;
import com.huadin.assetstatistics.utils.IntentUtils;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.ContentValues.TAG;

/**
 * Created by 华电 on 2017/7/19.
 */

public class InventoryAssetsFragment extends BaseFragment {


  @BindView(R.id.recyclerview)
  RecyclerView mRecyclerview;
  @BindView(R.id.ll_table_title)
  LinearLayout mLlTableTitle;
  Unbinder unbinder;
  private MyAdapter mAdapter;
  private ArrayList<AssetsStyle> assets;

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
    initListener();
  }


  private void initView() {
    ((MainActivity) mActivity).mToolbar.setTitle("库存资产");
    mRecyclerview.setLayoutManager(new LinearLayoutManager(mActivity));

    assets = new ArrayList<>();
    DbUtils.deleteAll(AssetsStyle.class);

    for (int i = 0; i < Contants.assetsType.length; i++) {
      AssetsStyle asset = new AssetsStyle();
      asset.setAsssetStyle(Contants.assetsType[i]);

      List<AssetDetail> list = DbUtils.query(AssetDetail.class, Contants.assetsType[i]);
      asset.setCount(list.size());

      asset.setUnit("个");

      //插入数据库
      DbUtils.insert(asset);

      assets.add(asset);
    }

    Log.i("InventoryAssetsFragment", "size: " + DbUtils.queryAll(AssetsStyle.class).size());
    mAdapter = new MyAdapter(assets);
    mRecyclerview.setAdapter(mAdapter);
  }

  private void initListener() {
    mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(int position) {
        AssetsStyle asset = assets.get(position);
        Bundle bundle = new Bundle();
       // bundle.putParcelable("asset",asset);
        IntentUtils.startActivity(mActivity, AssetsItemActivity.class,bundle,false);
      }
    });
  }
  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }
}
