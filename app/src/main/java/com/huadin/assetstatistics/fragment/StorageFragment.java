package com.huadin.assetstatistics.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.activity.MainActivity;
import com.huadin.assetstatistics.adapter.OutAssetsAdapter;
import com.huadin.assetstatistics.bean.AssetDetail;
import com.huadin.assetstatistics.utils.RFIDUtils;

import java.util.ArrayList;

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

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_outbound, null);
    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    ((MainActivity)mActivity).mToolbar.setTitle("入库统计");

    initView();
  }

  private void initView() {
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
    mRv.setLayoutManager(linearLayoutManager);
    for (int i = 0; i < 20; i++) {
      AssetDetail assetDetail = new AssetDetail();

      assetDetail.setAssetName("脚扣" + i);
      assetDetail.setDeviceId("设备型号" + i);
      assetDetail.setUsedCompany("使用单位"+ i);
      assetDetail.setManufacturer("生成厂家"+ i);
      assetDetail.setDateOfProduction("生成日期"+ i);
      assetDetail.setInspectionNumber("检测编号"+ i);
      assetDetail.setArchivesNumber("档案编号"+ i);
      assetDetail.setCheckDate("校验日期"+ i);
      assetDetail.setNextCheckDate("下次校验日期"+ i);
      assetDetail.setCheckPeople("校验员"+ i);

      assetDetails.add(assetDetail);
    }
    OutAssetsAdapter mAdapter = new OutAssetsAdapter(assetDetails);
    mRv.setAdapter(mAdapter);
  }
  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @OnClick(R.id.btn_scan)
  public void onViewClicked() {

    if(!RFIDUtils.getInstance().isConnect()){
      RFIDUtils.getInstance().connect();
    }
    RFIDUtils.getInstance().readData();
  }
}
