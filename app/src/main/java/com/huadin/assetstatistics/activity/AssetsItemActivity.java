package com.huadin.assetstatistics.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.adapter.AssetDetailAdapter;
import com.huadin.assetstatistics.adapter.MyAdapter;
import com.huadin.assetstatistics.adapter.OutAssetsAdapter;
import com.huadin.assetstatistics.bean.AssetDetail;
import com.huadin.assetstatistics.utils.DbUtils;
import com.huadin.assetstatistics.widget.MyDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/7/19.
 */

public class AssetsItemActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
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
        initData();

    }

  private void initListener() {
    dialog.setOnDismissListener(new OnDismissListener() {
      @Override
      public void onDismiss(SVProgressHUD hud) {
        finish();
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
        /*for (int i = 0; i < 20; i++) {
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
        }*/
        mAdapter = new OutAssetsAdapter(assetDetails);
        mRecyclerview.addItemDecoration(new MyDecoration(this, MyDecoration.VERTICAL_LIST));
        mRecyclerview.setAdapter(mAdapter);
    }

    private void initData() {
//    查询数据库
        List<AssetDetail> list = DbUtils.queryByName(AssetDetail.class, assetName);

        if(list.size() == 0){
            dialog.showInfoWithStatus("没有任何资产");
        }

        assetDetails.clear();
        assetDetails.addAll(list);
        if(mAdapter == null){
            mAdapter = new OutAssetsAdapter(assetDetails);
            mRecyclerview.addItemDecoration(new MyDecoration(this, MyDecoration.VERTICAL_LIST));
            mRecyclerview.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }
    }

}
