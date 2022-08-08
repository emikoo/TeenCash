package com.teenteen.teencash.presentation.ui.bottom_sheets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.teenteen.teencash.R
import com.teenteen.teencash.databinding.BsSettingsBinding
import com.teenteen.teencash.presentation.base.BaseBottomSheetDialogFragment
import com.teenteen.teencash.presentation.extensions.isGone
import com.teenteen.teencash.presentation.extensions.isVisible
import com.teenteen.teencash.presentation.interfaces.UpdateData
import com.teenteen.teencash.presentation.utills.showAlertDialog
import com.teenteen.teencash.view_model.UserProfileViewModel

class BottomSheetSettings(
    private val key: String ,
    private val updater: UpdateData ,
    private val name: String? = null
) : BaseBottomSheetDialogFragment<BsSettingsBinding>() {

    lateinit var viewModel: UserProfileViewModel

    override fun attachBinding(
        list: MutableList<BsSettingsBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(BsSettingsBinding.inflate(layoutInflater , container , attachToRoot))
    }

    override fun setupViews(){
        viewModel = ViewModelProvider(this)[UserProfileViewModel::class.java]
        when (key) {
            CATEGORY_SETTINGS -> binding.categoryContainer.isVisible()
            PIGGY_SETTINGS -> binding.categoryContainer.isVisible()
            PLUS_BTN -> binding.categoryContainer.isGone()
        }
        setupListeners()
    }

    private fun setupListeners(){
        binding.btnEdit.setOnClickListener {

        }

        binding.btnDelete.setOnClickListener {
            if (key == CATEGORY_SETTINGS) {
                showAlertDialog(
                    requireContext() ,
                    this ,
                    titleText = getString(R.string.delete_category) ,
                    subtitleText = getString(R.string.are_u_sure_delete),
                    buttonText = getString(R.string.yes) ,
                    action = this::deleteCategory
                )
            } else if (key == PIGGY_SETTINGS) {
                showAlertDialog(
                    requireContext() ,
                    this ,
                    titleText = getString(R.string.delete_piggy) ,
                    subtitleText = getString(R.string.are_u_sure_delete),
                    buttonText = getString(R.string.yes) ,
                    action = this::deletePiggy
                )
            }
        }
    }

    private fun deleteCategory() {
        viewModel.deleteCategory(prefs.getCurrentUserId() , name!!)
        updater.updateCategory()
        this.dismiss()
    }

    private fun deletePiggy() {
        viewModel.deletePiggy(prefs.getCurrentUserId() , name!!)
        updater.updatePiggyBank()
        this.dismiss()
    }

    companion object {
        const val CATEGORY_SETTINGS = "CATEGORY_SETTINGS"
        const val PIGGY_SETTINGS = "PIGGY_SETTINGS"
        const val PLUS_BTN = "PLUS_BTN"
    }
}