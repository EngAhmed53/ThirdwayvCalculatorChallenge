package com.shouman.apps.thirdwayv.calac.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shouman.apps.thirdwayv.calac.data.model.ItemCell
import com.shouman.apps.thirdwayv.calac.databinding.CellListItemBinding


/**
 * This is the Operation Cells RecyclerView Adapter class, this adapter extend <b>ListAdapter</b>
 * and take a <b>DiffUtil.ItemCallback</b> as an Parameter to calculates the difference between two lists
 * and outputs a list of update operations.
 */
class CellGridAdapter(private val onCellClickListener: OnCellClickListener) :
    ListAdapter<ItemCell, CellGridAdapter.CellViewHolder>(
        DiffCallback
    ) {

    /**
     * This is the <b>DiffUtil</b> class used in calculating the difference between two lists
     */
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

    /**
     * A ViewHolderClass for caching potentially expensive <b>View.findViewById(int)</b> results.
     */
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

    /**
     * onCreateViewHolder only creates a new view holder when there are no existing view holders
     * which the <b>RecyclerView</b> can reuse.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        return CellViewHolder(
            CellListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**
     * This Method fill the viewHolder with data and attach a <b>ClickListener</b> to it
     */
    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onCellClickListener.onClick(item)
        }
    }

    /**
     * This InnerClass act as a <b>ClickListener</b> for viewHolderItem,
     * @param clickListener - lambda action take an ItemCell and return a Unit
     */
    class OnCellClickListener(val clickListener: (itemCell: ItemCell) -> Unit) {
        fun onClick(item: ItemCell) = clickListener(item)
    }
}