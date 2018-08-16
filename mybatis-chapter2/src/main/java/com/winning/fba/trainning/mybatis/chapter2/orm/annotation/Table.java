package com.winning.fba.trainning.mybatis.chapter2.orm.annotation;

import java.lang.annotation.*;

/**
 * description:
 *
 * @author Smart CHow
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
    String name();
}
