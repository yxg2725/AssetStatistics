package com.huadin.assetstatistics.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.adapter.BaseAdapter;
import com.huadin.assetstatistics.adapter.DataInAdapter;
import com.huadin.assetstatistics.bean.AssetDetail;
import com.huadin.assetstatistics.bean.DataInEntity;
import com.huadin.assetstatistics.utils.Contants;
import com.huadin.assetstatistics.utils.DataOutOrInUtils;
import com.huadin.assetstatistics.utils.DbUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class DataInActivity extends BaseActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.rv)
  RecyclerView rv;
  @BindView(R.id.ll_bottom)
  LinearLayout llBottom;
  @BindView(R.id.btn_select_all)
  Button btnSelectAll;
  @BindView(R.id.btn_data_in)
  Button btnDataIn;
  private ArrayList<String> list = new ArrayList<>();
  private DataInAdapter mAdapter;
  private SVProgressHUD dialog;
  private boolean isLongclikc = false;

  public Set<Integer> positionSet = new HashSet<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_data_in);
    ButterKnife.bind(this);

    initView();
    initListener();
    initData();
  }


  private void initView() {
    dialog = new SVProgressHUD(this);
    initToolbar(toolbar, "数据导入", true);


    rv.setLayoutManager(new LinearLayoutManager(this));
    mAdapter = new DataInAdapter(this, list);
    rv.setAdapter(mAdapter);
  }

  private void initListener() {
    dialog.setOnDismissListener(new OnDismissListener() {
      @Override
      public void onDismiss(SVProgressHUD hud) {
        finish();
      }
    });

    //单机
    mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(int position) {
        if (isLongclikc) {
          addOrRemove(position);
        }
      }
    });

    //长按
    mAdapter.setOnItemLongClickListener(new DataInAdapter.OnItemLongClickListener() {
      @Override
      public void onItemLongClick(int position) {
        isLongclikc = true;
        addOrRemove(position);
      }
    });
  }

  private void initData() {
    //查询指定文件夹下的所有文件

    File file = new File(Environment.getExternalStorageDirectory(), Contants.ROOTFILENAME);
    if (!file.exists()) {
      return;
    } else {
      File[] files = file.listFiles();
      if (files.length == 0) {
        dialog.showInfoWithStatus("无数据");
        return;
      }
      for (File file1 : files) {
        String name = file1.getName();
        if(name.endsWith(".xls")){
          list.add(name);
          Log.i("DataInActivity", "initData: " + name);
        }

      }

      mAdapter.notifyDataSetChanged();
    }
  }

  /**
   * 选中或取消选中
   *
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
      isLongclikc = false;
    } else {
      llBottom.setVisibility(View.VISIBLE);
    }
    // 更新列表界面，否则无法显示已选的item
    mAdapter.notifyDataSetChanged();
  }

  private void cancelSeleted() {
    positionSet.clear();
    btnSelectAll.setText("全选");
    updateUI();
  }


  /**
   * 全选
   */
  private void selectAll() {
    for (int i = 0; i < list.size(); i++) {
      positionSet.add(i);
    }

    btnSelectAll.setText("取消全选");
    updateUI();
  }


  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == android.R.id.home) {
      finish();
    }
    return super.onKeyDown(keyCode, event);
  }

  @OnClick({R.id.btn_select_all, R.id.btn_data_in})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.btn_select_all:
        if(btnSelectAll.getText().equals("全选")){
          selectAll();
        }else{
          cancelSeleted();
        }

        break;
      case R.id.btn_data_in:
        dataIn();
        break;
    }
  }

  /**
   * 导入
   */
  private void dataIn() {
    new MyAsyncTask().execute();
  }

  class MyAsyncTask extends AsyncTask<Void,Void,Boolean>{

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      dialog.showWithStatus("导入中...", SVProgressHUD.SVProgressHUDMaskType.Black);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
      for(int position: positionSet){
        String s = list.get(position);
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator + Contants.ROOTFILENAME, s);

        boolean success = DataOutOrInUtils.dataIn(file, DataInActivity.this);
        if(!success){
          return false;
        }

      }
      return true;
    }

    @Override
    protected void onPostExecute(Boolean success) {
      super.onPostExecute(success);
      if(!success){
        dialog.showErrorWithStatus("导入失败");
        return;
      }else {
        dialog.showSuccessWithStatus("导入成功");
      }


    }
  }
}
