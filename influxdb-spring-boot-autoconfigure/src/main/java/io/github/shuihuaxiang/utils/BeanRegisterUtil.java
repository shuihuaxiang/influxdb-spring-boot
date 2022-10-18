package io.github.shuihuaxiang.utils;

import io.github.shuihuaxiang.exception.BusinessException;
import io.github.shuihuaxiang.exception.ExceptionErrorCode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @Author: kimli
 * @Date: 2022/10/18 2:33
 * @Description: 注册bean
 */
public class BeanRegisterUtil {
    /**
     * 注册bean实例
     * @param applicationContext 上下文
     * @param name  bean名称
     * @param clazz beanFactory生成的bean名称
     * @param args  beanFactory 构造函数传的参数
     */

    public static void registerBean(ConfigurableApplicationContext applicationContext, String name, Class clazz,Object...args) {
        if(applicationContext.containsBean(name)) {
            Object bean = applicationContext.getBean(name);
            if (!bean.getClass().isAssignableFrom(clazz)) {
                throw new BusinessException(ExceptionErrorCode.REPEAT_PARAMETER);
            }
        }
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        for (Object arg : args) {
            beanDefinitionBuilder.addConstructorArgValue(arg);
        }
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
        beanFactory.registerBeanDefinition(name, beanDefinition);
    }
}
