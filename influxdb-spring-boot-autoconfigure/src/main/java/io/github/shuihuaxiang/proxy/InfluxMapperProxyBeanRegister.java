package io.github.shuihuaxiang.proxy;

import io.github.shuihuaxiang.annotations.InfluxMapper;
import io.github.shuihuaxiang.utils.BeanRegisterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * influxMapper动态代理注册类
 * @Author: kimli
 * @Date: 2022/10/18 0:10
 * @Description: 扫描@Influx注解，动态代理增强，注册bean到spring容器
 */
@Slf4j
public class InfluxMapperProxyBeanRegister {

    private ConfigurableApplicationContext applicationContext;

    public InfluxMapperProxyBeanRegister(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void initMapper(){
        Set<Class<?>> classes=new HashSet<>();
        try {
            ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
            List<String> packagesNames = AutoConfigurationPackages.get(beanFactory);
            if(packagesNames.size()<=0){
                return;
            }
            for (String packagesName : packagesNames) {
                //组装类路径
                String locationPattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                        ClassUtils.convertClassNameToResourcePath(packagesName) + "/**/*.class";
                Resource[] resources = applicationContext.getResources(locationPattern);
                if(resources.length<=0){
                    continue;
                }
                MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(applicationContext);
                for (Resource resource : resources) {
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    String className = metadataReader.getClassMetadata().getClassName();
                    Class<?> clazz = Class.forName(className);
                    if (!clazz.isInterface()) {
                        continue;
                    }
                    Annotation[] annotations = clazz.getAnnotations();
                    if(annotations.length<=0){
                        continue;
                    }
                    for (Annotation annotation : annotations) {

                        if(annotation instanceof InfluxMapper){
                            classes.add(clazz);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(classes.size()<=0){
            return;
        }
        //jdk动态代理增强，然后注册成bean
        for (Class<?> aClass : classes) {
            log.debug("注册bean：{}  准备。。。。。。",aClass.getName());
            String[] names = applicationContext.getBeanFactory().getBeanDefinitionNames();
            Arrays.stream(names).filter(a->a.contains("influx")).forEach(a-> System.out.println("得到bean名字："+a));
            BeanRegisterUtil.registerBean(applicationContext, aClass.getSimpleName(), InfluxMapperFactoryBean.class,aClass);
        }
    }

}
