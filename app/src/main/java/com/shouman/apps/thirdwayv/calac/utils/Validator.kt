package com.shouman.apps.thirdwayv.calac.utils


/**
 * This Singleton represent an inout validator object.
 */
object Validator {

    /**
     * This Method return a boolean depend on the operand inserted.
     * @param operand - inserted operand.
     * @return Boolean
     */
    fun isOperandValid(operand: String?): Boolean {
        operand?.let {
            val intOperand = operand.toIntOrNull();
            return intOperand != null
        }
        return false

    }
}