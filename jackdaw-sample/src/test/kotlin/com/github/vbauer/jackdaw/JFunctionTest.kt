package com.github.vbauer.jackdaw

import com.github.vbauer.jackdaw.base.BaseTest
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Assert.fail
import org.junit.Test

/**
 * @author Vladislav Bauer
 */

class JFunctionTest : BaseTest() {

    @Test
    fun testCompanyFactoryConstructor() {
        checkConstructor(CompanyFunctions::class)
    }

    @Test
    fun testCompanyFunctions() {
        val id = 7
        val company = Company().apply {
            setId(id)
        }

        assertThat(CompanyFunctions.ID.apply(company), equalTo(id))
        assertThat(CompanyFunctions.LLP.apply(company), equalTo(false))
        assertThat(CompanyFunctions.LISTED.apply(company), equalTo(false))

        assertThat(CompanyFunctions.ID.apply(null), nullValue())
        assertThat(CompanyFunctions.LISTED.apply(null), nullValue())
    }

    @Test(expected = NullPointerException::class)
    fun testCompanyFunctionsNPE() {
        fail(CompanyFunctions.LLP.apply(null).toString())
    }

}
