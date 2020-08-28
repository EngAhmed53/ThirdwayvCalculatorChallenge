package com.shouman.apps.thirdwayv.calac.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shouman.apps.thirdwayv.calac.data.model.ItemCell
import java.util.*


interface IRepository {
    fun addNewCell(itemCell: ItemCell);
    fun removeItem(itemCell: ItemCell)
    fun getAllCells(): LiveData<LinkedList<ItemCell>>
}

object MainRepository : IRepository {

    private val database = MutableLiveData<LinkedList<ItemCell>>()

    override fun addNewCell(itemCell: ItemCell) {
        val list = database.value ?: LinkedList<ItemCell>()
        list.addFirst(itemCell)
        database.value = list
    }

    override fun removeItem(itemCell: ItemCell) {
        val list = database.value
        if (list.isNullOrEmpty()) return

        list.remove(itemCell)
        database.value = list
    }

    override fun getAllCells(): LiveData<LinkedList<ItemCell>> {
        return database
    }
}
