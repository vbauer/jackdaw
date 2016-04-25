package com.github.vbauer.jackdaw.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>&#64;JFactoryMethod allows to use pattern Factory Method for object instantiation. To use this annotation
 * it is necessary to have setters and default constructor in original class.</p>
 *
 * <br/> Original class Company:
 * <pre>{@code
 * &#64;JFactoryMethod
 * public class Company {
 *     private int id;
 *     private String name;
 *
 *     public int getId() { return id; }
 *     public void setId(final int id) { this.id = id; }
 *
 *     public String getName() { return name; }
 *     public void setName(final String name) { this.name = name; }
 * }
 * }</pre>
 *
 * Generated class CompanyFactory:
 * <pre>{@code
 * public final class CompanyFactory {
 *     private CompanyFactory() {
 *         throw new UnsupportedOperationException();
 *     }
 *     public static Company create(final int id, final String name) {
 *         final Company object = new Company();
 *         object.setId(id);
 *         object.setName(name);
 *         return object;
 *     }
 * }
 * }</pre>
 *
 * @author Vladislav Bauer
 */

@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface JFactoryMethod {

    String DEFAULT_METHOD_NAME = "create";

    /**
     * Factory method name (default value is {@link #DEFAULT_METHOD_NAME}).
     * @return method name
     */
    String method() default DEFAULT_METHOD_NAME;

    /**
     * Use only specified fields in factory method (it is an empty array by default).
     * @return true to use specified fields or false otherwise.
     */
    String[] arguments() default {};

    /**
     * Use all fields of class in factory method (default value is true).
     * @return true to use all fields or false otherwise
     */
    boolean all() default true;

}
