package com.github.vbauer.jackdaw.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * <p>Java annotation processors and other systems use ServiceLoader to register implementations of well-known types
 * using META-INF metadata. However, it is easy for a developer to forget to update or correctly specify the service
 * descriptors. Metadata will be generated for any class annotated with @JService.</p>
 *
 * Example:
 * <pre>{@code
 * public interface BaseType {}
 *
 * &#64;JService(BaseType.class)
 * public class TypeA implements BaseType {}
 *
 * &#64;JService(BaseType.class)
 * public class TypeB implements BaseType {}
 * }</pre>
 *
 * Generated file `META-INF/services/BaseType`:
 * <pre>
 * TypeA
 * TypeB
 * </pre>

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
