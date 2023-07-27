package com.qf.qfseckill.anno;

import java.lang.annotation.*;

/**
 * @author 严玉恒Liz
 * @date 2023/7/27 19:21
 */
@Target({ElementType.TYPE,ElementType.FIELD,ElementType.METHOD})
//运行时
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited


public @interface Limiter {
}
