package com.huadin.assetstatistics.widget.dragItem;

/**
 * Created by 华电 on 2017/8/4.
 */

public interface ItemTouchHelperAdapter {
  //数据交换
  void onItemMove(int fromPosition,int toPosition);
  //数据删除
  void onItemDissmiss(int position);
}
