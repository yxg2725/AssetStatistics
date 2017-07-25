package com.huadin.assetstatistics.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.fragment.InventoryAssetsFragment;
import com.huadin.assetstatistics.fragment.OutboundFragment;
import com.huadin.assetstatistics.fragment.SettingFragment;
import com.huadin.assetstatistics.fragment.StorageFragment;
import com.huadin.assetstatistics.utils.DialogUtils;
import com.huadin.assetstatistics.utils.RFIDUtils;
import com.huadin.assetstatistics.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    RFIDUtils.getInstance().connect();//连接RFID

    initView();
    initListener();

    //默认展示库存资产fragment
    ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
  }

  private void initView(){
    initToolbar(mToolbar,"",false);
  }


  private void initListener() {
    mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId){
          case R.id.rb_inventory:
            //切换资产库存fragment
            if(mInventoryAssetsFragment == null){
              mInventoryAssetsFragment = new InventoryAssetsFragment();
            }
            setFragmentShow(mInventoryAssetsFragment);
            break;
          case R.id.rb_outbound:
            //切换出库统计fragment
            if(mOutboundFragment == null){
              mOutboundFragment = new OutboundFragment();
            }
            setFragmentShow(mOutboundFragment);
            break;
          case R.id.rb_storage:
            //切换入库统计fragment
            if(mStorageFragment == null){
              mStorageFragment = new StorageFragment();
            }
            setFragmentShow(mStorageFragment);
            break;
          case R.id.rb_setting:
            //切换设置fragment
            if(mSettingFragment == null){
              mSettingFragment = new SettingFragment();
            }
            setFragmentShow(mSettingFragment);
            break;
        }
      }
    });

  }


  private void setFragmentShow(Fragment fragment) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.fl_container,fragment).commit();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.inventory_menu,menu);
    return true;

  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()){
      case R.id.menu_add_category:
        DialogUtils.show(this);
        break;
      case R.id.menu_import:
        ToastUtils.show(mToolbar,"数据导入");
        break;
      case R.id.menu_export:
        ToastUtils.show(mToolbar,"数据导出");
        break;
    }
    return super.onOptionsItemSelected(item);
  }
}
