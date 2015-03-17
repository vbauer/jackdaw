package com.github.vbauer.jackdaw.annotation;

import javax.tools.Diagnostic;
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
@Target({
    ElementType.TYPE,
    ElementType.FIELD,
    ElementType.METHOD,
    ElementType.CONSTRUCTOR,
    ElementType.ANNOTATION_TYPE,
    ElementType.PARAMETER
})
public @interface JMessage {

    String value();

    Diagnostic.Kind type() default Diagnostic.Kind.MANDATORY_WARNING;

    boolean details() default false;

}
