package com.github.vbauer.jackdaw.annotation;

import com.github.vbauer.jackdaw.annotation.type.JFunctionType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The @JFunction annotation generates Function implementation to use functional-way for programming.
 *
 * There are several ways to generate function or group of functions. It depends on the annotation location:
 * <ul>
 *     <li>annotation on field - generate function only for one specified field.</li>
 *     <li>annotation on method without args - generate function using method with empty list of arguments and
 *     non-void return-value.</li>
 *     <li>annotation on class - generate functions using 2 previous strategies
 *     (for all fields and simple methods).</li>
 * </ul>
 *
 * <br/> Original class Company:
 * <pre>{@code
 * public class Company {
 *     &#64;JFunction private int id;
 *
 *     public int getId() { return id; }
 *     public void setId(final int id) { this.id = id; }
 * }
 * }</pre>
 *
 * Generated class CompanyFunctions:
 * <pre>{@code
 * public final class CompanyFunctions {
 *     public static final Function<Company, Integer> ID = new Function<Company, Integer>() {
 *         public Integer apply(final Company input) {
 *             return (input != null) ? input.getId() : null;
 *         }
 *     };
 * }
 * }</pre>
 *
 * @author Vladislav Bauer
 */

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD })
public @interface JFunction {

    JFunctionType type() default JFunctionType.GUAVA;

}
