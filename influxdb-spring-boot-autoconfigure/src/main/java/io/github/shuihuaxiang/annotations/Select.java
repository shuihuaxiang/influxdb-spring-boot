package io.github.shuihuaxiang.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: kimli
 * @Date: 2022/10/17 22:33
 * @Description: 注解@Select
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Select {
    /**
     *  sql语句，支持拼接，占位方式：#{ } 或者 @${ }
     * @return
     */
    String value();

    /**
     * 返回结果类型
     *
     * @return
     */
    Class resultType();
}
