package com.shouman.apps.thirdwayv.calac.data.model

import androidx.databinding.BaseObservable

data class ItemCell(
    val id: Long = System.currentTimeMillis(),
    val operation: Char? = '+',
    val operand: Int? = 1
) : BaseObservable() {

    fun getValue(): String = operation.toString() + operand
}