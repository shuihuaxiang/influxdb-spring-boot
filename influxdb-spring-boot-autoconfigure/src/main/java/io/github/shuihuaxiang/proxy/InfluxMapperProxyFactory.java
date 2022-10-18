package io.github.shuihuaxiang.proxy;

import io.github.shuihuaxiang.annotations.Delete;
import io.github.shuihuaxiang.annotations.Insert;
import io.github.shuihuaxiang.annotations.Select;
import io.github.shuihuaxiang.core.InfluxdbExecutes;
import io.github.shuihuaxiang.utils.AnnotationParameterUtil;
import io.github.shuihuaxiang.utils.AnnotationResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * 代理工厂
 * @Author: kimli
 * @Date: 2022/10/18 2:08
 * @Description: 实例jdk动态代理，做方法增强。根据注解内容，操作influxDB得到返回内容
 */
@Slf4j
public class InfluxMapperProxyFactory implements InvocationHandler {
    private InfluxdbExecutes influxdbExecutes;

    private boolean isTimezone;

    private String database;

    public InfluxMapperProxyFactory(InfluxdbExecutes influxdbExecutes,boolean isTimezone,String database) {
        this.influxdbExecutes = influxdbExecutes;
        this.isTimezone=isTimezone;
        this.database=database;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Annotation[] annotations = method.getAnnotations();
        if (annotations.length<=0){
            return null;
        }
        for (Annotation annotation : annotations) {
            //插入
            if (annotation instanceof Insert) {
                int result = influxdbExecutes.insert(args, isTimezone, database);
                Class<?> returnType = method.getReturnType();
                if("void".equals(returnType.toString())){
                    log.debug("Insert成功:{}条",result);
                }else {
                    log.debug("Insert成功:{}条",result);
                    return result;
                }
            }
            //删除
            if (annotation instanceof Delete) {
                String sql = ((Delete) annotation).value();
                Parameter[] parameters = method.getParameters();
                sql = AnnotationParameterUtil.parameterHandle(parameters, args, sql);
                log.debug("influxDB执行sql：{}",sql);
                List<QueryResult.Result> resultList = influxdbExecutes.delete(sql, database);
                log.debug("删除数据返回结果：{}", resultList.toString());
            }
            //查询
            if (annotation instanceof Select) {
                String sql = ((Select) annotation).value();
                Class aClass = ((Select) annotation).resultType();
                Parameter[] parameters = method.getParameters();
                sql = AnnotationParameterUtil.parameterHandle(parameters, args, sql);
                log.debug("influxDB执行sql：{}",sql);
                List list = influxdbExecutes.select(sql, aClass);
                log.debug("influxDB返回结果条数：{}",list.size());
                /**
                 * 一般返回类型为list<Object> 和Object
                 */
                Class<?> returnType = method.getReturnType();

                return AnnotationResultUtil.resultSetHandle(list,returnType);
            }
        }
        return null;
    }
}
