package com.shouman.apps.thirdwayv.calac.utils

import com.shouman.apps.thirdwayv.calac.R
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ColorUtilsTest(
    val expected:Int,
    val input:Char,
    val scenario:String
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "test case: {2}")
        fun testCases() = listOf(
            arrayOf(R.color.plusColor, '+', "test plus operation"),
            arrayOf(R.color.subColor, '-', "test minus operation"),
            arrayOf(R.color.multiColor, 'x', "test times operation"),
            arrayOf(R.color.divColor, '÷', "test div operation"),
        )
    }

    @Test
    fun test_getBackgroundColorByOperation() {

        val expected = this.expected

        val actual = ColorUtils.getBackgroundColorByOperation(input)

        assertEquals(expected, actual)
    }
}