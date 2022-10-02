package com.teenteen.teencash.presentation.ui.fragments.main.settings.achievement

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.antkotlinproject.base.BaseAdapter
import com.example.antkotlinproject.base.BaseViewHolder
import com.teenteen.teencash.R
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.databinding.ItemAchievementBinding
import com.teenteen.teencash.databinding.ItemEmptyBinding
import com.teenteen.teencash.presentation.extensions.toSymbol

class AchievementAdapter(private val dataSet: List<Category>,
                         private val listener: AchievementClickListener
): BaseAdapter() {

    class AchievementViewHolder(binding: ItemAchievementBinding): BaseViewHolder(binding) {
        val title: TextView = binding.title
        val subtitle: TextView = binding.subtitle
        val delete: ImageButton = binding.btnDelete
    }

    class EmptyViewHolder(binding: ItemEmptyBinding) : BaseViewHolder(binding) {
        val image: ImageView = binding.image
    }

    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): BaseViewHolder {
        return if (viewType == VIEW_TYPE_DATA) AchievementViewHolder(ItemAchievementBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
        else EmptyViewHolder(ItemEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder , position: Int) {
        val type = getItemViewType(position)
        if (type == VIEW_TYPE_DATA) setupAchievementViewHolder(holder as AchievementViewHolder , position)
        else setupEmptyViewHolder(holder as EmptyViewHolder)
    }

    override fun getItemCount(): Int {
        return if (dataSet.isEmpty()) 1
        else dataSet.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataSet.isEmpty()) VIEW_TYPE_EMPTY
        else VIEW_TYPE_DATA
    }

    private fun setupAchievementViewHolder(holder: AchievementViewHolder , position: Int) {
        val item = dataSet[position]
        holder.title.text = item.name
        holder.subtitle.text = "${item.secondAmount} ${item.currency.toString().toSymbol()}"
        holder.itemView.setOnClickListener { listener.onClickListener(item) }
        holder.delete.setOnClickListener { listener.deleteAchievement(item) }
    }

    private fun setupEmptyViewHolder(holder: EmptyViewHolder) {
        holder.image.setImageResource(R.drawable.ic_trofy)
    }

    companion object {
        const val VIEW_TYPE_DATA = 1
        const val VIEW_TYPE_EMPTY = 2
    }

    interface AchievementClickListener{
        fun onClickListener(item: Category)
        fun deleteAchievement(item: Category)
    }
}