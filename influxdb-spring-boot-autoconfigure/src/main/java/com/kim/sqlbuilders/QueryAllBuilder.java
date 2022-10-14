package com.kim.sqlbuilders;

import com.kim.exception.BusinessException;
import com.kim.exception.ExceptionErrorCode;
import org.influxdb.annotation.Measurement;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

/**
 * @Author: kimli
 * @Date: 2022/10/14 11:31
 * @Description: 封装查询全部sql
 */
public class QueryAllBuilder {

    private Class domainClass;

    private String resultSql;

    private String sortSql = null;

    private StringBuilder whereSql = null;

    private StringBuilder groupBySql = null;

    private String pageSql = null;


    public QueryAllBuilder(Class domainClass) {
        this.domainClass = domainClass;
    }
    public Class getDomainClass() {
        return domainClass;
    }

    public String getResultSql() {
        return resultSql;
    }
    /**
     * 开始时间：大于等于
     *
     * @param startTime
     * @return
     */
    public QueryAllBuilder start(String startTime) {
        if (startTime != null && !startTime.isEmpty()) {
            whereSql.append(" and time>='" + startTime + "' ");
        }
        return this;
    }

    /**
     * 结束时间：小于等于
     *
     * @param endTime
     * @return
     */
    public QueryAllBuilder end(String endTime) {
        if (endTime != null && !endTime.isEmpty()) {
            whereSql.append(" and time<='" + endTime + "' ");
        }
        return this;
    }

    /**
     * 查询条件Map<String,String> map
     *
     * @param map
     * @return
     */
    public QueryAllBuilder where(Map<String, String> map) {
        if (!map.isEmpty() && map.size() > 0) {
            whereSql=new StringBuilder(" where 1=1 ");
            for (String key : map.keySet()) {
                whereSql.append(" and " + key + "= '" + map.get(key) + "' ");
            }
        }
        return this;
    }

    /**
     * 分页
     *
     * @param page
     * @param pageSize
     * @return
     */
    public QueryAllBuilder page(Long page, Long pageSize) {
        pageSql = " limit " + pageSize + " offset " + (page - 1) * pageSize;
        return this;
    }
    /**
     * 分组
     *
     * @param group
     * @return
     */
    public QueryAllBuilder groupBy(String...group) {
        if(group.length>0){
             groupBySql=new StringBuilder(" group by ");
            for (int i = 0; i < group.length; i++) {
                if(i==group.length-1){
                    groupBySql.append(group[i]+" ");
                    continue;
                }
                groupBySql.append(group[i]+",");
            }
        }
        return this;
    }
    /**
     * 排序
     *
     * @param orders
     * @return
     */
    public QueryAllBuilder orderBy(SortOrders orders) {

        sortSql = " order by " + orders;
        return this;
    }

    /**
     * 组装查询sql
     */
    public void build() {
        Measurement measurement = (Measurement) domainClass.getAnnotation(Measurement.class);
        String measurementName= measurement.name();
        StringBuilder sb = new StringBuilder("select * from " + measurementName);

        if (whereSql != null) {
            sb.append(whereSql);
        }
        if (groupBySql != null) {
            sb.append(groupBySql);
        }
        if (sortSql != null) {
            sb.append(sortSql);
        }
        if (pageSql != null) {
            sb.append(pageSql);
        }
        resultSql = sb.toString();
    }
}
