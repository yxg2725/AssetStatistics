package com.huadin.assetstatistics.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.bean.AssetDetail;
import com.huadin.assetstatistics.event.Event;
import com.huadin.assetstatistics.utils.Contants;
import com.huadin.assetstatistics.utils.DatePickDialogUtil;
import com.huadin.assetstatistics.utils.DbUtils;
import com.huadin.assetstatistics.utils.PinyinUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AssetDetailActivity extends BaseActivity {

  @BindView(R.id.toolbar)
  Toolbar mToolbar;
  @BindView(R.id.sp_assets_name)
  Spinner spAssetsName;
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
  @BindView(R.id.ib_calendar_production_date)
  ImageButton ibCalendarProductionDate;
  @BindView(R.id.ib_calendar_check_date)
  ImageButton ibCalendarCheckDate;
  @BindView(R.id.ib_calendar_next_check_date)
  ImageButton ibCalendarNextCheckDate;
  private String barcode;

  private boolean isExist = false;
  private AssetDetail mAssetDetail;
  private AssetDetail inputAssetDetail;
  private String tag;
  private int selectedPosition = 0;

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

    if (mAssetDetail != null) {
      setresult(mAssetDetail);
      isExist = true;
    }else{
      String format = String.format("%05d" , Integer.valueOf(barcode));
      String jyx = PinyinUtil.getFirstletter("绝缘靴");
      etArchivesNum.setText(jyx + "-"+format);
    }
    //不存在则手动输入

    initView();
    initListener();
  }

  private void initListener() {
    spAssetsName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         selectedPosition = position;
        spAssetsName.setSelection(position);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {}
    });
  }


  private void setresult(AssetDetail assetDetail) {
    /*etAssetsName.setText(assetDetail.getAssetName());
    int selectedItemPosition = spAssetsName.getSelectedItemPosition();
    String s = Contants.assetsType[selectedItemPosition];
    spAssetsName.setSelection();*/

    int selectedPosition = getSelectedPosition(assetDetail.getAssetName());
    spAssetsName.setSelection(selectedPosition);
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


  private void saveData() {
    inputAssetDetail = new AssetDetail();

    String assetsName = Contants.assetsType[selectedPosition];

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
    if (tag.equals("OutboundFragment")) {
      inputAssetDetail.setExist("no");
    } else if (tag.equals("StorageFragment")) {
      inputAssetDetail.setExist("yes");
    }


    if (isExist) {
      //删除这条信息
      DbUtils.delete(mAssetDetail);
    }
    DbUtils.insert(inputAssetDetail);
  }


  @OnClick({R.id.btn_save,R.id.ib_calendar_production_date, R.id.ib_calendar_check_date, R.id.ib_calendar_next_check_date})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.btn_save:
        saveData();
        //将数据传回去
        Event.InventoryAssetsEvent inventoryAssetsEvent = new Event.InventoryAssetsEvent();
        inventoryAssetsEvent.setAssetDetail(inputAssetDetail);
        inventoryAssetsEvent.setTag(tag);
        EventBus.getDefault().post(inventoryAssetsEvent);
        finish();
        break;
      case R.id.ib_calendar_production_date:
        new DatePickDialogUtil(this,-1,false).datePicKDialog(etDateOfProduction);
        break;
      case R.id.ib_calendar_check_date:
        new DatePickDialogUtil(this,-1,false).datePicKDialog(etCheckDate);
        break;
      case R.id.ib_calendar_next_check_date:
        new DatePickDialogUtil(this,-1,false).datePicKDialog(etNextCheckDate);
        break;
    }
  }

  public int getSelectedPosition(String name){
    for (int i = 0; i < Contants.assetsType.length; i++) {
      if(Contants.assetsType[i].equals(name)){
        return i;
      }
    }

    return 0;
  }
}
