package com.github.vbauer.jackdaw.annotation;

import com.github.vbauer.jackdaw.annotation.type.JFunctionType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Vladislav Bauer
 */

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD })
public @interface JFunction {

    JFunctionType type() default JFunctionType.GUAVA;

}
