package com.huadin.assetstatistics.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by 华电 on 2017/7/19.
 */

public class ToastUtils {

  private static Snackbar mSnackbar;

  public static void show(View view, String msg){
    if(mSnackbar == null){
      mSnackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT);
    }
    mSnackbar.setText(msg).show();
  }
}
