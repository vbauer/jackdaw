package com.github.vbauer.jackdaw.annotation;

import com.github.vbauer.jackdaw.annotation.type.JPredicateType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>The @JPredicate annotation generates Predicate implementation to use functional-way for programming.</p>
 *
 * There are several ways to generate predicate or group of predicates.
 * It depends on the annotation location:
 *
 * <ul>
 *     <li>annotation on field - generate predicate only for one specified field.</li>
 *     <li>annotation on method without args - generate predicate using method with empty list of arguments
 *     and non-void return-value.</li>
 *     <li>annotation on class - generate predicate using 2 previous strategies
 *     (for all fields and simple methods).</li>
 * </ul>
 *
 * <br/> Original class Company:
 * <pre>{@code
 * public class Company {
 *     &#64;JPredicate(reverse = true) private boolean listed;
 * }
 * }</pre>
 *
 * Generated class CompanyPredicates:
 * <pre>{@code
 * public final class CompanyPredicates {
 *     public static final Predicate<Company> LISTED = new Predicate<Company>() {
 *         public boolean apply(final Company input) {
 *             return !input.isListed();
 *         }
 *     };
 * }
 * }</pre>
 *
 * To generate reverse predicate, use parameter "reverse".
 *
 * @author Vladislav Bauer
 */

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD })
public @interface JPredicate {

    JPredicateType type() default JPredicateType.GUAVA;

    boolean reverse() default false;

}
