package com.github.vbauer.jackdaw.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>&#64;JAdapter allows to create class with empty method implementations using some interface or class.
 * Using generated class, you can override only needed methods (like in Swing, ex: MouseAdapter).</p>
 *
 * <br/> Original class MouseListener:
 * <pre>{@code
 * &#64;JAdapter
 * public interface MouseListener {
 *     void onMove(int x, int y);
 *     void onPressed(int button);
 * }
 * }</pre>
 *
 * Generated class MouseListenerAdapter:
 * <pre>{@code
 * public class MouseListenerAdapter implements MouseListener {
 *     public void onMove(final int x, final int y) {}
 *     public void onPresses(final int button) {}
 * }
 * }</pre>
 *
 * @author Vladislav Bauer
 */

@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface JAdapter {
}
