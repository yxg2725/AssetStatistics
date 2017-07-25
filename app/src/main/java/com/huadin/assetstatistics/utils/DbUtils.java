package com.huadin.assetstatistics.utils;

import com.huadin.assetstatistics.bean.dao.DaoManager;
import com.huadin.assetstatistics.gen.AssetDetailDao;
import com.huadin.assetstatistics.gen.AssetsStyleDao;
import com.huadin.assetstatistics.gen.DaoSession;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import static android.R.attr.name;

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

  public static<T> List<T> queryByName(Class<T> clazz,String name){
    DaoSession daoSession = DaoManager.getInstance().getDaoSession();
    List<T> list = daoSession.queryBuilder(clazz).where(AssetDetailDao.Properties.AssetName.eq(name)).build().list();
    return list;
  }
  public static<T> T queryByCode(Class<T> clazz,String code){
    DaoSession daoSession = DaoManager.getInstance().getDaoSession();
    T unique = daoSession.queryBuilder(clazz).where(AssetDetailDao.Properties.Barcode.eq(code)).build().unique();
    if(unique == null){
      return null;
    }
    return unique;
  }
  public static<T> List<T> queryByExist(Class<T> clazz,String exist){
    DaoSession daoSession = DaoManager.getInstance().getDaoSession();
    List<T> list = daoSession.queryBuilder(clazz).where(AssetDetailDao.Properties.Exist.eq(exist)).build().list();

    return list;
  }
}
