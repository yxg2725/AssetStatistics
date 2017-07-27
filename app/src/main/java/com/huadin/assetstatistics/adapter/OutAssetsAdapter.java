package com.huadin.assetstatistics.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.flyco.labelview.LabelView;
import com.huadin.assetstatistics.R;
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
    AssetDetail assetDetail = list.get(postion);
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
    com.flyco.labelview.LabelView labelView = (LabelView) itemView.findViewById(R.id.labelView);


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
    labelView.setText("合格");
    labelView.setGravity(Gravity.TOP|Gravity.RIGHT);
    labelView.setBgColor (Color.parseColor("#3F9FE0"));
    labelView.setFillTriangle(true);
  }




}
