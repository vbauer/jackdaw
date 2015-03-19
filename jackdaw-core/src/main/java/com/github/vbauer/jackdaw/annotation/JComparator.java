package com.github.vbauer.jackdaw.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>To generated safe and well-coded comparator, you have to write a lot of boilerplate code.
 * &#64;JComparator annotation allows to simplify this situation. To generate reverse order comparator,
 * use parameter "reverse".</p>
 *
 * <p>There are several ways to generate comparator or group of comparators.
 * It depends on the annotation location:</p>
 *
 * <ul>
 *     <li>annotation on field - generate comparator only for one specified field.</li>
 *     <li>annotation on method without args - generate comparator using method with empty list of arguments and
 *     non-void return-value.</li>
 *     <li>annotation on class - generate comparators using 2 previous strategies
 *     (for all fields and simple methods).</li>
 * </ul>
 *
 * <br/> Original class Company:
 * <pre>{@code
 * public class Company {
 *     &#64;JComparator private String name;
 *     private long revenue;
 *
 *     public String getName() { return name; }
 *     public void setName(final String name) { this.name = name; }
 *
 *     &#64;JComparator public long getRevenue() { return revenue; }
 *     public void setRevenue(final long revenue) { this.revenue = revenue; }
 * }
 * }</pre>
 *
 * Generated class CompanyComparators:
 * <pre>{@code
 * public final class CompanyComparators {
 *     public static final Comparator<Company> NAME = new Comparator<Company>() {
 *         public int compare(final Company o1, final Company o2) {
 *             final String v1 = o1 == null ? null : o1.getName();
 *             final String v2 = o2 == null ? null : o2.getName();
 *             if (v1 == v2) {
 *                 return 0;
 *             } else if (v1 == null) {
 *                 return -1;
 *             } else if (v2 == null) {
 *                 return 1;
 *             }
 *             return v1.compareTo(v2);
 *         }
 *     };
 *     public static final Comparator<Company> REVENUE = new Comparator<Company>() {
 *         public int compare(final Company o1, final Company o2) {
 *             final Long v1 = o1 == null ? null : o1.getRevenue();
 *             final Long v2 = o2 == null ? null : o2.getRevenue();
 *             if (v1 == v2) {
 *                 return 0;
 *             } else if (v1 == null) {
 *                 return -1;
 *             } else if (v2 == null) {
 *                 return 1;
 *             }
 *             return v1.compareTo(v2);
 *         }
 *     };
 *     private CompanyComparators() {
 *         throw new UnsupportedOperationException();
 *     }
 * }
 * }</pre>
 *
 * @author Vladislav Bauer
 */

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD })
public @interface JComparator {

    boolean reverse() default false;

}
