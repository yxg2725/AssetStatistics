package com.huadin.assetstatistics.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.huadin.assetstatistics.R;
import com.huadin.assetstatistics.bean.AssetDetail;

import java.util.ArrayList;

/**
 * Created by admin on 2017/7/19.
 */

public class AssetDetailAdapter extends BaseAdapter<AssetDetail> {

    public AssetDetailAdapter(ArrayList list) {
        super(list);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_asset_detail;
    }



    @Override
    public void setItemView(View itemView,int position) {
        TextView tvAssetName = (TextView) itemView.findViewById(R.id.tv_asset_name);
        TextView tvEnterEime = (TextView) itemView.findViewById(R.id.tv_enter_time);
        TextView tvReceivePeople = (TextView) itemView.findViewById(R.id.tv_receive_people);

        AssetDetail assetDetail = list.get(position);
        tvAssetName.setText(assetDetail.getAssetName());
        tvEnterEime.setText(assetDetail.getEnterTime());
        tvReceivePeople.setText(assetDetail.getPeople());
    }



}
