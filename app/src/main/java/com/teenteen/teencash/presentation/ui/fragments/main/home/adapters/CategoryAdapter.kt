package com.teenteen.teencash.presentation.ui.fragments.main.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.antkotlinproject.base.BaseAdapter
import com.example.antkotlinproject.base.BaseViewHolder
import com.teenteen.teencash.R
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.databinding.ItemButtonBinding
import com.teenteen.teencash.databinding.ItemCategoryBinding
import com.teenteen.teencash.presentation.ui.fragments.main.home.HomeFragment
import com.teenteen.teencash.presentation.utills.IconType.getProjectIconType

class CategoryAdapter(
    private val dataSet: List<Category> ,
    private val buttonListener: CategoryClickListener ,
    private val key: String
) :
    BaseAdapter() {

    enum class ViewHolderType {
        ITEM , BUTTON
    }

    class CategoryViewHolder(binding: ItemCategoryBinding) : BaseViewHolder(binding) {
        val name: TextView = binding.name
        val limit: TextView = binding.limit
        val icon: ImageView = binding.icon
        val dots: ImageButton = binding.btnDots
    }

    class ButtonViewHolder(binding: ItemButtonBinding) : BaseViewHolder(binding) { val plus: View = binding.plus}

    override fun onCreateViewHolder(viewGroup: ViewGroup , viewType: Int): BaseViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(viewGroup.context) , viewGroup , false)
        val bindingButton =
            ItemButtonBinding.inflate(
                LayoutInflater.from(viewGroup.context) ,
                viewGroup ,
                false
            )
        return when (viewType) {
            ViewHolderType.ITEM.ordinal -> CategoryViewHolder(binding)
            ViewHolderType.BUTTON.ordinal -> ButtonViewHolder(bindingButton)
            else -> CategoryViewHolder(binding)
        }
    }

    override fun onBindViewHolder(viewHolder: BaseViewHolder , position: Int) {
        when (viewHolder) {
            is CategoryViewHolder -> setupCategoryViewHolder(
                viewHolder ,
                position
            )
            is ButtonViewHolder -> setupButtonViewHolder(viewHolder , position)
            else -> setupCategoryViewHolder(viewHolder as CategoryViewHolder , position)
        }
    }

    private fun setupCategoryViewHolder(viewHolder: CategoryViewHolder , position: Int) {
        val item = dataSet[position]
        viewHolder.name.text = item.name
        viewHolder.limit.text = "0/${item.secondAmount}"
        viewHolder.icon.setBackgroundResource(getProjectIconType(item.iconId))
        if (key == HomeFragment.CATEGORY_KEY) {
            viewHolder.itemView.setBackgroundResource(R.drawable.bg_category_blue)
            viewHolder.name.setTextColor(viewHolder.itemView.resources.getColor(R.color.blue031952))
            viewHolder.limit.setTextColor(viewHolder.itemView.resources.getColor(R.color.blue031952))
            viewHolder.dots.setOnClickListener {
                buttonListener.onCategoryDotsClickListener(item)
            }
            viewHolder.itemView.setOnLongClickListener {
                buttonListener.onCategoryDotsClickListener(item)
                true
            }
        } else if (key == HomeFragment.PIGGY_BANK_KEY) {
            viewHolder.itemView.setBackgroundResource(R.drawable.bg_category_orange)
            viewHolder.name.setTextColor(viewHolder.itemView.resources.getColor(R.color.dark_brown))
            viewHolder.limit.setTextColor(viewHolder.itemView.resources.getColor(R.color.brown))
            viewHolder.dots.setOnClickListener {
                buttonListener.onPiggyDotsClickListener(item)
            }
            viewHolder.itemView.setOnLongClickListener {
                buttonListener.onPiggyDotsClickListener(item)
                true
            }
        }
    }

    private fun setupButtonViewHolder(viewHolder: ButtonViewHolder , position: Int) {
        if (key == HomeFragment.CATEGORY_KEY) {
            viewHolder.itemView.setBackgroundResource(R.drawable.bg_category_blue)
            viewHolder.plus.setBackgroundResource(R.drawable.ic_add_blue)
            viewHolder.itemView.setOnClickListener {
                buttonListener.onAddCategoryClickListener(dataSet[position])
            }
        } else if (key == HomeFragment.PIGGY_BANK_KEY) {
            viewHolder.itemView.setBackgroundResource(R.drawable.bg_category_orange)
            viewHolder.plus.setBackgroundResource(R.drawable.ic_add_brown)
            viewHolder.itemView.setOnClickListener {
                buttonListener.onAddPiggyClickListener(dataSet[position])
            }
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

    interface CategoryClickListener {
        fun onAddCategoryClickListener(item: Category)
        fun onAddPiggyClickListener(item: Category)
        fun onCategoryDotsClickListener(item: Category)
        fun onPiggyDotsClickListener(item: Category)
    }
}
