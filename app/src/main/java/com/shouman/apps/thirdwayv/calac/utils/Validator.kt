package com.shouman.apps.thirdwayv.calac.utils

object Validator {

    fun isOperandValid(operand: String?): Boolean {
        operand?.let {
            val intOperand = operand.toIntOrNull();
            return intOperand != null
        }
        return false

    }
}