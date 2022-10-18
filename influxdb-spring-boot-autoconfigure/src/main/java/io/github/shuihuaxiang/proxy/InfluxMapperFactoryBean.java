package io.github.shuihuaxiang.proxy;

import io.github.shuihuaxiang.InfluxProperties;
import io.github.shuihuaxiang.core.InfluxdbExecutes;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Proxy;

/**
 * 手动生成bean
 * @Author: kimli
 * @Date: 2022/10/18 1:42
 * @Description: 生成bean，主要是要做代理增强
 */
public class InfluxMapperFactoryBean implements FactoryBean {
    /**
     * 初始化所需的接口实例
     */
    private Class interfaceClass;

    /**
     * 需要在这里注入influxdbExecutes，动态代理里面注入不了
     */
    @Autowired
    private InfluxdbExecutes influxdbExecutes;
    @Autowired
    InfluxProperties influxProperties;

    private boolean isTimezone;

    private String database;

    public InfluxMapperFactoryBean(Class interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    @Override
    public Object getObject() throws Exception {
        this.isTimezone = influxProperties.isTimezone();
        this.database = influxProperties.getDatabase();
        Object proxyInstance = Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new InfluxMapperProxyFactory(influxdbExecutes,this.isTimezone,this.database));

        return proxyInstance;
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }
}
