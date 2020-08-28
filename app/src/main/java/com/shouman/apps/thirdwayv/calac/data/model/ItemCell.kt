package com.shouman.apps.thirdwayv.calac.data.model

import androidx.databinding.BaseObservable

data class ItemCell(
    val id: Long,
    val operation: Char,
    val operand: Int
) : BaseObservable() {

    fun getValue(): String = operation.toString() + operand
}