package com.shouman.apps.thirdwayv.calac.bindingAdapter

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.shouman.apps.thirdwayv.calac.R
import com.shouman.apps.thirdwayv.calac.adapter.CellGridAdapter
import com.shouman.apps.thirdwayv.calac.data.model.ItemCell


/**
 * This Method is a RecyclerView BindingAdapter that fill the recycler view adapter with data
 * @param list - a List of <b>ItemCell</b>
 */
@BindingAdapter("cellsList")
fun RecyclerView.setData(list: List<ItemCell>?) {
    list?.let {
        val gridAdapter = adapter as CellGridAdapter
        gridAdapter.submitList(list)
    }
}

/**
 * This Method is a TextView BindingAdapter that fill the text with result data
 * @param result - a result String
 */
@BindingAdapter("result")
fun TextView.setResult(result: String?) {
    result?.let {
        text = context.getString(R.string.result, it)
    }
}

/**
 * This Method is a TextInputLayout BindingAdapter that add or remove
 * the endIcon from the TextInputLayout
 * @param isTextValid - a boolean determine the status of the inserted text
 */
@BindingAdapter("setEndIconActive")
fun TextInputLayout.setCheckMarkActive(isTextValid: Boolean) {
    isEndIconVisible = isTextValid
}

/**
 * This Method is a TextView BindingAdapter that set the text with cell value
 * @param itemCell - the cell connected with this textView
 */
@BindingAdapter("cellValue")
fun TextView.setText(itemCell: ItemCell?) {
    itemCell?.let {
        println(itemCell.getValue())
        text = itemCell.getValue()
    }
}

/**
 * This Method is a TextView BindingAdapter that set the textView background if and only if the selected
 * operation match the text on the textView
 * @param operation - the selected math operation
 */
@BindingAdapter("selectedStatus")
fun TextView.setBackground(operation: Char?) {
    background = if (operation == text[0]) {
        ContextCompat.getDrawable(context, R.drawable.button_box_shape_selected)
    } else {

        ContextCompat.getDrawable(context, R.drawable.button_box_shape)
    }
}





