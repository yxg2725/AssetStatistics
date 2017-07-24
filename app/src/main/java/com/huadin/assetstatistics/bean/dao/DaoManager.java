package com.huadin.assetstatistics.bean.dao;

/**
 * Created by 华电 on 2017/5/12.
 */

import android.database.sqlite.SQLiteDatabase;

import com.huadin.assetstatistics.app.MyApplication;
import com.huadin.assetstatistics.gen.DaoMaster;
import com.huadin.assetstatistics.gen.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * 进行数据库的管理
 * 1.创建数据库
 * 2.创建数据库表
 * 3.对数据库进行增删查改
 * 4.对数据库进行升级
 */
public class DaoManager {
  private static  final String   TAG = DaoManager.class.getSimpleName();
  private static  final String  DB_NAME="asset.db";//数据库名称
  private volatile  static DaoManager mDaoManager;//多线程访问
  private  static DaoMaster.DevOpenHelper mHelper;
  private static  DaoMaster mDaoMaster;
  private static DaoSession mDaoSession;
  private static SQLiteDatabase db;

  /**
   * 使用单例模式获得操作数据库的对象
   * @return
   */
  public  static DaoManager getInstance(){
    DaoManager instance = null;
    if (mDaoManager==null){
      synchronized (DaoManager.class){
        if (instance==null){
          instance = new DaoManager();
          mDaoManager = instance;
        }
      }
    }
    return mDaoManager;
  }


  /**
   * 判断数据库是否存在，如果不存在则创建
   * @return
   */
  public DaoMaster getDaoMaster(){
    if (null == mDaoMaster){
      mHelper =  new DaoMaster.DevOpenHelper(MyApplication.getContext(),DB_NAME,null);
      mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
    }
    return mDaoMaster;
  }

  /**
   * 完成对数据库的增删查找
   * @return
   */
  public DaoSession getDaoSession(){
    if (null == mDaoSession){
      if (null == mDaoMaster){
        mDaoMaster = getDaoMaster();
      }
      mDaoSession = mDaoMaster.newSession();
    }
    return mDaoSession;
  }

  /**
   * 设置debug模式开启或关闭，默认关闭
   * @param flag
   */
  public void setDebug(boolean flag){
    QueryBuilder.LOG_SQL = flag;
    QueryBuilder.LOG_VALUES = flag;
  }

  /**
   * 关闭数据库
   */
  public void closeDataBase(){
    closeHelper();
    closeDaoSession();
  }

  public void closeDaoSession(){
    if (null != mDaoSession){
      mDaoSession.clear();
      mDaoSession = null;
    }
  }

  public  void  closeHelper(){
    if (mHelper!=null){
      mHelper.close();
      mHelper = null;
    }
  }

}
