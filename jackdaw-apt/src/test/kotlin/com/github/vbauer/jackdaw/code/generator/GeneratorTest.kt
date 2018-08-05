package com.github.vbauer.jackdaw.code.generator

import com.github.vbauer.jackdaw.annotation.*
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * @author Vladislav Bauer
 */

class GeneratorTest {

    @Test
    fun testGeneratorsContract() {
        val data = hashMapOf(
            JAdapterCodeGenerator() to JAdapter::class,
            JBeanCodeGenerator() to JBean::class,
            JBuilderCodeGenerator() to JBuilder::class,
            JClassDescriptorCodeGenerator() to JClassDescriptor::class,
            JComparatorCodeGenerator() to JComparator::class,
            JFactoryMethodCodeGenerator() to JFactoryMethod::class,
            JFunctionCodeGenerator() to JFunction::class,
            JMessageCodeGenerator() to JMessage::class,
            JPredicateCodeGenerator() to JPredicate::class,
            JServiceCodeGenerator() to JService::class,
            JSupplierCodeGenerator() to JSupplier::class
        )

        data.forEach { (g, a) -> assertThat(g.annotation, equalTo(a.java)) }
    }

}
