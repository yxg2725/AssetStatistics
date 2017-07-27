package com.huadin.assetstatistics.app;

import android.app.Application;
import android.content.Context;

import com.huadin.assetstatistics.utils.MyExceptionHandler;

/**
 * Created by 华电 on 2017/7/19.
 */

public class MyApplication extends Application {

  private static Context context;
  public  static boolean connectSuccess;

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
