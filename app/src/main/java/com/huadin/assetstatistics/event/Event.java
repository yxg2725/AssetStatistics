package com.huadin.assetstatistics.event;

import com.huadin.assetstatistics.bean.AssetDetail;

/**
 * Created by 华电 on 2017/7/25.
 */

public class Event {
  public static class InventoryAssetsEvent{
    private AssetDetail assetDetail;
    private String tag;

    public String getTag() {
      return tag;
    }

    public void setTag(String tag) {
      this.tag = tag;
    }

    public AssetDetail getAssetDetail() {
      return assetDetail;
    }
    public void setAssetDetail(AssetDetail assetDetail) {
      this.assetDetail = assetDetail;
    }
  }
}
