package com.github.vbauer.jackdaw.annotation.type;

/**
 * You can specify different predicate interfaces for implementation generation:
 *
 * <ul>
 *     <li>GUAVA - Guava predicates (com.google.common.base.Predicate)</li>
 *     <li>JAVA - predicates from Java 8 (java.util.function.Predicate)</li>
 * </ul>
 *
 * @author Vladislav Bauer
 */

public enum  JPredicateType {

    JAVA,

    GUAVA

}
