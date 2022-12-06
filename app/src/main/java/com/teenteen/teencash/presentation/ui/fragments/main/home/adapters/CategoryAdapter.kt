package com.teenteen.teencash.presentation.ui.fragments.main.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginTop
import com.example.antkotlinproject.base.BaseAdapter
import com.example.antkotlinproject.base.BaseViewHolder
import com.teenteen.teencash.R
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.databinding.ItemButtonCategoryBinding
import com.teenteen.teencash.databinding.ItemCategoryBinding
import com.teenteen.teencash.presentation.extensions.isGone
import com.teenteen.teencash.presentation.extensions.toSymbol
import com.teenteen.teencash.presentation.utills.CategoryAdapterKeys
import com.teenteen.teencash.presentation.utills.IconType.getProjectIconType

class CategoryAdapter(
    private val dataSet: List<Category> ,
    private val buttonListener: CategoryClickListener ,
    private val key: CategoryAdapterKeys
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

    class ButtonViewHolder(binding: ItemButtonCategoryBinding) : BaseViewHolder(binding) { val plus: View = binding.plus}

    override fun onCreateViewHolder(viewGroup: ViewGroup , viewType: Int): BaseViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(viewGroup.context) , viewGroup , false)
        val bindingButton =
            ItemButtonCategoryBinding.inflate(
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
        viewHolder.icon.setImageResource(getProjectIconType(item.iconId))
        when (key) {
            CategoryAdapterKeys.CATEGORY -> {
                viewHolder.limit.text = "${item.firstAmount}/${item.secondAmount} ${item.currency.toString().toSymbol()}"
                setupViewsByKey(viewHolder, R.drawable.bg_category_blue, R.color.blue031952, item, R.color.grey58)
                viewHolder.dots.setOnClickListener { buttonListener.onCategoryDotsClickListener(item) }
                viewHolder.itemView.setOnLongClickListener {
                    buttonListener.onCategoryDotsClickListener(item)
                    true
                }
                viewHolder.itemView.setOnClickListener { buttonListener.onCategoryClickListener(item) }
            }
            CategoryAdapterKeys.PIGGY_BANK -> {
                viewHolder.limit.text = "${item.firstAmount}/${item.secondAmount} ${item.currency.toString().toSymbol()}"
                setupViewsByKey(viewHolder, R.drawable.bg_category_orange, R.color.dark_brown, item, R.color.brown)
                viewHolder.dots.setOnClickListener { buttonListener.onPiggyDotsClickListener(item) }
                viewHolder.itemView.setOnLongClickListener {
                    buttonListener.onPiggyDotsClickListener(item)
                    true
                }
                viewHolder.itemView.setOnClickListener { buttonListener.onPiggyClickListener(item) }
            }
            CategoryAdapterKeys.EARNINGS -> {
                viewHolder.limit.text = "${item.firstAmount} ${item.currency.toString().toSymbol()}"
                setupViewsByKey(viewHolder, R.drawable.bg_category_green, R.color.dark_green, item, R.color.dark_green)
                viewHolder.dots.setOnClickListener { buttonListener.onEarningDotsClickListener(item) }
                viewHolder.itemView.setOnLongClickListener {
                    buttonListener.onEarningDotsClickListener(item)
                    true
                }
                viewHolder.itemView.setOnClickListener { buttonListener.onEarningClickListener(item) }
            }
        }
    }

    private fun setupButtonViewHolder(viewHolder: ButtonViewHolder , position: Int) {
        when (key) {
            CategoryAdapterKeys.CATEGORY -> {
                setupButtonViewsByKey(viewHolder, R.drawable.bg_category_blue, R.drawable.ic_add_blue)
                viewHolder.itemView.setOnClickListener {
                    buttonListener.onAddCategoryClickListener(dataSet[position])
                }
            }
            CategoryAdapterKeys.EARNINGS -> {
                setupButtonViewsByKey(viewHolder, R.drawable.bg_category_green, R.drawable.ic_add_green)
                viewHolder.itemView.setOnClickListener {
                    buttonListener.onAddIncomeClickListener(dataSet[position])
                }
            }
            CategoryAdapterKeys.PIGGY_BANK -> {
                setupButtonViewsByKey(viewHolder, R.drawable.bg_category_orange, R.drawable.ic_add_brown)
                viewHolder.itemView.setOnClickListener {
                    buttonListener.onAddPiggyClickListener(dataSet[position])
                }
            }
        }
    }

    private fun setupViewsByKey(viewHolder: CategoryViewHolder, background: Int, textColor: Int, item: Category, limitColor: Int) {
        viewHolder.itemView.setBackgroundResource(background)
        viewHolder.name.setTextColor(viewHolder.itemView.resources.getColor(textColor))
        if (item.firstAmount >= item.secondAmount && key != CategoryAdapterKeys.EARNINGS) {
            viewHolder.limit.setTextColor(viewHolder.itemView.resources.getColor(R.color.red))
        } else if (key == CategoryAdapterKeys.EARNINGS) {
            viewHolder.limit.isGone()
            viewHolder.icon.setColorFilter(viewHolder.itemView.resources.getColor(R.color.dark_green))
        } else {
            viewHolder.limit.setTextColor(viewHolder.itemView.resources.getColor(limitColor))
        }
    }

    private fun setupButtonViewsByKey(viewHolder: ButtonViewHolder, background: Int, button: Int) {
        viewHolder.itemView.setBackgroundResource(background)
        viewHolder.plus.setBackgroundResource(button)
    }

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
        fun onAddIncomeClickListener(item: Category)
        fun onCategoryDotsClickListener(item: Category)
        fun onPiggyDotsClickListener(item: Category)
        fun onCategoryClickListener(item: Category)
        fun onPiggyClickListener(item: Category)
        fun onEarningClickListener(item: Category)
        fun onEarningDotsClickListener(item: Category)
    }
}
