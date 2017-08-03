package com.huadin.assetstatistics.utils;

import java.util.ArrayList;

/**
 * Created by admin on 2017/7/19.
 */

public interface Contants {
  String[] assetsType = {"绝缘靴","绝缘手套","安全带","绝缘拉杆","地线杆","验电器","脚扣","个人安保线"
  ,"安全帽","绝缘梯"};

  String[] TABLETITLE_DETAILE = {"工具名称","型号","使用单位","生产厂家","出厂日期","检测编号","档案编号","检验日期"
  ,"下次检验日期","检验员","是否出库"};

  String[] TABLETITLE_TOTAL = {"工具名称","总量","库存统计","出库统计"};

  String[] SHEETNAMES = {"明细","总体"};

  String[] TABLEHEAD = {"安全工器具详细信息统计","安全工器具出入库情况统计"};

  String TABLENAME = "安全工器具使用情况统计表.xls";

  String[] GOODORBAD  = {"合格","不合格"};

  String PATCH_SCAN = "patch_scan";

}
