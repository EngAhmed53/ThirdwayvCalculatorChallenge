package com.shouman.apps.thirdwayv.calac.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.shouman.apps.thirdwayv.calac.data.model.ItemCell
import com.shouman.apps.thirdwayv.calac.ui.main.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainRepositoryTest {

    lateinit var mainRepository: MainRepository

    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        mainRepository = MainRepository()
    }

    @Test
    fun test_addNewCell_firstTime() {

        val expected = 1

        val cell = ItemCell(1L)
        mainRepository.clearAll()
        mainRepository.addNewCell(cell)

        Thread.sleep(100)
        val actual = mainRepository.getLatestRecord().getOrAwaitValue()


        assertNotNull(actual)
        assertEquals(expected, actual.size)
    }


    @Test
    fun test_addNewCell_multiple() {

        val expected = 2

        val cell = ItemCell()
        val cell2 = ItemCell()

        mainRepository.clearAll()
        mainRepository.addNewCell(cell)
        mainRepository.addNewCell(cell2)

        Thread.sleep(100)
        val actual = mainRepository.getLatestRecord().getOrAwaitValue()

        assertNotNull(actual)
        assertEquals(expected, actual.size)
    }


    @Test
    fun test_removeItem_whileItemsFound() {

        val expected = 0

        mainRepository.clearAll()
        val cell = ItemCell(23L)
        mainRepository.addNewCell(cell)
        mainRepository.removeItem(cell)

        Thread.sleep(100)
        val actual = mainRepository.getLatestRecord().getOrAwaitValue()

        assertNotNull(actual)
        assertEquals(expected, actual.size)
    }

    @Test
    fun test_removeItem_whileNoItemsFound() {

        mainRepository.clearAll()
        val cell = ItemCell(22L)
        mainRepository.removeItem(cell)

        val actual = mainRepository.getLatestRecord().getOrAwaitValue()
        assertNull(actual)
    }

    @Test
    fun test_getLatestRecord_oneItem() {

        val cell = ItemCell()

        val expected = listOf(
            cell
        )

        mainRepository.clearAll()
        mainRepository.addNewCell(cell)

        Thread.sleep(100)
        val actual = mainRepository.getLatestRecord().getOrAwaitValue()

        assertNotNull(actual)
        assertEquals(expected, actual)
    }


    @Test
    fun test_getLatestRecord_twoItem() {

        val cell = ItemCell()
        val cell2 = ItemCell()

        val expected = listOf(
            cell2,
            cell
        )

        mainRepository.clearAll()
        mainRepository.addNewCell(cell)
        mainRepository.addNewCell(cell2)

        Thread.sleep(100)
        val actual = mainRepository.getLatestRecord().getOrAwaitValue()

        assertNotNull(actual)
        assertEquals(expected, actual)
    }


    @Test
    fun test_getLatestRecord_moreThenTwo() {

        val cell = ItemCell()
        val cell2 = ItemCell()
        val cell3 = ItemCell()
        val cell4 = ItemCell()
        val cell5 = ItemCell()

        val expected = listOf(
            cell5,
            cell4,
            cell3,
            cell2,
            cell
        )

        mainRepository.clearAll()
        mainRepository.addNewCell(cell)
        mainRepository.addNewCell(cell2)
        mainRepository.addNewCell(cell3)
        mainRepository.addNewCell(cell4)
        mainRepository.addNewCell(cell5)

        Thread.sleep(100)
        val actual = mainRepository.getLatestRecord().getOrAwaitValue()

        assertNotNull(actual)
        assertEquals(expected, actual)
    }


    @Test
    fun test_undo_withDataInserted() {
        val cell = ItemCell()
        val cell2 = ItemCell()
        val cell3 = ItemCell()
        val cell4 = ItemCell()
        val cell5 = ItemCell()

        val expected = listOf(
            cell4,
            cell3,
            cell2,
            cell
        )

        mainRepository.clearAll()
        mainRepository.addNewCell(cell)
        mainRepository.addNewCell(cell2)
        mainRepository.addNewCell(cell3)
        mainRepository.addNewCell(cell4)
        mainRepository.addNewCell(cell5)
        mainRepository.undo()

        Thread.sleep(100)
        val actual = mainRepository.getLatestRecord().getOrAwaitValue()

        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun test_undo_withoutDataInserted() {

        mainRepository.clearAll()
        mainRepository.undo()
        val actual = mainRepository.getLatestRecord().getOrAwaitValue()

        assertNull(actual)
    }


    @Test
    fun test_redo_withDataInserted() {
        val cell = ItemCell(1L)
        val cell2 = ItemCell(2L)
        val cell3 = ItemCell(3L)
        val cell4 = ItemCell(4L)
        val cell5 = ItemCell(5L)

        val expected = listOf(
            cell5,
            cell4,
            cell3,
            cell2,
            cell
        )

        mainRepository.clearAll()
        mainRepository.addNewCell(cell)
        mainRepository.addNewCell(cell2)
        mainRepository.addNewCell(cell3)
        mainRepository.addNewCell(cell4)
        mainRepository.addNewCell(cell5)
        mainRepository.undo()
        mainRepository.redo()


        Thread.sleep(100)
        val actual = mainRepository.getLatestRecord().getOrAwaitValue()

        assertNotNull(actual)
        assertEquals(expected, actual)
    }


    @Test
    fun test_redo_withoutDataInserted() {

        mainRepository.clearAll()
        mainRepository.undo()
        mainRepository.redo()
        val actual = mainRepository.getLatestRecord().getOrAwaitValue()

        assertNull(actual)
    }

    @Test
    fun test_isUndoAvailable_withDataInserted() {
        val expected = true
        val cell = ItemCell()
        val cell2 = ItemCell()
        val cell3 = ItemCell()

        mainRepository.clearAll()
        mainRepository.addNewCell(cell)
        mainRepository.addNewCell(cell2)
        mainRepository.addNewCell(cell3)
        mainRepository.removeItem(cell3)

        Thread.sleep(100)
        val actual = mainRepository.isUndoAvailable().getOrAwaitValue()

        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun test_isUndoAvailable_withDataInsertedAfterUndoAll() {
        val expected = false
        val cell = ItemCell()
        val cell2 = ItemCell()
        val cell3 = ItemCell()

        mainRepository.clearAll()
        mainRepository.addNewCell(cell)
        mainRepository.addNewCell(cell2)
        mainRepository.addNewCell(cell3)
        mainRepository.removeItem(cell3)
        mainRepository.undo()
        mainRepository.undo()
        mainRepository.undo()
        mainRepository.undo()

        val actual = mainRepository.isUndoAvailable().getOrAwaitValue()

        assertNotNull(actual)
        assertEquals(expected, actual)
    }


    @Test
    fun test_isUndoAvailable_withoutDataInserted() {
        val expected = false

        mainRepository.clearAll()

        val actual = mainRepository.isUndoAvailable().getOrAwaitValue()

        assertNotNull(actual)
        assertEquals(expected, actual)
    }


    @Test
    fun test_isRedoAvailable_withDataInsertedAfter() {
        val expected = false
        val cell = ItemCell()
        val cell2 = ItemCell()
        val cell3 = ItemCell()

        mainRepository.clearAll()
        mainRepository.addNewCell(cell)
        mainRepository.addNewCell(cell2)
        mainRepository.addNewCell(cell3)
        mainRepository.removeItem(cell3)
        mainRepository.redo()

        val actual = mainRepository.isRedoAvailable().getOrAwaitValue()

        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun test_isRedoAvailable_withDataInsertedAfterUndo() {
        val expected = true
        val cell = ItemCell()
        val cell2 = ItemCell()
        val cell3 = ItemCell()

        mainRepository.clearAll()
        mainRepository.addNewCell(cell)
        mainRepository.addNewCell(cell2)
        mainRepository.addNewCell(cell3)
        mainRepository.removeItem(cell3)
        mainRepository.undo()
        mainRepository.undo()
        mainRepository.undo()
        mainRepository.undo()

        Thread.sleep(100)
        val actual = mainRepository.isRedoAvailable().getOrAwaitValue()

        assertNotNull(actual)
        assertEquals(expected, actual)
    }


    @Test
    fun test_isRedoAvailable_withoutDataInserted() {
        val expected = false

        mainRepository.clearAll()

        val actual = mainRepository.isRedoAvailable().getOrAwaitValue()

        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun clearAll() {

        mainRepository.clearAll()

        val actual = mainRepository.getLatestRecord().getOrAwaitValue()

        assertNull(actual)
    }

}