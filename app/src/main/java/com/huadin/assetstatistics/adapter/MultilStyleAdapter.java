package com.huadin.assetstatistics.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.labelview.LabelView;
import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.activity.BatchScanActivity;
import com.huadin.assetstatistics.activity.ImageActivity;
import com.huadin.assetstatistics.activity.MainActivity;
import com.huadin.assetstatistics.app.MyApplication;
import com.huadin.assetstatistics.bean.AssetDetail;
import com.huadin.assetstatistics.utils.DbUtils;
import com.huadin.assetstatistics.utils.DialogUtils;
import com.huadin.assetstatistics.widget.dragItem.ItemTouchHelperAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

/**
 * Created by 华电 on 2017/8/3.
 */

public class MultilStyleAdapter extends RecyclerView.Adapter  {
  private static final int NORECORD = 0;
  private static final int NORMAL = 1;
  private final BatchScanActivity context;
  private final ArrayList<Object> list;
  private NoRecordHolder holder0;
  private NormalHolder holder1;

  public MultilStyleAdapter(BatchScanActivity context, ArrayList<Object> list) {
    this.context = context;
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
      View view0 = LayoutInflater.from(parent.getContext()).inflate(R.layout.no_record_item,parent,false);

      return new NoRecordHolder(view0);
    }else{
      View view1 = View.inflate(parent.getContext(), R.layout.item_out_asset,null);
      return new NormalHolder(view1);
    }

  }

  public RecyclerView.ViewHolder getViewHolder( int position){
    int itemViewType = getItemViewType(position);
    if(itemViewType == NORECORD){
      return holder0;
    }
    return holder1;
  }

  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
    Object object = list.get(position);
    int itemViewType = getItemViewType(position);

    if(itemViewType == NORECORD){
      holder0 = (NoRecordHolder) holder;
      String code = (String)object;
      holder0.tvCode.setText(code);
      holder0.setItemBackGround(position);
    }else{
      holder1 = (NormalHolder) holder;
      holder1.setItemBackGround(position);
      final AssetDetail asset = (AssetDetail)object;

      MyApplication.showImageView(asset.getImgPath(), holder1.iv);//图片
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

      holder1.usedDepartment.setText(asset.getUsedDepartment());//使用部门
      holder1.custodian.setText(asset.getCustodian());//保管人
      holder1.datePurchase.setText(asset.getDatePurchase());//购置日期

      holder1.labelView.setGravity(Gravity.TOP|Gravity.RIGHT);

      holder1.labelView.setFillTriangle(true);

      if(TextUtils.equals(asset.getIsGood(),"不合格")){
        holder1.labelView.setText("不合格");
        holder1.labelView.setBgColor (Color.parseColor("#FFCC3232"));
      }else{
        holder1.labelView.setText("合格");
        holder1.labelView.setBgColor (Color.parseColor("#3F9FE0"));
      }

      holder1.iv.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if(holder1.iv.getDrawable() != null){
            Intent intent = new Intent(context, ImageActivity.class);
            intent.putExtra("imgPath", asset.getImgPath());
            context.startActivity(intent);
          }
        }
      });

      holder1.ivDown.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if(holder1.llBottom.getVisibility() == View.GONE){
            holder1.llBottom.setVisibility(View.VISIBLE);
            MyApplication.showImageView(R.drawable.arrow_up, holder1.ivDown);
          }else{
            holder1.llBottom.setVisibility(View.GONE);
            MyApplication.showImageView(R.drawable.arrow_down, holder1.ivDown);
          }
        }
      });
    }

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(listener != null){
          listener.onItemClick(holder.getLayoutPosition());
        }
      }
    });

    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {

        if(longClikcListener != null){
          //显示CheckBox
          longClikcListener.onItemLongClick(position);
        }

        /*DialogUtils.showMDDialog(context, "提示", "是否删除此数据", new DialogUtils.OnResponseCallBack() {
          @Override
          public void onPositiveClick() {
            //删除数据库
            if (list.get(holder.getLayoutPosition()) instanceof AssetDetail){
              DbUtils.delete(list.get(holder.getLayoutPosition()));
            }

            list.remove(holder.getLayoutPosition());
            notifyItemRemoved(holder.getLayoutPosition());

          }

          @Override
          public void onNegativeClick() {
            notifyDataSetChanged();
          }
        });*/
        return true;
      }
    });
  }

  @Override
  public int getItemCount() {
    return list == null ? 0:list.size();
  }

  class NoRecordHolder extends RecyclerView.ViewHolder{

    public TextView tvCode;
    public CheckBox checkBox;
    public NoRecordHolder(View itemView) {
      super(itemView);
      tvCode = (TextView) itemView.findViewById(R.id.tv_barcode);
      checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
    }


    public void setItemBackGround(int position) {

      Set<Integer> positionSet = context.positionSet;
      if (positionSet.contains(position)) {
        itemView.setBackgroundColor(Color.BLUE);
      } else {
        itemView.setBackgroundColor(Color.WHITE);
      }
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
    public final CheckBox checkBox;

    public final TextView usedDepartment;
    public final TextView custodian;
    public final TextView datePurchase;

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
      checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);

      usedDepartment = (TextView) itemView.findViewById(R.id.used_department);
      custodian = (TextView) itemView.findViewById(R.id.custodian);
      datePurchase = (TextView) itemView.findViewById(R.id.date_purchase);

    }

    public void setItemBackGround(int position) {
      Set<Integer> positionSet = context.positionSet;
      if (positionSet.contains(position)) {
        itemView.setBackgroundColor(Color.BLUE);
      } else {
        itemView.setBackgroundColor(Color.WHITE);
      }
    }
  }

  public OnItemClickListener listener;
  public interface OnItemClickListener{
    void onItemClick(int position);
  }

  public void setOnItemClickListener(OnItemClickListener listener){
    this.listener = listener;
  }

  public OnItemLongClickListener longClikcListener;
  public interface OnItemLongClickListener{
    void onItemLongClick(int position);
  }

  public void setOnItemLongClickListener(OnItemLongClickListener longClikcListener){
    this.longClikcListener = longClikcListener;
  }

}
