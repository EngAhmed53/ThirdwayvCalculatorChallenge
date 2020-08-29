package com.shouman.apps.thirdwayv.calac.utils

import com.shouman.apps.thirdwayv.calac.R

object ColorUtils {

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