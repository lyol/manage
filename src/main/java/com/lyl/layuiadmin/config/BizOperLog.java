package com.lyl.layuiadmin.config;


import com.lyl.layuiadmin.enums.OperType;

import java.lang.annotation.*;

/**
 * 	业务操作注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BizOperLog {
    /**操作类型*/
    OperType operType() default OperType.Query;

    /**备注*/
    String memo() default "";
}
