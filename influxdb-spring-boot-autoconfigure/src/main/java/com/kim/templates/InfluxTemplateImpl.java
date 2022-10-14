package com.kim.templates;

import com.kim.InfluxProperties;
import com.kim.exception.BusinessException;
import com.kim.exception.ExceptionErrorCode;
import com.kim.sqlbuilders.DeleteBuilder;
import com.kim.sqlbuilders.QueryAllBuilder;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.annotation.Measurement;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.impl.InfluxDBMapper;

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
    private InfluxDB influxDB;

    private InfluxDBMapper influxDBMapper;

    private InfluxProperties influxProperties;

    public InfluxTemplateImpl(InfluxDB influxDB, InfluxDBMapper influxDBMapper, InfluxProperties influxProperties) {
        this.influxDB = influxDB;
        this.influxDBMapper = influxDBMapper;
        this.influxProperties = influxProperties;
    }

    @Override
    public <T> List<T> select(QueryAllBuilder builder) {
        Class domainClass = builder.getDomainClass();
        String sql = builder.getResultSql();
        log.debug("查询数据sql：{}", sql);
        if (sql == null || "".equals(sql)) {
            throw new BusinessException(ExceptionErrorCode.EMPTY_PARAMETER);
        }
        List<T> list = influxDBMapper.query(new Query(sql), domainClass);
        log.debug("查询数据{}条结果：{}", list.size(), list.toString());
        return list;
    }

    @Override
    public void insert(List<Object> list) {
        if (list.isEmpty()) {
            throw new BusinessException(ExceptionErrorCode.EMPTY_PARAMETER);
        }
        Class<?> domainClass = list.get(0).getClass();
        List<Point> pointList = new ArrayList<>();
        for (Object o : list) {
            Point point;
            if (influxProperties.isTimezone()) {
                point = Point.measurementByPOJO(domainClass)
                        .addFieldsFromPOJO(o)
                        .time(LocalDateTime.now().plusHours(8).toInstant(ZoneOffset.of("+8")).toEpochMilli(), TimeUnit.MILLISECONDS)
                        .build();
            } else {
                point = Point.measurementByPOJO(domainClass)
                        .addFieldsFromPOJO(o)
                        .build();
            }
            pointList.add(point);
        }
        BatchPoints batchPoints = BatchPoints.builder().points(pointList).build();
        Measurement measurement = domainClass.getAnnotation(Measurement.class);
        String retentionPolicy = measurement.retentionPolicy();
        influxDB.setDatabase(influxProperties.getDatabase());
        influxDB.setRetentionPolicy(retentionPolicy);
        influxDB.write(batchPoints);
        log.debug("写入数据{}条成功:{}", pointList.size(), pointList);
    }

    @Override
    public void delete(DeleteBuilder builder) {

        String sql = builder.getResultSql();

        log.debug("删除数据sql：{}", sql);
        if (sql == null || "".equals(sql)) {
            throw new BusinessException(ExceptionErrorCode.EMPTY_PARAMETER);
        }
        List list = influxDB.query(new Query(sql, influxProperties.getDatabase())).getResults();
        log.debug("删除数据返回结果：{}", list.toString());
    }

    @Override
    public <T> List<T> execute(String sql) {

        List list = influxDB.query(new Query(sql, influxProperties.getDatabase())).getResults();
        log.debug("查询结果：{}", list.toString());
        return list;
    }


}
