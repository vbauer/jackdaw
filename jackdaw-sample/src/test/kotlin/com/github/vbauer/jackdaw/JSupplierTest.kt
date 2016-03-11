package com.github.vbauer.jackdaw

import com.github.vbauer.jackdaw.base.BaseTest
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * @author Vladislav Bauer
 */

class JSupplierTest : BaseTest() {

    @Test
    fun testCompanySuppliersConstructor() {
        checkConstructor(CompanySuppliers::class)
    }

    @Test
    fun testCompanySuppliers() {
        val name = "test"
        val revenue = 7L
        val llp = false

        val company = Company().apply {
            setName(name)
            setRevenue(revenue)
        }

        assertThat(CompanySuppliers.getName(company).get(), equalTo(name))
        assertThat(CompanySuppliers.llp(company).get(), equalTo(llp))
        assertThat(CompanySuppliers.getRevenue(company).get(), equalTo(revenue))
    }

}
