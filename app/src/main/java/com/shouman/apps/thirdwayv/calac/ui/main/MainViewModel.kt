package com.shouman.apps.thirdwayv.calac.ui.main

import androidx.lifecycle.*
import com.shouman.apps.thirdwayv.calac.data.model.ItemCell
import com.shouman.apps.thirdwayv.calac.data.repository.IRepository
import com.shouman.apps.thirdwayv.calac.data.repository.MainRepository
import java.util.*

/**
 * This class is the ViewModel class for the <b>MainFragment</b>
 * @param mainRepository - any Repository that implements the IRepository interface
 */
class MainViewModel(private val mainRepository: IRepository) : ViewModel() {

    /**
     * The Data LiveObject used to populate the CellsItem RecyclerView
     */
    private val _data = mainRepository.getLatestRecord()
    val data: LiveData<List<ItemCell>>
        get() = _data

    /**
     * The selectedOperationLiveData LiveObject represent the selected operation (+,-,x,÷)
     */
    private val _selectedOperationLiveData = MutableLiveData<Char?>()
    val selectedOperationLiveData: LiveData<Char?>
        get() = _selectedOperationLiveData

    /**
     * The operandLiveData LiveObject represent the entered operand from the user.
     */
    val operandLiveData = MutableLiveData<String?>()

    /**
     * The isUndoAvailable LiveObject represent if undo operation is available.
     */
    val isUndoAvailable = mainRepository.isUndoAvailable()

    /**
     * The isRedoAvailable LiveObject represent if redo operation is available.
     */
    val isRedoAvailable = mainRepository.isRedoAvailable()

    /**
     * The result LiveObject represent the result of inserted operations cells.
     */
    val result = Transformations.map(_data) {
        calculate(it)
    }

    /**
     * This method make a new cell object and call the <b>MainRepository</b> addNewCell() Method then restore the input state.
     */
    fun addNewCell() {
        mainRepository.addNewCell(
            ItemCell(
                System.currentTimeMillis(),
                _selectedOperationLiveData.value,
                operandLiveData.value?.toInt()
            )
        )
        restoreInputState()
    }

    /**
     * This method takes a cell object and call the <b>MainRepository</b> removeItem() Method.
     * @param item - item to remove
     */
    fun removeCell(item: ItemCell) {
        mainRepository.removeItem(item)
    }

    /**
     * This Method call the <b>MainRepository</b> undo() Method.
     */
    fun undo() {
        mainRepository.undo()
    }

    /**
     * This Method call the <b>MainRepository</b> redo() Method.
     */
    fun redo() {
        mainRepository.redo()
    }

    /**
     * This Method restore the selectedOperationLiveData and operandLiveData state.
     */
    private fun restoreInputState() {
        _selectedOperationLiveData.value = null
        operandLiveData.value = null
    }

    /**
     * This Method take an char of operation from the user and assign it to _selectedOperationLiveData,
     * this method ensure the the user can only select one operation.
     * @param operation - selected operation.
     */
    fun selectOperation(operation: Char?) {
        _selectedOperationLiveData.value = operation
    }


    /**
     * This Method do a calculation on the list of cells items add by the user.
     * @param list - list of cells items.
     * @return a string represent a calculation result.
     */
    private fun calculate(list: List<ItemCell>?): String? {
        if (list.isNullOrEmpty()) {
            return "0.0"
        }
        val reversedList = LinkedList(list)
        reversedList.reverse()
        var result = 0.0

        try {
            reversedList.forEach {
                when (it.operation) {
                    '+' -> result += it.operand!!
                    '-' -> result -= it.operand!!
                    'x' -> result *= it.operand!!
                    '÷' -> {
                        result /= it.operand!!
                        if (result == Double.POSITIVE_INFINITY || result == Double.NEGATIVE_INFINITY) {
                            throw ArithmeticException("divide by zero")
                        }
                    }
                }
            }
        } catch (e: java.lang.ArithmeticException) {
            return 0.0.toString()
        } finally {
            reversedList.clear()
        }
        return result.toString()
    }

}

/**
 * This class is a factory class for the <b>MainViewModel</b> class.
 */
@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val mainRepository: MainRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(mainRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}