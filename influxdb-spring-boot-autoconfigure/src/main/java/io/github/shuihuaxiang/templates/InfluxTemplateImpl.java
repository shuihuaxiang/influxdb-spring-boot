package io.github.shuihuaxiang.templates;

import io.github.shuihuaxiang.InfluxProperties;
import io.github.shuihuaxiang.core.InfluxdbExecutes;
import io.github.shuihuaxiang.exception.BusinessException;
import io.github.shuihuaxiang.exception.ExceptionErrorCode;
import io.github.shuihuaxiang.sqlbuilders.DeleteBuilder;
import io.github.shuihuaxiang.sqlbuilders.QueryAllBuilder;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.annotation.Measurement;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.BeanFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: kimli
 * @Date: 2022/10/14 0:32
 * @Description:
 */
@Slf4j
public class InfluxTemplateImpl implements InfluxTemplate {


    private InfluxdbExecutes influxdbExecutes;

    private boolean isTimezone;

    private String database;

    public InfluxTemplateImpl(InfluxProperties influxProperties, InfluxdbExecutes influxdbExecutes) {

        this.influxdbExecutes = influxdbExecutes;
        this.isTimezone = influxProperties.isTimezone();
        this.database = influxProperties.getDatabase();

    }

    @Override
    public <T> List<T> select(QueryAllBuilder builder) {
        Class domainClass = builder.getDomainClass();
        String sql = builder.getResultSql();
        log.debug("查询数据sql：{}", sql);
        if (sql == null || "".equals(sql)) {
            throw new BusinessException(ExceptionErrorCode.EMPTY_PARAMETER);
        }
        List<T> list = influxdbExecutes.select(sql, domainClass);
        log.debug("查询数据{}条结果：{}", list.size(), list.toString());
        return list;
    }

    @Override
    public void insert(List<Object> list) {
        if (list.isEmpty()) {
            throw new BusinessException(ExceptionErrorCode.EMPTY_PARAMETER);
        }
        int insertCount = influxdbExecutes.insertList(list, isTimezone, database);
        log.debug("写入数据成功:{}条数据", insertCount);
    }



    @Override
    public void delete(DeleteBuilder builder) {

        String sql = builder.getResultSql();

        log.debug("删除数据sql：{}", sql);
        if (sql == null || "".equals(sql)) {
            throw new BusinessException(ExceptionErrorCode.EMPTY_PARAMETER);
        }

        List<QueryResult.Result> list = influxdbExecutes.delete(sql,database);

        log.debug("删除数据返回结果：{}", list.toString());
    }

    @Override
    public <T> List<T> execute(String sql) {
        List<T> list = influxdbExecutes.execute(sql, database);
        log.debug("查询结果：{}", list.toString());
        return list;
    }

//    @Override
//    public void getPackpages() {
//        List<String> list = AutoConfigurationPackages.get(this.beanFactory);
//        String[] strings = StringUtils.toStringArray(list);
//        for (String s : list) {
//            log.debug("得到包名", s);
//        }
//    }
//
//
//    @Override
//    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
//        this.beanFactory=beanFactory;
//    }
}
