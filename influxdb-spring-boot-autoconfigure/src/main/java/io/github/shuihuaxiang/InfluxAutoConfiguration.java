package io.github.shuihuaxiang;

import io.github.shuihuaxiang.core.InfluxdbExecutes;
import io.github.shuihuaxiang.proxy.InfluxMapperProxyBeanRegister;
import io.github.shuihuaxiang.templates.InfluxTemplate;
import io.github.shuihuaxiang.templates.InfluxTemplateImpl;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.impl.InfluxDBMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: kimli
 * @Date: 2022/10/13 21:53
 * @Description:
 */
@Slf4j
@EnableConfigurationProperties(InfluxProperties.class)
@Configuration
public class InfluxAutoConfiguration {

    @Bean(name = "influxDB")
    public InfluxDB influxDB(InfluxProperties influxProperties){
        InfluxDB influxDB = InfluxDBFactory.connect(influxProperties.getUrl(), influxProperties.getUser(), influxProperties.getPassword());
        influxDB.setDatabase(influxProperties.getDatabase());
        return influxDB;
    }
    @Bean(name = "influxDBMapper")
    public InfluxDBMapper influxDBMapper(InfluxDB influxDB){
        return new InfluxDBMapper(influxDB);
    }

    @Bean(name = "influxdbExecutes")
    public InfluxdbExecutes influxdbExecutes(InfluxDB influxDB, InfluxDBMapper influxDBMapper){

        return new InfluxdbExecutes(influxDB,influxDBMapper);
    }

    @Bean(name = "influxTemplate")
    public InfluxTemplate InfluxTemplate( InfluxdbExecutes influxdbExecutes,InfluxProperties influxProperties){

        return new InfluxTemplateImpl(influxProperties,influxdbExecutes);
    }
    @Bean(name = "proxyMapperBeanRegister")
    public InfluxMapperProxyBeanRegister proxyMapperBeanRegister(ConfigurableApplicationContext applicationContext){

        return new InfluxMapperProxyBeanRegister(applicationContext);
    }

}
