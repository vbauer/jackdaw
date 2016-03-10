package com.github.vbauer.jackdaw

import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * @author Vladislav Bauer
 */

class JBuilderTest {

    @Test
    fun testCompanyBuilder() {
        val id = 7
        val listed = true
        val name = "test"
        val revenue = 12L

        val company = CompanyBuilder.create()
            .id(id)
            .listed(listed)
            .name(name)
            .revenue(revenue)
            .build()

        assertThat(company.id, equalTo(id))
        assertThat(company.isListed, equalTo(listed))
        assertThat(company.name, equalTo(name))
        assertThat(company.revenue, equalTo(revenue))
    }

}