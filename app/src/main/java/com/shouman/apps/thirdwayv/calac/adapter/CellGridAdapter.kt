package com.shouman.apps.thirdwayv.calac.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shouman.apps.thirdwayv.calac.data.model.ItemCell
import com.shouman.apps.thirdwayv.calac.databinding.CellListItemBinding

class CellGridAdapter(private val onCellClickListener: OnCellClickListener) :
    ListAdapter<ItemCell, CellGridAdapter.CellViewHolder>(
        DiffCallback
    ) {

    companion object DiffCallback :
        DiffUtil.ItemCallback<ItemCell>() {
        override fun areItemsTheSame(
            oldItem: ItemCell,
            newItem: ItemCell
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: ItemCell,
            newItem: ItemCell
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class CellViewHolder(
        private val mBinding: CellListItemBinding
    ) : RecyclerView.ViewHolder(mBinding.root) {
        fun bind(item: ItemCell) {
            mBinding.item = item
            mBinding.root.setOnClickListener {
            }
            mBinding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        return CellViewHolder(
            CellListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onCellClickListener.onClick(item)
        }
    }

    class OnCellClickListener(val clickListener: (itemCell: ItemCell) -> Unit) {
        fun onClick(item: ItemCell) = clickListener(item)
    }
}