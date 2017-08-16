package com.huadin.assetstatistics.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.app.MyApplication;
import com.huadin.assetstatistics.bean.AssetDetail;
import com.huadin.assetstatistics.fragment.InventoryAssetsFragment;
import com.huadin.assetstatistics.fragment.OutAndEnterFragment;
import com.huadin.assetstatistics.fragment.SettingFragment;
import com.huadin.assetstatistics.utils.Contants;
import com.huadin.assetstatistics.utils.DataOutOrInUtils;
import com.huadin.assetstatistics.utils.DbUtils;
import com.huadin.assetstatistics.utils.ExcelUtils;
import com.huadin.assetstatistics.utils.KT50_B2.ModuleManager;
import com.huadin.assetstatistics.utils.KT50_B2.RFIDUtils2;
import com.huadin.assetstatistics.utils.RFIDUtils;
import com.huadin.assetstatistics.utils.SharedPreferenceUtils;
import com.huadin.assetstatistics.utils.ToastUtils;
import com.huadin.assetstatistics.widget.MyFab;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {

  @BindView(R.id.toolbar)
  public Toolbar mToolbar;
  @BindView(R.id.fl_container)
  FrameLayout mFlContainer;
  @BindView(R.id.radio_group)
  RadioGroup mRadioGroup;
  @BindView(R.id.btn_scan)
  MyFab btnScan;
  private InventoryAssetsFragment mInventoryAssetsFragment;
  private OutAndEnterFragment mOutboundFragment;
  private OutAndEnterFragment mOutAndEnterFragment;
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
//    RFIDUtils.getInstance(this).connectAsync();//连接RFID KT45Q 和IDATA
    if(!MyApplication.connectSuccess){
      RFIDUtils2.getInstance(this).connect();//KT50_B2
    }


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
            changeUi(group, 0);
            break;
          case R.id.rb_outbound:
            //切换出库统计fragment
            if (mOutboundFragment == null) {
              mOutboundFragment = new OutAndEnterFragment();
            }
            Bundle bundle = new Bundle();
            bundle.putString("tag", "out");
            mOutboundFragment.setArguments(bundle);

            setFragmentShow(mOutboundFragment);
            changeUi(group, 1);
            break;
          case R.id.rb_storage:
            //切换入库统计fragment
            if (mOutAndEnterFragment == null) {
              mOutAndEnterFragment = new OutAndEnterFragment();
            }

            Bundle bundle2 = new Bundle();
            bundle2.putString("tag", "enter");
            mOutAndEnterFragment.setArguments(bundle2);
            setFragmentShow(mOutAndEnterFragment);
            changeUi(group, 2);
            break;
        }
      }
    });

  }

  private void changeUi(RadioGroup group, int id) {

    for (int i = 0; i < group.getChildCount(); i++) {
      RadioButton button = (RadioButton) group.getChildAt(i);
      if (i == id) {
        button.setTextColor(Color.WHITE);
      } else {
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
    return true;

  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_import:
        //跳转到导入界面
        Intent intent1 = new Intent(this,DataInActivity.class);
        startActivity(intent1);
        break;
      case R.id.menu_about:
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
        break;
      case R.id.menu_export:
        dataOut();
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * 导出
   */
  private void dataOut() {
    dialog.showWithStatus("导出中...");
    boolean issuccess = DataOutOrInUtils.dataOut();
    if (issuccess) {
      dialog.showSuccessWithStatus("导出成功");
    } else {
      dialog.showErrorWithStatus("导出失败");
    }
  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    /*if (RFIDUtils.getInstance(this).mHandler != null) {
      RFIDUtils.getInstance(this).mHandler.removeCallbacksAndMessages(null);
    }

    if (RFIDUtils.getInstance(this).mReader != null) {
      RFIDUtils.getInstance(this).mReader.CloseReader();
      RFIDUtils.getInstance(this).mRpower.PowerDown();
    }
*/

    MyApplication.getLinkage().CancelOperation();
    ModuleManager.destroyLibSO();
    MyApplication.connectSuccess = false;
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


  @OnClick(R.id.btn_scan)
  public void onViewClicked() {
    if(!MyApplication.connectSuccess){
      //RFIDUtils.getInstance(activity).connectAsync();
      RFIDUtils2.getInstance(this).connect();
    }else{
      //批量扫描
      Intent intent = new Intent(this, BatchScanActivity.class);
      startActivity(intent);
    }

  }


}
