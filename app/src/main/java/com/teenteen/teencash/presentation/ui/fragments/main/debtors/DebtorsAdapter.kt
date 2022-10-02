package com.teenteen.teencash.presentation.ui.fragments.main.debtors

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.antkotlinproject.base.BaseAdapter
import com.example.antkotlinproject.base.BaseViewHolder
import com.teenteen.teencash.R
import com.teenteen.teencash.data.model.Debtor
import com.teenteen.teencash.databinding.ItemDebtorBinding
import com.teenteen.teencash.databinding.ItemEmptyBinding
import com.teenteen.teencash.presentation.extensions.toSymbol
import com.teenteen.teencash.presentation.utills.DebtorAdapterKeys

class DebtorsAdapter(private val dataSet: List<Debtor>, val listener: DebtorClickListener, val key: DebtorAdapterKeys) : BaseAdapter() {

    class DebtorViewHolder(binding: ItemDebtorBinding) : BaseViewHolder(binding) {
        val amount: TextView = binding.amount
        val name: TextView = binding.name
        val delete: ImageButton = binding.ibDelete
        val edit: ImageButton = binding.ibEdit
    }

    class EmptyViewHolder(binding: ItemEmptyBinding) : BaseViewHolder(binding) {
        val image: ImageView = binding.image
    }

    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): BaseViewHolder {
        return if (viewType == VIEW_TYPE_DATA) DebtorViewHolder(
            ItemDebtorBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
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
        if (type == VIEW_TYPE_DATA) setupDebtorViewHolder(holder as DebtorViewHolder, position)
        else setupEmptyViewHolder(holder as EmptyViewHolder)
    }

    private fun setupDebtorViewHolder(holder: DebtorViewHolder , position: Int) {
        val item = dataSet[position]
        holder.amount.text = "${item.amount} ${item.currency!!.toSymbol()}"
        holder.name.text = item.name
        holder.delete.setOnClickListener { listener.deleteDebtor(item, key) }
        holder.edit.setOnClickListener { listener.editDebtor(item, key) }
    }

    private fun setupEmptyViewHolder(holder: EmptyViewHolder) {
        if (key == DebtorAdapterKeys.MOTHERFUCKER) holder.image.setBackgroundResource(R.drawable.ic_morty)
        else holder.image.setBackgroundResource(R.drawable.ic_rick)
    }

    companion object {
        val VIEW_TYPE_DATA = 1
        val VIEW_TYPE_EMPTY = 2
    }

    interface DebtorClickListener {
        fun deleteDebtor(item: Debtor, key: DebtorAdapterKeys)
        fun editDebtor(item: Debtor, key: DebtorAdapterKeys)
    }
}