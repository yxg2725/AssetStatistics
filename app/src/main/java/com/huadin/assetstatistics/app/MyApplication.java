package com.huadin.assetstatistics.app;

import android.app.Application;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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

  public static void showImageView(String path,ImageView img) {
    Glide.with(MyApplication.context)
            .load(path)
            .diskCacheStrategy(DiskCacheStrategy.NONE)//不保留磁盘缓存
            .skipMemoryCache(true)//不保留内存缓存
            .crossFade()//设置渐渐显示的效果
            .into(img);
  }
}
