package com.github.vbauer.jackdaw

import com.github.vbauer.jackdaw.base.BaseTest
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * @author Vladislav Bauer
 */

class JPredicateTest : BaseTest() {

    @Test
    fun testCompanyPredicatesConstructor() {
        checkConstructor(CompanyPredicates::class)
    }

    @Test
    fun testCompanyPredicates() {
        val company = Company()

        assertThat(CompanyPredicates.LISTED.evaluate(company), equalTo(true))
        assertThat(CompanyPredicates.LLP.apply(company), equalTo(false))
    }

}
