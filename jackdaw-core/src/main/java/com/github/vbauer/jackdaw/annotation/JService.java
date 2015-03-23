package com.github.vbauer.jackdaw.annotation;

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
@Target(ElementType.TYPE)
public @interface JService {

    /**
     * Interface implemented by this service provider.
     * @return interface class
     */
    Class<?> value();

}
