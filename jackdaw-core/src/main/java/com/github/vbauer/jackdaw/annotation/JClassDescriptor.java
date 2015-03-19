package com.github.vbauer.jackdaw.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Sometimes it is necessary to use reflection, so it will be useful to have some string constants.
 * &#64;JClassDescriptor generates it for you easily.</p>
 *
 * <br/> Original class Company:
 * <pre>{@code
 * &#64;JClassDescriptor
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
 * Generated class CompanyClassDescriptor:
 * <pre>{@code
 * public final class CompanyClassDescriptor {
 *     public static final String FIELD_ID = "id";
 *     public static final String FIELD_NAME = "name";
 *
 *     public static final String METHOD_ID = "getId";
 *     public static final String METHOD_SET_ID = "setId";
 *     public static final String METHOD_NAME = "getName";
 *     public static final String METHOD_SET_NAME = "setName";
 *
 *     private CompanyClassDescriptor() {
 *         throw new UnsupportedOperationException();
 *     }
 * }
 * }</pre>
 *
 * @author Vladislav Bauer
 */

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
public @interface JClassDescriptor {
}
