package com.huadin.assetstatistics.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.huadin.assetstatistics.R;

/**
 * Created by 华电 on 2017/7/19.
 */

public class DialogUtils {


  public static void show(final Activity context, String title, String message, final OnPositiveCall call){

    AlertDialog.Builder dialog = new AlertDialog.Builder(context);

    dialog.setTitle(title);
    dialog.setIcon(android.R.drawable.ic_dialog_alert);
    dialog.setMessage(message);
    dialog.setPositiveButton("确定",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                if(call != null){
                  call.goOn();
                }
              }
            });
    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
    dialog.show();
  }

  public void dismiss(){

  }

  public static void showMDDialog(Context context){
    MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
            .title("入库资产")
            .customView(R.layout.custom_dialog, true)
            .positiveText("确定")
            .negativeText("取消")
            .onPositive(new MaterialDialog.SingleButtonCallback() {
              @Override
              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

              }
            });
    MaterialDialog dialog = builder.build();
    dialog.show();

  }

  public interface OnPositiveCall{
    void goOn();
  }
}
