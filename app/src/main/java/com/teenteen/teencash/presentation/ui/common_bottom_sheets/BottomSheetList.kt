package com.teenteen.teencash.presentation.ui.common_bottom_sheets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.teenteen.teencash.R
import com.teenteen.teencash.data.model.ListBS
import com.teenteen.teencash.databinding.BsListBinding
import com.teenteen.teencash.presentation.base.BaseBottomSheetDialogFragment
import com.teenteen.teencash.presentation.extensions.isGone
import com.teenteen.teencash.presentation.extensions.isVisible
import com.teenteen.teencash.presentation.interfaces.UpdateData
import com.teenteen.teencash.presentation.utills.ListBottomSheetKeys
import com.teenteen.teencash.presentation.utills.showAlertDialog
import com.teenteen.teencash.view_model.UserProfileViewModel

class BottomSheetList(
    private val key: ListBottomSheetKeys ,
    private val updater: UpdateData ,
    private val name: String? = null
) : BaseBottomSheetDialogFragment<BsListBinding>(), AddBSAdapter.onListClickedListener {

    lateinit var viewModel: UserProfileViewModel
    lateinit var adapter: AddBSAdapter
    lateinit var list: MutableList<ListBS>

    override fun attachBinding(
        list: MutableList<BsListBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(BsListBinding.inflate(layoutInflater , container , attachToRoot))
    }

    override fun setupViews() {
        viewModel = ViewModelProvider(this)[UserProfileViewModel::class.java]
        when (key) {
            ListBottomSheetKeys.CHANGE_CURRENCY -> {
                binding.title.isVisible()
            }
            ListBottomSheetKeys.CHANGE_LANGUAGE -> {
                binding.title.isVisible()
            }
            ListBottomSheetKeys.PLUS -> {
                binding.title.isGone()
            }
            ListBottomSheetKeys.CATEGORY_SETTINGS -> {
                setupSettingsView(ListBottomSheetKeys.CATEGORY_SETTINGS)
            }
            ListBottomSheetKeys.PIGGY_BANK_SETTINGS -> {
                setupSettingsView(ListBottomSheetKeys.PIGGY_BANK_SETTINGS)
            }
        }
    }

    private fun setupSettingsView(key: ListBottomSheetKeys) {
        list = mutableListOf(
            ListBS(R.drawable.ic_edit, R.string.edit),
            ListBS(R.drawable.ic_delete, R.string.delete))
        setupRecyclerView(list, key)
        binding.title.isGone()
    }

    private fun setupRecyclerView(array: MutableList<ListBS>, key: ListBottomSheetKeys) {
        adapter = AddBSAdapter(array, this, key)
        binding.recyclerView.adapter = adapter
    }

    override fun onDeleteCategory() {
        showAlertDialog(
            requireContext() ,
            this ,
            titleText = getString(R.string.delete_category) ,
            subtitleText = getString(R.string.are_u_sure_delete) ,
            buttonText = getString(R.string.yes) ,
            action = this::deleteCategory
        )
    }

    override fun onDeletePiggyBank() {
        showAlertDialog(
            requireContext() ,
            this ,
            titleText = getString(R.string.delete_piggy) ,
            subtitleText = getString(R.string.are_u_sure_delete) ,
            buttonText = getString(R.string.yes) ,
            action = this::deletePiggy
        )
    }

    override fun onEditCategory() {}

    override fun onEditPiggyBank() {}

    private fun deleteCategory() {
        viewModel.deleteCategory(prefs.getCurrentUserId() , name !!)
        updater.updateCategory()
        this.dismiss()
    }

    private fun deletePiggy() {
        viewModel.deletePiggy(prefs.getCurrentUserId() , name !!)
        updater.updatePiggyBank()
        this.dismiss()
    }
}