package com.shouman.apps.thirdwayv.calac.ui.main

import androidx.lifecycle.*
import com.shouman.apps.thirdwayv.calac.data.model.ItemCell
import com.shouman.apps.thirdwayv.calac.data.repository.IRepository
import com.shouman.apps.thirdwayv.calac.data.repository.MainRepository
import java.util.*

class MainViewModel(private val mainRepository: IRepository) : ViewModel() {


    private val _data = mainRepository.getLatestRecord()
    val data: LiveData<List<ItemCell>>
        get() = _data

    private val _selectedOperationLiveData = MutableLiveData<Char?>()
    val selectedOperationLiveData: LiveData<Char?>
        get() = _selectedOperationLiveData

    val operandLiveData = MutableLiveData<String?>()

    val isUndoAvailable = mainRepository.isUndoAvailable()

    val isRedoAvailable = mainRepository.isRedoAvailable()

    val result = Transformations.map(_data) {
        calculate(it)
    }

    fun addNewCell() {
        mainRepository.addNewCell(
            ItemCell(
                System.currentTimeMillis(),
                _selectedOperationLiveData.value!!,
                operandLiveData.value!!.toInt()
            )
        )
        restoreInputState()
    }

    fun removeCell(item: ItemCell) {
        mainRepository.removeItem(item)
    }

    fun undo() {
        mainRepository.undo()
    }

    fun redo() {
        mainRepository.redo()
    }

    private fun restoreInputState() {
        _selectedOperationLiveData.value = null
        operandLiveData.value = null
    }

    fun selectOperation(operation: Char?) {
        _selectedOperationLiveData.value = operation
    }


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
                    '+' -> result += it.operand
                    '-' -> result -= it.operand
                    'x' -> result *= it.operand
                    '÷' -> {
                        result /= it.operand
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


    override fun onCleared() {
        super.onCleared()
        mainRepository.clearAll()
    }
}

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