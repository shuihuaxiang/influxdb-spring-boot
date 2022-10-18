package io.github.shuihuaxiang.annotations;

import java.lang.annotation.*;
/**
 * @Author: kimli
 * @Date: 2022/10/17 22:33
 * @Description: 注解@Delete
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Delete {
    /**
     *  sql语句，支持拼接，占位方式：#{ } 或者 @${ }
     *
     * @return
     */
    String value();
}
