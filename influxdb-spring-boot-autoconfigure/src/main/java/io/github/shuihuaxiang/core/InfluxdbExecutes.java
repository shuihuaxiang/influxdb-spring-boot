package io.github.shuihuaxiang.core;

import io.github.shuihuaxiang.exception.BusinessException;
import io.github.shuihuaxiang.exception.ExceptionErrorCode;
import org.influxdb.InfluxDB;
import org.influxdb.annotation.Measurement;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBMapper;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: kimli
 * @Date: 2022/10/17 23:38
 * @Description:
 */
public class InfluxdbExecutes {

    private InfluxDB influxDB;

    private InfluxDBMapper influxDBMapper;

    public InfluxdbExecutes(InfluxDB influxDB, InfluxDBMapper influxDBMapper) {
        this.influxDB = influxDB;
        this.influxDBMapper = influxDBMapper;
    }

    public <T> List<T> select(String sql, Class<T> clszz) {
        return influxDBMapper.query(new Query(sql), clszz);
    }
    public int insert(Object[] args, boolean isTimezone, String database) {
        if (args.length!=1) {
            throw new BusinessException(ExceptionErrorCode.PARAMETER_ERROR);
        }
        Object arg = args[0];
        if(arg instanceof List){
            return insertList((ArrayList) arg, isTimezone, database);
        }else {
            influxDBMapper.save(arg);
            return 1;
        }
    }
    @NotNull
    public int insertList(List<Object> list, boolean isTimezone, String database) {
        Class<?> domainClass = list.get(0).getClass();
        List<Point> pointList = new ArrayList<>();
        for (Object o : list) {
            Point point;
            if (isTimezone) {
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
        influxDB.setDatabase(database);
        influxDB.setRetentionPolicy(retentionPolicy);
        influxDB.write(batchPoints);
        return pointList.size();
    }

    public List<QueryResult.Result> delete(String sql, String database) {

        List<QueryResult.Result> list = influxDB.query(new Query(sql, database)).getResults();

        return list;
    }

    public <T> List<T> execute(String sql, String database) {

        List list = influxDB.query(new Query(sql, database)).getResults();

        return list;
    }
}
