package com.huadin.assetstatistics.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.labelview.LabelView;
import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.activity.ImageActivity;
import com.huadin.assetstatistics.app.MyApplication;
import com.huadin.assetstatistics.bean.AssetDetail;
import com.huadin.assetstatistics.utils.DatePickDialogUtil;

import java.util.ArrayList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by 华电 on 2017/7/22.
 */

public class OutAssetsAdapter extends BaseAdapter<AssetDetail> {
  public OutAssetsAdapter(Context context, ArrayList list) {
    super(context,list);
  }

  @Override
  public int getItemLayoutId() {
    return R.layout.item_out_asset;
  }

  @Override
  public void setItemView(View itemView, int postion) {
    final AssetDetail assetDetail = list.get(postion);
    TextView deveiceName = (TextView) itemView.findViewById(R.id.device_name);
    TextView deveiceId = (TextView) itemView.findViewById(R.id.device_id);
    TextView usedCompany = (TextView) itemView.findViewById(R.id.used_company);
    TextView manufacturer = (TextView) itemView.findViewById(R.id.manufacturer);
    TextView dateOfProduction = (TextView) itemView.findViewById(R.id.date_of_production);
    TextView inspectionNumber = (TextView) itemView.findViewById(R.id.inspection_number);
    TextView archivesNumber = (TextView) itemView.findViewById(R.id.archives_number);
    TextView checkDate = (TextView) itemView.findViewById(R.id.check_date);
    TextView nextCheckDate = (TextView) itemView.findViewById(R.id.next_check_date);
    TextView checkPeople = (TextView) itemView.findViewById(R.id.check_people);

    TextView department = (TextView) itemView.findViewById(R.id.used_department);
    TextView custodian = (TextView) itemView.findViewById(R.id.custodian);
    TextView datePurchase = (TextView) itemView.findViewById(R.id.date_purchase);

    final ImageView iv = (ImageView) itemView.findViewById(R.id.iv);
    final ImageView ivDown = (ImageView) itemView.findViewById(R.id.iv_down);
    final LinearLayout llBottom = (LinearLayout) itemView.findViewById(R.id.ll_bottom);
    com.flyco.labelview.LabelView labelView = (LabelView) itemView.findViewById(R.id.labelView);

    MyApplication.showImageView(assetDetail.getImgPath(),iv);//图片
    deveiceName.setText(assetDetail.getAssetName());//设备名称
    deveiceId.setText(assetDetail.getDeviceId());//设备型号
    usedCompany.setText(assetDetail.getUsedCompany());//使用单位
    manufacturer.setText(assetDetail.getManufacturer());//生产厂家
    dateOfProduction.setText(assetDetail.getDateOfProduction());//生产日期
    inspectionNumber.setText(assetDetail.getInspectionNumber());//检测编号
    archivesNumber.setText(assetDetail.getArchivesNumber());//档案编号
    checkDate.setText(assetDetail.getCheckDate());//检验日期
    nextCheckDate.setText(assetDetail.getNextCheckDate());//下次校验日期
    checkPeople.setText(assetDetail.getCheckPeople());//校验员

    department.setText(assetDetail.getUsedDepartment());//使用部门
    custodian.setText(assetDetail.getCustodian());//保管人
    datePurchase.setText(assetDetail.getDatePurchase());//购置日期

    labelView.setGravity(Gravity.TOP|Gravity.RIGHT);
    labelView.setFillTriangle(true);
    if(TextUtils.equals(assetDetail.getIsGood(),"不合格")){
      labelView.setText("不合格");
      labelView.setBgColor (Color.parseColor("#FFCC3232"));
    }else{
      labelView.setText("合格");
      labelView.setBgColor (Color.parseColor("#3F9FE0"));
    }



      iv.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if(iv.getDrawable() != null){
          Intent intent = new Intent(context, ImageActivity.class);
          intent.putExtra("imgPath", assetDetail.getImgPath());
          context.startActivity(intent);
          }
        }
      });

    ivDown.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(llBottom.getVisibility() == View.GONE){
          llBottom.setVisibility(View.VISIBLE);
          MyApplication.showImageView(R.drawable.arrow_up,ivDown);
        }else{
          llBottom.setVisibility(View.GONE);
          MyApplication.showImageView(R.drawable.arrow_down,ivDown);
        }

      }
    });


  }




}
