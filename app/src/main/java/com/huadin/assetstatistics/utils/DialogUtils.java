package com.huadin.assetstatistics.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.huadin.assetstatistics.R;

/**
 * Created by 华电 on 2017/7/19.
 */

public class DialogUtils {

  public static void show(final Activity context){

    AlertDialog.Builder customizeDialog = new AlertDialog.Builder(context);
    final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_category,null);
    customizeDialog.setTitle("添加资产分类");
    customizeDialog.setView(dialogView);
    customizeDialog.setPositiveButton("确定",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                // 获取EditView中的输入内容
              }
            });
    customizeDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        // 获取EditView中的输入内容
      }
    });
    customizeDialog.show();
  }

  public void dismiss(){

  }
}
