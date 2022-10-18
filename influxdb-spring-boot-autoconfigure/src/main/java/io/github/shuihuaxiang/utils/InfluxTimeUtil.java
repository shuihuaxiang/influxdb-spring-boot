package io.github.shuihuaxiang.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: kimli
 * @Date: 2022/10/18 23:17
 * @Description: influxDB time时间转换
 */

public class InfluxTimeUtil {

    /**
     * 格式转换
     *
     * @param oldTime          要转换的时间,例如：2020-09-19T09:58:34.926Z
     * @param formatterPattern 时间格式，例如：yyyy-MM-dd hh:mm:ss
     * @return String
     */
    public static String conversionTime(String oldTime, String formatterPattern) {
        LocalDateTime dateTime = LocalDateTime.parse(oldTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return dateTime.format(DateTimeFormatter.ofPattern(formatterPattern));
    }

}
