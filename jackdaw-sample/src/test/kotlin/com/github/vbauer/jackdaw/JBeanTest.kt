package com.github.vbauer.jackdaw

import com.github.vbauer.jackdaw.bean.TypeA
import com.github.vbauer.jackdaw.bean.TypeB
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * @author Vladislav Bauer
 */

class JBeanTest {

    @Test
    fun testUser() {
        val id = 3
        val username = "user"
        val password = "pass"
        val activated = true
        val admin = false

        val user = User().apply {
            setId(id)
            setUsername(username)
            setPassword(password)
            setActivated(activated)
            isAdmin = admin
        }

        assertThat(user.id, equalTo(id))
        assertThat(user.username, equalTo(username))
        assertThat(user.password, equalTo(password))
        assertThat(user.admin, equalTo(admin))
    }

    @Test
    fun testTypeA() {
        val hasSomething = true
        val typeA = TypeA().apply {
            isHasSomething = hasSomething
        }

        assertThat(typeA.isHasSomething, equalTo(hasSomething))
    }

    @Test
    fun testTypeB() {
        val type = TypeA()
        val typeB = TypeB().apply {
            setType(type)
        }

        assertThat(typeB.type, equalTo(type))
    }

}
