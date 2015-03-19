package com.github.vbauer.jackdaw.annotation;

import javax.tools.Diagnostic;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>The &#64;JMessage annotation does not generate any additional code, instead of this it prints information in
 * logs during project compiling. It could be useful to make some really meaningful notes for you or your team,
 * instead of using TODOs in comments.</p>
 *
 * Available parameters:
 * <ul>
 *     <li>value - List of notes, that will be logged.</li>
 *     <li>type - Logging level (default value is Diagnostic.Kind.MANDATORY_WARNING).</li>
 *     <li>details - Add information about annotated element with note message (default value is false).</li>
 * </ul>
 *
 * <br/> Example:
 * <pre>{@code
 * &#64;JMessage({
 *     "Do not forget to remove this class in the next release",
 *     "MouseListener interface will be used instead of it"
 * })
 * public abstract class AbstractMouseListener implements MouseListener {
 *     // Some piece of code.
 * }
 * }</pre>
 *
 * Part of compilation output:
 * <pre>
 * [INFO] --- maven-processor-plugin:2.2.4:process (process) @ jackdaw-sample ---
 * [WARNING] diagnostic: warning: Do not forget to remove this class in the next release
 * [WARNING] diagnostic: warning: MouseListener interface will be used instead of it
 * </pre>
 *
 * This feature could be also useful in pair with CI servers (detect [WARNING] and make some additional actions).
 *
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

    String[] value();

    Diagnostic.Kind type() default Diagnostic.Kind.MANDATORY_WARNING;

    boolean details() default false;

}
