package com.huadin.assetstatistics.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 华电 on 2017/7/19.
 */

public class AssetsStyle {

  private String asssetStyle;//设备类型名称
  private int count;//总个数
  private int existNum;//库存个数
  private int outNum;//出库个数
  private int notGood;//不合格个数

  public int getNotGood() {
    return notGood;
  }

  public void setNotGood(int notGood) {
    this.notGood = notGood;
  }

  public String getAsssetStyle() {
    return asssetStyle;
  }

  public void setAsssetStyle(String asssetStyle) {
    this.asssetStyle = asssetStyle;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public int getExistNum() {
    return existNum;
  }

  public void setExistNum(int existNum) {
    this.existNum = existNum;
  }

  public int getOutNum() {
    return outNum;
  }

  public void setOutNum(int outNum) {
    this.outNum = outNum;
  }
}
