package com.kim;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: kimli
 * @Date: 2022/10/13 23:54
 * @Description:
 */
@ConfigurationProperties(prefix = "influxdb")
@Configuration
public class InfluxProperties {
    /**
     * influxdb连接地址
     */
    private String url;
    /**
     * 用户名
     */
    private String user;
    /**
     * 密码
     */
    private String password;
    /**
     * 数据库名称
     */
    private String database;
    /**
     * insert方法是否使用北京时区，默认为true
     */
    private boolean timezone =true ;

    public InfluxProperties() {
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public boolean isTimezone() {
        return timezone;
    }

    public void setTimezone(boolean timezone) {
        this.timezone = timezone;
    }
}
