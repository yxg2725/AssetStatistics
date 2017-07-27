package com.huadin.assetstatistics.utils;

import android.content.Context;
import android.util.Log;


import com.huadin.assetstatistics.bean.AssetDetail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import static android.R.attr.format;
import static jxl.Workbook.getWorkbook;

/**
 * 转换成Excel
 *
 * @author Jack Zhang
 */
public class ExcelUtils {

    private static final String TAG = "ExcelUtils";
    public static WritableFont arial14font = null;
    public static WritableCellFormat arial14format = null;
    public static WritableFont arial10font = null;
    public static WritableCellFormat arial10format = null;
    public static WritableFont arial12font = null;
    public static WritableCellFormat arial12format = null;
    public final static String UTF8_ENCODING = "UTF-8";
    public final static String GBK_ENCODING = "GBK";



    public static void format() {
        try {
            //表头
            arial14font = new WritableFont(WritableFont.createFont("宋体"), 14,
                    WritableFont.BOLD);
            //	arial14font.setColour(jxl.format.Colour.LIGHT_BLUE);
            arial14format = new WritableCellFormat(arial14font);
            arial14format.setAlignment(jxl.format.Alignment.CENTRE);
            arial14format.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.THIN);
            //arial14format.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);

            //标题
            arial10font = new WritableFont(WritableFont.createFont("宋体"), 11,
                    WritableFont.BOLD);
            arial10format = new WritableCellFormat(arial10font);
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            arial10format.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.THIN);
            arial10format.setBackground(jxl.format.Colour.GRAY_25);

            //内容
            arial12font = new WritableFont(WritableFont.createFont("宋体"), 11);
            arial12format = new WritableCellFormat(arial12font);
            arial12format.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.THIN);
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    public static void initExcel(String fileName, //excel文件名
                                 List<String> sheetNames,//sheet名
                                 List<String[]> tableTitles ,//标题
                                 List<String> tableHeads//表头
                                 ){

        format();//格式化表格

        WritableWorkbook workbook = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            workbook = Workbook.createWorkbook(file);//根据文件名创建Excel表格对象

            for (int i = 0; i < sheetNames.size(); i++) {
                WritableSheet sheet = workbook.createSheet(sheetNames.get(i), i);//根据sheet名创建sheet
                String[] title = tableTitles.get(i);//获取各个sheet标题
                for (int col = 0; col < title.length; col++) {
                    sheet.addCell(new Label(col, 1, title[col], arial10format));//给各个sheet添加标题
                }
                sheet.mergeCells(0, 0, title.length - 1, 0);//按标题的长度合并单元格
                sheet.getSettings().setDefaultColumnWidth(10);//设置单元格宽度
                sheet.addCell(new Label(0,0,tableHeads.get(i),arial14format));//添加表头
            }

            workbook.write();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "initExcel: " + e.toString());

        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static boolean writeObjListToExcel(
            ArrayList<ArrayList<ArrayList<String>>> cursorDatas,//数据
            String fileName, Context c) {
        if (cursorDatas != null && cursorDatas.size() > 0) {
            WritableWorkbook writebook = null;
            InputStream in = null;
            try {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);
                in = new FileInputStream(new File(fileName));
                Workbook workbook = getWorkbook(in);
                writebook = Workbook.createWorkbook(new File(fileName),
                        workbook);

                for (int j = 0; j < cursorDatas.size(); j++) {
                    for (int i = 0; i < cursorDatas.get(j).size(); i++) {
                        ArrayList<String> list = (ArrayList<String>) cursorDatas.get(j).get(i);
                        for (int k = 0; k < list.size(); k++) {
                            writebook.getSheet(j).addCell(new Label(k, i + 2, list.get(k), arial12format));
                        }
                    }
                }
                int sum = 0;
               /* // 假如Sheet为空，删除该Sheet
                for (int i = 0; i < cursorDatas.size(); i++) {
                    if (cursorDatas.get(i).size() == 0) {
                        writebook.removeSheet(i - sum);
                        sum++;
                    }
                }*/
                writebook.write();
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "write: " + e.toString());
                return false;
            } finally {
                if (writebook != null) {
                    try {
                        writebook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return true;
    }

   /* public static List<DataInEntity> read2DB(File f, Context con) {
        List<DataInEntity> mList = new ArrayList<>();
        try {
            Workbook course = Workbook.getWorkbook(f);
            for(int a = 0; a < course.getNumberOfSheets(); a++) {

                ArrayList<ArrayList<String>> sheetList = new ArrayList<>();
                // 循环多个Sheet
                Sheet sheet = course.getSheet(a);
                String name = course.getSheet(a).getName();
                Cell cell = null;
                for (int i = 2; i < sheet.getRows(); i++) {
                    // 第三行开始
                    ArrayList<String> rowList = new ArrayList<>();
                    for(int j = 0; j < sheet.getColumns(); j++) {
                        // 第一列开始
                        cell = sheet.getCell(j, i);

                        rowList.add(cell.getContents());
                    }
                    sheetList.add(rowList);
                }
                DataInEntity data = new DataInEntity(name, sheetList);
                mList.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mList;
    }*/


}
