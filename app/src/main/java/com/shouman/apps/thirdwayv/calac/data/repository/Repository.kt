package com.shouman.apps.thirdwayv.calac.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.shouman.apps.thirdwayv.calac.data.model.ItemCell
import com.shouman.apps.thirdwayv.calac.executors.AppExecutors
import java.util.*


interface IRepository {
    fun addNewCell(itemCell: ItemCell)
    fun removeItem(itemCell: ItemCell)
    fun getLatestRecord(): LiveData<List<ItemCell>>
    fun undo()
    fun redo()
    fun isRedoAvailable(): LiveData<Boolean>
    fun isUndoAvailable(): LiveData<Boolean>
    fun clearAll()
}

object MainRepository : IRepository {

    private val history = MutableLiveData<LinkedList<LinkedList<ItemCell>>>().apply {
        val initialHistory = LinkedList<LinkedList<ItemCell>>()
        initialHistory.add(LinkedList())
        value = initialHistory
    }

    private val cursor = MutableLiveData<Int?>()

    private val redoCounter = MutableLiveData<Int?>()


    override fun addNewCell(itemCell: ItemCell) {

        AppExecutors.getsInstance().diskIO.execute {

            var historyLinkedList = history.value
            if (historyLinkedList.isNullOrEmpty()) {
                historyLinkedList = LinkedList()
                val firstRecord = LinkedList<ItemCell>()
                firstRecord.addFirst(itemCell)
                historyLinkedList.push(firstRecord)
                history.postValue(historyLinkedList)
                cursor.postValue(historyLinkedList.indexOf(firstRecord))

                return@execute
            }

            val lastRecord = historyLinkedList.peek()
            if (lastRecord != null) {
                val newRecord = LinkedList(lastRecord)
                newRecord.addFirst(itemCell)
                historyLinkedList.push(newRecord)
                history.postValue(historyLinkedList)
                cursor.postValue(historyLinkedList.indexOf(newRecord))
            }
        }
    }

    override fun removeItem(itemCell: ItemCell) {

        AppExecutors.getsInstance().diskIO.execute {

            val historyLinkedList = history.value
            if (historyLinkedList.isNullOrEmpty()) return@execute

            val lastRecord = historyLinkedList.peek()
            if (lastRecord != null) {

                val newRecord = LinkedList(lastRecord)
                newRecord.remove(itemCell)
                historyLinkedList.push(newRecord)
                history.postValue(historyLinkedList)
                cursor.postValue(historyLinkedList.indexOf(newRecord))

            }

        }
    }

    override fun getLatestRecord(): LiveData<List<ItemCell>> {
        return Transformations.map(history) {
            it.peek()?.toList()
        }
    }

    override fun undo() {

        AppExecutors.getsInstance().diskIO.execute {

            val cursorPosition = cursor.value
            val historyLinkedList = history.value
            if (historyLinkedList.isNullOrEmpty()
                || cursorPosition == null
                || cursorPosition == historyLinkedList.size - 1
            ) return@execute

            val previousRecord = historyLinkedList[cursorPosition + 1]
            historyLinkedList.push(previousRecord)
            history.postValue(historyLinkedList)
            cursor.postValue(cursorPosition + 2)
            redoCounter.postValue((redoCounter.value ?: 0) + 1)

        }
    }

    override fun redo() {

        AppExecutors.getsInstance().diskIO.execute {

            val redoTimes = redoCounter.value
            if (redoTimes == null || redoTimes == 0) return@execute

            val cursorPosition = cursor.value
            val historyLinkedList = history.value
            if (historyLinkedList.isNullOrEmpty() || cursorPosition == null || cursorPosition == 0) return@execute

            val followingRecord = historyLinkedList[cursorPosition - 1]
            historyLinkedList.push(followingRecord)
            history.postValue(historyLinkedList)
            redoCounter.postValue((redoCounter.value ?: 0) - 1)
            cursor.postValue(cursorPosition)

        }
    }

    override fun isRedoAvailable(): LiveData<Boolean> {
        return Transformations.map(redoCounter) {
            it != null && it > 0
        }
    }

    override fun isUndoAvailable(): LiveData<Boolean> {
        return Transformations.map(cursor) {
            val history = history.value

            history != null && it != null && it < history.size - 1
        }
    }

    override fun clearAll() {
        this.cursor.value = null
        this.history.value = null
        this.redoCounter.value = null
    }
}