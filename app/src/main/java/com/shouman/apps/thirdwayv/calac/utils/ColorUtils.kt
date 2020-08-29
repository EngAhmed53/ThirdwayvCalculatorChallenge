package com.shouman.apps.thirdwayv.calac.utils

import com.shouman.apps.thirdwayv.calac.R

/**
 * This Singleton represent a color utils object.
 */
object ColorUtils {


    /**
     * This Method return a color resource depend on the selected math operation by the user,
     * or throw an Exception if the operation not supported.
     * @param operation - selected operation.
     * @return color resource.
     * @throws IllegalArgumentException
     */
    fun getBackgroundColorByOperation(operation: Char): Int {
        return when (operation) {
            '+' -> R.color.plusColor
            '-' -> R.color.subColor
            'x' -> R.color.multiColor
            '÷' -> R.color.divColor
            else -> throw IllegalArgumentException("operation not accepted")
        }
    }
}