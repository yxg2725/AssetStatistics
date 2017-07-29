package com.huadin.assetstatistics.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.activity.AssetDetailActivity;
import com.huadin.assetstatistics.activity.MainActivity;
import com.huadin.assetstatistics.adapter.OutAssetsAdapter;
import com.huadin.assetstatistics.app.MyApplication;
import com.huadin.assetstatistics.bean.AssetDetail;
import com.huadin.assetstatistics.event.Event;
import com.huadin.assetstatistics.utils.DbUtils;
import com.huadin.assetstatistics.utils.RFIDUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 入库统计
 */

public class StorageFragment extends BaseFragment {

  @BindView(R.id.btn_scan)
  Button mBtnScan;
  @BindView(R.id.rv)
  RecyclerView mRv;
  Unbinder unbinder;
  private ArrayList<AssetDetail> assetDetails = new ArrayList<>();
  private OutAssetsAdapter mAdapter;


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_outbound, null);
    unbinder = ButterKnife.bind(this, view);

    EventBus.getDefault().register(this);
    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    ((MainActivity)mActivity).mToolbar.setTitle("入库统计");

    initView();
    initData();
  }



  private void initView() {
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
    mRv.setLayoutManager(linearLayoutManager);
    mAdapter = new OutAssetsAdapter(mActivity,assetDetails);
    mRv.setAdapter(mAdapter);
  }

  private void initData() {
    //查询数据库 已入库的资产 并且是合格的
    List<AssetDetail> list = DbUtils.queryByExistAndGood(AssetDetail.class, "yes");
    assetDetails.clear();
    assetDetails.addAll(list);
    mAdapter.notifyDataSetChanged();
  }
  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @OnClick(R.id.btn_scan)
  public void onViewClicked() {
    final MainActivity activity = (MainActivity) mActivity;
    if(!MyApplication.connectSuccess){
          RFIDUtils.getInstance(activity).connectAsync();
    }
    if(!MyApplication.connectSuccess){
      return;
    }

    //RFIDUtils.getInstance().readData(activity);
   // RFIDUtils.getInstance().readOneByOne(activity,"StorageFragment");
    /*Intent intent = new Intent(activity, AssetDetailActivity.class);
    intent.putExtra("result","000001");
    activity.startActivity(intent);*/

    RFIDUtils.getInstance(activity).readAsync("StorageFragment");
  }


  @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
  public void onInventoryAssetEvent(Event.InventoryAssetsEvent event){
    if(event.getTag().equals("StorageFragment")){
      initData();
    }
  }
  @Override
  public void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

}
