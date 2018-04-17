package com.github.vbauer.jackdaw

import com.github.vbauer.jackdaw.bean.IgnoredBeanModel
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matchers.emptyIterable
import org.hamcrest.Matchers.not
import org.junit.Assert.assertThat
import org.junit.Test
import java.util.*

/**
 * @author Vladislav Bauer
 */

class JIgnoreTest {

    @Test
    fun testIgnoredBean() {
        assertThat(generatedClass("Adapter"), notExist())
        assertThat(beanClass(), notExist())
        assertThat(generatedClass("Builder"), notExist())
        assertThat(generatedClass("ClassDescriptor"), notExist())
        assertThat(generatedClass("Comparators"), notExist())
        assertThat(generatedClass("Factory"), notExist())
        assertThat(generatedClass("Functions"), notExist())
        assertThat(generatedClass("Predicates"), notExist())
        assertThat(generatedClass("Suppliers"), notExist())

        val loader = ServiceLoader.load(IgnoredBeanModel::class.java)
        assertThat(loader, emptyIterable())
    }


    private val pkg = "com.github.vbauer.jackdaw.bean"

    private fun notExist() = not(HasClassMatcher())
    private fun generatedClass(name: String) = "$pkg.IgnoredBeanModel$name"
    private fun beanClass() = "$pkg.IgnoredBean"


    class HasClassMatcher : BaseMatcher<String>() {
        override fun matches(item: Any?): Boolean {
            return try {
                Class.forName(item.toString())
                true
            } catch (ex: Exception) {
                false
            }
        }

        override fun describeTo(description: Description) {
            description.appendText("existed class")
        }
    }
}

