package io.github.shuihuaxiang.templates;

import io.github.shuihuaxiang.sqlbuilders.DeleteBuilder;
import io.github.shuihuaxiang.sqlbuilders.QueryAllBuilder;

import java.util.List;

/**
 * @Author: kimli
 * @Date: 2022/10/14 0:07
 * @Description: 数据库执行类顶层接口
 */
public interface InfluxTemplate {
    /**
     * 查询方法
     * @param builder
     * @param <T>
     * @return List<T>
     */
     <T> List<T> select(QueryAllBuilder builder);

    /**
     * 插入方法
     * @param list
     */
     void insert(List<Object> list);

    /**
     * 删除
     * @param builder
     */
     void delete(DeleteBuilder builder);

    /**
     * 封装自定义sql的查询方法
     * @param sql
     * @param <T>
     * @return
     */
     <T> List<T> execute(String sql);

}
