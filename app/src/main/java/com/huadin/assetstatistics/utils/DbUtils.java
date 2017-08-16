package com.huadin.assetstatistics.utils;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.huadin.assetstatistics.bean.AssetDetail;
import com.huadin.assetstatistics.bean.dao.DaoManager;
import com.huadin.assetstatistics.gen.AssetDetailDao;
import com.huadin.assetstatistics.gen.DaoSession;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

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
    List<T> list = daoSession.queryBuilder(clazz).where(AssetDetailDao.Properties.AssetName.like("%"+name+"%")).build().list();
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

  /**
   * 根据档案编号查询
   */
  public static<T> T queryByArchivesNumber(Class<T> clazz,String code){
    DaoSession daoSession = DaoManager.getInstance().getDaoSession();
    T unique = daoSession.queryBuilder(clazz).where(AssetDetailDao.Properties.ArchivesNumber.eq(code)).build().unique();
    if(unique == null){
      return null;
    }
    return unique;
  }

  public static ArrayList<String> queryByCol(String colName){
    ArrayList<String> list = new ArrayList<>();
    Database database = DaoManager.getInstance().getDaoMaster().getDatabase();
    Cursor cursor = database.rawQuery("SELECT "+colName+" FROM ASSET_DETAIL", null);
    while (cursor.moveToNext()) {
      String string = cursor.getString(0);

      if(!TextUtils.isEmpty(string) && !list.contains(string)){
        list.add(string);
        Log.i("DbUtils", "queryCol: " + string);
      }
    }
    cursor.close();
    return list;
  }



  //是否在库中的查询
  public static<T> List<T> queryByExist(Class<T> clazz,String exist){
    DaoSession daoSession = DaoManager.getInstance().getDaoSession();
    List<T> list = daoSession.queryBuilder(clazz).where(AssetDetailDao.Properties.Exist.eq(exist)).build().list();

    return list;
  }

  //出库或入库 且 自用或外用的
  public static<T> List<T> queryByExistAndUser(Class<T> clazz,String exist,boolean selfUse){
    DaoSession daoSession = DaoManager.getInstance().getDaoSession();
    QueryBuilder<T> where = daoSession.queryBuilder(clazz)
            .where(AssetDetailDao.Properties.Exist.eq(exist));
    List<T> list;
    if(selfUse){
       list = where.where(AssetDetailDao.Properties.UsedCompany.eq("密云供电公司")).build().list();
    }else{
      list = where.where(AssetDetailDao.Properties.UsedCompany.notEq("密云供电公司")).build().list();
    }

    return list;
  }
  //指定类型的工具 是否在库中的查询
  public static<T> List<T> queryByStyleAndExist(Class<T> clazz,String assetName,String exist){
    DaoSession daoSession = DaoManager.getInstance().getDaoSession();
    List<T> list = daoSession.queryBuilder(clazz).where(AssetDetailDao.Properties.AssetName.like("%"+assetName+"%"),
            AssetDetailDao.Properties.Exist.eq(exist)).build().list();

    return list;
  }

  //指定类型的工具 不合格个数查询
  public static<T> List<T> queryByStyleAndIsGood(Class<T> clazz,String assetName){
    DaoSession daoSession = DaoManager.getInstance().getDaoSession();
    List<T> list = daoSession.queryBuilder(clazz).where(AssetDetailDao.Properties.AssetName.like("%"+assetName+"%"),
            AssetDetailDao.Properties.IsGood.eq("不合格")).build().list();

    return list;
  }

  public static<T> void update(T bean){
    DaoSession daoSession = DaoManager.getInstance().getDaoSession();
    daoSession.update(bean);
  }
}
