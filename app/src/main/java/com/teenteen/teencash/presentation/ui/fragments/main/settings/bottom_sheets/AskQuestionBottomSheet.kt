package com.teenteen.teencash.presentation.ui.fragments.main.settings.bottom_sheets

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.teenteen.teencash.R
import com.teenteen.teencash.databinding.BsAskQuestionBinding
import com.teenteen.teencash.presentation.base.BaseBottomSheetDialogFragment
import com.teenteen.teencash.presentation.utills.checkInternetConnection


class AskQuestionBottomSheet: BaseBottomSheetDialogFragment<BsAskQuestionBinding>() {
    override fun setupViews() {
        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnText.setOnClickListener { checkInternetConnection(this::composeEmail, requireContext()) }
    }

    private fun composeEmail() {
        val emailIntent = Intent(
            Intent.ACTION_SENDTO , Uri.fromParts(
                "mailto" , "emikoomi13@gmail.com" , null))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT , "TeenCash")
        startActivity(Intent.createChooser(emailIntent , getString(R.string.send_email_to_developer)))
        dismiss()
    }

    override fun attachBinding(
        list: MutableList<BsAskQuestionBinding> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ) {
        list.add(BsAskQuestionBinding.inflate(layoutInflater, container, attachToRoot))
    }
}