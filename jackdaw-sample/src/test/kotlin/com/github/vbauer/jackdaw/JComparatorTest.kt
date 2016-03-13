package com.github.vbauer.jackdaw

import com.github.vbauer.jackdaw.base.BaseTest
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * @author Vladislav Bauer
 */

class JComparatorTest : BaseTest() {

    @Test
    fun testCompanyComparatorsId() {
        // a = b
        assertThat(
            CompanyComparators.ID.compare(null, null),
            equalTo(0)
        )
        assertThat(
            CompanyComparators.ID.compare(
                Company().apply { id = 0 },
                Company().apply { id = 0 }
            ),
            equalTo(0)
        )

        // a < b (but reverse)
        assertThat(
            CompanyComparators.ID.compare(null, Company()),
            equalTo(1)
        )
        assertThat(
            CompanyComparators.ID.compare(
                Company().apply { id = 0 },
                Company().apply { id = 1 }
            ),
            equalTo(1)
        )

        // a > b (but reverse)
        assertThat(
            CompanyComparators.ID.compare(Company(), null),
            equalTo(-1)
        )
        assertThat(
            CompanyComparators.ID.compare(
                Company().apply { id = 1 },
                Company().apply { id = 0 }
            ),
            equalTo(-1)
        )
    }

    @Test
    fun testCompanyComparatorsName() {
        // a = b
        assertThat(
            CompanyComparators.NAME.compare(null, null),
            equalTo(0)
        )
        assertThat(
            CompanyComparators.NAME.compare(
                Company().apply { name = "a" },
                Company().apply { name = "a" }
            ),
            equalTo(0)
        )

        // a < b
        assertThat(
            CompanyComparators.NAME.compare(
                null,
                Company().apply { name = "a" }
            ),
            equalTo(-1)
        )
        assertThat(
            CompanyComparators.NAME.compare(
                Company().apply { name = "a" },
                Company().apply { name = "b" }
            ),
            equalTo(-1)
        )

        // a > b
        assertThat(
            CompanyComparators.NAME.compare(
                Company().apply { name = "a" },
                null
            ),
            equalTo(1)
        )
        assertThat(
            CompanyComparators.NAME.compare(
                Company().apply { name = "b" },
                Company().apply { name = "a" }
            ),
            equalTo(1)
        )
    }

}
