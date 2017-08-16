package com.huadin.assetstatistics.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.activity.AssetDetailActivity;
import com.huadin.assetstatistics.activity.BatchScanActivity;
import com.huadin.assetstatistics.activity.MainActivity;
import com.huadin.assetstatistics.adapter.BaseAdapter;
import com.huadin.assetstatistics.adapter.OutAssetsAdapter;
import com.huadin.assetstatistics.app.MyApplication;
import com.huadin.assetstatistics.bean.AssetDetail;
import com.huadin.assetstatistics.event.Event;
import com.huadin.assetstatistics.utils.Contants;
import com.huadin.assetstatistics.utils.DbUtils;
import com.huadin.assetstatistics.utils.RFIDUtils;
import com.huadin.assetstatistics.utils.SharedPreferenceUtils;
import com.huadin.assetstatistics.widget.MyFab;
import com.huadin.assetstatistics.widget.dragItem.SimpleItemTouchHelperCallback;

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

public class OutAndEnterFragment extends BaseFragment implements BaseAdapter.OnItemClickListener {


  @BindView(R.id.rv)
  RecyclerView mRv;
  @BindView(R.id.sp_category)
  Spinner spCategory;
  Unbinder unbinder;
  private ArrayList<AssetDetail> assetDetails = new ArrayList<>();
  private OutAssetsAdapter mAdapter;
  private String tag;
  private SVProgressHUD dialog;


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

    Bundle bundle = getArguments();
    tag = (String) bundle.get("tag");//判断是出库统计还是入库统计

    initView();
    initListener();
  }

  private void initListener() {
    mAdapter.setOnItemClickListener(this);

    spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
          case 0://所有
            initData(0);
            break;
          case 1://自用
            initData(1);
            break;
          case 2://外用
            initData(2);
            break;

        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
      }
    });
  }


  private void initView() {
    dialog = new SVProgressHUD(mActivity);
    if(tag.equals("out")){
      ((MainActivity)mActivity).mToolbar.setTitle("出库统计");
    }else if(tag.equals("enter")){
      ((MainActivity)mActivity).mToolbar.setTitle("入库统计");
    }


    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
    mRv.setLayoutManager(linearLayoutManager);
    mAdapter = new OutAssetsAdapter(mActivity,assetDetails);
    mRv.setAdapter(mAdapter);

    //先实例化Callback
    ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
    //用Callback构造ItemtouchHelper
    ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
    //调用ItemTouchHelper的attachToRecyclerView方法建立联系
    touchHelper.attachToRecyclerView(mRv);
  }

  public String getFromTag(){
    return tag;
  }

  private void initData(int position) {
    //查询数据库 已入库的资产 并且是合格的
    List<AssetDetail> list = null;
    if(tag.equals("out")){

      switch(position){
        case 0://全部
          list = DbUtils.queryByExist(AssetDetail.class, "出库");
          break;
        case 1://自用
          list = DbUtils.queryByExistAndUser(AssetDetail.class, "出库",true);
          break;
        case 2://外用
          list = DbUtils.queryByExistAndUser(AssetDetail.class, "出库",false);
          break;
      }

    }else if(tag.equals("enter")){
      switch(position){
        case 0:
          list = DbUtils.queryByExist(AssetDetail.class, "入库");
          break;
        case 1:
          list = DbUtils.queryByExistAndUser(AssetDetail.class, "入库",true);
          break;
        case 2:
          list = DbUtils.queryByExistAndUser(AssetDetail.class, "入库",false);
          break;
      }

    }

    if(list.size() == 0 ){
        dialog.showInfoWithStatus("无数据");
    }

    assetDetails.clear();
    assetDetails.addAll(list);
    mAdapter.notifyDataSetChanged();

  }

  @Override
  public void onResume() {
    super.onResume();
    initData(0);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }


  @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
  public void onInventoryAssetEvent(Event.InventoryAssetsEvent event){
    //if(event.getTag().equals("out")|| event.getTag().equals("enter")){
      initData(0);
    //}
  }
  @Override
  public void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  @Override
  public void onItemClick(int position) {
    Intent intent = new Intent(mActivity, AssetDetailActivity.class);
    intent.putExtra("tag",tag);
    intent.putExtra("result",assetDetails.get(position).getArchivesNumber());
    mActivity.startActivity(intent);
    mActivity.overridePendingTransition(R.anim.right_in, R.anim.leftout);
  }


}
