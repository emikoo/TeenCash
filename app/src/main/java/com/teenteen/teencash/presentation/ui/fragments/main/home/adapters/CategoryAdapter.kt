package com.teenteen.teencash.presentation.ui.fragments.main.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.antkotlinproject.base.BaseAdapter
import com.example.antkotlinproject.base.BaseViewHolder
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.databinding.ButtonCategoryBinding
import com.teenteen.teencash.databinding.ItemCategoryBinding

class CategoryAdapter(
    private val dataSet: List<Category> ,
    private val buttonListener: AddClickListener
) :
    BaseAdapter() {

    enum class ViewHolderType{
        ITEM, BUTTON
    }

    class CategoryViewHolder(binding: ItemCategoryBinding) : BaseViewHolder(binding) {
        val name: TextView = binding.name
        val limit: TextView = binding.limit
        val icon: ImageView = binding.icon
    }

    class ButtonViewHolder(binding: ButtonCategoryBinding) : BaseViewHolder(binding) {}

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        val bindingButton =
            ButtonCategoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return when (viewType) {
            ViewHolderType.ITEM.ordinal -> CategoryViewHolder(binding)
            ViewHolderType.BUTTON.ordinal -> ButtonViewHolder(bindingButton)
            else -> CategoryViewHolder(binding)
        }
    }

    override fun onBindViewHolder(viewHolder: BaseViewHolder, position: Int) {
        when (viewHolder) {
            is CategoryViewHolder -> setupCategoryViewHolder(
                viewHolder as CategoryViewHolder ,
                position
            )
            is ButtonViewHolder -> setupButtonViewHolder(viewHolder as ButtonViewHolder , position)
            else -> setupCategoryViewHolder(viewHolder as CategoryViewHolder , position)
        }
    }

    private fun setupCategoryViewHolder(viewHolder: CategoryViewHolder , position: Int) {
        val item = dataSet[position]
        viewHolder.name.text = item.name
        viewHolder.limit.text = "0/${item.limit}"
        viewHolder.icon.setBackgroundResource(item.icon.toInt())
    }

    private fun setupButtonViewHolder(viewHolder: ButtonViewHolder , position: Int) {
        viewHolder.itemView.setOnClickListener {
            buttonListener.onAddClickListener(dataSet[position])
        }
    }

//    fun addItem(item: Category) {
//        dataSet.add(dataSet.size - 1, item)
//        notifyDataSetChanged()
////        notifyItemRangeInserted(items.lastIndex, items.count()-1)
//    }
//
//    fun deleteItem(position: Int) {
//        dataSet.removeAt(position)
//        notifyItemRemoved(position)
//        notifyItemRangeChanged(position, itemCount)
//    }
//
//    fun restoreItem(item: Category?, position: Int){
//        item?.let {
//            dataSet.add(position, it)
//            notifyItemRangeChanged(position, itemCount)
//            notifyDataSetChanged()
//        }
//    }

    override fun getItemViewType(position: Int): Int {
        return when (dataSet[position].name) {
            "" -> ViewHolderType.BUTTON.ordinal
            else -> ViewHolderType.ITEM.ordinal
        }
    }

    override fun getItemCount() = dataSet.size

    interface AddClickListener {
        fun onAddClickListener(item: Category)
    }
}
