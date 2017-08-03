package com.huadin.assetstatistics.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.adapter.MultilStyleAdapter;
import com.huadin.assetstatistics.bean.AssetDetail;
import com.huadin.assetstatistics.utils.DbUtils;
import com.huadin.assetstatistics.utils.RFIDUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PatchScanActivity extends BaseActivity {

  @BindView(R.id.recyclerview)
  RecyclerView recyclerview;
  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.btn_stop)
  Button btnStop;
  @BindView(R.id.tv_result)
  TextView tvResult;
  private ArrayList<Object> list = new ArrayList();
  private ArrayList<String> barcodeList = new ArrayList();
  private HashSet<String> set;
  private MultilStyleAdapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_patch_scan);
    ButterKnife.bind(this);


    EventBus.getDefault().register(this);
    initView();

    initData();
  }


  private void initView() {
    initToolbar(toolbar, "批量扫描", true);
    barcodeList.clear();
    list.clear();
    tvResult.setVisibility(View.VISIBLE);
    btnStop.setVisibility(View.VISIBLE);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    recyclerview.setLayoutManager(linearLayoutManager);
    //mAdapter = new OutAssetsAdapter(this, list);
    mAdapter = new MultilStyleAdapter(this, list);
    recyclerview.setAdapter(mAdapter);
  }


  private void initData() {
    RFIDUtils.getInstance(this).readBatch(this);
  }

  @Subscribe
  public void onReceiveDataEvent(final HashSet<String> set) {
    this.set = set;
    Iterator<String> iterator = set.iterator();
    while (iterator.hasNext()) {
      String next = iterator.next();
      if (!barcodeList.contains(next)) {
        barcodeList.add(next);
        AssetDetail assetDetail = DbUtils.queryByCode(AssetDetail.class, next);
        if(assetDetail == null){
          list.add(next);
        }else{
          assetDetail.setExist("入库");
          list.add(assetDetail);
        }



      }
    }

    if (mAdapter != null) {
      mAdapter.notifyDataSetChanged();

    }

    tvResult.setText("扫描到" + set.size() + "件工具");
    /*DialogUtils.getInstance().showMDDialog(this, "扫描结果", "扫描到" + set.size() + "件工具", new DialogUtils.OnPositiveCall() {
      @Override
      public void goOn() {
        RFIDUtils.mHandler.removeCallbacksAndMessages(null);
       // RFIDUtils.getInstance(PatchScanActivity.this).disConnect();
        if (DialogUtils.getInstance().dialog1 != null && DialogUtils.getInstance().dialog1.isShowing()){
          DialogUtils.getInstance().dialog1.dismiss();
          DialogUtils.getInstance().dialog1 = null;
        }
      }
    });*/
  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
    pauseScan();
  }


  @Override
  protected void onStop() {
    super.onStop();
    pauseScan();
  }

  @OnClick(R.id.btn_stop)
  public void onViewClicked() {
    pauseScan();
  }

  private void pauseScan() {
    if (RFIDUtils.mHandler != null) {
      RFIDUtils.mHandler.removeCallbacksAndMessages(null);
    }
    tvResult.setVisibility(View.GONE);
    btnStop.setVisibility(View.GONE);
  }


  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == android.R.id.home){
      pauseScan();
    }
    return super.onKeyDown(keyCode, event);
  }
}
