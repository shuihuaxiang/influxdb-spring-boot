package io.github.shuihuaxiang.utils;

import java.lang.reflect.Parameter;

/**
 * @Author: kimli
 * @Date: 2022/10/18 17:38
 * @Description: 注解方式根据参数组装sql的处理工具类
 */
public class AnnotationParameterUtil {
    /**
     * 根据占位拼接sql：支持#{ } 和${ } 两种类型
     * @param parameters 参数名
     * @param args 参数实际值
     * @param sql
     * @return 新的sql字符串
     */
    public static String parameterHandle(Parameter[] parameters, Object[] args, String sql){
        for(int i=0;i<parameters.length;i++){
            Class<?> parameterType=parameters[i].getType();
            String parameterName=parameters[i].getName();

            if(parameterType==String.class){
                sql=sql.replaceAll("\\#\\{"+parameterName+"\\}","'"+args[i].toString()+"'");
                sql=sql.replaceAll("\\$\\{"+parameterName+"\\}","'"+args[i].toString()+"'");
            }else {
                sql=sql.replaceAll("\\#\\{"+parameterName+"\\}",args[i].toString());
                sql=sql.replaceAll("\\$\\{"+parameterName+"\\}",args[i].toString());
            }
        }
        return sql;
    }
}
