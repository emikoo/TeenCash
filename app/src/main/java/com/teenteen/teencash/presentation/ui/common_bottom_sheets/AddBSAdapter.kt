package com.teenteen.teencash.presentation.ui.common_bottom_sheets

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import com.example.antkotlinproject.base.BaseAdapter
import com.example.antkotlinproject.base.BaseViewHolder
import com.teenteen.teencash.data.model.ListBS
import com.teenteen.teencash.databinding.ItemBsListBinding
import com.teenteen.teencash.presentation.extensions.isGone
import com.teenteen.teencash.presentation.extensions.isVisible
import com.teenteen.teencash.presentation.utills.ListBottomSheetKeys

class AddBSAdapter(
    private val dataSet: List<ListBS>,
    private val listener: onListClickedListener,
    private val key: ListBottomSheetKeys
) : BaseAdapter() {

    class AddBSViewHolder(binding: ItemBsListBinding): BaseViewHolder(binding) {
        val image: ImageView = binding.imageView
        val btn: Button = binding.button
        val radio: RadioButton = binding.radio
    }

    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): BaseViewHolder {
        val binding =
            ItemBsListBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return AddBSViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder , position: Int) {
        setupAddBSViewHolder(holder as AddBSViewHolder, position)
    }

    private fun setupAddBSViewHolder(holder: AddBSViewHolder , position: Int) {
        val item = dataSet[position]
        holder.image.setBackgroundResource(item.image)
        holder.btn.setText(item.btnText)
        when(key) {
            ListBottomSheetKeys.CATEGORY_SETTINGS -> {
                holder.btn.setOnClickListener{
                    if (position == 0) listener.onEditCategory()
                    else listener.onDeleteCategory()
                }
                holder.radio.isGone()
            }
            ListBottomSheetKeys.PIGGY_BANK_SETTINGS -> {
                holder.btn.setOnClickListener{
                    if (position == 0) listener.onEditPiggyBank()
                    else listener.onDeletePiggyBank()
                }
                holder.radio.isGone()
            }
            ListBottomSheetKeys.PLUS -> {
                holder.radio.isGone()
            }
            ListBottomSheetKeys.CHANGE_LANGUAGE -> {
                holder.radio.isVisible()
            }
            ListBottomSheetKeys.CHANGE_CURRENCY -> {
                holder.radio.isVisible()
            }
        }
    }

    override fun getItemCount(): Int = dataSet.size

    interface onListClickedListener{
        fun onDeleteCategory()
        fun onDeletePiggyBank()
        fun onEditCategory()
        fun onEditPiggyBank()
    }
}