package com.huadin.assetstatistics.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.huadin.assetstatistics.utils.Contants.assetsType;
import static java.lang.System.currentTimeMillis;


public class AssetDetailActivity extends BaseActivity {

  private static final int RESULTCODE_1 = 1;
  @BindView(R.id.toolbar)
  Toolbar mToolbar;
  @BindView(R.id.sp_assets_name)
  Spinner spAssetsName;
  @BindView(R.id.sp_isGood)
  Spinner spIsGood;
  @BindView(R.id.et_assets_id)
  AutoCompleteTextView etAssetsId;
  @BindView(R.id.et_company)
  AutoCompleteTextView etCompany;
  @BindView(R.id.et_manufacturer)
  AutoCompleteTextView etManufacturer;
  @BindView(R.id.et_date_of_production)
  EditText etDateOfProduction;
  @BindView(R.id.check_num)
  AutoCompleteTextView etCheckNum;
  @BindView(R.id.et_archives_num)
  AutoCompleteTextView etArchivesNum;
  @BindView(R.id.et_check_date)
  EditText etCheckDate;
  @BindView(R.id.et_next_check_date)
  EditText etNextCheckDate;
  @BindView(R.id.et_check_people)
  AutoCompleteTextView etCheckPeople;
  @BindView(R.id.iv)
  ImageView iv;
  @BindView(R.id.btn_enter)
  Button btnEnter;
  @BindView(R.id.btn_out)
  Button btnOut;

  @BindView(R.id.btn_check_num)
  Button btnCheckNum;


  @BindView(R.id.ib_calendar_production_date)
  ImageButton ibCalendarProductionDate;
  @BindView(R.id.ib_calendar_check_date)
  ImageButton ibCalendarCheckDate;
  @BindView(R.id.ib_calendar_next_check_date)
  ImageButton ibCalendarNextCheckDate;
  @BindView(R.id.et_department)
  AutoCompleteTextView etDepartment;
  @BindView(R.id.et_date_purchase)
  EditText etDatePurchase;
  @BindView(R.id.ib_calendar_date_purchase)
  ImageButton ibCalendarDatePurchase;
  @BindView(R.id.et_custodian)
  AutoCompleteTextView etCustodian;

  @BindView(R.id.ll_sure)
  LinearLayout llSure;
  @BindView(R.id.ll_enter_out)
  LinearLayout llEnterOut;
  private String archivesNum;//档案编号

  private boolean isExist = false;
  private AssetDetail mAssetDetail;
  private AssetDetail inputAssetDetail;
  private String tag;
  private int selectedPosition = 0;
  private int goodOrBadPosition = 0;
  private String imgPath;
  private String szm;//首字母
  private SVProgressHUD dialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_asset_detail);
    ButterKnife.bind(this);

    AndPermission.with(this)
            .requestCode(1)
            .permission(Manifest.permission.CAMERA)
            .start();

    //刚开始隐藏软键盘
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    dialog = new SVProgressHUD(this);
    dialog.setOnDismissListener(new OnDismissListener() {
      @Override
      public void onDismiss(SVProgressHUD hud) {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
      }
    });
    Intent intent = getIntent();
    tag = intent.getStringExtra("tag");
    //获取扫到的条码号
    archivesNum = intent.getStringExtra("result");
    //照片路径
    imgPath = Environment.getExternalStorageDirectory() + "/安全工器具/img/" + archivesNum + ".jpg";

    //查询数据库
    mAssetDetail = DbUtils.queryByArchivesNumber(AssetDetail.class, archivesNum);


    String itemName = (String) spAssetsName.getSelectedItem();
    //String format = String.format("%05d" , Long.valueOf(barcode));
    szm = PinyinUtil.getFirstletter(itemName);//首字母

    if (mAssetDetail != null) {
      setresult(mAssetDetail);
      isExist = true;
    } else {
      if (tag.equals("InventoryAssetsFragment")) {
        dialog.showInfoWithStatus("没有记录过此资产");
      }

      etArchivesNum.setText(archivesNum);
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

       /* String itemName = (String) spAssetsName.getSelectedItem();
        //String format = String.format("%05d" , Long.valueOf(barcode));
        szm = PinyinUtil.getFirstletter(itemName);
        etArchivesNum.setText(szm + "-DA-" + archivesNum);

        if (mAssetDetail == null) {
          String timeStamp = System.currentTimeMillis() + "";
          timeStamp = timeStamp.substring(timeStamp.length() - 8);
          if (!etCheckNum.getText().equals("")) {
            etCheckNum.setText(szm + "-JC-" + timeStamp);
          }
        }*/

      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
      }
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
        if (iv.getDrawable() != null) {
          DialogUtils.show(AssetDetailActivity.this, "提示", "是否删除此照片?", new DialogUtils.OnPositiveCall() {
            @Override
            public void goOn() {
              MyApplication.showImageView("", iv);
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

        calendar.add(Calendar.MONTH, 6);
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

    if(TextUtils.equals(assetDetail.getIsGood(),"合格")){
      spIsGood.setSelection(0);
    }else if(TextUtils.equals(assetDetail.getIsGood(),"不合格")){
      spIsGood.setSelection(1);
    }

    etAssetsId.setText(assetDetail.getDeviceId());
    etCompany.setText(assetDetail.getUsedCompany());
    etManufacturer.setText(assetDetail.getManufacturer());
    etDateOfProduction.setText(assetDetail.getDateOfProduction());
    etCheckNum.setText(assetDetail.getInspectionNumber());
    etArchivesNum.setText(assetDetail.getArchivesNumber());
    etCheckDate.setText(assetDetail.getCheckDate());
    etNextCheckDate.setText(assetDetail.getNextCheckDate());
    etCheckPeople.setText(assetDetail.getCheckPeople());

    etDepartment.setText(assetDetail.getUsedDepartment());
    etDatePurchase.setText(assetDetail.getDatePurchase());
    etCustodian.setText(assetDetail.getCustodian());
    MyApplication.showImageView(assetDetail.getImgPath(), iv);

  }

  private void initView() {

    if (tag.equals("enter")) {
      initToolbar(mToolbar, "资产入库", true);
      llSure.setVisibility(View.VISIBLE);
      llEnterOut.setVisibility(View.GONE);
    } else if (tag.equals("out")) {
      initToolbar(mToolbar, "资产出库", true);
      llSure.setVisibility(View.VISIBLE);
      llEnterOut.setVisibility(View.GONE);
    } else if (tag.equals("InventoryAssetsFragment")) {
      initToolbar(mToolbar, "资产校验", true);
      llSure.setVisibility(View.VISIBLE);
      llEnterOut.setVisibility(View.GONE);
    } else if (tag.equals("BatchScanActivity")) {
      initToolbar(mToolbar, "批量统计", true);
      llSure.setVisibility(View.GONE);
      llEnterOut.setVisibility(View.VISIBLE);
    }

    //规格型号
    setAutoCompleteTextData(etAssetsId,"DEVICE_ID");

    //使用单位
    setAutoCompleteTextData(etCompany,"used_company");

    //使用部门
    setAutoCompleteTextData(etDepartment,"USED_DEPARTMENT");

    //生成厂家
    setAutoCompleteTextData(etManufacturer,"MANUFACTURER");

    //检测编号
    setAutoCompleteTextData(etCheckNum,"INSPECTION_NUMBER");

    //档案编号
    setAutoCompleteTextData(etArchivesNum,"ARCHIVES_NUMBER");

    //保管人
    setAutoCompleteTextData(etCustodian,"CUSTODIAN");

    //校验员
    setAutoCompleteTextData(etCheckPeople,"CHECK_PEOPLE");

  }

  private void setAutoCompleteTextData(AutoCompleteTextView view,String colName) {
    view.setThreshold(1);
    ArrayList<String> deviceIdList = DbUtils.queryByCol(colName);
    ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,deviceIdList);
    view.setAdapter(arrayAdapter);
  }


  private void saveData(String message) {
    inputAssetDetail = new AssetDetail();

    String assetsName = assetsType[selectedPosition];
    String assetsId = etAssetsId.getText().toString().trim();
    String company = etCompany.getText().toString().trim();
    String manufacturer = etManufacturer.getText().toString().trim();
    String dateOfProduction = etDateOfProduction.getText().toString().trim();
    String checkNum = etCheckNum.getText().toString().trim();
    String archivesNum = etArchivesNum.getText().toString().trim();
    String checkDate = etCheckDate.getText().toString().trim();
    String nextCheckDate = etNextCheckDate.getText().toString().trim();
    String checkPeople = etCheckPeople.getText().toString().trim();

    String department = etDepartment.getText().toString().trim();//使用部门
    String datePurchase = etDatePurchase.getText().toString().trim();//购置日期
    String custodian = etCustodian.getText().toString().trim();//保管人

    //将校验员保存到本地
    SharedPreferenceUtils sharedPreferenceUtils = new SharedPreferenceUtils(this);
    sharedPreferenceUtils.putString("checkPeople", checkPeople);

    //合格
    String isGood = (String) spIsGood.getSelectedItem();

    inputAssetDetail.setCheckPeople(checkPeople);
    inputAssetDetail.setCheckDate(checkDate);
    inputAssetDetail.setManufacturer(manufacturer);
    inputAssetDetail.setArchivesNumber(archivesNum);
    inputAssetDetail.setAssetName(assetsName);
    inputAssetDetail.setBarcode("");
    inputAssetDetail.setDateOfProduction(dateOfProduction);
    inputAssetDetail.setDeviceId(assetsId);
    inputAssetDetail.setNextCheckDate(nextCheckDate);
    inputAssetDetail.setUsedCompany(company);
    inputAssetDetail.setInspectionNumber(checkNum);
    inputAssetDetail.setIsGood(isGood);

    inputAssetDetail.setUsedDepartment(department);
    inputAssetDetail.setDatePurchase(datePurchase);
    inputAssetDetail.setCustodian(custodian);


    if (iv.getDrawable() != null) {
      inputAssetDetail.setImgPath(imgPath);
    }
    String outOrBackTime = DateUtil.timestamp2ymd(System.currentTimeMillis());
    if (message.contains("入库")) {
      inputAssetDetail.setExist("入库");
      inputAssetDetail.setBackTime(outOrBackTime);
    } else if (message.contains("出库")) {
      inputAssetDetail.setExist("出库");
      inputAssetDetail.setOutTime(outOrBackTime);
    }else if(message.contains("不合格")){
      inputAssetDetail.setIsGood("不合格");
    }else if(message.contains("确定修改")){
      if(isGood.equals("合格")){
        inputAssetDetail.setExist("入库");
      }
    }


    /*if (tag.equals("out")) {
      inputAssetDetail.setExist("出库");
    } else if (tag.equals("enter")) {
      inputAssetDetail.setExist("入库");
    }else if(tag.equals("InventoryAssetsFragment")){
      inputAssetDetail.setExist(mAssetDetail.getExist());
    }*/

    if (isExist) {
      //删除这条信息
      DbUtils.delete(mAssetDetail);
    }

    DbUtils.insert(inputAssetDetail);

  }


  @OnClick({R.id.btn_enter, R.id.btn_out, R.id.ib_calendar_production_date, R.id.ib_calendar_check_date,
          R.id.ib_calendar_next_check_date, R.id.iv, R.id.btn_check_num,R.id.btn_sure})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.btn_enter:
        enterOrOutlibrary("确定入库？");
        /*if(tag.equals("enter")){
          message = "确定入库？";
        }else if(tag.equals("out")){
          message = "确定出库？";
        }else if(tag.equals("InventoryAssetsFragment")||tag.equals("BatchScanActivity")){
          message = "确定修改？";
        }*/


        break;
      case R.id.btn_out://出库
        enterOrOutlibrary("确定出库？");
        break;
      case R.id.ib_calendar_production_date://出厂日期
        new DatePickDialogUtil(this, -1, false).datePicKDialog(etDateOfProduction);
        break;
      case R.id.ib_calendar_check_date://校验日期
        new DatePickDialogUtil(this, -1, false).datePicKDialog(etCheckDate);
        break;
      case R.id.ib_calendar_next_check_date://下次校验日期
        new DatePickDialogUtil(this, -1, false).datePicKDialog(etNextCheckDate);
        break;
      case R.id.iv:
        if (iv.getDrawable() == null) {
          CameraUtil.takePhoto(this, archivesNum);
        } else {
          Intent intent = new Intent(this, ImageActivity.class);
          intent.putExtra("imgPath", imgPath);
          startActivity(intent);
        }
        break;
      case R.id.btn_check_num:
        //自动生成检测编号
        String checkNum = szm + "-JC-" + currentTimeMillis();
        etCheckNum.setText(checkNum);
      case R.id.btn_sure:

        enterOrOutlibrary("确定修改？");

        /*DialogUtils.showMDDialog(this, "提示", "确认修改？", new DialogUtils.OnResponseCallBack() {
          @Override
          public void onPositiveClick() {
            saveData("确定");
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
          }
          @Override
          public void onNegativeClick() {}
        });*/

        break;
    }
  }

  /**
   *
   * 出库或入库
   *
   * @param message
   */
  private void enterOrOutlibrary(final String message) {
    if (((String) spIsGood.getSelectedItem()).equals("不合格")) {
      DialogUtils.show(this, "提示", "该工具不合格，禁止使用！", new DialogUtils.OnPositiveCall() {
        @Override
        public void goOn() {
          if (isExist) {
            mAssetDetail.setIsGood("不合格");
            mAssetDetail.setExist("");
            DbUtils.update(mAssetDetail);
          } else {
            saveData("不合格");
          }
          setResult(RESULTCODE_1);
          finish();
          overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
      });
    } else {
      DialogUtils.show(this, "提示", message, new DialogUtils.OnPositiveCall() {
        @Override
        public void goOn() {
          saveData(message);
          //将数据传回去
          Event.InventoryAssetsEvent inventoryAssetsEvent = new Event.InventoryAssetsEvent();
          inventoryAssetsEvent.setAssetDetail(inputAssetDetail);
          inventoryAssetsEvent.setTag(message);
          EventBus.getDefault().post(inventoryAssetsEvent);
          setResult(RESULTCODE_1);
          finish();
          overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }

      });
    }
  }

  public int getSelectedPosition(String name) {
    for (int i = 0; i < assetsType.length; i++) {
      if (name.contains(assetsType[i])) {
        return i;
      }
    }

    return 0;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      MyApplication.showImageView(imgPath, iv);
    }
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
