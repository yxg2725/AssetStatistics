package com.huadin.assetstatistics.utils;

import com.huadin.assetstatistics.bean.dao.DaoManager;
import com.huadin.assetstatistics.gen.AssetDetailDao;
import com.huadin.assetstatistics.gen.DaoSession;

import java.util.List;

/**
 * Created by 华电 on 2017/7/24.
 */

public class DbUtils {

  //插入一条数据
  public static<T> void insert(T bean){
    DaoSession daoSession = DaoManager.getInstance().getDaoSession();
    daoSession.insertOrReplace(bean);
  }

//  删除指定一条数据
  public static<T> void delete(T bean){
    DaoSession daoSession = DaoManager.getInstance().getDaoSession();
    daoSession.delete(bean);
  }

//  查询表中的所有数据
  public static<T> List<T> queryAll(Class<T> clazz){
    DaoSession daoSession = DaoManager.getInstance().getDaoSession();
    List<T> list = daoSession.loadAll(clazz);
    return list;
  }

//  删除表中的所数据
  public static <T> void deleteAll(Class<T> clazz){
    DaoSession daoSession = DaoManager.getInstance().getDaoSession();
    daoSession.deleteAll(clazz);
  }

//  根据工具名称的查询
  public static<T> List<T> queryByName(Class<T> clazz,String name){
    DaoSession daoSession = DaoManager.getInstance().getDaoSession();
    List<T> list = daoSession.queryBuilder(clazz).where(AssetDetailDao.Properties.AssetName.eq(name)).build().list();
    return list;
  }

  //根据条码号的查询
  public static<T> T queryByCode(Class<T> clazz,String code){
    DaoSession daoSession = DaoManager.getInstance().getDaoSession();
    T unique = daoSession.queryBuilder(clazz).where(AssetDetailDao.Properties.Barcode.eq(code)).build().unique();
    if(unique == null){
      return null;
    }
    return unique;
  }

  //是否在库中的查询
  public static<T> List<T> queryByExist(Class<T> clazz,String exist){
    DaoSession daoSession = DaoManager.getInstance().getDaoSession();
    List<T> list = daoSession.queryBuilder(clazz).where(AssetDetailDao.Properties.Exist.eq(exist)).build().list();

    return list;
  }
  //指定类型的工具 是否在库中的查询
  public static<T> List<T> queryByStyleAndExist(Class<T> clazz,String assetName,String exist){
    DaoSession daoSession = DaoManager.getInstance().getDaoSession();
    List<T> list = daoSession.queryBuilder(clazz).where(AssetDetailDao.Properties.AssetName.eq(assetName),
            AssetDetailDao.Properties.Exist.eq(exist)).build().list();

    return list;
  }

  public static<T> void update(T bean){
    DaoSession daoSession = DaoManager.getInstance().getDaoSession();
    daoSession.update(bean);
  }
}
