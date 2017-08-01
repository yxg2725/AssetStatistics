package com.huadin.assetstatistics.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.app.MyApplication;
import com.huadin.assetstatistics.bean.AssetDetail;
import com.huadin.assetstatistics.event.Event;
import com.huadin.assetstatistics.utils.CameraUtil;
import com.huadin.assetstatistics.utils.Contants;
import com.huadin.assetstatistics.utils.DatePickDialogUtil;
import com.huadin.assetstatistics.utils.DateUtil;
import com.huadin.assetstatistics.utils.DbUtils;
import com.huadin.assetstatistics.utils.DialogUtils;
import com.huadin.assetstatistics.utils.PinyinUtil;
import com.huadin.assetstatistics.utils.SharedPreferenceUtils;
import com.yanzhenjie.permission.AndPermission;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.System.currentTimeMillis;


public class AssetDetailActivity extends BaseActivity {

  @BindView(R.id.toolbar)
  Toolbar mToolbar;
  @BindView(R.id.sp_assets_name)
  Spinner spAssetsName;
  @BindView(R.id.sp_isGood)
  Spinner spIsGood;
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

  @BindView(R.id.btn_check_num)
  Button btnCheckNum;



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
  private int goodOrBadPosition = 0;
  private String imgPath;
  private String szm;//首字母

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_asset_detail);
    ButterKnife.bind(this);

    AndPermission.with(this)
            .requestCode(1)
            .permission(Manifest.permission.CAMERA)
            .start();

    Intent intent = getIntent();
    tag = intent.getStringExtra("tag");
    //获取扫到的条码号
    barcode = intent.getStringExtra("result");
    //照片路径
    imgPath = Environment.getExternalStorageDirectory() + "/安全工器具/img/"  + barcode + ".jpg";

    //查询数据库
    mAssetDetail = DbUtils.queryByCode(AssetDetail.class, barcode);


    String itemName = (String) spAssetsName.getSelectedItem();
    //String format = String.format("%05d" , Long.valueOf(barcode));
    szm = PinyinUtil.getFirstletter(itemName);//首字母

    if (mAssetDetail != null) {
      setresult(mAssetDetail);
      isExist = true;
    }else{
      etArchivesNum.setText(szm + "-DA-"+barcode);

      SharedPreferenceUtils sharedPreferenceUtils = new SharedPreferenceUtils(this);
      String checkPeople = sharedPreferenceUtils.getString("checkPeople");
      etCheckPeople.setText(checkPeople);
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

        String itemName = (String) spAssetsName.getSelectedItem();
        //String format = String.format("%05d" , Long.valueOf(barcode));
        szm = PinyinUtil.getFirstletter(itemName);
        etArchivesNum.setText(szm + "-DA-"+barcode);

        if(mAssetDetail  == null){
          String timeStamp = System.currentTimeMillis()+ "";
          timeStamp = timeStamp.substring(timeStamp.length()-8);
          if(!etCheckNum.getText().equals("")){
            etCheckNum.setText(szm + "-JC-" + timeStamp);
          }
        }

      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {}
    });

    spIsGood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        goodOrBadPosition = position;
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    iv.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        if (iv.getDrawable()!=null) {
          DialogUtils.show(AssetDetailActivity.this, "提示", "是否删除此照片?", new DialogUtils.OnPositiveCall() {
            @Override
            public void goOn() {
              MyApplication.showImageView("",iv);
            }
          });
        }
        return true;
      }
    });

    etCheckDate.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        long l = DateUtil.convert2long(s.toString().trim(), "yyyy-MM-dd");
        Date date = new Date(l);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.MONTH,6);
        String nextDate = DateUtil.timestamp2ymd(calendar.getTimeInMillis());
        etNextCheckDate.setText(nextDate);

      }
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
    int position = getSelectedPosition(assetDetail.getIsGood());
    spIsGood.setSelection(position);
    MyApplication.showImageView(assetDetail.getImgPath(),iv);

  }

  private void initView() {

    if(tag.equals("StorageFragment")){
      initToolbar(mToolbar, "资产入库", true);
    }else if(tag.equals("OutboundFragment")){
      initToolbar(mToolbar, "资产出库", true);
    }else if(tag.equals("InventoryAssetsFragment")){
      initToolbar(mToolbar, "资产校验", true);
    }

  }


  private void saveData() {
    inputAssetDetail = new AssetDetail();

    String assetsName = Contants.assetsType[selectedPosition];
    String assetsId = etAssetsId.getText().toString().trim();
    String company = etCompany.getText().toString().trim();
    String manufacturer = etManufacturer.getText().toString().trim();
    String dateOfProduction = etDateOfProduction.getText().toString().trim();
    String checkNum = etCheckNum.getText().toString().trim();
    String archivesNum = etArchivesNum.getText().toString().trim();
    String checkDate = etCheckDate.getText().toString().trim();
    String nextCheckDate = etNextCheckDate.getText().toString().trim();
    String checkPeople = etCheckPeople.getText().toString().trim();

    //将校验员保存到本地
    SharedPreferenceUtils sharedPreferenceUtils = new SharedPreferenceUtils(this);
    sharedPreferenceUtils.putString("checkPeople",checkPeople);


    String isGood = Contants.GOODORBAD[goodOrBadPosition];


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
    inputAssetDetail.setIsGood(isGood);

    if(iv.getDrawable() != null){
      inputAssetDetail.setImgPath(imgPath);
    }

    if(isGood.equals("合格")){
      if (tag.equals("OutboundFragment")) {
        inputAssetDetail.setExist("no");
      } else if (tag.equals("StorageFragment")) {
        inputAssetDetail.setExist("yes");
      }
    }


    if (isExist) {
      //删除这条信息
      DbUtils.delete(mAssetDetail);
    }
    DbUtils.insert(inputAssetDetail);

  }


  @OnClick({R.id.btn_save,R.id.ib_calendar_production_date, R.id.ib_calendar_check_date,
          R.id.ib_calendar_next_check_date,R.id.iv,R.id.btn_check_num})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.btn_save:
        saveData();
        //将数据传回去
          Event.InventoryAssetsEvent inventoryAssetsEvent = new Event.InventoryAssetsEvent();
          inventoryAssetsEvent.setAssetDetail(inputAssetDetail);
          inventoryAssetsEvent.setTag(tag);
          EventBus.getDefault().post(inventoryAssetsEvent);

        if(((String)spIsGood.getSelectedItem()).equals("不合格")){
          DialogUtils.show(this, "提示", "该工具不合格，禁止使用！", new DialogUtils.OnPositiveCall() {
            @Override
            public void goOn() {
              finish();
            }
          });
        }else{
          finish();
        }

        break;
      case R.id.ib_calendar_production_date://出厂日期
       new DatePickDialogUtil(this,-1,false).datePicKDialog(etDateOfProduction);
        break;
      case R.id.ib_calendar_check_date://校验日期
        new DatePickDialogUtil(this,-1,false).datePicKDialog(etCheckDate);
        break;
      case R.id.ib_calendar_next_check_date://下次校验日期
        new DatePickDialogUtil(this,-1,false).datePicKDialog(etNextCheckDate);
        break;
      case R.id.iv:
        if (iv.getDrawable()==null) {
            CameraUtil.takePhoto(this,barcode);
        } else {
          Intent intent = new Intent(this, ImageActivity.class);
          intent.putExtra("imgPath", imgPath);
          startActivity(intent);
        }
        break;
      case R.id.btn_check_num:
        //自动生成检测编号
        String checkNum = szm+ "-JC-" + currentTimeMillis();
        etCheckNum.setText(checkNum);
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

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK){
      MyApplication.showImageView(imgPath,iv);
    }
  }
}
