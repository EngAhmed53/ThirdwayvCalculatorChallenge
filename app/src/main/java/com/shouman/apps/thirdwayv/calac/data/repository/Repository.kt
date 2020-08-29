package com.shouman.apps.thirdwayv.calac.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.shouman.apps.thirdwayv.calac.data.model.ItemCell
import com.shouman.apps.thirdwayv.calac.executors.AppExecutors
import java.util.*

/**
 * This Interface Represent the behavior for each repository implements it
 */
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

class MainRepository : IRepository {

    /**
     * This MutableLive Data save the history of the operation the user inserted or removed during
     * application life cycle.
     * it start with an empty linkedList as a first action.
     */
    private val history = MutableLiveData<LinkedList<LinkedList<ItemCell>>>().apply {
        val initialHistory = LinkedList<LinkedList<ItemCell>>()
        initialHistory.add(LinkedList())
        value = initialHistory
    }

    /**
     * This MutableLive Data save the <b>History Cursor Position</b> which selected by the user
     * it used to return the user the selected history operations list.
     */
    private val cursor = MutableLiveData<Int?>()

    /**
     * This MutableLive Data save the number of how many time the user pressed a successful undo clicks.
     */
    private val redoCounter = MutableLiveData<Int?>()


    /**
     * This method add a new cell item and add a new history record it history live data, it run in background thread to not block the main thread.
     * @param itemCell - the new cell to add
     */
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

    /**
     * This method remove cell item and add a history record to history live data, it run in background thread to not block the main thread.
     * @param itemCell - the cell to remove
     */
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

    /**
     * This method return the most new record for the user to populate the recycler view with it
     *@return LiveData object with a list of cellsItems
     */
    override fun getLatestRecord(): LiveData<List<ItemCell>> {
        return Transformations.map(history) {
            it?.peek()?.toList()
        }
    }

    /**
     * This Method undo the last operation done by the user, and it run in background thread.
     */
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


    /**
     * This Method redo the last undo operation done by the user, and it run in background thread.
     */
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

    /**
     * This method return a liveData with boolean to represent if redo operation is available.
     */
    override fun isRedoAvailable(): LiveData<Boolean> {
        return Transformations.map(redoCounter) {
            it != null && it > 0
        }
    }

    /**
     * This method return a liveData with boolean to represent if undo operation is available.
     */
    override fun isUndoAvailable(): LiveData<Boolean> {
        return Transformations.map(cursor) {
            val history = history.value

            history != null && it != null && it < history.size - 1
        }
    }

    /**
     * This method clear all database, history and counters.
     */
    override fun clearAll() {
        this.cursor.value = null
        this.history.value = null
        this.redoCounter.value = null
    }
}