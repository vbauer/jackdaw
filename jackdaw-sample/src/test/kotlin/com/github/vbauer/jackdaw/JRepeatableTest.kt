package com.github.vbauer.jackdaw

import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * @author Vladislav Bauer
 */

class JRepeatableTest {

    @Test
    fun testAbstractUserModel() {
        val annotation = AbstractUserModel::class.java.getAnnotation(RoleList::class.java)
        val roles = annotation.value.asList()

        assertThat(roles, hasSize<Role>(3))
        assertThat(findRole(roles, "user"), notNullValue())
        assertThat(findRole(roles, "moderator"), notNullValue())
        assertThat(findRole(roles, "administrator"), notNullValue())
    }

    private fun findRole(roles: List<Role>, role: String) =
        roles.any { r: Role -> r.value == role }

}
