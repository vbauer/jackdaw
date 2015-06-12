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

    /**
     * List of notes, that will be logged.
     * @return list of messages
     */
    String[] value();

    /**
     * Logging level (default value is Diagnostic.Kind.MANDATORY_WARNING).
     * @return logging level
     */
    Diagnostic.Kind type() default Diagnostic.Kind.MANDATORY_WARNING;

    /**
     * Add information about annotated element with note message (default value is false).
     * @return add details information in note
     */
    boolean details() default false;

    /**
     * Show message only after the given date.
     * Supported formats:
     * <ul>
     *     <li>yyyy-MM-dd</li>
     *     <li>yyyy/MM/dd</li>
     *     <li>dd-MM-yyyy</li>
     *     <li>dd/MM/yyyy</li>
     * </ul>
     * @return deadline date
     */
    String after() default "";

    /**
     * Show message only after the given date.
     * Supported formats are the same like for the {@link JMessage#after()}.
     * @return formatted date
     */
    String before() default "";

}
