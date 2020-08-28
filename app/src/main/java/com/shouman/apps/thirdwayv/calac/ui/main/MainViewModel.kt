package com.shouman.apps.thirdwayv.calac.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shouman.apps.thirdwayv.calac.data.model.ItemCell
import com.shouman.apps.thirdwayv.calac.data.repository.MainRepository
import java.util.*


enum class Operation {
    PLUS,
    SUB,
    MULTI,
    DIV
}

class MainViewModel(val mainRepository: MainRepository) : ViewModel() {

    private val _data = MutableLiveData(mainRepository.getAllCells())
    val data:LiveData<LinkedList<ItemCell>>
    get() = _data

    private val _selectedOperationLiveData = MutableLiveData<Operation?>()
    val selectedOperationLiveData: LiveData<Operation?>
        get() = _selectedOperationLiveData


    fun addNewCell(operand: Int) {
        mainRepository.addNewCell(
            ItemCell(
                System.currentTimeMillis(),
                when (_selectedOperationLiveData.value) {
                    Operation.PLUS -> '+'
                    Operation.SUB -> '-'
                    Operation.DIV -> '÷'
                    Operation.MULTI -> 'x'
                    else -> throw java.lang.IllegalArgumentException("This operation not accepted")
                },
                operand
            )
        )
        _data.value = mainRepository.getAllCells();
    }
}


@Suppress("UNCHECKED_CAST")
class CustomerProfileViewModelFactory(
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