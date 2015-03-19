package com.github.vbauer.jackdaw.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>&#64;JBean generates some boilerplate code that is normally associated with simple POJOs (Plain Old Java Objects)
 * and beans:</p>
 * <ul>
 *     <li>getters for all non-static/private fields,</li>
 *     <li>setters for all non-static/private/final fields,</li>
 *     <li>and copy constructors from super class</li>
 * </ul>
 *
 * Original class AbstractUserModel:
 * <pre>{@code
 * &#64;JBean
 * public abstract class AbstractUserModel {
 *     protected int id;
 *     protected String username;
 *     protected String password;
 *     protected boolean admin;
 * }
 * }</pre>
 *
 * Generated class User:
 * <pre>{@code
 * public class User extends AbstractUserModel {
 *     public User() { super(); }
 *
 *     public void setId(final int id) { this.id = id; }
 *     public void getId() { return id; }
 *
 *     public String getUsername() { return username; }
 *     public void setUsername(final String username) { this.username = username; }
 *
 *     public String getPassword() { return password; }
 *     public void setPassword(final String password) { this.password = password; }
 *
 *     public boolean isAdmin() { return admin; }
 *     public void setAdmin(final boolean admin) { this.admin = admin; }
 * }
 * }</pre>
 *
 * Prefix 'Abstract' and postfix 'Model' will be removed if they are presented
 *
 * @author Vladislav Bauer
 */

@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface JBean {
}
