package com.github.vbauer.jackdaw.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The &#64;JBuilder annotation produces complex builder APIs for your classes.
 *
 * <br/> Original class Company:
 * <pre>{@code
 * &#64;JBuilder
 * public class Company {
 *     private int id;
 *     private String name;
 *     private Set<String> descriptions;
 *
 *     public int getId() { return id; }
 *     public void setId(final int id) { this.id = id; }
 *
 *     public String getName() { return name; }
 *     public void setName(final String name) { this.name = name; }
 *
 *     public Set<String> getDescriptions() { return descriptions; }
 *     public void setDescriptions(final Set<String> descriptions) { this.descriptions = descriptions; }
 * }
 * }</pre>
 *
 * Generated class CompanyBuilder:
 * <pre>{@code
 * public class CompanyBuilder {
 *     private int id;
 *     private String name;
 *     private Set<String> descriptions;
 *
 *     public static CompanyBuilder create() {
 *         return new CompanyBuilder();
 *     }
 *     public CompanyBuilder id(final int id) {
 *         this.id = id;
 *         return this;
 *     }
 *     public CompanyBuilder name(final String name) {
 *         this.name = name;
 *         return this;
 *     }
 *     public CompanyBuilder descriptions(final Set<String> descriptions) {
 *         this.descriptions = descriptions;
 *         return this;
 *     }
 *     public Company build() {
 *         final Company object = new Company();
 *         object.setId(id);
 *         object.setName(name);
 *         object.setListed(listed);
 *         object.setDescriptions(descriptions);
 *         return object;
 *     }
 * }
 * }</pre>
 *
 * JBuilder lets you automatically produce the code required to have your class be instantiable with code such as:
 * <pre>{@code CompanyBuilder.create()
 *     .id(1)
 *     .name("John Smith")
 *     .descriptions(Collections.singleton("Good guy"))
 *     .build()
 * }</pre>
 *
 * @author Vladislav Bauer
 */

@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface JBuilder {
}
