package io.github.shuihuaxiang.annotations;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author: kimli
 * @Date: 2022/10/17 22:33
 * @Description:
 */
@Documented
@Inherited
@Retention(RUNTIME)
@Target({ TYPE, METHOD, FIELD, PARAMETER })
public @interface InfluxMapper {
}
