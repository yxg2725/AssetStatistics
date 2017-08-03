package com.huadin.assetstatistics.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 华电 on 2017/7/31.
 */

public class SharedPreferenceUtils {

  private static SharedPreferences mSp;


  public  SharedPreferenceUtils(Activity context){
    if(mSp == null){
      mSp = context.getSharedPreferences("config", MODE_PRIVATE);
    }
  }

  public void putString(String key,String value){
    mSp.edit().putString(key,value).commit();
  }

  public String getString(String key){
    return mSp.getString(key,"");
  }

  public void putBoolean(String key,boolean b){
    mSp.edit().putBoolean(key,b).commit();
  }

  public boolean getBoolean(String key,boolean def){
    boolean aBoolean = mSp.getBoolean(key, def);
    return aBoolean;
  }
}
