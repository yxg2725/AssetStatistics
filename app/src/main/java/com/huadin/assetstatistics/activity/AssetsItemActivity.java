package com.huadin.assetstatistics.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.adapter.BaseAdapter;
import com.huadin.assetstatistics.adapter.OutAssetsAdapter;
import com.huadin.assetstatistics.bean.AssetDetail;
import com.huadin.assetstatistics.utils.DbUtils;
import com.huadin.assetstatistics.widget.MyDecoration;
import com.huadin.assetstatistics.widget.dragItem.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/7/19.
 */

public class AssetsItemActivity extends BaseActivity implements BaseAdapter.OnItemClickListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.sp_category)
    Spinner spCategory;
    private ArrayList<AssetDetail> assetDetails = new ArrayList<>();
    private String assetName;
    private OutAssetsAdapter mAdapter;
    private SVProgressHUD dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_item);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        assetName = intent.getStringExtra("assetName");

        initView();
        initListener();

    }

  @Override
  protected void onResume() {
    super.onResume();
    initData(0);//所有
  }

  private void initListener() {
    mAdapter.setOnItemClickListener(this);
    dialog.setOnDismissListener(new OnDismissListener() {
      @Override
      public void onDismiss(SVProgressHUD hud) {
        int categoryID = spCategory.getSelectedItemPosition();
        if(categoryID == 0){
          finish();
          overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
      }
    });
    spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
          case 0://所有
            initData(0);
            break;
          case 1://出库
            initData(1);
            break;
          case 2://入库
            initData(2);
            break;
          case 3://不合格
            initData(3);
            break;

        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
  }


  private void initView() {
       initToolbar(mToolbar,"资产明细",true);
        initRecyclerView();
        dialog = new SVProgressHUD(this);
    }



    private void initRecyclerView() {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new OutAssetsAdapter(this,assetDetails);
        mRecyclerview.addItemDecoration(new MyDecoration(this, MyDecoration.VERTICAL_LIST));
        mRecyclerview.setAdapter(mAdapter);

      //先实例化Callback
      ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
      //用Callback构造ItemtouchHelper
      ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
      //调用ItemTouchHelper的attachToRecyclerView方法建立联系
      touchHelper.attachToRecyclerView(mRecyclerview);
    }

    private void initData(int categoryId) {
    //    查询数据库
      List<AssetDetail> list = null;
      switch (categoryId){
        case 0://所有
          list = DbUtils.queryByName(AssetDetail.class, assetName);
          break;
        case 1://出库
          list = DbUtils.queryByStyleAndExist(AssetDetail.class, assetName,"出库");
          break;
        case 2://入库
          list = DbUtils.queryByStyleAndExist(AssetDetail.class, assetName,"入库");
          break;
        case 3://不合格
          list = DbUtils.queryByStyleAndIsGood(AssetDetail.class, assetName);
          break;
      }

        if(list.size() == 0 ){
            dialog.showInfoWithStatus("无数据");
        }

        assetDetails.clear();
        assetDetails.addAll(list);
        if(mAdapter == null){
            mAdapter = new OutAssetsAdapter(this,assetDetails);
            mRecyclerview.addItemDecoration(new MyDecoration(this, MyDecoration.VERTICAL_LIST));
            mRecyclerview.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }
    }

  @Override
  public void onItemClick(int position) {
    Intent intent = new Intent(this, AssetDetailActivity.class);
    intent.putExtra("tag","InventoryAssetsFragment");
    intent.putExtra("result",assetDetails.get(position).getArchivesNumber());
    startActivity(intent);
    overridePendingTransition(R.anim.right_in, R.anim.leftout);
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == android.R.id.home){
      finish();
      overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
    return super.onKeyDown(keyCode, event);

  }
}
