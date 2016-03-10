package com.github.vbauer.jackdaw.base

import com.pushtorefresh.private_constructor_checker.PrivateConstructorChecker
import kotlin.reflect.KClass

/**
 * @author Vladislav Bauer
 */

abstract class BaseTest {

    fun checkConstructor(clazz: KClass<out Any>) {
        PrivateConstructorChecker
            .forClass(clazz.java)
            .expectedTypeOfException(UnsupportedOperationException::class.java)
            .check()
    }

}