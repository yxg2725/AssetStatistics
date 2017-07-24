package com.huadin.assetstatistics.utils;

import com.huadin.assetstatistics.bean.dao.DaoManager;
import com.huadin.assetstatistics.gen.AssetDetailDao;
import com.huadin.assetstatistics.gen.AssetsStyleDao;
import com.huadin.assetstatistics.gen.DaoSession;

import org.greenrobot.greendao.query.Query;

import java.util.List;

/**
 * Created by 华电 on 2017/7/24.
 */

public class DbUtils {

  public static<T> void insert(T bean){
    DaoSession daoSession = DaoManager.getInstance().getDaoSession();
    daoSession.insertOrReplace(bean);
  }

  public static<T> void delete(T bean){
    DaoSession daoSession = DaoManager.getInstance().getDaoSession();
    daoSession.delete(bean);
  }

  public static<T> List<T> queryAll(Class<T> clazz){
    DaoSession daoSession = DaoManager.getInstance().getDaoSession();
    List<T> list = daoSession.loadAll(clazz);
    return list;
  }

  public static <T> void deleteAll(Class<T> clazz){
    DaoSession daoSession = DaoManager.getInstance().getDaoSession();
    daoSession.deleteAll(clazz);
  }

  public static<T> List<T> query(Class<T> clazz,String name){
    DaoSession daoSession = DaoManager.getInstance().getDaoSession();
    List<T> list = daoSession.queryBuilder(clazz).where(AssetDetailDao.Properties.AssetName.eq(name)).build().list();
    return list;
  }
}
