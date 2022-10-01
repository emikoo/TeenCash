package com.teenteen.teencash.presentation.ui.fragments.main.settings.bottom_sheets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.teenteen.teencash.R
import com.teenteen.teencash.data.model.Category
import com.teenteen.teencash.databinding.BsAchievementBinding
import com.teenteen.teencash.presentation.base.BaseBottomSheetDialogFragment
import com.teenteen.teencash.presentation.utills.showAlertDialog
import com.teenteen.teencash.view_model.MainViewModel

class AchievementBottomSheet(val item: Category) :
    BaseBottomSheetDialogFragment<BsAchievementBinding>() {

    lateinit var viewModel: MainViewModel

    override fun setupViews() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding.title.text = item.name
        binding.goal.text = item.secondAmount.toString()
//        Glide.with(binding.image)
//            .load(item.image)
//            .placeholder(R.drawable.ic_piggy_bank)
//            .into(binding.image)
        setupListener()
    }

    private fun setupListener() {
        binding.btnDelete.setOnClickListener {
            showAlertDialog(
                requireContext() ,
                this ,
                titleText = getString(R.string.delete_achievement) ,
                subtitleText = getString(R.string.are_u_sure_delete) ,
                buttonText = getString(R.string.yes) ,
                action = this::delete
            )
        }
        binding.btnImage.setOnClickListener {}
    }

    private fun delete() {
        viewModel.deleteAchievement(prefs.getCurrentUserId() , item.docName)
        dialog?.dismiss()
    }

    override fun attachBinding(
        list: MutableList<BsAchievementBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(BsAchievementBinding.inflate(layoutInflater , container , attachToRoot))
    }
}