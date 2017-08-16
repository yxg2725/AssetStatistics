package com.huadin.assetstatistics.app;

import android.app.Application;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huadin.assetstatistics.utils.MyExceptionHandler;
import com.uhf.linkage.Linkage;

import static android.R.attr.path;

/**
 * Created by 华电 on 2017/7/19.
 */

public class MyApplication extends Application {

  private static Context context;
  public  static boolean connectSuccess = false;
  public  static boolean canScan = false;
  private static Linkage link;

  @Override
  public void onCreate() {
    super.onCreate();

    context = this;
//    Thread.setDefaultUncaughtExceptionHandler(MyExceptionHandler.getInstance(getApplicationContext()));
    link = new Linkage();
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

  public static void showImageView(int id,ImageView img) {
    Glide.with(MyApplication.context)
            .load(id)
            .diskCacheStrategy(DiskCacheStrategy.NONE)//不保留磁盘缓存
            .skipMemoryCache(true)//不保留内存缓存
            .crossFade()//设置渐渐显示的效果
            .into(img);
  }

  public static Linkage getLinkage() {
    if (link == null) {
      link = new Linkage();
    }
    return link;
  }

}
