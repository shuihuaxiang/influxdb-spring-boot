package io.github.shuihuaxiang.sqlbuilders;

import java.util.Map;

/**
 * @Author: kimli
 * @Date: 2022/10/15 1:44
 * @Description: 封装删除sql
 */
public class DeleteBuilder {

    private String measurementName;

    private String resultSql;

    private StringBuilder whereSql = null;

    public DeleteBuilder(String measurementName) {
        this.measurementName = measurementName;
    }


    public String getResultSql() {
        return resultSql;
    }

    /**
     * 删除条件Map<String,String> map
     *
     * @param map
     * @return
     */
    public DeleteBuilder where(Map<String, String> map) {
        if (!map.isEmpty() && map.size() > 0) {
            whereSql=new StringBuilder(" where 1=1 ");
            for (String key : map.keySet()) {
                whereSql.append(" and " + key + "= '" + map.get(key) + "' ");
            }
        }
        return this;
    }
    /**
     * 组装删除sql
     */
    public void build() {

        StringBuilder sb = new StringBuilder("delete from " + measurementName);

        if (whereSql != null) {
            sb.append(whereSql);
        }
        resultSql = sb.toString();
    }

}
