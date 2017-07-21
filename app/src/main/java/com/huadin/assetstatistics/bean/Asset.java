package com.huadin.assetstatistics.bean;

/**
 * Created by 华电 on 2017/7/19.
 */

public class Asset {
  private String name;
  private int count;
  private String unit;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }
}
