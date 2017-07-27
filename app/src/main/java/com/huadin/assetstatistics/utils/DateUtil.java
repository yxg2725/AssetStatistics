package com.huadin.assetstatistics.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by lxj on 2016/8/30.
 */
public class DateUtil {
    public static String timestamp2ymd(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(timestamp);
    }

    public static String toymdhms(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(timestamp);
    }

    public static String tohms(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(timestamp);
    }

    /**
     * 将日期格式的字符串转换为长整型
     *
     * @param date
     * @param format
     * @return
     */
    public static long convert2long(String date, String format) {

        SimpleDateFormat sf = new SimpleDateFormat(format);
        try {
            long time = sf.parse(date).getTime();
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
