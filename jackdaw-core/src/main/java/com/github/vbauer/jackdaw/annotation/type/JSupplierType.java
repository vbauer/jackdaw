package com.github.vbauer.jackdaw.annotation.type;

/**
 * Enum type which allows to specify different supplier interfaces for implementation generation.
 *
 * @author Vladislav Bauer
 */

public enum JSupplierType {

    /**
     * Java 8 supplier (java.util.function.Supplier)
     */
    JAVA,

    /**
     * Guava predicate (com.google.common.base.Supplier)
     */
    GUAVA

}
