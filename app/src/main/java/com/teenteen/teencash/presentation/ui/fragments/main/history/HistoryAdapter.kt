package com.teenteen.teencash.presentation.ui.fragments.main.history

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.antkotlinproject.base.BaseAdapter
import com.example.antkotlinproject.base.BaseViewHolder
import com.teenteen.teencash.R
import com.teenteen.teencash.data.model.History
import com.teenteen.teencash.databinding.ItemEmptyBinding
import com.teenteen.teencash.databinding.ItemHistoryBinding
import com.teenteen.teencash.presentation.utills.IconType.getProjectIconType

class HistoryAdapter(private val dataSet: List<History>) : BaseAdapter() {

    class HistoryViewHolder(binding: ItemHistoryBinding) : BaseViewHolder(binding) {
        val amount: TextView = binding.amount
        val name: TextView = binding.name
        val date: TextView = binding.date
        val image: ImageView = binding.image
    }

    class EmptyViewHolder(binding: ItemEmptyBinding) : BaseViewHolder(binding) {
        val image: ImageView = binding.image
    }

    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): BaseViewHolder {
        return if (viewType == VIEW_TYPE_DATA) HistoryViewHolder(
            ItemHistoryBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        ) else EmptyViewHolder(
            ItemEmptyBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        )
    }

    override fun getItemCount(): Int {
        return if (dataSet.isEmpty()) 1
        else dataSet.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataSet.isEmpty()) VIEW_TYPE_EMPTY
        else VIEW_TYPE_DATA
    }

    override fun onBindViewHolder(holder: BaseViewHolder , position: Int) {
        val type = getItemViewType(position)
        if (type == VIEW_TYPE_DATA) setupHistoryViewHolder(holder as HistoryViewHolder , position)
        else setupEmptyViewHolder(holder as EmptyViewHolder)
    }

    private fun setupHistoryViewHolder(holder: HistoryViewHolder , position: Int) {
        val item = dataSet[position]
        if (item.isSpent) {
            holder.amount.text = "- ${item.amount} kgs"
            holder.amount.setTextColor(holder.itemView.resources.getColor(R.color.red))
        } else {
            holder.amount.text = "+ ${item.amount} kgs"
            holder.amount.setTextColor(holder.itemView.resources.getColor(R.color.green))
        }
        holder.name.text = item.name
        holder.image.setImageResource(getProjectIconType(item.image))
    }

    private fun setupEmptyViewHolder(holder: EmptyViewHolder) {
        holder.image.setImageResource(R.drawable.ic_debtors)
    }

    companion object {
        const val VIEW_TYPE_DATA = 1
        const val VIEW_TYPE_EMPTY = 2
    }
}