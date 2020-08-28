package com.shouman.apps.thirdwayv.calac.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shouman.apps.thirdwayv.calac.data.model.ItemCell
import java.util.*


interface IRepository {
    fun addNewCell(itemCell: ItemCell);
    fun removeItem(itemCell: ItemCell)
    fun getAllCells(): LinkedList<ItemCell>
}

object MainRepository : IRepository {

    private val database = LinkedList<ItemCell>()

    override fun addNewCell(itemCell: ItemCell) {
        database.add(itemCell)
    }

    override fun removeItem(itemCell: ItemCell) {
        database.remove(itemCell)
    }

    override fun getAllCells(): LinkedList<ItemCell> {
        return database
    }
}
