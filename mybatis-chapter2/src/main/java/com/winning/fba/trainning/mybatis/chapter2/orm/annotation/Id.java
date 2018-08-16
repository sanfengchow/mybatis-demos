package com.winning.fba.trainning.mybatis.chapter2.orm.annotation;

import java.lang.annotation.*;

/**
 * description:
 *
 * @author Smart Chow
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Id {
    String name();
    String type() default "int";
    int length() default 20;
    int increment() default 1;
}
