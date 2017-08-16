package com.huadin.assetstatistics.utils;

import java.util.ArrayList;

/**
 * Created by admin on 2017/7/19.
 */

public interface Contants {
  String ROOTFILENAME = "安全工器具";

  String[] assetsType = {"绝缘靴","绝缘手套","安全带","绝缘拉杆","地线杆","验电器","脚扣","个人安保线"
  ,"安全帽","绝缘梯"};

  String[] TABLETITLE_DETAILE = {"序号","工器具名称","编号","生成厂家","规格型号","使用单位/部门","使用部门/班组","保管人","购置日期","上次实验日期"
  ,"下次实验日期"};

  String[] TABLETITLE_TOTAL = {"工具名称","总量","库存统计","出库统计"};

  String[] SHEETNAMES = {"工器具"};

  String[] TABLEHEAD = {"工器具"};

  String TABLENAME = "工器具.xls";

  String[] GOODORBAD  = {"合格","不合格"};

  String PATCH_SCAN = "patch_scan";

}
