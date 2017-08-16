package com.huadin.assetstatistics.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.adapter.MultilStyleAdapter;
import com.huadin.assetstatistics.app.MyApplication;
import com.huadin.assetstatistics.bean.AssetDetail;
import com.huadin.assetstatistics.event.Event;
import com.huadin.assetstatistics.utils.DbUtils;
import com.huadin.assetstatistics.utils.DialogUtils;
import com.huadin.assetstatistics.utils.KT50_B2.RFIDUtils2;
import com.huadin.assetstatistics.utils.RFIDUtils;
import com.huadin.assetstatistics.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.string.cancel;
import static com.huadin.assetstatistics.app.MyApplication.canScan;


public class BatchScanActivity extends BaseActivity {

  private static final int REQUEST_1 = 1;
  @BindView(R.id.recyclerview)
  RecyclerView recyclerview;
  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.btn_stop)
  Button btnStop;
  @BindView(R.id.tv_result)
  TextView tvResult;

  @BindView(R.id.tv_select_all)
  Button tvSelectAll;
  @BindView(R.id.tv_delete)
  Button tvDelete;
  @BindView(R.id.tv_select_enter)
  Button tvEnter;
  @BindView(R.id.tv_select_out)
  Button tvOut;

  @BindView(R.id.ll_bottom)
  LinearLayout llBottom;
  @BindView(R.id.fl_bottom)
  FrameLayout flBottom;
  private ArrayList<Object> list = new ArrayList();
  private ArrayList<String> barcodeList = new ArrayList();
  public Set<Integer> positionSet = new HashSet<>();
  private HashSet<String> set;
  private MultilStyleAdapter mAdapter;
  private boolean longClick = false;
  private ArrayList<AssetDetail> selectedlist;


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

    flBottom.setVisibility(View.VISIBLE);
    btnStop.setVisibility(View.VISIBLE);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    recyclerview.setLayoutManager(linearLayoutManager);
    //mAdapter = new OutAssetsAdapter(this, list);
    mAdapter = new MultilStyleAdapter(this, list);
    recyclerview.setAdapter(mAdapter);
  }

  @Override
  protected void onResume() {
    super.onResume();

  }

  private void initListener() {
    mAdapter.setOnItemClickListener(new MultilStyleAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(int position) {
        Object object = list.get(position);
        if(!longClick){
          Intent intent = new Intent(BatchScanActivity.this, AssetDetailActivity.class);

          if (object instanceof String) {
            String barcode = (String) object;
            intent.putExtra("result", barcode);
          } else {
            AssetDetail asset = (AssetDetail) object;
            String archivesNumber = asset.getArchivesNumber();
            intent.putExtra("result", archivesNumber);
          }
          intent.putExtra("tag", "BatchScanActivity");
          startActivityForResult(intent,REQUEST_1);
        }else{
          addOrRemove(position);
          /*RecyclerView.ViewHolder holder = mAdapter.getViewHolder(position);
          holder.itemView.setBackgroundColor(Color.BLUE);*/
        }

      }
    });

    mAdapter.setOnItemLongClickListener(new MultilStyleAdapter.OnItemLongClickListener() {
      @Override
      public void onItemLongClick(int position) {
        if(btnStop.getVisibility() ==View.VISIBLE ){
          btnStop.performClick();

        }
        flBottom.setVisibility(View.VISIBLE);
        llBottom.setVisibility(View.VISIBLE);
        longClick = true;
        addOrRemove(position);
      }
    });
  }

  private void initData() {
//    RFIDUtils.getInstance(this).readBatch(this);
    MyApplication.canScan = true;
    RFIDUtils2.getInstance(this).readData();
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
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

  /**
   * 选中或取消选中
   * @param position
   */
  private void addOrRemove(int position) {
    if (positionSet.contains(position)) {
      // 如果包含，则撤销选择
      positionSet.remove(position);
    } else {
      // 如果不包含，则添加
      positionSet.add(position);
    }

    updateUI();

  }

  /**
   * 更新选中或取消选中的ui
   */
  private void updateUI() {
    if (positionSet.size() == 0) {
      // 如果没有选中任何的item，则退出多选模式
      llBottom.setVisibility(View.GONE);
      tvResult.setText("扫描到" + list.size() + "件工具");
      longClick = false;

    } else {

      llBottom.setVisibility(View.VISIBLE);
      // 设置ActionMode标题
      tvResult.setText("扫描到" + list.size() + "件工具" +"(选中"+positionSet.size()+"条数据)");
    }
    // 更新列表界面，否则无法显示已选的item
    mAdapter.notifyDataSetChanged();
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

  @OnClick({R.id.btn_stop,R.id.tv_select_all,R.id.tv_delete,R.id.tv_select_enter,R.id.tv_select_out})
  public void onViewClicked(View view) {
    switch (view.getId()){
      case R.id.btn_stop://停止扫描
        pauseScan();
        break;
      case R.id.tv_select_all://全选
        if(tvSelectAll.getText().equals("全选")){
          selectAll();
        }else{
          cancelSeleted();
        }

        break;
      case R.id.tv_delete://删除
        deleteSeletedPosition();
        break;
      case R.id.tv_select_enter://入库
        DialogUtils.show(this, "提示", "确定入库？", new DialogUtils.OnPositiveCall() {
          @Override
          public void goOn() {
            enterOrOutLibrary("入库");
          }
        });

        break;
      case R.id.tv_select_out://出库
        DialogUtils.show(this, "提示", "确定出库？", new DialogUtils.OnPositiveCall() {
          @Override
          public void goOn() {
            enterOrOutLibrary("出库");
          }
        });
        break;
    }

  }

  private void cancelSeleted() {
    positionSet.clear();
    tvSelectAll.setText("全选");
    updateUI();
  }

  /**
   * 全选
   */
  private void selectAll() {
    for (int i = 0; i < list.size(); i++) {
      positionSet.add(i);
    }

    tvSelectAll.setText("取消全选");
    updateUI();
  }

  /**
   * 删除选中的item
   */
  private void deleteSeletedPosition() {
    /*for (int i = 0; i < positionSet.size(); i++) {
      list.remove(positionSet);
    }*/
    Set<Object> valueSet = new HashSet<>();
    for (int position : positionSet) {
      valueSet.add(list.get(position));
    }

    for (Object object : valueSet) {
      list.remove(object);
      mAdapter.notifyDataSetChanged();
    }
    positionSet.clear();
    updateUI();
  }

  /**
   * 入库
   */
  private void enterOrOutLibrary(String message) {

    if(isAllRecord()){//判断选中的是否有未统计的数据

      for(AssetDetail asset : selectedlist){
        asset.setExist(message);
        DbUtils.update(asset);
      }
      finish();
    }else {
      ToastUtils.show(btnStop,"还有未统计的数据，请重新选择");
      return;
    }

  }

  /**
   * 是否全部统计过
   */
  private boolean isAllRecord() {
    selectedlist = new ArrayList<>();
    selectedlist.clear();
    for(int position : positionSet){
      AssetDetail assetDetail = DbUtils.queryByArchivesNumber(AssetDetail.class, barcodeList.get(position));
      if (assetDetail == null) {
        return false;
      }
      selectedlist.add(assetDetail);
    }
    return true;
  }

  /**
   * 出库
   */
  private void outLibrary() {

  }

  private void pauseScan() {
    /*if (RFIDUtils.mHandler != null) {
      RFIDUtils.mHandler.removeCallbacksAndMessages(null);
    }*/

    MyApplication.canScan = false;
    btnStop.setVisibility(View.GONE);
    flBottom.setVisibility(View.GONE);
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
      updateData();
    }
  }

  /**
   * 更新数据
   */
  private void updateData() {
    list.clear();
    for (int i = 0; i < barcodeList.size(); i++) {
      AssetDetail assetDetail = DbUtils.queryByArchivesNumber(AssetDetail.class, barcodeList.get(i));
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

  /*@OnClick({R.id.btn_out, R.id.btn_enter})
  public void onViewClicked(View view) {
    for (Object object : list) {
      if (object instanceof String) {
        ToastUtils.show(toolbar,"有未统计的工具，请统计完成后再出入库！");
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
  }*/

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(requestCode== REQUEST_1){
      updateData();
    }
  }
}
