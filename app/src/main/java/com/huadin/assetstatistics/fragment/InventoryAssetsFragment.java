package com.huadin.assetstatistics.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.huadin.assetstatistics.event.Event;
import com.huadin.assetstatistics.utils.Contants;
import com.huadin.assetstatistics.utils.DbUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

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
    mAdapter = new MyAdapter(mActivity, assets);
    mRecyclerview.setAdapter(mAdapter);
  }


  @Override
  public void onResume() {
    super.onResume();
    initData();
  }

  @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
  public void onInventoryAssetEvent(Event.InventoryAssetsEvent event){
    if(event.getTag().equals("InventoryAssetsFragment")){
      initData();
    }
  }

  private void initData() {

    assets.clear();
    for (int i = 0; i < Contants.assetsType.length; i++) {
      AssetsStyle asset = new AssetsStyle();
      asset.setAsssetStyle(Contants.assetsType[i]);

      //总个数查询
      List<AssetDetail> list = DbUtils.queryByName(AssetDetail.class, Contants.assetsType[i]);
      asset.setCount(list.size());

      //库存个数查询
      List<AssetDetail> existList = DbUtils.queryByStyleAndExist(AssetDetail.class, Contants.assetsType[i], "入库");
      asset.setExistNum(existList.size());

      //出库个数查询
      List<AssetDetail> outList = DbUtils.queryByStyleAndExist(AssetDetail.class, Contants.assetsType[i], "出库");
      asset.setOutNum(outList.size());

      //不合格个数查询
      List<AssetDetail> notGoodList = DbUtils.queryByStyleAndIsGood(AssetDetail.class, Contants.assetsType[i]);
      asset.setNotGood(notGoodList.size());

      assets.add(asset);
    }

   /* Log.i("InventoryAssetsFragment", "size: " + DbUtils.queryAll(AssetsStyle.class).size());
    mAdapter = new MyAdapter(assets);
    mRecyclerview.setAdapter(mAdapter);*/

    mAdapter.notifyDataSetChanged();
  }


  private void initListener() {
    mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(int position) {
        AssetsStyle asset = assets.get(position);
       /* Bundle bundle = new Bundle();
       // bundle.putParcelable("asset",asset);
        IntentUtils.startActivity(mActivity, AssetsItemActivity.class,bundle,false);*/

        Intent intent = new Intent(mActivity, AssetsItemActivity.class);
        intent.putExtra("assetName", asset.getAsssetStyle());
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.right_in, R.anim.leftout);

      }
    });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

}
