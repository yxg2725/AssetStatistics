package com.huadin.assetstatistics.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.adapter.MultilStyleAdapter;
import com.huadin.assetstatistics.bean.AssetDetail;
import com.huadin.assetstatistics.event.Event;
import com.huadin.assetstatistics.utils.DbUtils;
import com.huadin.assetstatistics.utils.RFIDUtils;
import com.huadin.assetstatistics.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BatchScanActivity extends BaseActivity {

  @BindView(R.id.recyclerview)
  RecyclerView recyclerview;
  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.btn_stop)
  Button btnStop;
  @BindView(R.id.tv_result)
  TextView tvResult;
  @BindView(R.id.btn_out)
  Button btnOut;
  @BindView(R.id.btn_enter)
  Button btnEnter;
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
    initListener();
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

  private void initListener() {
    mAdapter.setOnItemClickListener(new MultilStyleAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(int position) {
        Intent intent = new Intent(BatchScanActivity.this, AssetDetailActivity.class);

        Object object = list.get(position);
        if (object instanceof String) {
          String barcode = (String) object;
          intent.putExtra("result", barcode);
        } else {
          AssetDetail asset = (AssetDetail) object;
          String barcode = asset.getBarcode();
          intent.putExtra("result", barcode);
        }
        intent.putExtra("tag", "BatchScanActivity");
        startActivity(intent);
      }
    });
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
        if (assetDetail == null) {
          list.add(next);
        } else {
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
       // RFIDUtils.getInstance(BatchScanActivity.this).disConnect();
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
    if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == android.R.id.home) {
      pauseScan();
    }
    return super.onKeyDown(keyCode, event);
  }

  @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
  public void onBatchscanResultEvent(Event.InventoryAssetsEvent event) {
    if (event.getTag().equals(this.getClass().getSimpleName())) {

      //更新数据
      list.clear();
      for (int i = 0; i < barcodeList.size(); i++) {
        AssetDetail assetDetail = DbUtils.queryByCode(AssetDetail.class, barcodeList.get(i));
        if (assetDetail == null) {
          list.add(barcodeList.get(i));
        } else {
          list.add(assetDetail);
        }
      }

      if (mAdapter != null) {
        mAdapter.notifyDataSetChanged();

      }
    }
  }

  @OnClick({R.id.btn_out, R.id.btn_enter})
  public void onViewClicked(View view) {
    for (Object object : list) {
      if (object instanceof String) {
        ToastUtils.show(btnEnter,"有未统计的工具，请统计完成后再出入库！");
        return;
      }
    }


    for (int i = 0; i < list.size(); i++) {
      AssetDetail asset = (AssetDetail)list.get(i);
      switch (view.getId()) {
        case R.id.btn_out:
          asset.setExist("出库");
          break;
        case R.id.btn_enter:
          asset.setExist("入库");
          break;
      }

      DbUtils.update(asset);
    }

    finish();
  }
}
