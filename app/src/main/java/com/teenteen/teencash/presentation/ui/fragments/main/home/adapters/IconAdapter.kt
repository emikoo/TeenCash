package com.teenteen.teencash.presentation.ui.fragments.main.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.example.antkotlinproject.base.BaseAdapter
import com.example.antkotlinproject.base.BaseViewHolder
import com.teenteen.teencash.data.model.CategoryName
import com.teenteen.teencash.databinding.ItemIconBinding

class IconAdapter(val dataSet: MutableList<CategoryName> , private val listener: onIconClickListener) :
    BaseAdapter() {

    class IconListViewHolder(viewBinding: ItemIconBinding) : BaseViewHolder(viewBinding) {
        val icon: ImageView = viewBinding.icon
    }

    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): BaseViewHolder {
        val binding =
            ItemIconBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return IconListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder , position: Int) {
        setupIconListViewHolder(holder as IconListViewHolder , position)
    }

    private fun setupIconListViewHolder(holder: IconListViewHolder , position: Int) {
        val item = dataSet[position]
        holder.icon.setBackgroundResource(item.name.toInt())
        holder.itemView.setOnClickListener {
            listener.onItemClick(item)
        }
    }

    override fun getItemCount(): Int = dataSet.size

    interface onIconClickListener {
        fun onItemClick(item: CategoryName)
    }
}