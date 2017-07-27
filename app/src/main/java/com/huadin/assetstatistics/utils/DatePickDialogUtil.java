package com.huadin.assetstatistics.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;


import com.huadin.assetstatistics.R;

import java.util.Calendar;

/**
 * Created by 华电 on 2017/5/13.
 */

public class DatePickDialogUtil implements DatePicker.OnDateChangedListener {
  private DatePicker datePicker;
  private AlertDialog ad;
  private String dateTime;
  private long initDateTime;
  private Context context;
  private boolean isSetMinDate;

  /**
   * 日期时间弹出选择框构造函数
   *
   * @param context
   *            ：调用的父activity
   * @param initDateTime
   *            初始日期时间值，作为弹出窗口的标题和日期时间初始值
   */
  public DatePickDialogUtil(Context context, long initDateTime, boolean isSetMinDate) {
    this.context = context;
    this.initDateTime = initDateTime;
    this.isSetMinDate = isSetMinDate;
  }

  public void init(DatePicker datePicker) {
    Calendar calendar = Calendar.getInstance();
    if (initDateTime > 0) {
      calendar.setTimeInMillis(initDateTime);
    } else {
      initDateTime = System.currentTimeMillis();
    }

    if(isSetMinDate){
      try{
        datePicker.setMinDate(initDateTime);
      }catch(Exception ex){
      }

    }
    datePicker.init(calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH), this);
  }

  /**
   * 弹出日期时间选择框方法
   *
   * @param inputDate
   *            :为需要设置的日期时间文本编辑框
   * @return
   */
  public AlertDialog datePicKDialog(final EditText inputDate) {

    LinearLayout dateTimeLayout = (LinearLayout) LayoutInflater.from(context)
            .inflate(R.layout.common_datetime,null);
    datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);

    init(datePicker);


    ad = new AlertDialog.Builder(context)
            .setTitle(DateUtil.toymdhms(initDateTime))
            .setView(dateTimeLayout)
            .setPositiveButton("设置", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int whichButton) {
                ad.dismiss();
                inputDate.setText(dateTime);

              }
            })
            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int whichButton) {
                inputDate.setText("");
                ad.dismiss();
              }
            }).show();

    onDateChanged(null, 0, 0, 0);
    return ad;
  }

  public void onDateChanged(DatePicker view, int year, int monthOfYear,
                            int dayOfMonth) {
    // 获得日历实例
    Calendar calendar = Calendar.getInstance();
      calendar.set(datePicker.getYear(), datePicker.getMonth(),
              datePicker.getDayOfMonth(),0,0,0);
      dateTime = DateUtil.timestamp2ymd(calendar.getTimeInMillis());

    ad.setTitle(dateTime);
  }
}
