package com.huadin.assetstatistics.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 华电 on 2017/7/19.
 */

@Entity
public class AssetsStyle {
  @Id
  private Long id;
  private String asssetStyle;//设备类型名称
  private int count;
  private String unit;

  @Generated(hash = 1442711058)
  public AssetsStyle(Long id, String asssetStyle, int count, String unit) {
    this.id = id;
    this.asssetStyle = asssetStyle;
    this.count = count;
    this.unit = unit;
  }

  @Generated(hash = 882752957)
  public AssetsStyle() {
  }

  /*public AssetsStyle(){}
  protected AssetsStyle(Parcel in) {
    asssetStyle = in.readString();
    count = in.readInt();
    unit = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(asssetStyle);
    dest.writeInt(count);
    dest.writeString(unit);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<AssetsStyle> CREATOR = new Creator<AssetsStyle>() {
    @Override
    public AssetsStyle createFromParcel(Parcel in) {
      return new AssetsStyle(in);
    }

    @Override
    public AssetsStyle[] newArray(int size) {
      return new AssetsStyle[size];
    }
  };*/

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

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
