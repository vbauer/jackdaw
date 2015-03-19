package com.github.vbauer.jackdaw.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>There are some situations where you want to apply the same annotation to a declaration or type use.
 * As of the Java SE 8 release, repeating annotations enable you to do this. If you don't/can't use Java 8,
 * then &#64;JRepeatable helps you to resolve this problem using extra list-annotation.</p>
 *
 * <br/> Original annotation @Role:
 * <pre>{@code
 * &#64;JRepeatable
 * &#64;Retention(RetentionPolicy.CLASS)
 * &#64;Target(ElementType.TYPE)
 * public &#64;interface Role {
 * }
 * }</pre>
 *
 * Generated annotation @RoleList:
 * <pre>{@code
 * &#64;Retention(java.lang.annotation.RetentionPolicy.CLASS)
 * &#64;Target(java.lang.annotation.ElementType.TYPE)
 * public &#64;interface RoleList {
 *     Role[] value();
 * }
 * }</pre>
 *
 * @author Vladislav Bauer
 */

@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.ANNOTATION_TYPE)
public @interface JRepeatable {
}
