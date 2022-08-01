package com.example.teencash.ui.bottom_sheet.icon

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.teenteen.teencash.data.model.CategoryName
import com.teenteen.teencash.databinding.BsIconBinding
import com.teenteen.teencash.presentation.base.BaseBottomSheetDialogFragment
import com.teenteen.teencash.presentation.interfaces.PickerItem
import com.teenteen.teencash.presentation.ui.fragments.main.home.adapters.IconAdapter
import com.teenteen.teencash.presentation.utills.IconType.iconArray

class IconListBottomSheet(private val listener: PickerItem) :
    BaseBottomSheetDialogFragment<BsIconBinding>() , IconAdapter.onIconClickListener {
    lateinit var adapter: IconAdapter
    override fun attachBinding(
        list: MutableList<BsIconBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(BsIconBinding.inflate(layoutInflater , container , attachToRoot))
    }

    override fun setupViews() {
        adapter = IconAdapter(iconArray, this)
        binding.ibReturn.setOnClickListener {
            this.dismiss()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter
    }

    override fun onItemClick(item: CategoryName) {
        adapter.dataSet.forEach { it.is_used = false }
        item.is_used = !item.is_used
        adapter.notifyDataSetChanged()
        this.dismiss()
        listener.chosenIcon(item)
    }
}