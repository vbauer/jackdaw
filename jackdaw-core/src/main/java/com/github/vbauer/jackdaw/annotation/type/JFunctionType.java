package com.github.vbauer.jackdaw.annotation.type;

/**
 * You can specify different function interfaces for implementation generation:
 *
 * <ul>
 *     <li>GUAVA - Guava functions (com.google.common.base.Function)</li>
 *     <li>JAVA - functions from Java 8 (java.util.function.Function)</li>
 * </ul>
 *
 * @author Vladislav Bauer
 */

public enum JFunctionType {

    JAVA,

    GUAVA

}
