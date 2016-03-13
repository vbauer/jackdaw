package com.github.vbauer.jackdaw

import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * @author Vladislav Bauer
 */

class JAdapterTest {

    @Test
    fun testMouseListenerAdapter() {
        val adapter = MouseListenerAdapter()
        checkAdapter(adapter)
    }

    @Test
    fun testAbstractMouseListenerAdapter() {
        val adapter = AbstractMouseListenerAdapter(0, null)
        checkAdapter(adapter)
    }

    private fun checkAdapter(adapter: MouseListener) {
        assertThat(adapter.x, equalTo(0))
        assertThat(adapter.y, nullValue())
        assertThat(adapter.press(0), nullValue())
        assertThat(adapter.isPressed(0), equalTo(false))
    }

}
