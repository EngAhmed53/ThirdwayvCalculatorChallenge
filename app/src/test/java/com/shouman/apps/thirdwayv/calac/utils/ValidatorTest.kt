package com.shouman.apps.thirdwayv.calac.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ValidatorTest(
    val expected: Boolean,
    val input: String?,
    val scenario: String
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "test case: {2}")
        fun testCases() = listOf(
            arrayOf(true, "123", "test numbers"),
            arrayOf(true, "0", "test zero"),
            arrayOf(false, "12eee", "test string with not Digits chars"),
            arrayOf(false, "120202020020202020202020202020", "test number bigger then Integer.max"),
            arrayOf(
                false,
                "-120202020020202020202020202020",
                "test number smaller then Integer.min"
            ),
            arrayOf(false, null, "test null"),
        )
    }

    @Test
    fun isOperandValid() {
        val expected = this.expected

        val actual = Validator.isOperandValid(input)

        assertEquals(actual, expected)
    }
}