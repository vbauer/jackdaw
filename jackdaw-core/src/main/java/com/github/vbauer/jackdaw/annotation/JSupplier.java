package com.github.vbauer.jackdaw.annotation;

import com.github.vbauer.jackdaw.annotation.type.JSupplierType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The @JSupplier annotation generates Supplier implementation to use functional-way for programming.
 *
 * There are several ways to generate supplier or group of suppliers. It depends on the annotation location:
 * <ul>
 *     <li>annotation on field - generate supplier only for one specified field.</li>
 *     <li>annotation on method without args - generate supplier using method with empty list of arguments and
 *     non-void return-value.</li>
 *     <li>annotation on class - generate supplier using 2 previous strategies
 *     (for all fields and simple methods).</li>
 * </ul>
 *
 * <br/> Original class Company:
 * <pre>{@code
 * public class Company {
 *     &#64;JSupplier private int id;
 *
 *     public int getId() { return id; }
 *     public void setId(final int id) { this.id = id; }
 * }
 * }</pre>
 *
 * Generated class CompanySuppliers:
 * <pre>{@code
 * public final class CompanySuppliers {
 *     public static Supplier<Integer> getId(final Company o) {
 *         return new Supplier<Integer>() {
 *             public Integer get() {
 *                 return o.getId();
 *             }
 *         };
 *     }
 * }
 * }</pre>
 *
 * @author Vladislav Bauer
 */

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD })
public @interface JSupplier {

    /**
     * Type of supplier interface for implementation generation (default is GUAVA).
     * @return type of supplier interface
     */
    JSupplierType type() default JSupplierType.GUAVA;

}
