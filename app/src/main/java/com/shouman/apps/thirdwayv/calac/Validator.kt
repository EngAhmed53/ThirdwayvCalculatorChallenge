package com.shouman.apps.thirdwayv.calac

class Validator {
    companion object {
        fun isOperandValid(operand: String?): Boolean {
            operand?.let {
                val intOperand = operand.toIntOrNull();
                return intOperand != null
            }
            return false
        }
    }
}