package com.huadin.assetstatistics.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.bean.AssetDetail;
import com.huadin.assetstatistics.event.Event;
import com.huadin.assetstatistics.utils.DbUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AssetDetailActivity extends BaseActivity {

  @BindView(R.id.toolbar)
  Toolbar mToolbar;
  @BindView(R.id.et_assets_name)
  EditText etAssetsName;
  @BindView(R.id.et_assets_id)
  EditText etAssetsId;
  @BindView(R.id.et_company)
  EditText etCompany;
  @BindView(R.id.et_manufacturer)
  EditText etManufacturer;
  @BindView(R.id.et_date_of_production)
  EditText etDateOfProduction;
  @BindView(R.id.check_num)
  EditText etCheckNum;
  @BindView(R.id.et_archives_num)
  EditText etArchivesNum;
  @BindView(R.id.et_check_date)
  EditText etCheckDate;
  @BindView(R.id.et_next_check_date)
  EditText etNextCheckDate;
  @BindView(R.id.et_check_people)
  EditText etCheckPeople;
  @BindView(R.id.iv)
  ImageView iv;
  @BindView(R.id.btn_save)
  Button btnSave;
  private String barcode;

  private boolean isExist = false;
  private AssetDetail mAssetDetail;
  private AssetDetail inputAssetDetail;
  private String tag;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_asset_detail);
    ButterKnife.bind(this);

    Intent intent = getIntent();
    tag = intent.getStringExtra("tag");
    //获取扫到的条码号
    barcode = intent.getStringExtra("result");

    //查询数据库
    mAssetDetail = DbUtils.queryByCode(AssetDetail.class, barcode);

    if (mAssetDetail != null){
      setresult(mAssetDetail);
      isExist = true;
    }
    //不存在则手动输入

    initView();
  }

  private void setresult(AssetDetail assetDetail) {
    etAssetsName.setText(assetDetail.getAssetName());
    etAssetsId.setText(assetDetail.getDeviceId());
    etCompany.setText(assetDetail.getUsedCompany());
    etManufacturer.setText(assetDetail.getManufacturer());
    etDateOfProduction.setText(assetDetail.getDateOfProduction());
    etCheckNum.setText(assetDetail.getInspectionNumber());
    etArchivesNum.setText(assetDetail.getArchivesNumber());
    etCheckDate.setText(assetDetail.getCheckDate());
    etNextCheckDate.setText(assetDetail.getNextCheckDate());
    etCheckPeople.setText(assetDetail.getCheckPeople());

  }

  private void initView() {
    initToolbar(mToolbar, "资产入库", true);
  }

  @OnClick(R.id.btn_save)
  public void onViewClicked() {
    saveData();
    //将数据传回去
    Event.InventoryAssetsEvent inventoryAssetsEvent = new Event.InventoryAssetsEvent();
    inventoryAssetsEvent.setAssetDetail(inputAssetDetail);
    inventoryAssetsEvent.setTag(tag);
    EventBus.getDefault().post(inventoryAssetsEvent);
    finish();
  }

  private void saveData() {
    inputAssetDetail = new AssetDetail();
    String assetsName = etAssetsName.getText().toString();
    String assetsId = etAssetsId.getText().toString();
    String company = etCompany.getText().toString();
    String manufacturer = etManufacturer.getText().toString();
    String dateOfProduction = etDateOfProduction.getText().toString();
    String checkNum = etCheckNum.getText().toString();
    String archivesNum = etArchivesNum.getText().toString();
    String checkDate = etCheckDate.getText().toString();
    String nextCheckDate = etNextCheckDate.getText().toString();
    String checkPeople = etCheckPeople.getText().toString();

    inputAssetDetail.setCheckPeople(checkPeople);
    inputAssetDetail.setCheckDate(checkDate);
    inputAssetDetail.setManufacturer(manufacturer);
    inputAssetDetail.setArchivesNumber(archivesNum);
    inputAssetDetail.setAssetName(assetsName);
    inputAssetDetail.setBarcode(barcode);
    inputAssetDetail.setDateOfProduction(dateOfProduction);
    inputAssetDetail.setDeviceId(assetsId);
    inputAssetDetail.setNextCheckDate(nextCheckDate);
    inputAssetDetail.setUsedCompany(company);
    inputAssetDetail.setInspectionNumber(checkNum);
    if(tag.equals("OutboundFragment")){
      inputAssetDetail.setExist("no");
    }else if(tag.equals("StorageFragment")){
      inputAssetDetail.setExist("yes");
    }


    if (isExist){
      //删除这条信息
      DbUtils.delete(mAssetDetail);
    }
    DbUtils.insert(inputAssetDetail);
  }


}
