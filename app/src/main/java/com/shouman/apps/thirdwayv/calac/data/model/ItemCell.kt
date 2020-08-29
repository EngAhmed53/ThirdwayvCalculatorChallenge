package com.shouman.apps.thirdwayv.calac.data.model

import androidx.databinding.BaseObservable

/**
 * This is a data class witch represent a single cell
 * @param id - cell id
 * @param operation - cell selected operation
 * @param operand - cell inserted operand
 */
data class ItemCell(
    val id: Long = System.currentTimeMillis(),
    val operation: Char? = '+',
    val operand: Int? = 1
) : BaseObservable() {

    /**
     * This Method return each cell data <b>(operation and operand)</b>
     */
    fun getValue(): String = operation.toString() + operand
}