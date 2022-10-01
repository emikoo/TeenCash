package com.teenteen.teencash.presentation.ui.common_bottom_sheets

import androidx.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.teenteen.teencash.R
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.data.model.ListBS
import com.teenteen.teencash.databinding.BsListBinding
import com.teenteen.teencash.presentation.base.BaseBottomSheetDialogFragment
import com.teenteen.teencash.presentation.extensions.isGone
import com.teenteen.teencash.presentation.extensions.isVisible
import com.teenteen.teencash.presentation.extensions.show
import com.teenteen.teencash.presentation.extensions.updateLanguage
import com.teenteen.teencash.presentation.interfaces.UpdateData
import com.teenteen.teencash.presentation.interfaces.UpdateLanguage
import com.teenteen.teencash.presentation.utills.AddBottomSheetKeys
import com.teenteen.teencash.presentation.utills.ListBottomSheetKeys
import com.teenteen.teencash.presentation.utills.showAlertDialog
import com.teenteen.teencash.view_model.MainViewModel

class BottomSheetList(
    private val key: ListBottomSheetKeys ,
    private val updater: UpdateData? = null ,
    private val itemCategory: Category? = null,
    private val updateLang: UpdateLanguage? = null
) : BaseBottomSheetDialogFragment<BsListBinding>(), AddBSAdapter.onListClickedListener {

    lateinit var viewModel: MainViewModel
    lateinit var adapter: AddBSAdapter
    lateinit var list: MutableList<ListBS>
    var currentBalance = 0
    var spentToday = 0
    var savedAmount = 0

    override fun attachBinding(
        list: MutableList<BsListBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(BsListBinding.inflate(layoutInflater , container , attachToRoot))
    }

    override fun setupViews() {
        viewModel = MainViewModel()
        subscribeTolIveData()
        when (key) {
            ListBottomSheetKeys.CHANGE_CURRENCY -> {
                binding.title.isVisible()
            }
            ListBottomSheetKeys.CHANGE_LANGUAGE -> {
                binding.title.isVisible()
                binding.title.text = getString(R.string.select_lang)
                list = mutableListOf(
                    ListBS(title = getString(R.string.kyrgyz), subtitle = getString(R.string.kyrgyz_or)),
                    ListBS(title = getString(R.string.russian), subtitle = getString(R.string.russian_or)),
                    ListBS(title = getString(R.string.english), subtitle = getString(R.string.english_or))
                )
                setupSettingsView(list, ListBottomSheetKeys.CHANGE_LANGUAGE)
            }
            ListBottomSheetKeys.CATEGORY_SETTINGS -> {
                binding.title.isGone()
                list = mutableListOf(
                    ListBS(image = R.drawable.ic_edit_rectangle, title = getString(R.string.edit)),
                    ListBS(image = R.drawable.ic_delete, title = getString(R.string.delete)))
                setupSettingsView(list, ListBottomSheetKeys.CATEGORY_SETTINGS)
            }
            ListBottomSheetKeys.PIGGY_BANK_SETTINGS -> {
                binding.title.isGone()
                list = mutableListOf(
                    ListBS(image = R.drawable.ic_tick, title = getString(R.string.achieved)),
                    ListBS(image = R.drawable.ic_edit_rectangle, title = getString(R.string.edit)),
                    ListBS(image = R.drawable.ic_delete, title = getString(R.string.delete)))
                setupSettingsView(list, ListBottomSheetKeys.PIGGY_BANK_SETTINGS)
            }
        }
    }

    private fun setupSettingsView(array: MutableList<ListBS>, key: ListBottomSheetKeys) {
        setupRecyclerView(array, key)
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

    override fun onEditCategory() {
        BottomSheetAdd(updater!!, AddBottomSheetKeys.UPDATE_CATEGORY, itemCategory = itemCategory)
            .show(activity?.supportFragmentManager)
        dialog?.dismiss()
    }

    override fun onEditPiggyBank() {
        BottomSheetAdd(updater!!, AddBottomSheetKeys.UPDATE_PIGGY, itemCategory = itemCategory)
            .show(activity?.supportFragmentManager)
        dialog?.dismiss()
    }

    override fun onAchieved() {
        itemCategory?.let {
            val newSavedAmount = savedAmount - itemCategory.firstAmount
            viewModel.deletePiggy(prefs.getCurrentUserId() , itemCategory.docName)
            viewModel.updateSavedAmount(prefs.getCurrentUserId(), newSavedAmount)
            viewModel.createAchievement(prefs.getCurrentUserId(), "${itemCategory.name}${itemCategory.secondAmount}", itemCategory)
            updater!!.achieved()
            updater.updatePiggyBank()
            this.dismiss()
        }
    }

    override fun onLanguageSelected(item: ListBS) {
        when (item.subtitle) {
            getString(R.string.kyrgyz_or) -> {
                PreferenceManager(requireContext()).updateLanguage("ky", requireContext(), prefs)
            }
            getString(R.string.russian_or) -> {
                PreferenceManager(requireContext()).updateLanguage("ru", requireContext(), prefs)
            }
            getString(R.string.english_or) -> {
                PreferenceManager(requireContext()).updateLanguage("eng", requireContext(), prefs)
            }
        }
        updateLang?.updateLanguage()
        dismiss()
    }



    private fun deleteCategory() {
        itemCategory?.let {
            val newBalance = currentBalance + it.firstAmount
            val newSpentAmount = spentToday - it.firstAmount
            viewModel.deleteCategory(prefs.getCurrentUserId() , it.docName)
            viewModel.updateSpentAmount(prefs.getCurrentUserId(), newSpentAmount)
            viewModel.updateBalance(prefs.getCurrentUserId(), newBalance)
            updater!!.updateCategory()
            this.dismiss()
        }
    }

    private fun deletePiggy() {
        itemCategory?.let {
            val newBalance = currentBalance + it.firstAmount
            val newSavedAmount = savedAmount - it.firstAmount
            viewModel.deletePiggy(prefs.getCurrentUserId() , it.docName)
            viewModel.updateSavedAmount(prefs.getCurrentUserId(), newSavedAmount)
            viewModel.updateBalance(prefs.getCurrentUserId(), newBalance)
            updater!!.updatePiggyBank()
            this.dismiss()
        }
    }

    private fun subscribeTolIveData() {
        viewModel.getBalance(prefs.getCurrentUserId())
        viewModel.balance.observe(viewLifecycleOwner, Observer { currentBalance = it })
        viewModel.getSpentAmount(prefs.getCurrentUserId())
        viewModel.spentAmount.observe(viewLifecycleOwner, Observer { spentToday = it })
        viewModel.getSavedAmount(prefs.getCurrentUserId())
        viewModel.saved.observe(viewLifecycleOwner, Observer { savedAmount = it })
    }
}