package com.github.vbauer.jackdaw

import com.github.vbauer.jackdaw.base.BaseTest
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * @author Vladislav Bauer
 */

class JAdapterTest : BaseTest() {

    @Test
    fun testMouseListenerAdapter() {
        val adapter = MouseListenerAdapter()

        assertThat(adapter.x, equalTo(0))
        assertThat(adapter.y, nullValue())
        assertThat(adapter.press(0), nullValue())
        assertThat(adapter.isPressed(0), equalTo(false))
    }

}
