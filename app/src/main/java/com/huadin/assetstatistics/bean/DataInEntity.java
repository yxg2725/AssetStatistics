package com.huadin.assetstatistics.bean;

import java.util.ArrayList;

/**
 * Created by 华电 on 2017/8/14.
 */

public class DataInEntity {
  private String name;
  private ArrayList<ArrayList<String>> list;

  public DataInEntity(String name, ArrayList<ArrayList<String>> list) {
    this.name = name;
    this.list = list;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ArrayList<ArrayList<String>> getList() {
    return list;
  }

  public void setList(ArrayList<ArrayList<String>> list) {
    this.list = list;
  }
}
