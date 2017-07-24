package com.huadin.assetstatistics.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.huadin.assetstatistics.activity.MainActivity;
import com.huadin.assetstatistics.utils.MyExceptionHandler;
import com.huadin.assetstatistics.utils.RFIDUtils;
import com.pow.api.cls.RfidPower;
import com.uhf.api.cls.Reader;

/**
 * Created by 华电 on 2017/7/19.
 */

public class MyApplication extends Application {

  private static Context context;

  @Override
  public void onCreate() {
    super.onCreate();

    context = this;
    Thread.setDefaultUncaughtExceptionHandler(MyExceptionHandler.getInstance(getApplicationContext()));

  }

  public static Context getContext(){
    return context;
  }


}
