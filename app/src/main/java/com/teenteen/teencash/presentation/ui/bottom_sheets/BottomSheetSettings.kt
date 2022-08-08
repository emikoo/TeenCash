package com.teenteen.teencash.presentation.ui.bottom_sheets

import android.view.LayoutInflater
import android.view.ViewGroup
import com.teenteen.teencash.databinding.BsSettingsBinding
import com.teenteen.teencash.presentation.base.BaseBottomSheetDialogFragment
import com.teenteen.teencash.presentation.extensions.isGone
import com.teenteen.teencash.presentation.extensions.isVisible

class BottomSheetSettings(private val key: String): BaseBottomSheetDialogFragment<BsSettingsBinding>() {

    override fun attachBinding(
        list: MutableList<BsSettingsBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(BsSettingsBinding.inflate(layoutInflater, container, attachToRoot))
    }

    override fun setupViews(){
        when (key) {
            CATEGORY_SETTINGS -> binding.categoryContainer.isVisible()
            PLUS_BTN -> binding.categoryContainer.isGone()
        }
        setupListeners()
    }

    private fun setupListeners(){
        binding.btnEdit.setOnClickListener {

        }

        binding.btnDelete.setOnClickListener {

        }
    }

    companion object{
        const val CATEGORY_SETTINGS = "CATEGORY_SETTINGS"
        const val PLUS_BTN = "PLUS_BTN"
    }
}