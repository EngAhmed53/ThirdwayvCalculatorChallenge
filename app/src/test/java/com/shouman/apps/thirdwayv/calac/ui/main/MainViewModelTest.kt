package com.shouman.apps.thirdwayv.calac.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shouman.apps.thirdwayv.calac.data.model.ItemCell
import com.shouman.apps.thirdwayv.calac.data.repository.MainRepository
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class MainViewModelTest {


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Test
    fun test_isUndoAvailableWithData() {
        val expected = true

        val repo = mock(MainRepository::class.java)

        val isUndo: LiveData<Boolean> = MutableLiveData(true)

        repo.addNewCell(ItemCell(System.currentTimeMillis(), '+', 100))

        Mockito.`when`(repo.isUndoAvailable()).thenReturn(isUndo)


        val mainViewModel = MainViewModel(repo)

        val actual = mainViewModel.isUndoAvailable.getOrAwaitValue()


        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun test_isUndoAvailableWithoutData() {
        val expected = true

        val repo = mock(MainRepository::class.java)

        val isUndo: LiveData<Boolean> = MutableLiveData(true)

        val item = ItemCell(System.currentTimeMillis(), '+', 100)
        repo.addNewCell(item)
        repo.removeItem(item)

        Mockito.`when`(repo.isUndoAvailable()).thenReturn(isUndo)


        val mainViewModel = MainViewModel(repo)

        val actual = mainViewModel.isUndoAvailable.getOrAwaitValue()

        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun test_isUndoAvailableAfterUndoAll() {

        val expected = false

        val repo = mock(MainRepository::class.java)

        val isUndo: LiveData<Boolean> = MutableLiveData(false)

        val item = ItemCell(System.currentTimeMillis(), '+', 100)
        repo.addNewCell(item)
        repo.undo()

        Mockito.`when`(repo.isUndoAvailable()).thenReturn(isUndo)

        val mainViewModel = MainViewModel(repo)

        val actual = mainViewModel.isUndoAvailable.getOrAwaitValue()

        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun test_isRedoAvailableAfterUndoWithData() {
        val expected = true

        val repo = mock(MainRepository::class.java)

        val isRedo: LiveData<Boolean> = MutableLiveData(true)

        repo.addNewCell(ItemCell(System.currentTimeMillis(), '+', 100))

        Mockito.`when`(repo.isRedoAvailable()).thenReturn(isRedo)

        val mainViewModel = MainViewModel(repo)

        val actual = mainViewModel.isRedoAvailable.getOrAwaitValue()


        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun test_isRedoAvailableAfterUndoWithRemove() {

        val expected = true

        val repo = mock(MainRepository::class.java)

        val isRedo: LiveData<Boolean> = MutableLiveData(true)

        val item = ItemCell(System.currentTimeMillis(), '+', 100)
        repo.addNewCell(item)
        repo.removeItem(item)
        repo.undo()

        Mockito.`when`(repo.isUndoAvailable()).thenReturn(isRedo)


        val mainViewModel = MainViewModel(repo)

        val actual = mainViewModel.isUndoAvailable.getOrAwaitValue()


        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun test_isRedoAvailableAfterUndoRedo() {

        val expected = false

        val repo = mock(MainRepository::class.java)

        val isRedo: LiveData<Boolean> = MutableLiveData(false)

        Mockito.`when`(repo.isRedoAvailable()).thenReturn(isRedo)

        val mainViewModel = MainViewModel(repo)

        val actual = mainViewModel.isRedoAvailable.getOrAwaitValue()

        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun test_getDataLength() {
        val expected = 3

        val repo = mock(MainRepository::class.java)

        val items: List<ItemCell> = mutableListOf(
            ItemCell(2L, '+', 3),
            ItemCell(3L, '+', 2),
            ItemCell(4L, 'x', 5)
        ).reversed()

        val list: LiveData<List<ItemCell>> = MutableLiveData(items)

        Mockito.`when`(repo.getLatestRecord()).thenReturn(list)

        val mainViewModel = MainViewModel(repo)


        val actual = mainViewModel.data.getOrAwaitValue()

        assertNotNull(actual)
        assertEquals(expected, actual.size)
    }

    @Test
    fun test_getResultWithData() {
        val expected = "25.0"

        val repo = mock(MainRepository::class.java)

        val items: List<ItemCell> = mutableListOf(
            ItemCell(2L, '+', 3),
            ItemCell(3L, '+', 2),
            ItemCell(4L, 'x', 5)
        ).reversed()

        val list: LiveData<List<ItemCell>> = MutableLiveData(items)

        Mockito.`when`(repo.getLatestRecord()).thenReturn(list)

        val mainViewModel = MainViewModel(repo)


        val actual = mainViewModel.result.getOrAwaitValue()

        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun test_getResultWithoutData() {
        val expected = "0.0"

        val repo = mock(MainRepository::class.java)

        val items: List<ItemCell> = mutableListOf()

        val list: LiveData<List<ItemCell>> = MutableLiveData(items)

        Mockito.`when`(repo.getLatestRecord()).thenReturn(list)

        val mainViewModel = MainViewModel(repo)


        val actual = mainViewModel.result.getOrAwaitValue()

        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun test_getResultWithDataDivByZero() {
        val expected = "0.0"

        val repo = mock(MainRepository::class.java)

        val items: List<ItemCell> = mutableListOf(
            ItemCell(2L, '+', 3),
            ItemCell(3L, '+', 2),
            ItemCell(4L, '÷', 0)
        ).reversed()

        val list: LiveData<List<ItemCell>> = MutableLiveData(items)

        Mockito.`when`(repo.getLatestRecord()).thenReturn(list)

        val mainViewModel = MainViewModel(repo)


        val actual = mainViewModel.result.getOrAwaitValue()

        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun test_addNewCellOperandRestored() {

        val repo = mock(MainRepository::class.java)
        val mainViewModel = MainViewModel(repo)
        mainViewModel.addNewCell()

        val actualOperand = mainViewModel.operandLiveData.getOrAwaitValue()

        assertNull(actualOperand)

    }

    @Test
    fun test_addNewCellOperationRestored() {

        val repo = mock(MainRepository::class.java)
        val mainViewModel = MainViewModel(repo)
        mainViewModel.addNewCell()


        val actualOperation = mainViewModel.selectedOperationLiveData.getOrAwaitValue()

        assertNull(actualOperation)
    }

    @Test
    fun removeCell() {
        val repository = mock(MainRepository::class.java)
        val viewModel = MainViewModel(repository)

        val item = ItemCell()

        viewModel.removeCell(item)

        verify(repository).removeItem(item)
    }

    @Test
    fun redo() {
        val repository = mock(MainRepository::class.java)
        val viewModel = MainViewModel(repository)



        viewModel.redo()

        verify(repository).redo()
    }

    @Test
    fun undo() {
        val repository = mock(MainRepository::class.java)
        val viewModel = MainViewModel(repository)



        viewModel.undo()

        verify(repository).undo()
    }

    @Test
    fun selectOperation() {

        val expected = '+'

        val repo = mock(MainRepository::class.java)
        val mainViewModel = MainViewModel(repo)

        mainViewModel.selectOperation('+')

        val actual = mainViewModel.selectedOperationLiveData.getOrAwaitValue()

        assertEquals(expected, actual)
    }
}