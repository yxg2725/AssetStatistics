package com.huadin.assetstatistics.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.labelview.LabelView;
import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.activity.PatchScanActivity;
import com.huadin.assetstatistics.app.MyApplication;
import com.huadin.assetstatistics.bean.AssetDetail;

import java.util.ArrayList;

import static android.graphics.Typeface.NORMAL;

/**
 * Created by 华电 on 2017/8/3.
 */

public class MultilStyleAdapter extends RecyclerView.Adapter {
  private static final int NORECORD = 0;
  private static final int NORMAL = 1;
  private final PatchScanActivity patchScanActivity;
  private final ArrayList<Object> list;

  public MultilStyleAdapter(PatchScanActivity patchScanActivity, ArrayList<Object> list) {
    this.patchScanActivity = patchScanActivity;
    this.list = list;
  }

  @Override
  public int getItemViewType(int position) {
    if(list.get(position) instanceof String){
      return NORECORD;
    }else {
      return NORMAL;
    }
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    if(viewType == NORECORD){
      View view0 = LayoutInflater.from(parent.getContext()).inflate(R.layout.no_record_item,null);

      return new NoRecordHolder(view0);
    }else{
      View view1 = View.inflate(parent.getContext(), R.layout.item_out_asset,null);
      return new NormalHolder(view1);
    }

  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    Object object = list.get(position);
    int itemViewType = getItemViewType(position);

    if(itemViewType == NORECORD){
      NoRecordHolder holder0 = (NoRecordHolder) holder;
      String code = (String)object;
      holder0.tvCode.setText(code);
    }else{
      NormalHolder holder1 = (NormalHolder) holder;
      AssetDetail asset = (AssetDetail)object;

      MyApplication.showImageView(asset.getImgPath(),holder1.iv);//图片
      holder1.deveiceName.setText(asset.getAssetName());//设备名称
      holder1.deveiceId.setText(asset.getDeviceId());//设备型号
      holder1.usedCompany.setText(asset.getUsedCompany());//使用单位
      holder1.manufacturer.setText(asset.getManufacturer());//生产厂家
      holder1.dateOfProduction.setText(asset.getDateOfProduction());//生产日期
      holder1.inspectionNumber.setText(asset.getInspectionNumber());//检测编号
      holder1.archivesNumber.setText(asset.getArchivesNumber());//档案编号
      holder1.checkDate.setText(asset.getCheckDate());//检验日期
      holder1.nextCheckDate.setText(asset.getNextCheckDate());//下次校验日期
      holder1.checkPeople.setText(asset.getCheckPeople());//校验员
      holder1.labelView.setText("合格");
      holder1.labelView.setGravity(Gravity.TOP|Gravity.RIGHT);
      holder1.labelView.setBgColor (Color.parseColor("#3F9FE0"));
      holder1.labelView.setFillTriangle(true);
    }
  }

  @Override
  public int getItemCount() {
    return list == null ? 0:list.size();
  }

  class NoRecordHolder extends RecyclerView.ViewHolder{

    public TextView tvCode;
    public NoRecordHolder(View itemView) {
      super(itemView);
      tvCode = (TextView) itemView.findViewById(R.id.tv_barcode);
    }
  }

  class NormalHolder extends RecyclerView.ViewHolder{

    public final TextView deveiceName;
    public final TextView deveiceId;
    public final TextView usedCompany;
    public final TextView manufacturer;
    public final TextView dateOfProduction;
    public final TextView inspectionNumber;
    public final TextView archivesNumber;
    public final TextView checkDate;
    public final TextView nextCheckDate;
    public final TextView checkPeople;
    public final ImageView iv;
    public final ImageView ivDown;
    public final LinearLayout llBottom;
    public final LabelView labelView;

    public NormalHolder(View itemView) {
      super(itemView);

      deveiceName = (TextView) itemView.findViewById(R.id.device_name);
      deveiceId = (TextView) itemView.findViewById(R.id.device_id);
      usedCompany = (TextView) itemView.findViewById(R.id.used_company);
      manufacturer = (TextView) itemView.findViewById(R.id.manufacturer);
      dateOfProduction = (TextView) itemView.findViewById(R.id.date_of_production);
      inspectionNumber = (TextView) itemView.findViewById(R.id.inspection_number);
      archivesNumber = (TextView) itemView.findViewById(R.id.archives_number);
      checkDate = (TextView) itemView.findViewById(R.id.check_date);
      nextCheckDate = (TextView) itemView.findViewById(R.id.next_check_date);
      checkPeople = (TextView) itemView.findViewById(R.id.check_people);
      iv = (ImageView) itemView.findViewById(R.id.iv);
      ivDown = (ImageView) itemView.findViewById(R.id.iv_down);
      llBottom = (LinearLayout) itemView.findViewById(R.id.ll_bottom);
      labelView = (LabelView) itemView.findViewById(R.id.labelView);

    }
  }
}
