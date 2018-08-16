package com.winning.fba.trainning.mybatis.chapter2.orm.annotation;

import java.lang.annotation.*;

/**
 * @author Smart Chow
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
    String name();
    String type() default "string";
    int length() default 20;
}
