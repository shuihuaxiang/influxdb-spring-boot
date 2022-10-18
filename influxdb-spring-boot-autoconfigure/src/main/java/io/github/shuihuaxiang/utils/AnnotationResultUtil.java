package io.github.shuihuaxiang.utils;

import java.util.List;

/**
 * @Author: kimli
 * @Date: 2022/10/18 17:39
 * @Description: 注解方式返回结果集的处理工具类
 */
public class AnnotationResultUtil {
    /**
     * 处理结构封装：
     * 如果要求的返回类型returnType不是集合，就取influxDB返回集合的第一条
     * 如果要求的返回类型returnType是集合，直接返回集合
     * @param list
     * @param returnType
     * @return  Object
     */
    public static Object resultSetHandle(List<Object> list,Class returnType){
        if(returnType!=List.class){
            if(list.size()>0){
                return list.get(0);
            }
        }
        return list;
    }
}
