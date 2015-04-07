package com.github.vbauer.jackdaw.annotation.type;

/**
 * Enum type which allows to specify different function interfaces for implementation generation.
 *
 * @author Vladislav Bauer
 */

public enum JFunctionType {

    /**
     * Java 8 function (java.util.function.Function)
     */
    JAVA,

    /**
     * Guava function (com.google.common.base.Function)
     */
    GUAVA

}
