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

  private static DialogUtils dialogUtils;

  public static DialogUtils getInstance(){
    if (dialogUtils == null){
      dialogUtils = new DialogUtils();
    }
  return dialogUtils;
}
  private static AlertDialog.Builder dialog;
  private static MaterialDialog.Builder builder;
  public  MaterialDialog dialog1;

  public static void show(final Activity context, String title, String message, final OnPositiveCall call){

    if(dialog == null){
      dialog = new AlertDialog.Builder(context);
    }

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

  public  void showMDDialog(Context context, String title, String message, final OnPositiveCall call){
    if(builder == null){
      builder = new MaterialDialog.Builder(context);
    }

    if(dialog1 != null && dialog1.isShowing()){
      dialog1.setContent(message);
    }else{
      builder.title(title)
              .content(message)
              .positiveText("停止")
              .negativeText("取消")
              .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                  if(call != null){
                    call.goOn();

                  }
                }
              });
      dialog1 = builder.build();
      dialog1.setCancelable(false);
      dialog1.show();
    }

  }

  public interface OnPositiveCall{
    void goOn();
  }
}
