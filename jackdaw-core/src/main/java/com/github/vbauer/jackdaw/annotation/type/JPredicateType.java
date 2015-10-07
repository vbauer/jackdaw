package com.github.vbauer.jackdaw.annotation.type;

/**
 * Enum type which allows to specify different predicate interfaces for implementation generation.
 *
 * @author Vladislav Bauer
 */

public enum  JPredicateType {

    /**
     * Java 8 predicate (java.util.function.Predicate)
     */
    JAVA,

    /**
     * Guava predicate (com.google.common.base.Predicate)
     */
    GUAVA,

    /**
     * Apache Commons predicate (org.apache.commons.collections.Predicate)
     */
    COMMONS

}
