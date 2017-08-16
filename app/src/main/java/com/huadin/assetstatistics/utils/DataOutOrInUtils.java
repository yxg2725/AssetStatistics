package com.huadin.assetstatistics.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.huadin.assetstatistics.bean.AssetDetail;
import com.huadin.assetstatistics.bean.DataInEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * 数据导出或导出
 */

public class DataOutOrInUtils {

  public static boolean dataOut(){

    //导出的文件名
    String dirFileName = getDirFileName();

    //表格的sheet名
    List<String> sheetNames = Arrays.asList(Contants.SHEETNAMES);

    //表头
    String[] tablehead = Contants.TABLEHEAD;
    List<String> tableHeads = Arrays.asList(tablehead);

    //表格的标题
    List<String[]> titles = new ArrayList<>();
    titles.add(Contants.TABLETITLE_DETAILE);
    //titles.add(Contants.TABLETITLE_TOTAL);

    //初始化表格
    ExcelUtils.initExcel(dirFileName, sheetNames, titles, tableHeads);

    //获取数据
    ArrayList<ArrayList<ArrayList<String>>> contentList = new ArrayList<>();

    //1.明细sheet的数据
    ArrayList<ArrayList<String>> detailList = new ArrayList<>();
    List<AssetDetail> list = DbUtils.queryAll(AssetDetail.class);//明细的数
    Collections.sort(list);

    int num = 0;//序号
    for (AssetDetail asset : list) {
      ArrayList<String> assetList = new ArrayList<>();
      //按顺序填写
     /* {"序号","工器具名称","编号","生成厂家","规格型号","使用单位/部门",使用部门/班组"，"保管人","购置日期","上次实验日期"
              ,"下次实验日期"};*/
      assetList.add((++num) + "");//"序号"
      assetList.add(asset.getAssetName());//工器具名称
      assetList.add(asset.getArchivesNumber());//编号
      assetList.add(asset.getManufacturer());//生成厂家
      assetList.add(asset.getDeviceId());//规格型号
      assetList.add(asset.getUsedCompany());//使用单位/部门
      assetList.add(asset.getUsedDepartment());//使用部门/班组
      assetList.add(asset.getCustodian());//保管人
      assetList.add(asset.getDateOfProduction());//购置日期
      assetList.add(asset.getCheckDate());//上次实验日期
      assetList.add(asset.getNextCheckDate());//下次实验日期

      detailList.add(assetList);
    }

    /*//2.总体sheet的数据
    ArrayList<ArrayList<String>> totalList = new ArrayList<>();

    for (int i = 0; i < Contants.assetsType.length; i++) {
      ArrayList<String> List3 = new ArrayList<>();

      List3.add(Contants.assetsType[i]);
      //总个数查询
      List<AssetDetail> list1 = DbUtils.queryByName(AssetDetail.class, Contants.assetsType[i]);
      List3.add(list1.size() + "");

      //库存个数查询
      List<AssetDetail> existList = DbUtils.queryByStyleAndExist(AssetDetail.class, Contants.assetsType[i], "入库");
      List3.add(existList.size() + "");

      //出库个数查询
      List<AssetDetail> outList = DbUtils.queryByStyleAndExist(AssetDetail.class, Contants.assetsType[i], "出库");
      List3.add(outList.size() + "");

      totalList.add(List3);
    }*/

    contentList.add(detailList);
   // contentList.add(totalList);

    boolean issuccess = ExcelUtils.writeObjListToExcel(contentList, dirFileName);
    return issuccess;
  }


  private static String getDirFileName() {
    //获取文件名
    File file = new File(Environment.getExternalStorageDirectory(), Contants.ROOTFILENAME);
    if (!file.exists()) {
      file.mkdir();
    }

    return file.getAbsolutePath() + File.separator + Contants.TABLENAME;
  }

  /**
   * 导入
   * @return
   */
  public static boolean dataIn(File file, Context context){

    try{
      List<DataInEntity> dataInEntities = ExcelUtils.read2DB(file, context);
      //保存到数据库
      for (int i = 0; i < dataInEntities.size(); i++) {//遍历所有sheet
        DataInEntity dataInEntity = dataInEntities.get(i);
        String name = dataInEntity.getName();//sheetName
        ArrayList<ArrayList<String>> list = dataInEntity.getList();

        for (int j = 0; j < list.size(); j++) {//遍历所有行
          ArrayList<String> row = list.get(j);
          AssetDetail assetDetail = new AssetDetail();
          for (int k = 0; k < row.size(); k++) {//遍历所有列

            assetDetail.setAssetName(row.get(1).trim());
            assetDetail.setArchivesNumber(row.get(2).trim());
            assetDetail.setManufacturer(row.get(3).trim());
            assetDetail.setDeviceId(row.get(4).trim());
            assetDetail.setUsedCompany(row.get(5).trim());
            assetDetail.setUsedDepartment(row.get(6).trim());
            assetDetail.setCustodian(row.get(7).trim());
            assetDetail.setDatePurchase(row.get(8).trim());
            assetDetail.setCheckDate(row.get(9).trim());
            assetDetail.setNextCheckDate(row.get(10).trim());
            assetDetail.setExist("入库");
            assetDetail.setIsGood("合格");

            //查询是否存在
            AssetDetail assetDetail1 = DbUtils.queryByArchivesNumber(AssetDetail.class, row.get(2).trim());
            if(assetDetail1 != null){
              DbUtils.delete(assetDetail1);
            }
            DbUtils.insert(assetDetail);
          }

        }
      }
    }catch(Exception ex){
      Log.i(TAG, "dataIn: " + ex.toString());
      return false;
    }

    return true;
  }
}
