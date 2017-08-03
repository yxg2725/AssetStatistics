package com.huadin.assetstatistics.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.bean.AssetDetail;
import com.huadin.assetstatistics.fragment.InventoryAssetsFragment;
import com.huadin.assetstatistics.fragment.OutboundFragment;
import com.huadin.assetstatistics.fragment.SettingFragment;
import com.huadin.assetstatistics.fragment.StorageFragment;
import com.huadin.assetstatistics.utils.Contants;
import com.huadin.assetstatistics.utils.DbUtils;
import com.huadin.assetstatistics.utils.ExcelUtils;
import com.huadin.assetstatistics.utils.RFIDUtils;
import com.huadin.assetstatistics.utils.SharedPreferenceUtils;
import com.huadin.assetstatistics.utils.ToastUtils;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.button;


public class MainActivity extends BaseActivity {

  @BindView(R.id.toolbar)
  public Toolbar mToolbar;
  @BindView(R.id.fl_container)
  FrameLayout mFlContainer;
  @BindView(R.id.radio_group)
  RadioGroup mRadioGroup;
  private InventoryAssetsFragment mInventoryAssetsFragment;
  private OutboundFragment mOutboundFragment;
  private StorageFragment mStorageFragment;
  private SettingFragment mSettingFragment;
  private SVProgressHUD dialog;
  private String path;
  private String TAG = "MainActivity";
  private SharedPreferenceUtils sharedPreferenceUtils;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    RFIDUtils.getInstance(this).connectAsync();//连接RFID

    //权限申请
    AndPermission.with(this)
            .requestCode(100)
            .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .start();

    initView();
    initListener();

    //默认展示库存资产fragment
    ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
  }

  private void initView() {
    initToolbar(mToolbar, "", false);
    dialog = new SVProgressHUD(this);
    sharedPreferenceUtils = new SharedPreferenceUtils(this);
  }

  private void initListener() {
    mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

        switch (checkedId) {
          case R.id.rb_inventory:
            //切换资产库存fragment
            if (mInventoryAssetsFragment == null) {
              mInventoryAssetsFragment = new InventoryAssetsFragment();
            }
            setFragmentShow(mInventoryAssetsFragment);
            changeUi(group,0);
            break;
          case R.id.rb_outbound:
            //切换出库统计fragment
            if (mOutboundFragment == null) {
              mOutboundFragment = new OutboundFragment();
            }
            setFragmentShow(mOutboundFragment);
            changeUi(group,1);
            break;
          case R.id.rb_storage:
            //切换入库统计fragment
            if (mStorageFragment == null) {
              mStorageFragment = new StorageFragment();
            }
            setFragmentShow(mStorageFragment);
            changeUi(group,2);
            break;
          case R.id.rb_setting:
            //切换设置fragment
            if (mSettingFragment == null) {
              mSettingFragment = new SettingFragment();
            }
            setFragmentShow(mSettingFragment);
            changeUi(group,3);
            break;
        }
      }
    });

  }

  private void changeUi(RadioGroup group, int id) {

    for (int i = 0; i < group.getChildCount(); i++) {
      RadioButton button = (RadioButton) group.getChildAt(i);
      if(i == id){
        button.setTextColor(Color.WHITE);
      }else{
        button.setTextColor(Color.BLACK);
      }
    }
  }


  private void setFragmentShow(Fragment fragment) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.fl_container, fragment).commit();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.inventory_menu, menu);
    MenuItem item = menu.getItem(0);
    boolean aBoolean = sharedPreferenceUtils.getBoolean(Contants.PATCH_SCAN, false);
    item.setChecked(aBoolean);
    return true;

  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_scan_patch:
        item.setChecked(item.isChecked() ? false:true);
        Log.i("check", "item.isChecked(): " + item.isChecked());
        if(item.isChecked()){
          sharedPreferenceUtils.putBoolean(Contants.PATCH_SCAN,true);
        }else{
          sharedPreferenceUtils.putBoolean(Contants.PATCH_SCAN,false);
        }
        break;
      case R.id.menu_import:
        ToastUtils.show(mToolbar, "数据导入");
        break;
      case R.id.menu_about:
        Intent intent = new Intent(this,AboutActivity.class);
        startActivity(intent);
        break;
      case R.id.menu_export:
        dialog.showWithStatus("导出中...");
        //导出的文件名
        String dirFileName = getDirFileName();

        //表格的sheet名
        List<String> sheetNames = Arrays.asList(Contants.SHEETNAMES);

        //表头
        String[] tablehead = Contants.TABLEHEAD;
        List<String> tableHeads = Arrays.asList(tablehead);

        //表格的标题
        List<String[]> titles = new ArrayList<>();
        titles.add(Contants.TABLETITLE_DETAILE);
        titles.add(Contants.TABLETITLE_TOTAL);

        ExcelUtils.initExcel(dirFileName, sheetNames, titles, tableHeads);

        //获取数据
        ArrayList<ArrayList<ArrayList<String>>> contentList = new ArrayList<>();

        //明细sheet的数据
        ArrayList<ArrayList<String>> detailList = new ArrayList<>();
        List<AssetDetail> list = DbUtils.queryAll(AssetDetail.class);//明细的数
        Collections.sort(list);

        for (AssetDetail asset : list) {
          ArrayList<String> assetList = new ArrayList<>();
          assetList.add(asset.getAssetName());
          assetList.add(asset.getDeviceId());
          assetList.add(asset.getUsedCompany());
          assetList.add(asset.getManufacturer());
          assetList.add(asset.getDateOfProduction());
          assetList.add(asset.getInspectionNumber());
          assetList.add(asset.getArchivesNumber());
          assetList.add(asset.getCheckDate());
          assetList.add(asset.getNextCheckDate());
          assetList.add(asset.getCheckPeople());
          assetList.add(asset.getExist());

          detailList.add(assetList);
        }

        //总体sheet的数据
        ArrayList<ArrayList<String>> totalList = new ArrayList<>();

        for (int i = 0; i < Contants.assetsType.length; i++) {
          ArrayList<String> List3 = new ArrayList<>();

          List3.add(Contants.assetsType[i]);
          //总个数查询
          List<AssetDetail> list1 = DbUtils.queryByName(AssetDetail.class, Contants.assetsType[i]);
          List3.add(list1.size() + "");

          //库存个数查询
          List<AssetDetail> existList = DbUtils.queryByStyleAndExist(AssetDetail.class, Contants.assetsType[i], "入库");
          List3.add(existList.size() + "");

          //出库个数查询
          List<AssetDetail> outList = DbUtils.queryByStyleAndExist(AssetDetail.class, Contants.assetsType[i], "出库");
          List3.add(outList.size() + "");

          totalList.add(List3);
        }

        contentList.add(detailList);
        contentList.add(totalList);

        boolean issuccess = ExcelUtils.writeObjListToExcel(contentList, dirFileName, this);

        if (issuccess) {
          dialog.showSuccessWithStatus("导出成功");
        } else {
          dialog.showErrorWithStatus("导出失败");
        }
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  private String getDirFileName() {
    //获取文件名
    File file = new File(Environment.getExternalStorageDirectory(), "安全工器具");
    if (!file.exists()) {
      file.mkdir();
    }

    return file.getAbsolutePath() + "/" + Contants.TABLENAME;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (RFIDUtils.getInstance(this).mHandler != null) {
      RFIDUtils.getInstance(this).mHandler.removeCallbacksAndMessages(null);
    }

    if (RFIDUtils.getInstance(this).mReader != null) {
      RFIDUtils.getInstance(this).mReader.CloseReader();
      RFIDUtils.getInstance(this).mRpower.PowerDown();
    }

  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
      if ((System.currentTimeMillis() - exittime) > 2000) {
        Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
        exittime = System.currentTimeMillis();
      } else {
        finish();
      }

      return true;
    }
    return super.onKeyDown(keyCode, event);
  }


}
