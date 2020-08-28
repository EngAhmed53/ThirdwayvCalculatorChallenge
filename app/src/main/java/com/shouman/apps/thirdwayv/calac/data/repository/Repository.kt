package com.shouman.apps.thirdwayv.calac.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.shouman.apps.thirdwayv.calac.data.model.ItemCell
import java.util.*


interface IRepository {
    fun addNewCell(itemCell: ItemCell)
    fun removeItem(itemCell: ItemCell)
    fun getAllCells(): LiveData<LinkedList<ItemCell>>
    fun undo()
    fun redo()
    fun getRedoStackSize(): LiveData<Int>
    fun getUndoStackSize(): LiveData<Int>
    fun clearAll()
}

enum class OperationType {
    ADD_ITEM,
    REMOVE_ITEM
}

class Operation(
    val itemCell: ItemCell,
    var type: OperationType
)

object MainRepository : IRepository {

    private val database = MutableLiveData<LinkedList<ItemCell>>()

    private val undoStackLiveData = MutableLiveData<Stack<Operation>>()
    private val redoStackLiveData = MutableLiveData<Stack<Operation>>()

    override fun addNewCell(itemCell: ItemCell) {
        val list = database.value ?: LinkedList<ItemCell>()
        list.addFirst(itemCell)
        database.value = list

        val addOperation = Operation(itemCell, OperationType.ADD_ITEM)
        val undoStack = undoStackLiveData.value ?: Stack<Operation>()
        undoStack.push(addOperation)

        undoStackLiveData.value = undoStack
        redoStackLiveData.value = null
    }

    override fun removeItem(itemCell: ItemCell) {
        val list = database.value
        if (list.isNullOrEmpty()) return

        list.remove(itemCell)
        database.value = list

        val removeOperation = Operation(itemCell, OperationType.REMOVE_ITEM)
        val undoStack = undoStackLiveData.value ?: Stack()
        undoStack.push(removeOperation)

        undoStackLiveData.value = undoStack
    }

    override fun getAllCells(): LiveData<LinkedList<ItemCell>> {
        return database
    }

    override fun undo() {
        val undoStack = undoStackLiveData.value
        if (undoStack.isNullOrEmpty()) return

        val redoStack = redoStackLiveData.value ?: Stack()
        val data = database.value

        val lastOperation = undoStack.pop()
        when (lastOperation.type) {

            OperationType.ADD_ITEM -> {
                data?.remove(lastOperation.itemCell)
                lastOperation.type = OperationType.REMOVE_ITEM
                redoStack.push(lastOperation)
            }

            OperationType.REMOVE_ITEM -> {
                data?.addFirst(lastOperation.itemCell)
                lastOperation.type = OperationType.ADD_ITEM
                redoStack.push(lastOperation)
            }
        }

        redoStackLiveData.value = redoStack
        undoStackLiveData.value = undoStack
        database.value = data
    }

    override fun redo() {
        val redoStack = redoStackLiveData.value
        if (redoStack.isNullOrEmpty()) return

        val undoStack = undoStackLiveData.value ?: Stack()
        val data = database.value

        val lastOperation = redoStack.pop()

        when (lastOperation.type) {

            OperationType.ADD_ITEM -> {
                data?.remove(lastOperation.itemCell)
                lastOperation.type = OperationType.REMOVE_ITEM
                undoStack.push(lastOperation)
            }

            OperationType.REMOVE_ITEM -> {
                data?.addFirst(lastOperation.itemCell)
                lastOperation.type = OperationType.ADD_ITEM
                undoStack.push(lastOperation)
            }
        }
        redoStackLiveData.value = redoStack
        undoStackLiveData.value = undoStack
        database.value = data
    }

    override fun getUndoStackSize(): LiveData<Int> {
        return Transformations.map(undoStackLiveData) {
            it?.size
        }
    }

    override fun getRedoStackSize(): LiveData<Int> {
        return Transformations.map(redoStackLiveData) {
            it?.size
        }
    }

    override fun clearAll() {
        database.value = null
        undoStackLiveData.value = null
        redoStackLiveData.value = null
    }
}
