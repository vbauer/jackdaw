package com.github.vbauer.jackdaw

import com.github.vbauer.jackdaw.CompanyComparators.*
import com.github.vbauer.jackdaw.base.BaseTest
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import java.util.*

/**
 * @author Vladislav Bauer
 */

class JComparatorTest : BaseTest() {

    @Test
    fun testCompanyComparatorsConstructor() {
        checkConstructor(CompanyComparators::class)
    }

    @Test
    fun testCompanyComparatorsId() {
        // a = b
        checkComparator(ID, null, null, 0)
        checkComparator(ID, company { id = 0 }, company { id = 0 }, 0)

        // a < b (but reverse)
        checkComparator(ID, null, Company(), 1)
        checkComparator(ID, company { id = 0 }, company { id = 1 }, 1)

        // a > b (but reverse)
        checkComparator(ID, Company(), null, -1)
        checkComparator(ID, company { id = 1 }, company { id = 0 }, -1)
    }

    @Test
    fun testCompanyComparatorsName() {
        // a = b
        checkComparator(NAME, null, null, 0)
        checkComparator(NAME, company { name = "a" }, company { name = "a" }, 0)

        // a < b
        checkComparator(NAME, null, company { name = "a" }, -1)
        checkComparator(NAME, company { name = "a" }, company { name = "b" }, -1)

        // a > b
        checkComparator(NAME, company { name = "a" }, null, 1)
        checkComparator(NAME, company { name = "b" }, company { name = "a" }, 1)
    }

    @Test
    fun testCompanyComparatorsLlp() {
        // a = b
        checkComparator(LLP, company { llp = false }, company { llp = false }, 0)
        checkComparator(LLP, company { llp = true }, company { llp = true }, 0)

        // a < b
        checkComparator(LLP, company { llp = false }, company { llp = true }, -1)

        // a > b
        checkComparator(LLP, company { llp = true }, company { llp = false }, 1)
    }

    @Test
    fun testCompanyComparatorsRevenue() {
        // a = b
        checkComparator(REVENUE, null, null, 0)
        checkComparator(REVENUE, company { revenue = 1 }, company { revenue = 1 }, 0)

        // a < b
        checkComparator(REVENUE, null, company { revenue = 1 }, -1)
        checkComparator(REVENUE, company { revenue = 1 }, company { revenue = 2 }, -1)

        // a > b
        checkComparator(REVENUE, company { revenue = 1 }, null, 1)
        checkComparator(REVENUE, company { revenue = 2 }, company { revenue = 1 }, 1)
    }


    private fun checkComparator(
        comparator: Comparator<Company>, a: Company?, b: Company?, expected: Int
    ) = assertThat(comparator.compare(a, b), equalTo(expected))

    private fun company(init: Company.() -> Unit): Company = Company().apply(init)

}
