package com.shouman.apps.thirdwayv.calac.bindingAdapter

import android.content.res.ColorStateList
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.shouman.apps.thirdwayv.calac.R
import com.shouman.apps.thirdwayv.calac.adapter.CellGridAdapter
import com.shouman.apps.thirdwayv.calac.data.model.ItemCell
import java.util.*

@BindingAdapter("cellsList")
fun RecyclerView.setData(list: LinkedList<ItemCell>?) {
    list?.let {
        val gridAdapter = adapter as CellGridAdapter
        gridAdapter.submitList(list.toList())
    }
}

@BindingAdapter("result")
fun TextView.setResult(result: String?) {
    result?.let {
        text = context.getString(R.string.result, it)
    }
}

@BindingAdapter("setEndIconActive")
fun TextInputLayout.setCheckMarkActive(isTextValid: Boolean) {
    isEndIconVisible = isTextValid
}

@BindingAdapter("cellValue")
fun TextView.setText(itemCell: ItemCell?) {
    itemCell?.let {
        println(itemCell.getValue())
        text = itemCell.getValue()
    }
}

@BindingAdapter("selectedStatus")
fun TextView.setBackground(operation: Char?) {
    if (operation == text[0]) {
        background = ContextCompat.getDrawable(context, R.drawable.button_box_shape_selected)
    } else {

        background = ContextCompat.getDrawable(context, R.drawable.button_box_shape)
    }
}





