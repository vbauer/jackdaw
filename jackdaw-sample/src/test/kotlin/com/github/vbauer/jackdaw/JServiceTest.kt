package com.github.vbauer.jackdaw

import com.github.vbauer.jackdaw.bean.AbstractTypeA
import com.github.vbauer.jackdaw.bean.AbstractTypeB
import com.github.vbauer.jackdaw.bean.BaseType
import com.google.common.collect.Lists
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.hasSize
import org.junit.Assert.assertThat
import org.junit.Test
import java.util.*

/**
 * @author Vladislav Bauer
 */

class JServiceTest {

    @Test
    fun testBaseType() {
        val loader = ServiceLoader.load(BaseType::class.java)
        val beans = Lists.newArrayList<BaseType>(loader)
        assertThat(beans, hasSize(2))

        val classes = beans.map { b -> b.javaClass }
        assertThat(classes, hasItem(AbstractTypeA::class.java))
        assertThat(classes, hasItem(AbstractTypeB::class.java))
    }

}
